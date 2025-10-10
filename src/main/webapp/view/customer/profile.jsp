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

    <style>
        body {
            background: linear-gradient(135deg, #dfe9ff, #ffffff);
            font-family: 'Poppins', sans-serif;
        }
        .profile-card {
            max-width: 700px;
            margin: 60px auto;
            background-color: #fff;
            border-radius: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            padding: 40px;
        }
        .profile-avatar {
            width: 130px;
            height: 130px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #0d6efd;
        }
        .profile-title {
            font-weight: 700;
            color: #0d6efd;
        }
        .btn-save {
            background-color: #0d6efd;
            border: none;
            transition: 0.3s;
        }
        .btn-save:hover {
            background-color: #084fc1;
        }
        label {
            font-weight: 500;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="profile-card">
        <h2 class="text-center mb-4 profile-title">Hồ sơ cá nhân</h2>

        <form action="${pageContext.request.contextPath}/profile" method="post">
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
                           class="form-control" value="${user.email}" readonly>
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
            </div>

        </form>
    </div>

</div>


</body>
</html>
