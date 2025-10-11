<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Hồ sơ cá nhân</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


        <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/customerProfile.css">

</head>

<body>
<div class="container">
    <div class="profile-card">
        <h2 class="text-center mb-4 profile-title">Hồ sơ cá nhân</h2>

        <form action="${pageContext.request.contextPath}/profile" method="post">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <div class="text-center mb-4">
                <img src="${empty user.avatarUrl ? 'https://via.placeholder.com/130' : user.avatarUrl}"
                     alt="Avatar" class="profile-avatar mb-2">
                <div class="mt-2">
                    <input type="text" name="avatarUrl" class="form-control form-control-sm"
                           placeholder="Nhập URL ảnh đại diện (tùy chọn)" value="${user.avatarUrl}">
                </div>
            </div>

            <div class="row g-3">
                <div class="col-md-6">
                    <label for="fullName">Họ tên</label>
                    <input type="text" name="fullName" id="fullName"
                           class="form-control" value="${user.fullName}">
                </div>

                <div class="col-md-6">
                    <label for="email">Email</label>
                    <input type="email" id="email"
                           class="form-control" value="${user.email}">
                </div>

                <div class="col-md-6">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" name="phone" id="phone"
                           class="form-control" value="${user.phone}">
                </div>

                <div class="col-md-6">
                    <label for="dateOfBirth">Ngày sinh</label>
                    <input type="date" name="dateOfBirth" id="dateOfBirth"
                           value="${fn:substring(user.dateOfBirth, 0, 10)}"
                           class="form-control">
                </div>
            </div>

            <div class="form-check mt-3">
                <input type="checkbox" class="form-check-input" id="isMemberShip"
                       name="isMemberShip" ${user.memberShip ? "checked" : ""}>
                <label class="form-check-label" for="isMemberShip">Thành viên thân thiết</label>
            </div>

            <div class="text-center mt-4">
                <button type="submit" class="btn btn-save text-white px-5 py-2">
                    <i class="bi bi-save"></i> Cập nhật
                </button>
            </div>
            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/ticketHistory" class="btn btn-outline-info">
                    Xem lịch sử vé
                </a>
                <!-- Nút Đổi mật khẩu -->
                <button type="button" class="btn btn-outline-warning"
                        onclick="window.location.href='${pageContext.request.contextPath}/changePassword.jsp'">
                    Đổi mật khẩu
                </button>

            </div>

        </form>
    </div>

</div>


</body>
</html>
