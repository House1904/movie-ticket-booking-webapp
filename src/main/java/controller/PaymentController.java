package controller;

import model.*;
import model.enums.*;
import service.*;
import util.VNPayConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/payment")
public class PaymentController extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();
    private final TicketService ticketService = new TicketService();
    private final BookingService bookingService = new BookingService();
    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String url = "";

        Booking booking = (Booking) session.getAttribute("booking");
        Showtime showtime = (Showtime) session.getAttribute("showtime");
        Map<Seat, BigDecimal> seatPrices =
                (Map<Seat, BigDecimal>) session.getAttribute("seatPrices");

        // ===== 0. Kiểm tra session hợp lệ =====
        if (booking == null || showtime == null || seatPrices == null) {
            request.setAttribute("errorMessage", "Phiên thanh toán không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("/view/customer/fail.jsp").forward(request, response);
            return;
        }

        // ===== 1. Chọn khuyến mãi =====
        if ("selectPromo".equals(action)) {
            double total = seatPrices.values().stream().mapToDouble(BigDecimal::doubleValue).sum();
            List<Promotion> validPromos = promotionService.getValidPromotions(total);

            request.setAttribute("totalPrice", total);
            request.setAttribute("promotions", validPromos);
            url = "/view/customer/payment_select_promotion.jsp";
        }

        // ===== 2. Xác nhận thanh toán =====
        else if ("confirm".equals(action)) {
            String[] selectedIds = request.getParameterValues("promotionIds");
            double totalBefore = seatPrices.values().stream().mapToDouble(BigDecimal::doubleValue).sum();
            double discount = 0.0;

            if (selectedIds != null && selectedIds.length > 0) {
                for (String idStr : selectedIds) {
                    long id = Long.parseLong(idStr);
                    Promotion promo = promotionService.getPromotionById(id);
                    if (promo != null && promo.getStatus() == PromotionStatus.ACTIVE) {
                        if (promo.getPromotionType() == PromotionType.PERCENT) {
                            discount += totalBefore * (promo.getDiscountValue() / 100.0);
                        } else {
                            discount += promo.getDiscountValue();
                        }
                    }
                }
                // ✅ Lưu promotionIds vào session để dùng khi callback
                session.setAttribute("selectedPromotionIds", selectedIds);
            }

            double totalAfter = Math.max(totalBefore - discount, 0);

            session.setAttribute("discountedTotal", totalAfter);
            request.setAttribute("totalBeforeDiscount", totalBefore);
            request.setAttribute("discountAmount", discount);
            request.setAttribute("discountedTotal", totalAfter);
            request.setAttribute("seatPrices", seatPrices);
            request.setAttribute("showtime", showtime);
            request.setAttribute("booking", booking);

            url = "/view/customer/payment_qr.jsp";
        }

        // ===== 3. Tạo liên kết thanh toán VNPay =====
        else if ("createVNPay".equals(action)) {
            double totalAfter = (double) session.getAttribute("discountedTotal");
            long amount = (long) (totalAfter * 100); // VNPay yêu cầu nhân 100

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + booking.getId());
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);

            String ipAddress = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) ipAddress = "127.0.0.1";
            vnp_Params.put("vnp_IpAddr", ipAddress);

