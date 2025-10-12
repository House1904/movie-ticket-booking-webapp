<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/headerPartner.css">
<header class="partner-header">
    <div class="logo">
        <a>
            <img src="${pageContext.request.contextPath}/assets/images/LogoWeb.png" alt="Logo Rạp Phim">
        </a>
    </div>
    <nav class="menu">
        <ul>
            <li><a href="${pageContext.request.contextPath}/dashboard">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/manageCinema">Quản lý Rạp Phim</a></li>
        </ul>
    </nav>
    <div class="user-info">
        <c:if test="${not empty user}">
            <span>Xin chào, ${user.fullName}</span>
        </c:if>
        <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-login">Đăng xuất</a>
    </div>
</header>