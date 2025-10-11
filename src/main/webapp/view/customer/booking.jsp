<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Chọn ghế</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/booking.css">
</head>
<body>
<div class="booking">
<div class="showtimeInfo">
    <p><strong>Phim:</strong> ${title}</p>
    <p><strong>Rạp:</strong> ${cinema.name}</p>
    <p><strong>Ngày chiếu:</strong> ${day}</p>
    <p><strong>Giờ chiếu:</strong> ${time}</p>
    <p><strong>Phòng chiếu:</strong> ${auditorium.cinema.name}</p>
    <div class="summary-box">
        <p>Ghế đã chọn: <span id="selected-seats">—</span></p>
        <p>Tổng tiền: <span id="total-price">0</span> VND</p>
    </div>
</div>
<div class="bookSeat">
    <h1>Chọn ghế</h1>
    <div class="screen">Màn hình</div>
    <form action="booking" method="post">
        <input type="hidden" name="action" value="goPay">
        <c:set var="currentRow" value="" />
        <c:forEach var="seat" items="${seatList}" varStatus="status">
            <c:if test="${seat.rowLabel ne currentRow}">
                <c:if test="${not empty currentRow}">
                    </div>
                </c:if>
                <div class="seat-row">
                <c:set var="currentRow" value="${seat.rowLabel}" />
            </c:if>
                    <c:set var="price" value="${prices[status.index]}" />
            <c:set var="booked" value="false" />
            <c:forEach var="b" items="${bookedSeatIds}">
                <c:if test="${seat.id eq b}">
                    <c:set var="booked" value="true" />
                </c:if>
            </c:forEach>

            <c:choose>
                <c:when test="${booked}">
                    <label class="seat booked">${seat.rowLabel}${seat.seatNumber}</label>
                </c:when>
                <c:otherwise>
                    <input type="checkbox"
                           id="seat${seat.id}"
                           name="selectedSeats"
                           value="${seat.id}"
                           data-price="${price}"
                           class="seat-checkbox">
                    <label for="seat${seat.id}" class="seat ${seat.seatType}">
                            ${seat.rowLabel}${seat.seatNumber}
                    </label>
                </c:otherwise>
            </c:choose>

            <c:if test="${seat == seatList[seatList.size()-1]}"></div></c:if>
            <input type="hidden" name="seat_${seat.id}" value="${price}" />
        </c:forEach>

        <button type="submit" class="pay-btn">Thanh toán</button>
    </form>
</div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const checkboxes = document.querySelectorAll(".seat-checkbox");
        const totalPriceEl = document.getElementById("total-price");
        const selectedSeatsEl = document.getElementById("selected-seats");

        checkboxes.forEach(cb => {
            cb.addEventListener("change", () => {
                let total = 0;
                let selectedList = [];

                // Lặp qua tất cả checkbox đang được chọn
                document.querySelectorAll(".seat-checkbox:checked").forEach(checked => {
                    total += parseFloat(checked.dataset.price);

                    // Lấy label bằng cách tìm phần tử kế bên checkbox (label nằm ngay sau input)
                    const label = checked.nextElementSibling;
                    if (label && label.classList.contains("seat")) {
                        selectedList.push(label.textContent.trim());
                    }
                });

                // Cập nhật giao diện
                totalPriceEl.textContent = total.toLocaleString('vi-VN');
                selectedSeatsEl.textContent = selectedList.length > 0
                    ? selectedList.join(', ')
                    : 'Không có';
            });
        });
    });
</script>

</body>
</html>
