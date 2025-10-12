<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/common/header.jsp" />

<html>
<head>
    <title>Vé của tôi</title>
    <!-- 🔗 Liên kết đến file CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/my_tickets.css">
</head>
<body>

<h2 class="page-title">Vé của tôi</h2>

<c:if test="${empty tickets}">
    <p class="no-ticket">Bạn chưa có vé nào!</p>
</c:if>

<c:if test="${not empty tickets}">
    <table class="ticket-table">
        <tr>
            <th>#</th>
            <th>Phim</th>
            <th>Rạp</th>
            <th>Phòng chiếu</th>
            <th>Suất chiếu</th>
            <th>Ghế</th>
            <th>Giá</th>
            <th>Trạng thái</th>
            <th>QR / Hành động</th>
        </tr>

        <c:forEach var="t" items="${tickets}" varStatus="st">
            <tr>
                <td>${st.index + 1}</td>
                <td>${title}</td>
                <td>${t.showtime.auditorium.cinema.name}</td>
                <td>${nameAuditorium}</td>
                <td>${day}&nbsp;${time}</td>
                <td>${t.seat.rowLabel}${t.seat.seatNumber}</td>
                <td>${t.price}</td>
                <td>${t.status}</td>
                <td>
                    <c:choose>
                        <c:when test="${empty t.qrCode}">
                            <form method="post" action="${pageContext.request.contextPath}/tickets">
                                <input type="hidden" name="action" value="issue"/>
                                <input type="hidden" name="ticketId" value="${t.id}"/>
                                <button type="submit" class="btn issue">Phát hành vé</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/qr?code=${t.qrCode}"
                                 width="100" height="100" alt="QR Code"/><br/>
                            <small>${t.qrCode}</small><br/>
                            <c:if test="${t.status ne 'USED'}">
                                <form method="post" action="${pageContext.request.contextPath}/tickets">
                                    <input type="hidden" name="action" value="check"/>
                                    <input type="hidden" name="ticketId" value="${t.id}"/>
                                    <button type="submit" class="btn check">Check-in</button>
                                </form>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<!-- 🔙 Nút quay lại -->
<a href="${pageContext.request.contextPath}/home" class="back-home">Quay lại trang chủ</a>

</body>
</html>
