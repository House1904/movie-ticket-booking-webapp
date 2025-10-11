<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Phim yêu thích</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/favorite.css">
</head>
<body>
    <h1>Danh sách phim yêu thích của bạn ❤️</h1>

    <section class="movie-grid">
        <c:if test="${empty favorites}">
            <p>Bạn chưa thêm phim nào vào danh sách yêu thích.</p>
        </c:if>

        <c:forEach var="fav" items="${favorites}">
            <a href="${pageContext.request.contextPath}/movieDetail?id=${fav.movie.id}" class="movie-card-link">
                <div class="movie-card">
                    <img src="${fav.movie.posterUrl}" alt="${fav.movie.title}">
                    <h3>${fav.movie.title}</h3>
                    <p>${fav.movie.genre}</p>
                </div>
            </a>
        </c:forEach>
    </section>
</body>
</html>
