<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Thanh toán thành công</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/success.css">
</head>
<body>

<div class="container success">
    <h2>🎉 Thanh toán thành công!</h2>
    <p>Cảm ơn bạn đã đặt vé tại <b>TENIFINITY</b>.</p>

    <!-- THÔNG TIN THANH TOÁN -->
    <c:if test="${not empty payment}">
        <div class="summary-box">
            <p><b>Mã thanh toán:</b> ${payment.id}</p>
            <p><b>Thời gian thanh toán:</b>
                <fmt:formatDate value="${paidAtDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
            </p>
            <p><b>Trạng thái:</b> ${payment.status}</p>
        </div>
    </c:if>

    <!-- THÔNG TIN SUẤT CHIẾU -->
    <c:if test="${not empty showtime}">
        <div class="summary-box">
            <p><b>🎬 Phim:</b> ${title}</p>
            <p><b>🏢 Rạp:</b> ${showtime.auditorium.cinema.name}</p>
            <p><b>🪑 Phòng chiếu:</b> ${nameAuditorium}</p>
            <p><b>🗓️ Ngày chiếu:</b> ${day}</p>
            <p><b>⏰ Giờ bắt đầu:</b> ${time}</p>
        </div>
    </c:if>

    <!-- DANH SÁCH GHẾ VÀ TỔNG TIỀN -->
    <c:if test="${not empty seatPrices}">
        <div class="summary-box">
            <h3>🎟️ Vé của bạn</h3>
            <table>
                <thead>
                <tr>
                    <th>Ghế</th>
                    <th>Loại ghế</th>
                    <th>Giá (VND)</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="total" value="0" />
                <c:forEach var="entry" items="${seatPrices}">
                    <tr>
                        <td>${entry.key.rowLabel}${entry.key.seatNumber}</td>
                        <td>${entry.key.seatType}</td>
                        <td>${entry.value}</td>
                    </tr>
                    <c:set var="total" value="${total + entry.value}" />
                </c:forEach>
                </tbody>
                <tfoot>
                <tr>
                    <td style="font-weight:bold; color:#f1c40f;">Tổng cộng:</td>
                    <td></td>
                    <td style="text-align:right; font-weight:bold; color:#f1c40f;">
                        <fmt:formatNumber value="${total}" type="number" pattern="#,##0"/> VND
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </c:if>
    <c:if test="${not empty selectedPromos}">
        <div class="summary-box">
            <h3>🎁 Khuyến mãi đã áp dụng</h3>
            <ul>
                <c:forEach var="p" items="${selectedPromos}">
                    <li>
                            ${p.name}
                        (<c:choose>
                        <c:when test="${p.promotionType eq 'PERCENT'}">-${p.discountValue}%</c:when>
                        <c:otherwise>-<fmt:formatNumber value="${p.discountValue}" type="number" pattern="#,##0 ₫"/></c:otherwise>
                    </c:choose>)
                    </li>
                </c:forEach>
            </ul>
            <p><b>Tổng sau giảm:</b> <fmt:formatNumber value="${totalAfter}" type="number" pattern="#,##0 ₫"/></p>
        </div>
    </c:if>

    <!-- NÚT HÀNH ĐỘNG -->
    <div class="btn-group">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-success">🏠 Về trang chủ</a>
        <a href="${pageContext.request.contextPath}/tickets" class="btn btn-ticket">🎫 Xem vé của tôi</a>
    </div>
</div>

</body>
</html>
