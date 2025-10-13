<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/headerPartner.css">
<header class="partner-header">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/dashboard">
            <img src="${pageContext.request.contextPath}/assets/images/LogoWeb.png" alt="Logo Rạp Phim">
        </a>
    </div>
    <nav class="menu">
        <ul>
            <li><a href="${pageContext.request.contextPath}/manage-cinema">Quản lý Rạp Phim</a></li>
            <li><a href="${pageContext.request.contextPath}/manage-auditorium">Quản lý Phòng Chiếu</a></li>
            <li><a href="${pageContext.request.contextPath}/manage-showtime">Quản lý Suất Chiếu</a></li>
            <li><a href="${pageContext.request.contextPath}/manage-seat">Quản lý Ghế Ngồi</a></li>
        </ul>
    </nav>
    <div class="cloud">
        <li class="dropdown">
            <a href="#" class="dropbtn">👤 ${user.fullName}</a>
            <ul class="dropdown-content">
                <li><a href="partnerProfile">View Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </li>
    </div>
</header>