<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản lý Rạp phim</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/partnerCinema.css">
</head>
<body>
<div class="container">
    <h2>🎬 Quản lý Rạp phim</h2>

    <c:if test="${not empty error}">
        <div style="color: red; margin-bottom: 10px;">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/manageCinema" method="post">
        <c:if test="${cinema.id != 0}">
            <input type="hidden" name="id" value="${cinema.id}">
        </c:if>
        <label>Tên rạp:</label>
        <input type="text" name="name" value="${cinema.name}" required>
        <label>Địa chỉ:</label>
        <input type="text" name="address" value="${cinema.address}" required>
        <label>Số điện thoại:</label>
        <input type="text" name="phone" value="${cinema.phone}" required>
        <button type="submit">💾 Lưu</button>
    </form>

    <hr>
    <h3>Danh sách rạp phim</h3>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Tên rạp</th>
            <th>Địa chỉ</th>
            <th>Điện thoại</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${cinemas}">
            <tr>
                <td>${c.id}</td>
                <td>${c.name}</td>
                <td>${c.address}</td>
                <td>${c.phone}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/manageCinema?action=edit&id=${c.id}"
                       class="text-blue-500">Sửa</a>
                    <a href="${pageContext.request.contextPath}/manageCinema?action=delete&id=${c.id}"
                       class="text-red-500"
                       onclick="return confirm('⚠️ Bạn có chắc chắn muốn xóa rạp không');">
                        Xóa
                    </a>
                    <a href="${pageContext.request.contextPath}/manageCinema?action=mnAudit&cinemaId=${c.id}"
                       class="text-green-500">Xem Phòng Chiếu</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>