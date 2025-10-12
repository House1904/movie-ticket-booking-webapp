//package controller;
//
//import model.*;
//import model.enums.PaymentStatus;
//import model.enums.SeatBookedFormat;
//import model.enums.Status;
//import service.*;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Map;
//
//@WebServlet("/payment")
//public class PaymentController extends HttpServlet {
//    private final PaymentService paymentService = new PaymentService();
//    private final TicketService ticketService = new TicketService();
//    private final BookingService bookingService = new BookingService();
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String action = request.getParameter("action");
//        HttpSession session = request.getSession();
//        String url = "";
//
//        if ("confirm".equals(action)) {
//            url = "/view/customer/payment_qr.jsp";
//        }
//
//        else if ("callback".equals(action)) {
//            String result = request.getParameter("status"); // success | failed
//
//            Booking booking = (Booking) session.getAttribute("booking");
//            Showtime showtime = (Showtime) session.getAttribute("showtime");
//            Map<Seat, BigDecimal> seatPrices =
//                    (Map<Seat, BigDecimal>) session.getAttribute("seatPrices");
//
//            if (booking == null || showtime == null || seatPrices == null) {
//                request.setAttribute("errorMessage", "Phiên thanh toán không hợp lệ hoặc đã hết hạn.");
//                request.getRequestDispatcher("/view/customer/fail.jsp").forward(request, response);
//                return;
//            }
//
//            if (booking.getStatus() == Status.ISSUED) {
//                response.sendRedirect(request.getContextPath() + "/tickets");
//                return;
//            }
//
//            // ✅ Thành công
//            if ("success".equals(result)) {
//
//                // 🔄 Cập nhật trạng thái ghế: HOLD → CONFIRMED
//                for (Map.Entry<Seat, BigDecimal> entry : seatPrices.entrySet()) {
//                    Seat seat = entry.getKey();
//                    BookingSeat bs = bookingService.findBookingSeatBySeatAndShowtime(seat.getId(), showtime.getId());
//                    if (bs != null && bs.getStatus() == SeatBookedFormat.HOLD) {
//                        bookingService.updateBookingSeatStatus(bs, SeatBookedFormat.CONFIRMED);
//                    }
//                }
//
//                // 1️⃣ Ghi bản thanh toán
//                Payment payment = new Payment();
//                payment.setBooking(booking);
//                payment.setStatus(PaymentStatus.PAID);
//                payment.setCreateAt(LocalDateTime.now());
//                payment.setPaidAt(LocalDateTime.now());
//                payment.setExpiredAt(LocalDateTime.now().plusMinutes(10));
//
//                booking.setPayment(payment);
//
//                paymentService.insert(payment);
//
//                // 2️⃣ Sinh vé cho từng ghế đã chọn
//                booking.setTickets(new ArrayList<>());
//                for (Map.Entry<Seat, BigDecimal> entry : seatPrices.entrySet()) {
//                    Seat seat = entry.getKey();
//                    BigDecimal price = entry.getValue();
//
//                    Ticket ticket = new Ticket();
//                    ticket.setShowtime(showtime);
//                    ticket.setSeat(seat);
//                    ticket.setPrice(price.doubleValue());
//                    ticket.setStatus(Status.ISSUED);
//                    ticket.setCreatedAt(LocalDateTime.now());
//                    ticket.setIssuedAt(LocalDateTime.now());
//                    ticket.setBooking(booking);
//
//                    ticketService.insert(ticket);
//
//                    // Gắn ticket vào booking để giữ quan hệ đúng chiều
//                    booking.getTickets().add(ticket);
//                }
//
//                // 3️⃣ Cập nhật Booking
//                booking.setStatus(Status.ISSUED);
//                bookingService.updateBooking(booking);
//
//                // ✅ Chuyển LocalDateTime -> Date để JSP hiển thị bằng fmt
//                java.util.Date paidAtDate = java.util.Date.from(
//                        payment.getPaidAt()
//                                .atZone(java.time.ZoneId.systemDefault())
//                                .toInstant()
//                );
//
//                // ✅ Gửi sang JSP
//                request.setAttribute("payment", payment);
//                request.setAttribute("booking", booking);
//                request.setAttribute("seatPrices", seatPrices);
//                request.setAttribute("showtime", showtime);
//                request.setAttribute("paidAtDate", paidAtDate);
//
//                // 4️⃣ Xóa session tạm
//                session.removeAttribute("seatPrices");
//                session.removeAttribute("showtime");
//                session.removeAttribute("booking");
//
//                url = "/view/customer/success.jsp";
//            }
//
//            // ❌ Thất bại
//            else {
//                // 🔄 Cập nhật trạng thái ghế: HOLD → EXPIRED
//                for (Map.Entry<Seat, BigDecimal> entry : seatPrices.entrySet()) {
//                    Seat seat = entry.getKey();
//                    BookingSeat bs = bookingService.findBookingSeatBySeatAndShowtime(seat.getId(), showtime.getId());
//                    if (bs != null && bs.getStatus() == SeatBookedFormat.HOLD) {
//                        bookingService.updateBookingSeatStatus(bs, SeatBookedFormat.EXPIRED);
//                    }
//                }
//
//                booking.setStatus(Status.CANCELLED);
//                bookingService.updateBooking(booking);
//                request.setAttribute("errorMessage", "Thanh toán thất bại hoặc đã hết thời gian.");
//                url = "/view/customer/fail.jsp";
//            }
//        }
//
//        request.getRequestDispatcher(url).forward(request, response);
//    }
//}
package controller;

