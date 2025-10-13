<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${seat == null ? 'Thêm ghế' : 'Sửa ghế'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="container">
    <h2>${seat == null ? 'Thêm ghế' : 'Sửa ghế'}</h2>
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>
    <a class="back-btn" href="${pageContext.request.contextPath}/SeatController?action=list&auditoriumId=${auditorium.id}">Quay lại</a>
    <form action="${pageContext.request.contextPath}/SeatController" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="auditoriumId" value="${auditorium.id}">
        <input type="hidden" name="seatId" value="${seat != null ? seat.id : ''}">
        <label>Hàng ghế:</label>
        <select name="rowLabel" required>
            <option value="" ${seat == null || seat.rowLabel == null ? 'selected' : ''}>Chọn hàng ghế</option>
            <option value="A" ${seat != null && seat.rowLabel == 'A' ? 'selected' : ''}>A</option>
            <option value="B" ${seat != null && seat.rowLabel == 'B' ? 'selected' : ''}>B</option>
            <option value="C" ${seat != null && seat.rowLabel == 'C' ? 'selected' : ''}>C</option>
            <option value="D" ${seat != null && seat.rowLabel == 'D' ? 'selected' : ''}>D</option>
            <option value="E" ${seat != null && seat.rowLabel == 'E' ? 'selected' : ''}>E</option>
            <option value="F" ${seat != null && seat.rowLabel == 'F' ? 'selected' : ''}>F</option>
            <option value="G" ${seat != null && seat.rowLabel == 'G' ? 'selected' : ''}>G</option>
            <option value="H" ${seat != null && seat.rowLabel == 'H' ? 'selected' : ''}>H</option>
            <option value="I" ${seat != null && seat.rowLabel == 'I' ? 'selected' : ''}>I</option>
            <option value="J" ${seat != null && seat.rowLabel == 'J' ? 'selected' : ''}>J</option>
            <option value="K" ${seat != null && seat.rowLabel == 'K' ? 'selected' : ''}>K</option>
            <option value="L" ${seat != null && seat.rowLabel == 'L' ? 'selected' : ''}>L</option>
            <option value="M" ${seat != null && seat.rowLabel == 'M' ? 'selected' : ''}>M</option>
            <option value="N" ${seat != null && seat.rowLabel == 'N' ? 'selected' : ''}>N</option>
            <option value="O" ${seat != null && seat.rowLabel == 'O' ? 'selected' : ''}>O</option>
            <option value="P" ${seat != null && seat.rowLabel == 'P' ? 'selected' : ''}>P</option>
        </select>
        <label>Số ghế:</label>
        <select name="seatNumber" required>
            <option value="" ${seat == null || seat.seatNumber == null ? 'selected' : ''}>Chọn số ghế</option>
            <option value="01" ${seat != null && seat.seatNumber == '01' ? 'selected' : ''}>01</option>
            <option value="02" ${seat != null && seat.seatNumber == '02' ? 'selected' : ''}>02</option>
            <option value="03" ${seat != null && seat.seatNumber == '03' ? 'selected' : ''}>03</option>
            <option value="04" ${seat != null && seat.seatNumber == '04' ? 'selected' : ''}>04</option>
            <option value="05" ${seat != null && seat.seatNumber == '05' ? 'selected' : ''}>05</option>
            <option value="06" ${seat != null && seat.seatNumber == '06' ? 'selected' : ''}>06</option>
            <option value="07" ${seat != null && seat.seatNumber == '07' ? 'selected' : ''}>07</option>
            <option value="08" ${seat != null && seat.seatNumber == '08' ? 'selected' : ''}>08</option>
            <option value="09" ${seat != null && seat.seatNumber == '09' ? 'selected' : ''}>09</option>
            <option value="10" ${seat != null && seat.seatNumber == '10' ? 'selected' : ''}>10</option>
        </select>
        <label>Loại ghế:</label>
        <select name="seatType" required>
            <option value="NORMAL" ${seat != null && seat.seatType == 'NORMAL' ? 'selected' : ''}>Thường</option>
            <option value="VIP" ${seat != null && seat.seatType == 'VIP' ? 'selected' : ''}>VIP</option>
            <option value="COUPLE" ${seat != null && seat.seatType == 'COUPLE' ? 'selected' : ''}>Couple</option>
        </select>
        <button type="submit" class="save-btn">Lưu</button>
    </form>
</div>
</body>
</html>