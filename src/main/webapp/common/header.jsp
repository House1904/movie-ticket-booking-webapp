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
        <a href="home">
            <img alt="ICON" src="https://sdmntpraustraliaeast.oaiusercontent.com/files/00000000-4224-61fa-a706-84a32c84c64b/raw?se=2025-09-16T09%3A03%3A03Z&sp=r&sv=2024-08-04&sr=b&scid=4678314f-b3cb-5c9a-a719-6fc9c2771491&skoid=b7fc319f-b93c-4fac-ba5f-14fdc3f9209f&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2025-09-16T04%3A15%3A37Z&ske=2025-09-17T04%3A15%3A37Z&sks=b&skv=2024-08-04&sig=qWMK7lupkkFSdKhRhkz7CS41/KtNLJp5zyK6cOuKUA0%3D">
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
                <li><a href="showingMovie.jsp">Đang chiếu🎥</a></li>
                <li><a href="coming_soon.jsp">Sắp chiếu</a></li>
            </ul>
        </li>

        <!-- Rạp (POPUP) -->
        <li>
            <a href="cinema" onclick="openPopup(); return false;">RẠP 🎬</a>
            <jsp:include page="/view/customer/cinemaPopup.jsp" />
        </li>

        <!-- Dropdown Tin tức -->
        <li class="dropdown">
            <a href="#">TIN TỨC</a>
            <ul class="dropdown-content">
                <li><a href="promo.jsp">Khuyến mãi</a></li>
                <li><a href="blog.jsp">Cộng đồng</a></li>
            </ul>
        </li>
    </ul>
    <ul class="cloud">
        <!-- Search box -->
        <li>
            <form class="search-box" action="search.jsp" method="get">
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
            <div class="dropdown-content">
                <a href="profile.jsp">View Profile</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a>
            </div>
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
