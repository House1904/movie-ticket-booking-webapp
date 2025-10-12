<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Thanh toán VietQR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment_qr.css">
</head>
<body>
<div class="qr-card">

    <h2 class="qr-title">💳 Quét mã QR để thanh toán</h2>
    <p class="qr-sub">Vui lòng quét mã bằng ứng dụng ngân hàng hoặc ví điện tử để hoàn tất giao dịch.</p>

    <!-- Tính tổng tiền -->
    <c:set var="total" value="0" />
    <c:forEach var="entry" items="${seatPrices}">
        <c:set var="total" value="${total + entry.value}" />
    </c:forEach>

    <div class="qr-grid">
        <!-- Cột trái: QR -->
        <div class="qr-box">
            <img class="qr-img"
<%--                 src="https://img.vietqr.io/image/970422-0362227417-compact2.jpg?amount=${total}&addInfo=DH${booking.id}&accountName=LE VU HAO"--%>
                 src="https://img.vietqr.io/image/970422-0362227417-compact2.jpg?amount=${discountedTotal}&addInfo=DH${booking.id}&accountName=LE VU HAO"
                 alt="QR Code">
        </div>

        <!-- Cột phải: Thông tin thanh toán -->
        <div class="info">
            <h4>Chi tiết thanh toán</h4>
            <ul class="info-list">
                <li class="info-item">
                    <span class="info-label">🎬 Phim:</span>
                    <span class="info-value">${showtime.movie.title}</span>
                </li>
                <li class="info-item">
                    <span class="info-label">🏢 Rạp:</span>
                    <span class="info-value">${showtime.auditorium.cinema.name}</span>
                </li>
                <li class="info-item">
                    <span class="info-label">🪑 Phòng chiếu:</span>
                    <span class="info-value">${showtime.auditorium.name}</span>
                </li>
                <li class="info-item">
                    <span class="info-label">🗓️ Ngày chiếu:</span>
                    <span class="info-value">${day}</span>
                </li>
                <li class="info-item">
                    <span class="info-label">⏰ Giờ bắt đầu:</span>
                    <span class="info-value">${time}</span>
                </li>
<%--                <li class="info-item">--%>
<%--                    <span class="info-label">💰 Tổng tiền:</span>--%>
<%--                    <span class="info-value" style="color:#f1c40f;">${total} VND</span>--%>
<%--                </li>--%>
                <li class="info-item"><b>Tổng trước giảm:</b> <fmt:formatNumber value="${totalBeforeDiscount}" type="number" pattern="#,##0 ₫"/></li>
                <li class="info-item"><b>Giảm:</b> -<fmt:formatNumber value="${discountAmount}" type="number" pattern="#,##0 ₫"/></li>
                <li class="info-item"><b>Tổng thanh toán:</b> <fmt:formatNumber value="${discountedTotal}" type="number" pattern="#,##0 ₫"/></li>

                <li class="info-item">
                    <span class="info-label">🏦 Ngân hàng:</span>
                    <span class="info-value">MB Bank - 0362227417</span>
                </li>
                <li class="info-item">
                    <span class="info-label">👤 Chủ TK:</span>
                    <span class="info-value">LE VU HAO</span>
                </li>
                <li class="info-item">
                    <span class="info-label">🧾 Nội dung CK:</span>
                    <span class="info-value">DH${booking.id}</span>
                </li>
            </ul>
        </div>
    </div>

    <!-- Bộ đếm thời gian -->
    <div class="countdown">
        <span class="dot"></span>
        <span id="timer">⏳ Còn lại: 60s</span>
    </div>

    <!-- Nút hành động -->
    <div class="actions">
        <form id="successForm" action="${pageContext.request.contextPath}/payment" method="post">
            <input type="hidden" name="action" value="callback">
            <input type="hidden" name="status" value="success">
            <button type="submit" class="btn btn-success">✅ Tôi đã thanh toán</button>
        </form>

        <form id="failForm" action="${pageContext.request.contextPath}/payment" method="post">
            <input type="hidden" name="action" value="callback">
            <input type="hidden" name="status" value="failed">
            <button type="submit" class="btn btn-danger">❌ Hủy giao dịch</button>
        </form>
    </div>
</div>

<!-- Countdown script -->
<script>
    (function(){
        let timeLeft = 60;
        const timer = document.getElementById("timer");
        const failForm = document.getElementById("failForm");

        function render(){
            timer.textContent = "⏳ Còn lại: " + timeLeft + "s";
        }

        render();

        const countdown = setInterval(function(){
            timeLeft--;
            render();
            if(timeLeft <= 0){
                clearInterval(countdown);
                failForm.submit();
            }
        }, 1000);
    })();
</script>
</body>
</html>
