<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách phòng chiếu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="container">
    <h2>Danh sách phòng chiếu - ${cinema.name}</h2>
    <c:if test="${cinema != null && cinema.id != null}">
        <button class="add-btn"
                onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=add&cinemaId=${cinema.id}'">
            Thêm phòng chiếu
        </button>
    </c:if>

    <table>
        <tr>
            <th>ID</th>
            <th>Tên phòng</th>
            <th>Định dạng</th>
            <th>Ngày tạo</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="auditorium" items="${auditoriums}">
            <tr>
                <td>${auditorium.id}</td>
                <td>${auditorium.name}</td>
                <td>${auditorium.format}</td>
                <td>
                    <c:if test="${auditorium.id != null && cinema != null && cinema.id != null}">
                        <button class="sua-btn" onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=edit&id=${auditorium.id}&cinemaId=${cinema.id}'">Sửa</button>
                        <button class="quanly-btn" onclick="window.location.href='${pageContext.request.contextPath}/SeatController?action=list&auditoriumId=${auditorium.id}'">Quản lý ghế</button>
                        <button class="suatchieu-btn" onclick="window.location.href='${pageContext.request.contextPath}/manageShowtime?action=mnShowtime&auditoriumId=${auditorium.id}'">Quản lý suất chiếu</button>
                        <button class="delete-btn" onclick="if(confirm('Bạn có chắc muốn xóa phòng này?')) window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=delete&id=${auditorium.id}&cinemaId=${cinema.id}'">Xóa</button>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

    </table>
    <button class="back-btn" onclick="window.location.href='${pageContext.request.contextPath}/manageCinema?action=list'">Quay lại Trang Quản lý Rạp phim</button>
</div>
</body>
</html>