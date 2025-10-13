<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Đổi Mật Lhẩu</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/auth.css">
</head>
<body>
<div class="login-container">
    <h2>Đổi Mật Khẩu</h2>

    <!-- Gửi form đến servlet /auth-->
    <form action="${pageContext.request.contextPath}/auth" method="post">
        <label for="username">Tên đăng nhập:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Mật khẩu cũ:</label>
        <div class="password-wrapper">
            <script src="${pageContext.request.contextPath}/assets/js/toogleHiddenPassword.js"></script>
            <input type="password" id="password" name="password" required>
            <a href="javascript:void(0);" class="toggle-password" onclick="togglePassword()">👁</a>
        </div>

        <label for="password">Mật khẩu mới:</label>
        <div class="password-wrapper">
            <script src="${pageContext.request.contextPath}/assets/js/toogleHiddenPassword.js"></script>
            <input type="password" id="password" name="password" required>
            <a href="javascript:void(0);" class="toggle-password" onclick="togglePassword()">👁</a>
        </div>

        <input type="hidden" name="action" value="changePassword">
        <button type="submit">Đổi mật khẩu</button>
    </form>

    <!-- Hiển thị lỗi nếu có -->
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <a class="register-link" href="${pageContext.request.contextPath}/common/register.jsp">
        Chưa có tài khoản? Đăng ký ngay
    </a>
</div>
<script></script>
</body>
</html>