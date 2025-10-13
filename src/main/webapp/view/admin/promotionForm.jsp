<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
<head>
    <title>${promotion != null ? "Cập nhật ưu đãi" : "Thêm ưu đãi mới"} 🎁</title>

    <!-- Bootstrap + Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/promotion.css">
</head>

<body>
<div class="container py-5 fade-in">
    <div class="col-lg-7 mx-auto">
        <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5">

            <!-- Tiêu đề -->
            <div class="text-center mb-4">
                <h3 class="fw-semibold text-primary mb-1">
                    <i class="bi bi-gift-fill me-2"></i>
                    ${promotion != null ? "Cập nhật ưu đãi" : "Thêm ưu đãi mới"}
                </h3>
                <p class="text-muted small">Vui lòng nhập đầy đủ thông tin ưu đãi trước khi lưu</p>
            </div>

            <!-- Hiển thị lỗi -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center fw-semibold rounded-3 shadow-sm fade-in mb-4">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                </div>
            </c:if>

            <!-- Form -->
            <form action="promotion" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="id" value="${promotion.id}"/>

                <!-- Tên ưu đãi -->
                <div class="mb-3">
                    <label class="form-label fw-medium">
                        <i class="bi bi-tag-fill me-1 text-primary"></i> Tên ưu đãi
                    </label>
                    <input type="text" name="name" class="form-control" value="${promotion.name}" required>
                    <div class="invalid-feedback">Vui lòng nhập tên ưu đãi.</div>
                </div>

                <!-- Loại khuyến mãi -->
                <div class="mb-3">
                    <label class="form-label fw-medium">
                        <i class="bi bi-sliders2 me-1 text-primary"></i> Loại khuyến mãi
                    </label>
                    <select name="promotionType" class="form-select" required>
                        <option value="PERCENT" ${promotion.promotionType == 'PERCENT' ? "selected" : ""}>Giảm theo %</option>
                        <option value="AMOUNT" ${promotion.promotionType == 'AMOUNT' ? "selected" : ""}>Giảm cố định</option>
                    </select>
                </div>

                <!-- Giá trị giảm -->
                <div class="mb-3">
                    <label class="form-label fw-medium">
                        <i class="bi bi-cash-coin me-1 text-primary"></i> Giá trị giảm
                    </label>
                    <input type="number" step="0.01" name="discountValue" class="form-control"
                           value="${promotion.discountValue}" required>
                </div>

                <!-- Giá trị tối thiểu & tối đa -->
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-medium">
                            <i class="bi bi-graph-down me-1 text-primary"></i> Giá trị tối thiểu
                        </label>
                        <input type="number" step="0.01" name="minTotalPrice" class="form-control"
                               value="${promotion.minTotalPrice}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-medium">
                            <i class="bi bi-graph-up me-1 text-primary"></i> Giá trị tối đa
                        </label>
                        <input type="number" step="0.01" name="maxTotalPrice" class="form-control"
                               value="${promotion.maxTotalPrice}">
                    </div>
                </div>

                <!-- Thời gian -->
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-medium">
                            <i class="bi bi-calendar-date me-1 text-primary"></i> Ngày bắt đầu
                        </label>
                        <input type="datetime-local" name="startAt" class="form-control"
                               value="${promotion.startAt}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-medium">
                            <i class="bi bi-calendar-event me-1 text-primary"></i> Ngày kết thúc
                        </label>
                        <input type="datetime-local" name="endAt" class="form-control"
                               value="${promotion.endAt}" required>
                    </div>
                </div>

                <!-- Trạng thái -->
                <div class="mb-3">
                    <label class="form-label fw-medium">
                        <i class="bi bi-toggle-on me-1 text-primary"></i> Trạng thái
                    </label>
                    <select name="status" class="form-select">
                        <option value="ACTIVE" ${promotion.status == 'ACTIVE' ? "selected" : ""}>Hoạt động</option>
                        <option value="INACTIVE" ${promotion.status == 'INACTIVE' ? "selected" : ""}>Tạm ngưng</option>
                        <option value="EXPIRED" ${promotion.status == 'EXPIRED' ? "selected" : ""}>Hết hạn</option>
                    </select>
                </div>

                <!-- Hành động -->
                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-primary btn-lg px-5 me-2 rounded-3 shadow-sm">
                        <i class="bi bi-save me-1"></i> Lưu
                    </button>
                    <a href="promotion" class="btn btn-outline-secondary btn-lg px-5 rounded-3">
                        <i class="bi bi-x-circle me-1"></i> Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Validation -->
<script>
    (() => {
        'use strict';
        const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', e => {
                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
</script>
</body>
</html>
