<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quản lý Đối tác</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/partner.css">
</head>
<body>
<div class="container">
    <h2>Quản lý Đối tác</h2>

    <div class="form-section">
        <!-- Form thêm/sửa đối tác -->
        <div class="add-form">
            <h3>${partner == null ? 'Thêm Đối tác Mới' : 'Chỉnh sửa Đối tác'}</h3>
            <form action="admin" method="post">
                <input type="hidden" name="action" value="${partner == null ? 'add' : 'update'}">
                <input type="hidden" name="id" value="${partner.id}">
                <input type="text" name="fullName" value="${partner.fullName}" placeholder="Họ và Tên" required>
                <input type="email" name="email" value="${partner.email}" placeholder="Email" required>
                <input type="text" name="phone" value="${partner.phone}" placeholder="Số điện thoại" required>
                <select name="brand" required>
                    <option value="" ${partner.brand == null ? 'selected' : ''}>Thương hiệu</option>
                    <option value="CGV" ${partner.brand == 'CGV' ? 'selected' : ''}>CGV</option>
                    <option value="Lotte" ${partner.brand == 'Lotte' ? 'selected' : ''}>Lotte</option>
                    <option value="Galaxy" ${partner.brand == 'Galaxy' ? 'selected' : ''}>Galaxy</option>
                    <option value="BHD" ${partner.brand == 'BHD' ? 'selected' : ''}>BHD</option>
                    <option value="Beta" ${partner.brand == 'Beta' ? 'selected' : ''}>Beta</option>
                    <option value="Cinestar" ${partner.brand == 'Cinestar' ? 'selected' : ''}>Cinestar</option>
                    <option value="Đống Đa" ${partner.brand == 'Đống Đa' ? 'selected' : ''}>Đống Đa</option>
                    <option value="Mega GS" ${partner.brand == 'Mega GS' ? 'selected' : ''}>Mega GS</option>
                </select>
                <button type="submit">Lưu</button>
            </form>
        </div>

        <!-- Danh sách đối tác -->
        <div class="partner-list">
            <div class="cards" id="cardsContainer">
                <c:forEach var="p" items="${partners}">
                    <div class="card">
                        <div class="actions">
                            <a href="admin?action=edit&id=${p.id}" title="Chỉnh sửa">✏️</a>
                            <form action="admin" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${p.id}">
                                <button type="submit" onclick="return confirm('Bạn có muốn xóa đối tác này không?')" style="margin: 0; padding: 0;">🗑️</button>
                            </form>
                        </div>

                        <div class="brand-title">${p.brand}</div>
                        <h4>${p.fullName}</h4>
                        <small>${p.email}</small>
                        <small>${p.phone}</small>

                        <!-- ✅ Thông tin tài khoản -->
                        <c:if test="${not empty p.account}">
                            <div class="account-info">
                                <small><b>Tên đăng nhập:</b> ${p.account.userName}</small><br>
                                <small><b>Mật khẩu:</b> defaultPass123</small>
                            </div>
                        </c:if>

                        <!-- 🔘 Công tắc kích hoạt -->
                        <div class="switch">
                            <label>
                                <input type="checkbox" ${p.is_activate ? 'checked' : ''}>
                                <span class="slider"></span>
                            </label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- ⚠️ Cảnh báo lỗi -->
<c:if test="${param.error == 'email_exists'}">
    <script>alert("Email đã tồn tại! Vui lòng nhập email khác.");</script>
</c:if>
<c:if test="${param.error == 'phone_exists'}">
    <script>alert("Số điện thoại đã tồn tại!");</script>
</c:if>
<c:if test="${param.error == 'invalid_phone'}">
    <script>alert("Số điện thoại phải gồm đúng 10 chữ số!");</script>
</c:if>
<c:if test="${param.error == 'invalid_email'}">
    <script>alert("Email không hợp lệ!");</script>
</c:if>
</body>
</html>