<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Cập nhật thông tin rạp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="container">
    <h2>Cập nhật thông tin rạp chiếu phim</h2>
    <form action="${pageContext.request.contextPath}/CinemaController" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${cinema.id}">
        <label>Tên rạp:</label>
        <input type="text" name="name" value="${cinema.name}" readonly/>
        <label>Địa chỉ:</label>
        <input type="text" name="address" value="${cinema.address}" required/>
        <label>Số điện thoại:</label>
        <input type="text" name="phone" value="${cinema.phone}" required/>
        <button type="submit" class="update-btn">Cập nhật</button>
        <button type="button" class="back-btn" onclick="window.location.href='${pageContext.request.contextPath}/CinemaController?action=list'">Quay lại</button>
    </form>
</div>
</body>
</html>