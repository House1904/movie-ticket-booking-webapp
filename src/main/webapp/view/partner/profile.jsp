<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Thông tin đối tác</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <style>
        body {
            background: linear-gradient(135deg, #f3f7ff, #ffffff);
            font-family: 'Poppins', sans-serif;
        }
        .partner-card {
            max-width: 750px;
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
        .partner-title {
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
    <div class="partner-card">
        <h2 class="text-center mb-4 partner-title">Thông tin đối tác</h2>

        <form action="${pageContext.request.contextPath}/partnerProfile" method="post">
            <div class="text-center mb-4">
                <img src="${empty partner.avatarUrl ? 'https://via.placeholder.com/130' : partner.avatarUrl}"
                     alt="Avatar" class="profile-avatar mb-2">
            </div>

            <div class="row g-3">
                <div class="col-md-6">
                    <label for="fullName">Người đại diện</label>
                    <input type="text" name="fullName" id="fullName"
                           class="form-control" value="${partner.fullName}">
                </div>

                <div class="col-md-6">
                    <label for="email">Email</label>
                    <input type="email" id="email"
                           class="form-control" value="${partner.email}" readonly>
                </div>

                <div class="col-md-6">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" name="phone" id="phone"
                           class="form-control" value="${partner.phone}">
                </div>

                <div class="col-md-6">
                    <label for="brand">Thương hiệu</label>
                    <input type="text" name="brand" id="brand"
                           class="form-control" value="${partner.brand}">
                </div>
            </div>

            <div class="form-check mt-3">
                <input type="checkbox" class="form-check-input" id="is_activate"
                       name="is_activate" ${partner.is_activate ? "checked" : ""}>
                <label class="form-check-label" for="is_activate">Tài khoản đã kích hoạt</label>
            </div>

            <!-- Danh sách rạp -->
            <div class="mt-4">
                <h5 class="fw-semibold text-secondary mb-2">Danh sách rạp quản lý:</h5>
                <c:choose>
                    <c:when test="${empty partner.cinemas}">
                        <p class="text-muted fst-italic">Chưa có rạp nào được liên kết.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="list-group">
                            <c:forEach var="cinema" items="${partner.cinemas}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>
                                        🎬 <strong>${cinema.name}</strong>
                                        <small class="text-muted">(${cinema.address})</small>
                                    </span>
                                    <a href="${pageContext.request.contextPath}/cinema/detail?id=${cinema.id}" class="btn btn-sm btn-outline-primary">Xem</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="text-center mt-4">
                <button type="submit" class="btn btn-save text-white px-5 py-2">
                    <i class="bi bi-save"></i> Cập nhật
                </button>
            </div>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/promotion" class="btn btn-outline-primary">
                    Quản lý ưu đãi / Voucher
                </a>
            </div>

        </form>
    </div>
</div>
</body>
</html>
