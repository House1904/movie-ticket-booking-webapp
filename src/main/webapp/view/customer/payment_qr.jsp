<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Thanh toán VietQR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment_qr.css">
</head>
<body>
<div class="qr-wrapper">
    <div class="qr-card">
        <h2 class="qr-title">💳 Quét mã QR để thanh toán</h2>
        <p class="qr-sub">Vui lòng quét mã bằng ứng dụng ngân hàng hoặc ví điện tử để hoàn tất giao dịch.</p>

        <c:set var="total" value="0" />
        <c:forEach var="entry" items="${seatPrices}">
            <c:set var="total" value="${total + entry.value}" />
        </c:forEach>

        <div class="qr-grid">
            <!-- Cột trái: QR -->
            <div class="qr-box">
                <img class="qr-img"
                     src="https://img.vietqr.io/image/970422-0362227417-compact2.jpg?amount=${discountedTotal}&addInfo=DH${booking.id}&accountName=LE VU HAO"
                     alt="QR Code">
            </div>

            <!-- Cột phải: Thông tin -->
            <div class="info">
                <h4>Chi tiết thanh toán</h4>
                <ul class="info-list">
                    <li><span>🎬 Phim:</span> ${showtime.movie.title}</li>
                    <li><span>🏢 Rạp:</span> ${showtime.auditorium.cinema.name}</li>
                    <li><span>🪑 Phòng chiếu:</span> ${showtime.auditorium.name}</li>
                    <li><span>🗓️ Ngày chiếu:</span> ${day}</li>
                    <li><span>⏰ Giờ bắt đầu:</span> ${time}</li>
                    <li><span>💰 Tổng trước giảm:</span> <fmt:formatNumber value="${totalBeforeDiscount}" type="number" pattern="#,##0 ₫"/></li>
                    <li><span>💸 Giảm:</span> -<fmt:formatNumber value="${discountAmount}" type="number" pattern="#,##0 ₫"/></li>
                    <li><span>💴 Tổng thanh toán:</span> <span class="highlight"><fmt:formatNumber value="${discountedTotal}" type="number" pattern="#,##0 ₫"/></span></li>
                    <li><span>🏦 Ngân hàng:</span> MB Bank - 0362227417</li>
                    <li><span>👤 Chủ TK:</span> LE VU HAO</li>
                    <li><span>🧾 Nội dung CK:</span> DH${booking.id}</li>
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
                <button type="submit" class="btn btn-success">Tôi đã thanh toán</button>
            </form>

            <form id="failForm" action="${pageContext.request.contextPath}/payment" method="post">
                <input type="hidden" name="action" value="callback">
                <input type="hidden" name="status" value="failed">
                <button type="submit" class="btn btn-danger">Hủy giao dịch</button>
            </form>
        </div>
    </div>
</div>

<!-- Countdown -->
<script>
    (function(){
        let timeLeft = 60;
        const timer = document.getElementById("timer");
        const failForm = document.getElementById("failForm");

        function render(){
            timer.textContent = "⏳ Còn lại: " + timeLeft + "s";
        }
        render();

        const countdown = setInterval(() => {
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
