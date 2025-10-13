<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Đăng ký</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/auth.css">
</head>
<body>
<div class="login-container">
<h2>Đăng ký</h2>
<form action="${pageContext.request.contextPath}/auth" method="post">
    <label>Tên đăng nhập:</label><input type="text" name="username" required><br>
    <label>Mật khẩu:</label><input type="password" name="password" required><br>
    <label>Họ Tên:</label><input type="text" name="fullname" required><br>
    <label>Ngày Sinh:</label><input type="date" name="dateOfBirth" required>
    <label>Email:</label><input type="email" name="email" required><br>
    <label>Số Điện Thoại:</label><input type="tel" name="phone" required><br>
    <label>Avatar Url:</label><input type="url" name="avatarUrl"><br>
    <input type="hidden" name="action" value="signup">
    <button type="submit">Đăng ký</button>
</form>
<p style="color:red;"><c:out value="${error}" /></p>
<a href="${pageContext.request.contextPath}/common/login.jsp">Đã có tài khoản? Đăng nhập</a>
</div>
</body>
</html>