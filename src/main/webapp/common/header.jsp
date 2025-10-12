<%@ page import="model.Customer" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/styles.css">
    <script src="${pageContext.request.contextPath}/assets/js/popup.js"></script>
</head>
<body>
<div class="header">
    <!-- Logo -->
    <div class="logo">
        <a href="<%= request.getContextPath() %>/home">
            <img alt="ICON" src="<%= request.getContextPath() %>/assets/images/LogoWeb.png">
        </a>
    </div>
    <!-- Nút menu cho mobile -->
    <div class="menu-toggle" onclick="toggleMenu()">☰</div>
    <!-- Menu -->
    <ul class="nav" id="navMenu">
        <li>
            <a href="showtime"> SUẤT CHIẾU</a>
        </li>
        <!-- Dropdown Phim -->
        <li class="dropdown">
            <a href="#">PHIM 🍿▾</a>
            <ul class="dropdown-content">
                <li><a href="<%= request.getContextPath() %>/movie?action=showing">Đang chiếu🎥</a></li>
                <li><a href="<%= request.getContextPath() %>/movie?action=comming">Sắp chiếu</a></li>
            </ul>
        </li>

        <!-- Rạp (POPUP) -->
        <li>
            <a href="cinema" onclick="openPopup(); return false;">RẠP 🎬</a>
            <jsp:include page="/view/customer/cinemaPopup.jsp" />
        </li>

        <!-- Dropdown Tin tức -->
        <!-- Dropdown Rạp -->
        <li class="dropdown">
            <a href="#">TIN TỨC</a>
            <ul class="dropdown-content">
                <li><a href="<%= request.getContextPath() %>/public/articles">Tin tức</a></li>
                <li><a href="blog.jsp">Cộng đồng</a></li>
            </ul>
        </li>
    </ul>
    <ul class="cloud">
        <!-- Search box -->
        <li>
            <form action="movie" class="search-box" method="get">
                <input type="hidden" name="action" value="search">
                <input type="text" name="q" placeholder="Tìm phim...">
                <button type="submit">🔍</button>
            </form>
        </li>
        <li><a href="contact.jsp">LIÊN HỆ</a></li>
        <%
            Customer customer = (Customer) session.getAttribute("user");
            if (customer == null) {
        %>
        <li><a href="<%= request.getContextPath() %>/common/login.jsp" class="btn-login">LOGIN</a></li>
        <%
        } else {
        %>
        <!-- Nếu đã login -->
        <li class="dropdown">
            <a href="#" class="dropbtn">👤 <%= customer.getFullName() %></a>
            <ul class="dropdown-content">
                <li><a href="profile.jsp">View Profile</a></li>
                <li><a href="${pageContext.request.contextPath}/favorite">Favorite</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </li>
        <%
            }
        %>
    </ul>
</div>

<script>
    function toggleMenu(){
        document.getElementById("navMenu").classList.toggle("active");
    }
</script>
</body>
</html>
