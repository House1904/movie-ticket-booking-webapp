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
        <option value="Hành động">Hành động</option>
        <option value="Tình cảm">Hài</option>
        <option value="Gia đình">Gia đình</option>
        <option value="Kịch tính">Kịch tính</option>
    </select>
</section>

<section class="movie-grid">
    <%-- Mẫu card phim --%>
        <c:forEach var="movie" items="${movies}">
            <a href="${pageContext.request.contextPath}/movieDetail?id=${movie.id}" class="movie-card-link">
                <div class="movie-card" data-genres="${movie.genre}">
                    <img src="${movie.posterUrl}" alt="${movie.title}">
                    <h3>${movie.title}</h3>
                    <p>${movie.genre}</p>
                </div>
            </a>
        </c:forEach>
    <%-- thêm nhiều card tùy data backend --%>
</section>
<script>
    document.getElementById('filterSelect').addEventListener('change', function() {
        const selectedGenre = this.value.toLowerCase();

        document.querySelectorAll('.movie-card-link').forEach(card => {
            // Lấy danh sách genre từ attribute và convert thành mảng
            let genres = card.querySelector('.movie-card').dataset.genres
                .replace(/[\[\]]/g, '') // bỏ [ ]
                .split(',')
                .map(g => g.trim().toLowerCase());

            // Hiển thị nếu:
            // - Không chọn thể loại (tức là tất cả)
            // - Hoặc list của phim có chứa thể loại được chọn
            if (!selectedGenre || genres.includes(selectedGenre)) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>
