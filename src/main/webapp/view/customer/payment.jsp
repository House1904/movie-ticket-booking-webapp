<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Xác nhận thanh toán</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/payment.css">
</head>
<body>
<div class="container">
    <h2>Xác nhận thanh toán</h2>

    <div class="summary-box">
        <p><strong>Phim:</strong> ${title}</p>
        <p><strong>Rạp:</strong> ${showtime.auditorium.cinema.name}</p>
        <p><strong>Phòng chiếu:</strong>  ${nameAuditorium}</p>
        <p><strong>Ngày chiếu:</strong> ${day}</p>
        <p><strong>Giờ bắt đầu:</strong> ${time}</p>
    </div>

    <h3>Danh sách ghế đã chọn:</h3>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Ghế</th>
            <th>Loại ghế</th>
            <th>Giá (VND)</th>
        </tr>
        <c:forEach var="entry" items="${seatPrices}">
            <tr>
                <td>${entry.key.rowLabel}${entry.key.seatNumber}</td>
                <td>${entry.key.seatType}</td>
                <td>${entry.value}</td>
            </tr>
        </c:forEach>
    </table>

    <h3 style="margin-top:20px">
        Tổng tiền:
        <span style="color:red">
            <c:set var="total" value="0" />
            <c:forEach var="entry" items="${seatPrices}">
                <c:set var="total" value="${total + entry.value}" />
            </c:forEach>
            ${total} VND
        </span>
    </h3>

    <form action="${pageContext.request.contextPath}/payment" method="post">
        <input type="hidden" name="action" value="selectPromo">
        <button type="submit" class="btn btn-danger">Xác nhận thanh toán</button>
    </form>
</div>
</body>
</html>
