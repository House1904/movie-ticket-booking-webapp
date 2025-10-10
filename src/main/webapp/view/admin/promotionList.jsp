<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách ưu đãi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>🎁 Danh sách ưu đãi</h3>
            <a href="promotion?action=new" class="btn btn-success">➕ Thêm mới</a>
        </div>
        <table class="table table-striped table-bordered">
            <thead class="table-primary">
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Loại</th>
                <th>Giá trị</th>
                <th>Thời gian</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${promotions}">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.promotionType}</td>
                    <td>${p.discountValue}</td>
                    <td>${p.startAt} → ${p.endAt}</td>
                    <td>${p.status}</td>
                    <td>
                        <a href="promotion?action=edit&id=${p.id}" class="btn btn-sm btn-warning">Sửa</a>
                        <a href="promotion?action=delete&id=${p.id}" class="btn btn-sm btn-danger"
                           onclick="return confirm('Xóa ưu đãi này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
