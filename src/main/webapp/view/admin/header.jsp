<%@ page import="model.Customer" %>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/styles.css">
<script src="${pageContext.request.contextPath}/assets/js/popup.js"></script>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>


<div class="header">
    <!-- Logo -->
    <div class="logo">
        <a href="<%= request.getContextPath() %>/admin">
            <img alt="ICON" src="<%= request.getContextPath() %>/assets/images/LogoWeb.png">
        </a>
    </div>
    <!-- Nút menu cho mobile -->
    <div class="menu-toggle" onclick="toggleMenu()">☰</div>
    <!-- Menu -->
    <ul class="nav" id="navMenu">
        <!-- Dashboard -->
        <li>
            <a href="new.jsp">DASHBOARD</a>
        </li>
        <!-- Dropdown Quản lý hệ thống -->
        <li class="dropdown">
            <a href="#">QUẢN LÝ HỆ THỐNG ▾</a>
            <ul class="dropdown-content">
                <li><a href="${pageContext.request.contextPath}/admin?action=list">Quản lý đối tác</a></li>
                <li><a href="${pageContext.request.contextPath}/promotion">Quản lý khuyến mãi</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/articles">Quản lý tin tức</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/banners">Quản lý banner</a></li>
                <li><a href="<%= request.getContextPath() %>/manageMovie">Quản lý movie</a></li>
            </ul>
        </li>
    </ul>
    <ul class="cloud">
        <li><a href="<%= request.getContextPath() %>/auth?action=logout" class="btn-login">LOGOUT</a></li>
    </ul>
</div>

<script>
    function toggleMenu(){
        document.getElementById("navMenu").classList.toggle("active");
    }
</script>