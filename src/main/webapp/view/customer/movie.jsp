<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Phim đang chiếu</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/movie.css">
</head>
<body>
<header class="header1">
    <h1>Phim đang chiếu</h1>
    <p>Danh sách các phim hiện đang chiếu trên toàn quốc</p>
</header>

<section class="filter-bar">
    <select id="filterSelect">
        <option value="popular">Phổ biến</option>
        <option value="newest">Mới nhất</option>
        <option value="all">Tất cả</option>
        <option value="action">Hành động</option>
        <option value="comedy">Hài</option>
        <option value="family">Gia đình</option>
        <option value="romance">Tình cảm</option>
    </select>
</section>

<section class="movie-grid">
    <%-- Mẫu card phim --%>
        <c:forEach var="movie" items="${movies}">
            <a href="${pageContext.request.contextPath}/movieDetail?id=${movie.id}" class="movie-card-link">
                <div class="movie-card">
                    <img src="${movie.posterUrl}" alt="${movie.title}">
                    <h3>${movie.title}</h3>
                    <p>${movie.genre}</p>
                </div>
            </a>
        </c:forEach>
    <%-- thêm nhiều card tùy data backend --%>
</section>
</body>
</html>
