<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Thêm / Sửa Ưu Đãi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <h3 class="text-center mb-4">🎁 ${promotion != null ? "Cập nhật ưu đãi" : "Thêm ưu đãi mới"}</h3>

        <form action="promotion" method="post">
            <input type="hidden" name="id" value="${promotion.id}"/>

            <div class="mb-3">
                <label>Tên ưu đãi</label>
                <input type="text" name="name" class="form-control" value="${promotion.name}">
            </div>

            <div class="mb-3">
                <label>Loại khuyến mãi</label>
                <select name="promotionType" class="form-control">
                    <option value="PERCENT" ${promotion.promotionType == 'PERCENT' ? "selected" : ""}>Giảm theo %</option>
                    <option value="AMOUNT" ${promotion.promotionType == 'AMOUNT' ? "selected" : ""}>Giảm cố định</option>
                </select>
            </div>

            <div class="mb-3">
                <label>Giá trị giảm</label>
                <input type="number" step="0.01" name="discountValue" class="form-control" value="${promotion.discountValue}">
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label>Giá trị tối thiểu</label>
                    <input type="number" step="0.01" name="minTotalPrice" class="form-control" value="${promotion.minTotalPrice}">
                </div>
                <div class="col-md-6 mb-3">
                    <label>Giá trị tối đa</label>
                    <input type="number" step="0.01" name="maxTotalPrice" class="form-control" value="${promotion.maxTotalPrice}">
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label>Bắt đầu</label>
                    <input type="datetime-local" name="startAt" class="form-control"
                           value="${promotion.startAt}">
                </div>
                <div class="col-md-6 mb-3">
                    <label>Kết thúc</label>
                    <input type="datetime-local" name="endAt" class="form-control"
                           value="${promotion.endAt}">
                </div>
            </div>

            <div class="mb-3">
                <label>Trạng thái</label>
                <select name="status" class="form-control">
                    <option value="ACTIVE" ${promotion.status == 'ACTIVE' ? "selected" : ""}>Hoạt động</option>
                    <option value="INACTIVE" ${promotion.status == 'INACTIVE' ? "selected" : ""}>Tạm ngưng</option>
                    <option value="EXPIRED" ${promotion.status == 'EXPIRED' ? "selected" : ""}>Hết hạn</option>
                </select>
            </div>

            <div class="text-center">
                <button class="btn btn-primary px-4">Lưu</button>
                <a href="promotion?action=cancel" class="btn btn-secondary px-4">Hủy</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
