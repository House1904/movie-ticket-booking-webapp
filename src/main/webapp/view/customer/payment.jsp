<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Xác nhận thanh toán</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment.css?v=<%=System.currentTimeMillis()%>">
</head>
<body>
<div class="payment-wrapper">
    <div class="container">
        <h2>🎟️ Xác nhận thanh toán</h2>

        <div class="summary-box">
            <p><strong>Phim:</strong> ${title}</p>
            <p><strong>Rạp:</strong> ${showtime.auditorium.cinema.name}</p>
            <p><strong>Phòng chiếu:</strong> ${nameAuditorium}</p>
            <p><strong>Ngày chiếu:</strong> ${day}</p>
            <p><strong>Giờ bắt đầu:</strong> ${time}</p>
        </div>

        <h3>Danh sách ghế đã chọn:</h3>
        <table>
            <tr>
                <th>Ghế</th>
                <th>Loại ghế</th>
                <th>Giá (VND)</th>
            </tr>
            <c:set var="total" value="0" />
            <c:forEach var="entry" items="${seatPrices}">
                <tr>
                    <td>${entry.key.rowLabel}${entry.key.seatNumber}</td>
                    <td>${entry.key.seatType}</td>
                    <td><fmt:formatNumber value="${entry.value}" type="number" pattern="#,##0" /></td>
                    <c:set var="total" value="${total + entry.value}" />
                </tr>
            </c:forEach>
        </table>

        <div class="total">
            Tổng tiền: <span><fmt:formatNumber value="${total}" type="number" pattern="#,##0" /> ₫</span>
        </div>

        <div class="button-group">
            <button type="button" class="btn btn-outline" onclick="history.back()">Quay lại</button>

            <form action="${pageContext.request.contextPath}/payment" method="post" style="display:inline;">
                <input type="hidden" name="action" value="selectPromo">
                <button type="submit" class="btn btn-primary">Xác nhận thanh toán</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
