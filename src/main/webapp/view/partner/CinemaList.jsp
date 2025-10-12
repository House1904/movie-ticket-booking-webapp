<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách rạp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/seat.css">
</head>
<body>
<div class="container">
    <h2>Danh sách rạp chiếu phim</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Tên rạp</th>
            <th>Địa chỉ</th>
            <th>Số điện thoại</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="cinema" items="${cinemas}">
            <tr>
                <td>${cinema.id}</td>
                <td>${cinema.name}</td>
                <td>${cinema.address}</td>
                <td>${cinema.phone}</td>
                <td>
                    <button class="quanly-btn" onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=list&cinemaId=${cinema.id}'">Quản lý phòng chiếu</button>
                </td>
            </tr>
        </c:forEach>

        <c:choose>
            <c:when test="${not empty param.partnerId}">
                <h3>Rạp của Partner ID: ${partnerId}</h3>
            </c:when>
            <c:otherwise>
                <h3>Tất cả rạp</h3>
            </c:otherwise>
        </c:choose>
    </table>
</div>
</body>
</html>