<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách ghế - ${auditorium.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="container">
    <h2>Danh sách ghế - ${auditorium.name}</h2>
    <button class="add-btn" onclick="window.location.href='${pageContext.request.contextPath}/SeatController?action=add&auditoriumId=${auditorium.id}'">Thêm ghế</button>
    <table>
        <tr>
            <th>ID</th>
            <th>Hàng</th>
            <th>Số ghế</th>
            <th>Loại ghế</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="seat" items="${seats}">
            <tr>
                <td>${seat.id}</td>
                <td>${seat.rowLabel}</td>
                <td>${seat.seatNumber}</td>
                <td>${seat.seatType}</td>
                <td>${seat.active ? 'Hoạt động' : 'Không hoạt động'}</td>
                <td>
                    <button class="sua-btn" onclick="window.location.href='${pageContext.request.contextPath}/SeatController?action=edit&seatId=${seat.id}&auditoriumId=${auditorium.id}'">Sửa</button>
                    <button class="delete-btn" onclick="if(confirm('Bạn có chắc muốn xóa ghế này?')) window.location.href='${pageContext.request.contextPath}/SeatController?action=delete&seatId=${seat.id}&auditoriumId=${auditorium.id}'">Xóa</button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <button class="back-btn"  onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=list&cinemaId=${auditorium.cinema.id}'">Quay lại danh sách phòng chiếu</button>
</div>
</body>
</html>