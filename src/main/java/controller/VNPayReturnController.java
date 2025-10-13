package controller;

import util.VNPayConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/payment-return")
public class VNPayReturnController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Map<String, String> fields = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key);
            if (value != null && !value.isEmpty()) {
                fields.put(key, value);
            }
        }

        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        boolean first = true;
        for (String field : fieldNames) {
            String value = fields.get(field);
            if (value != null && !value.isEmpty()) {
                if (!first) hashData.append('&');
                first = false;
                hashData.append(field).append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
            }
        }

        String secureHashCheck = hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");

        // Log ra để kiểm tra
        System.out.println(">>> [VNPayReturn] hashData: " + hashData);
        System.out.println(">>> [VNPayReturn] vnp_SecureHash (VNPay gửi): " + vnp_SecureHash);
        System.out.println(">>> [VNPayReturn] secureHashCheck (tính lại): " + secureHashCheck);
        System.out.println(">>> [VNPayReturn] ResponseCode: " + vnp_ResponseCode);
        System.out.println(">>> [VNPayReturn] TransactionStatus: " + vnp_TransactionStatus);

        if (secureHashCheck.equals(vnp_SecureHash)
                && "00".equals(vnp_ResponseCode)
                && "00".equals(vnp_TransactionStatus)) {
            // Thành công
            request.getRequestDispatcher("/payment?action=callback&status=success").forward(request, response);
        } else {
            // Thất bại
            request.getRequestDispatcher("/payment?action=callback&status=failed").forward(request, response);
        }
    }

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
}
