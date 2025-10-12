<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>${auditorium == null ? 'Thêm phòng chiếu' : 'Sửa phòng chiếu'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="container">
    <h2>${auditorium == null ? 'Thêm phòng chiếu' : 'Sửa phòng chiếu'}</h2>
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/AuditoriumController" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="cinemaId" value="${cinema.id}">
        <input type="hidden" name="id" value="${auditorium != null ? auditorium.id : ''}">
        <label>Tên phòng:</label>
        <input type="text" name="name" value="${auditorium != null ? auditorium.name : ''}" required/>
        <label>Định dạng:</label>
        <select name="format" required>
            <option value="format2D" ${auditorium != null && auditorium.format == 'format2D' ? 'selected' : ''}>2D</option>
            <option value="format3D" ${auditorium != null && auditorium.format == 'format3D' ? 'selected' : ''}>3D</option>
            <option value="IMAX" ${auditorium != null && auditorium.format == 'IMAX' ? 'selected' : ''}>IMAX</option>
        </select>
        <button type="submit" class="save-btn">Lưu</button>
        <button class="back-btn" onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=list&cinemaId=${cinema.id}'">Quay lại</button>
    </form>
</div>
</body>
</html>