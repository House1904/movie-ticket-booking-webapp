<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    model.User user = (model.User) session.getAttribute("user");
    java.util.Map<Long, Boolean> favoriteMap = new java.util.HashMap<>();
    if (user != null) {
        service.FavoriteService favoriteService = new service.FavoriteService(
            new dao.FavoriteDAO(util.DBConnection.getEmFactory().createEntityManager())
        );
        java.util.List<model.Favorite> favorites = favoriteService.getFavoritesByUser(user);
        for (model.Favorite f : favorites) {
            favoriteMap.put(f.getMovie().getId(), true);
        }
    }
    else {
        String currentURL = request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        session.setAttribute("redirectAfterLogin", currentURL);
    }
    pageContext.setAttribute("favoriteMap", favoriteMap);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Phim </title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/movie.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
        .favorite-btn {
            background: none;
            border: none;
            cursor: pointer;
            font-size: 1.5rem;
            color: #aaa; /* màu xám mặc định */
        }
        .favorite-btn.favorited {
            color: red; /* màu đỏ nếu đã thích */
        }
    </style>
</head>
<body>
<header class="header1">
    <h1>${h1}</h1>
    <p>${p}</p>
</header>

<section class="filter-bar">
    <select id="filterSelect">
        <option value="all">Tất cả</option>
        <c:forEach var="g" items="${genreList}">
            <option value="${g}">${g}</option>
        </c:forEach>
    </select>
</section>

<section class="movie-grid">
    <c:forEach var="movie" items="${movies}">
        <div class="movie-card" data-id="${movie.id}" data-genres="${movie.genre}">
            <!-- Link bao poster -->
            <a href="${pageContext.request.contextPath}/movieDetail?id=${movie.id}" class="movie-card-link">
                <img src="${movie.posterUrl}" alt="${movie.title}" class="movie-poster">
                <h3>${movie.title}</h3>
                <p>${movie.genre}</p>
                <p>Độ tuổi: ${movie.ageLimit}</p>
                <p>${movie.duration} phút</p>
            </a>

            <!-- Nút trái tim -->
            <button class="favorite-btn ${favoriteMap[movie.id] ? 'favorited' : ''}"
                    data-id="${movie.id}" title="Thêm vào yêu thích">
                <i class="fa fa-heart"></i>
            </button>
        </div>
    </c:forEach>
</section>

<script>
    document.getElementById('filterSelect').addEventListener('change', function() {
        const selectedGenre = this.value.toLowerCase();

        document.querySelectorAll('.movie-card').forEach(card => {
            // Lấy danh sách genre từ attribute và convert thành mảng
            const genreData = card.dataset.genres ? card.dataset.genres.toLowerCase() : "";

            // Nếu chọn "all" hoặc không chọn gì thì hiện hết
            if (selectedGenre === 'all' || !selectedGenre) {
                card.style.display = 'block';
            }
            // Nếu movie chứa genre được chọn (so sánh substring cho dễ)
            else if (genreData.includes(selectedGenre)) {
                card.style.display = 'block';
            }
            // Không match -> ẩn
            else {
                card.style.display = 'none';
            }
        });
    });

    // Xử lý click trái tim
        document.querySelectorAll('.favorite-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                const movieId = this.dataset.id;
                const btnEl = this;

                fetch('<%=request.getContextPath()%>/favorite', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: 'movieId=' + movieId
                })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'added') {
                        btnEl.classList.add('favorited');
                    } else if (data.status === 'removed') {
                        btnEl.classList.remove('favorited');
                    } else if (data.message === 'not_logged_in') {
                        alert("Vui lòng đăng nhập để thêm yêu thích!");
                        window.location.href = '<%=request.getContextPath()%>/common/login.jsp';
                    }
                })
                .catch(err => console.error(err));
            });
        });
</script>
</body>
</html>
