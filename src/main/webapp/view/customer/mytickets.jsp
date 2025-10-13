<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Vé của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/my_tickets.css?v=<%=System.currentTimeMillis()%>">
</head>
<body>

<h2 class="page-title">🎟️ Vé của tôi</h2>

<c:if test="${empty tickets}">
    <p class="no-ticket">Bạn chưa có vé nào!</p>
</c:if>

<c:if test="${not empty tickets}">
    <div class="ticket-list">
        <c:forEach var="t" items="${tickets}" varStatus="st">
            <div class="ticket-card">
                <div class="ticket-header">
                    <span class="ticket-id">#${st.index + 1}</span>
                    <span class="status ${t.status}">${t.status}</span>
                </div>

                <div class="ticket-body">
                    <h3 class="movie-title">${t.showtime.movie.title}</h3>
                    <p>📍 <b>Rạp:</b> ${t.showtime.auditorium.cinema.name}</p>
                    <p>🪑 <b>Phòng chiếu:</b> ${t.showtime.auditorium.name}</p>
                    <p>⏰ <b>Suất chiếu:</b> ${t.showtime.startTime.toLocalDate()} ${t.showtime.startTime.toLocalTime()}</p>
                    <p>💺 <b>Ghế:</b> ${t.seat.rowLabel}${t.seat.seatNumber}</p>
                    <p>💰 <fmt:formatNumber value="${t.price}" type="number" pattern="#,##0 ₫"/></p>
                </div>

                <div class="ticket-footer">
                    <c:choose>
                        <c:when test="${empty t.qrCode}">
                            <form method="post" action="${pageContext.request.contextPath}/tickets">
                                <input type="hidden" name="action" value="issue"/>
                                <input type="hidden" name="ticketId" value="${t.id}"/>
                                <button type="submit" class="btn issue">Phát hành vé</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="qr-section">
                                <p class="qr-label">🎟️ Mã vé</p>
                                <img src="${pageContext.request.contextPath}/qr?code=${t.qrCode}"
                                     width="110" height="110" alt="QR Code" class="qr-image"/>
                                <p class="qr-code-text">${t.qrCode}</p>
                            </div>
                            <c:if test="${t.status ne 'USED'}">
                                <form method="post" action="${pageContext.request.contextPath}/tickets">
                                    <input type="hidden" name="action" value="check"/>
                                    <input type="hidden" name="ticketId" value="${t.id}"/>
                                    <button type="submit" class="btn check">Check-in</button>
                                </form>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>

<a href="${pageContext.request.contextPath}/home" class="back-home">🏠 Quay lại trang chủ</a>

</body>
</html>