//            // Thời gian tạo và hết hạn (VNPay dùng GMT+7)
//            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//            String vnp_CreateDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//            cld.add(Calendar.MINUTE, 15);
//            String vnp_ExpireDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            // ===== Thời gian tạo và hết hạn (Singapore server → VNPay GMT+7) =====
            // ===== Tạo thời gian chuẩn VN (bất kể server ở đâu) =====
            long nowUtcMillis = System.currentTimeMillis(); // giờ UTC thực của server
            long vnMillis = nowUtcMillis + 7 * 60 * 60 * 1000; // cộng thêm 7 tiếng → VN time

            Date vnDate = new Date(vnMillis);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC")); // format về UTC để không bị lệch

            String vnp_CreateDate = formatter.format(vnDate);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Hết hạn sau 60 phút
            long expireMillis = vnMillis + 60 * 60 * 1000;
            Date expireDate = new Date(expireMillis);
            String vnp_ExpireDate = formatter.format(expireDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // ===== Sắp xếp tham số + tạo chuỗi hash =====
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            boolean first = true;

            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    if (!first) {
                        hashData.append('&');
                        query.append('&');
                    }
                    first = false;

                    // Phải encode UTF-8 cho cả hash và query
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    query.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                }
            }

            String vnp_SecureHash = hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query;

            // Debug log
            System.out.println(">>> VNPay HashData: " + hashData);
            System.out.println(">>> VNPay SecureHash: " + vnp_SecureHash);
            System.out.println(">>> VNPay Redirect URL: " + paymentUrl);

            response.sendRedirect(paymentUrl);
            return;
        }

        // ===== 4. Callback (khi VNPay trả về kết quả) =====
        else if ("callback".equals(action)) {
            String result = request.getParameter("status");

            if (booking.getStatus() == Status.ISSUED) {
                response.sendRedirect(request.getContextPath() + "/tickets");
                return;
            }

            if ("success".equals(result)) {
                for (Seat seat : seatPrices.keySet()) {
                    BookingSeat bs = bookingService.findBookingSeatBySeatAndShowtime(seat.getId(), showtime.getId());
                    if (bs != null && bs.getStatus() == SeatBookedFormat.HOLD) {
                        bookingService.updateBookingSeatStatus(bs, SeatBookedFormat.CONFIRMED);
                    }
                }

                double totalAfter = (double) session.getAttribute("discountedTotal");

                Payment payment = new Payment();
                payment.setBooking(booking);
                payment.setStatus(PaymentStatus.PAID);
                payment.setCreateAt(LocalDateTime.now());
                payment.setPaidAt(LocalDateTime.now());
                payment.setExpiredAt(LocalDateTime.now().plusMinutes(10));

                booking.setPayment(payment);
                paymentService.insert(payment);

                // Sinh vé
                booking.setTickets(new ArrayList<>());
                for (Map.Entry<Seat, BigDecimal> entry : seatPrices.entrySet()) {
                    Ticket t = new Ticket();
                    t.setShowtime(showtime);
                    t.setSeat(entry.getKey());
                    t.setPrice(entry.getValue().doubleValue());
                    t.setStatus(Status.ISSUED);
                    t.setCreatedAt(LocalDateTime.now());
                    t.setIssuedAt(LocalDateTime.now());
                    t.setBooking(booking);
                    ticketService.insert(t);
                    booking.getTickets().add(t);
                }

                booking.setStatus(Status.ISSUED);
                bookingService.updateBooking(booking);

                // ✅ Ghi khuyến mãi đã chọn xuống bảng bookingPromotion
                String[] selectedIds = (String[]) session.getAttribute("selectedPromotionIds");
                List<Promotion> selectedPromos = new ArrayList<>();

                if (selectedIds != null && selectedIds.length > 0) {
                    BookingPromotionService bpService = new BookingPromotionService();
                    for (String idStr : selectedIds) {
                        try {
                            long promoId = Long.parseLong(idStr);
                            System.out.println("💾 [PaymentController] Lưu promotionID=" + promoId + " vào booking=" + booking.getId());
                            bpService.addPromotionToBooking(booking.getId(), promoId);

                            // ✅ Truy xuất đối tượng Promotion để hiển thị ở success.jsp
                            Promotion promo = promotionService.getPromotionById(promoId);
                            if (promo != null) selectedPromos.add(promo);
                        } catch (NumberFormatException ignored) {}
                    }
                }

                // ✅ Gửi danh sách khuyến mãi sang JSP
                request.setAttribute("selectedPromos", selectedPromos);

                java.util.Date paidAtDate = java.util.Date.from(
                        payment.getPaidAt().atZone(java.time.ZoneId.systemDefault()).toInstant());

                request.setAttribute("payment", payment);
                request.setAttribute("booking", booking);
                request.setAttribute("seatPrices", seatPrices);
                request.setAttribute("showtime", showtime);
                request.setAttribute("paidAtDate", paidAtDate);
                request.setAttribute("totalAfter", totalAfter);

                // Dọn session
                session.removeAttribute("seatPrices");
                session.removeAttribute("showtime");
                session.removeAttribute("booking");
                session.removeAttribute("discountedTotal");

                url = "/view/customer/success.jsp";
            } else {
                booking.setStatus(Status.CANCELLED);
                bookingService.updateBooking(booking);
                request.setAttribute("errorMessage", "Thanh toán thất bại hoặc hết thời gian.");
                url = "/view/customer/fail.jsp";
            }
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    // ===== Hàm ký HMAC SHA-512 =====
    private static String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey =
                    new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] hash = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
