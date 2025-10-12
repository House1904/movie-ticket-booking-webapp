<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Phim yêu thích</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/favorite.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
</head>
<body>
    <h1>Danh sách phim yêu thích của bạn ❤️</h1>

    <c:if test="${empty favorites}">
        <p>Bạn chưa thêm phim nào vào danh sách yêu thích.</p>
    </c:if>

    <section class="movie-grid">
        <c:forEach var="fav" items="${favorites}">
            <div class="movie-card" data-id="${fav.movie.id}">
                <a href="${pageContext.request.contextPath}/movieDetail?id=${fav.movie.id}" class="movie-card-link">
                    <img src="${fav.movie.posterUrl}" alt="${fav.movie.title}" class="movie-poster">
                    <h3>${fav.movie.title}</h3>
                    <p>
                        <c:forEach var="g" items="${fav.movie.genre}" varStatus="status">
                            ${g}<c:if test="${!status.last}">, </c:if>
                        </c:forEach>
                    </p>
                </a>
                <button class="favorite-btn favorited" data-id="${fav.movie.id}" title="Bỏ yêu thích">
                    <i class="fa fa-heart"></i>
                </button>
            </div>
        </c:forEach>
    </section>

    <script>
        // Toggle favorite
        document.querySelectorAll('.favorite-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                const movieId = this.dataset.id;
                const btnEl = this;

                fetch('<%=request.getContextPath()%>/favorite', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: 'movieId=' + movieId
                })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'added') {
                        btnEl.classList.add('favorited');
                    } else if (data.status === 'removed') {
                        btnEl.classList.remove('favorited');
                        // Optionally remove the card from favorite page
                        btnEl.closest('.movie-card').remove();
                    } else if (data.message === 'not_logged_in') {
                        alert("Vui lòng đăng nhập để thêm yêu thích!");
                        window.location.href = '<%=request.getContextPath()%>/common/login.jsp';
                    }
                }).catch(err => console.error(err));
            });
        });
    </script>
<%@ include file="../../common/footer.jsp" %>
</body>
</html>