import model.*;
import model.enums.*;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
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

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String url = "";

        Booking booking = (Booking) session.getAttribute("booking");
        Showtime showtime = (Showtime) session.getAttribute("showtime");
        Map<Seat, BigDecimal> seatPrices =
                (Map<Seat, BigDecimal>) session.getAttribute("seatPrices");

        // =============== VALIDATION CHUNG ===============
        if (booking == null || showtime == null || seatPrices == null) {
            request.setAttribute("errorMessage", "Phiên thanh toán không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("/view/customer/fail.jsp").forward(request, response);
            return;
        }

        // =============== ACTION 1: CHỌN KHUYẾN MÃI ===============
        if ("selectPromo".equals(action)) {
            double total = seatPrices.values().stream()
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();

            List<Promotion> validPromos = promotionService.getValidPromotions(total);

            request.setAttribute("totalPrice", total);
            request.setAttribute("promotions", validPromos);
            url = "/view/customer/payment_select_promotion.jsp";
        }

        // =============== ACTION 2: XÁC NHẬN THANH TOÁN ===============
        else if ("confirm".equals(action)) {
            String[] selectedIds = request.getParameterValues("promotionIds");
            double totalBefore = seatPrices.values().stream()
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();

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

            request.setAttribute("totalBeforeDiscount", totalBefore);
            request.setAttribute("discountAmount", discount);
            request.setAttribute("discountedTotal", totalAfter);
            request.setAttribute("seatPrices", seatPrices);
            request.setAttribute("showtime", showtime);
            request.setAttribute("booking", booking);

            // Lưu vào session để sử dụng ở bước thanh toán thật
            session.setAttribute("discountedTotal", totalAfter);

            url = "/view/customer/payment_qr.jsp";
        }

        // =============== ACTION 3: KẾT QUẢ CALLBACK (THÀNH CÔNG/THẤT BẠI) ===============
        else if ("callback".equals(action)) {
            String result = request.getParameter("status"); // success | failed

            if (booking.getStatus() == Status.ISSUED) {
                response.sendRedirect(request.getContextPath() + "/tickets");
                return;
            }

            // ✅ Thành công
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
//                payment.setTotalAmount(totalAfter);

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
                if (selectedIds != null && selectedIds.length > 0) {
                    BookingPromotionService bpService = new BookingPromotionService();
                    for (String idStr : selectedIds) {
                        try {
                            long promoId = Long.parseLong(idStr);
                            System.out.println("💾 [PaymentController] Lưu promotionID=" + promoId + " vào booking=" + booking.getId());
                            bpService.addPromotionToBooking(booking.getId(), promoId);
                        } catch (NumberFormatException ignored) {}
                    }
                }


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
            }
            // ❌ Thất bại
            else {
                for (Seat seat : seatPrices.keySet()) {
                    BookingSeat bs = bookingService.findBookingSeatBySeatAndShowtime(seat.getId(), showtime.getId());
                    if (bs != null && bs.getStatus() == SeatBookedFormat.HOLD) {
                        bookingService.updateBookingSeatStatus(bs, SeatBookedFormat.EXPIRED);
                    }
                }

                booking.setStatus(Status.CANCELLED);
                bookingService.updateBooking(booking);
                request.setAttribute("errorMessage", "Thanh toán thất bại hoặc đã hết thời gian.");
                url = "/view/customer/fail.jsp";
            }
        }

        request.getRequestDispatcher(url).forward(request, response);
    }
}

