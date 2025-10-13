<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
<head>
    <title>Danh sách ưu đãi 🎁</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Icon Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/promotion.css">
</head>

<body>
<header class="main-header"><%@ include file="header.jsp" %></header>
<div class="container py-5 fade-in" style="margin-top: 150px">
    <div class="card border-0 shadow-lg p-4 rounded-4">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
            <h3 class="fw-semibold text-primary mb-0">
                <i class="bi bi-gift-fill me-2"></i>Danh sách ưu đãi
            </h3>
            <a href="promotion?action=new" class="btn btn-success btn-lg rounded-3 px-4">
                <i class="bi bi-plus-circle me-1"></i> Thêm mới
            </a>
        </div>

        <!-- Table -->
        <div class="table-responsive">
            <table class="table table-hover align-middle text-center shadow-sm rounded-3 overflow-hidden">
                <thead class="table-primary text-uppercase small fw-semibold">
                <tr>
                    <th>ID</th>
                    <th>Tên ưu đãi</th>
                    <th>Loại</th>
                    <th>Giá trị</th>
                    <th>Thời gian áp dụng</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty promotions}">
                        <tr>
                            <td colspan="7" class="text-muted py-4 fst-italic">
                                <i class="bi bi-info-circle me-2"></i>Chưa có ưu đãi nào trong hệ thống.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${promotions}">
                            <tr>
                                <td class="fw-medium">${p.id}</td>
                                <td class="text-start">${p.name}</td>
                                <td>
                                    <span class="badge bg-info text-dark px-3 py-2">${p.promotionType}</span>
                                </td>
                                <td><strong>${p.discountValue}</strong></td>
                                <td>
                                    <small class="text-muted d-block">${p.startAt}</small>
                                    <i class="bi bi-arrow-right"></i>
                                    <small class="text-muted d-block">${p.endAt}</small>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.status == 'ACTIVE'}">
                                            <span class="badge bg-success px-3 py-2">Đang hoạt động</span>
                                        </c:when>
                                        <c:when test="${p.status == 'EXPIRED'}">
                                            <span class="badge bg-secondary px-3 py-2">Hết hạn</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark px-3 py-2">Sắp diễn ra</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="promotion?action=edit&id=${p.id}"
                                       class="btn btn-sm btn-outline-warning me-2 px-3 rounded-3">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="promotion?action=delete&id=${p.id}"
                                       class="btn btn-sm btn-outline-danger px-3 rounded-3"
                                       onclick="return confirm('Bạn có chắc muốn xóa ưu đãi này?')">
                                        <i class="bi bi-trash3"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
