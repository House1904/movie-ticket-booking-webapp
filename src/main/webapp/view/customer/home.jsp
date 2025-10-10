<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="model.User" %>
<%@ page import="model.Account" %>
<%
    Account account = (Account) session.getAttribute("account");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Movie Booking - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>
<%@ include file="../../common/header.jsp" %>

<div>
    <div class="nav-home">
        <div class="nav-left">
            <p><img src="<%= request.getContextPath() %>/assets/images/vitri.png" alt="icon">Thành phố Hồ Chí Minh</p>
        </div>
    </div>

    <!-- Banner -->
    <div class="banner-container">
        <div class="banner">
            <img id="bannerImg" src="https://colour.vn/wp-content/uploads/mau-banner-quang-cao-khuyen-mai.jpg" alt="Quảng cáo">
            <button class="banner-btn prev" onclick="changeSlide(-1)">&#10094;</button>
            <button class="banner-btn next" onclick="changeSlide(1)">&#10095;</button>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Now Showing Section -->
        <div class="section-header">
            <p class="section-title">Phim Đang Chiếu</p>
            <a href="nowShowingPosters.jsp" class="view-more">Xem thêm</a>
        </div>

        <!-- Now Showing Section -->
        <div class="now-showing-section">
            <button class="carousel-btn prev" onclick="scrollCarousel(-1)">&#10094;</button>
            <div class="now-showing-posters">
                <c:forEach var="movie" items="${nowShowingMovies}">
                    <div class="now-showing-details">
                        <div class="poster-container">
                            <img src="${movie.posterUrl}" alt="${movie.title}">
                            <div class="movie-tooltip">
                                <h3>${movie.title}</h3>
                                <p><strong>Thể loại:</strong> ${movie.genre}</p>
                                <p><strong>Năm phát hành:</strong> ${movie.releaseDate}</p>
                                <p><strong>Thời lượng: ${movie.duration} phút</strong></p>
                                <p><strong>Ngôn ngữ:</strong> ${movie.language}</p>
                                <p><strong>Diễn viên:</strong> ${movie.actor}</p>
                            </div>
                        </div>
                        <p>${movie.title} <c:if test="${not empty movie.ageLimit}">(${movie.ageLimit})</c:if></p>
                        <div class="button-group">
                            <a class="movie-btn book-ticket" href="${pageContext.request.contextPath}/selectShowtime?movieId=${movie.id}">Đặt Vé</a>
                            <c:if test="${not empty movie.trailerUrl}">
                                <a class="movie-btn trailer" target="_blank" href="${movie.trailerUrl}">Trailer</a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <button class="carousel-btn next" onclick="scrollCarousel(1)">&#10095;</button>
        </div>

        <!-- Upcoming Section -->
        <div class="section-header">
            <p class="section-title">Phim Sắp Chiếu</p>
            <a href="upcomingPosters.jsp" class="view-more">Xem thêm</a>
        </div>


        <!-- Upcoming Section -->
        <div class="upcoming-section">
            <button class="carousel-btn prev" onclick="scrollUpcoming(-1)">&#10094;</button>
            <div class="upcoming-posters">
                <c:forEach var="movie" items="${upcomingMovies}">
                    <div class="upcoming-details">
                        <div class="poster-container">
                            <img src="${movie.posterUrl}" alt="${movie.title}">
                            <div class="movie-tooltip">
                                <h3>${movie.title}</h3>
                                <p><strong>Thể loại:</strong> ${movie.genre}</p>
                                <p><strong>Năm phát hành:</strong> ${movie.releaseDate}</p>
                                <p><strong>Thời lượng: ${movie.duration} phút</strong></p>
                                <p><strong>Ngôn ngữ:</strong> ${movie.language}</p>
                                <p><strong>Diễn viên:</strong> ${movie.actor}</p>
                            </div>
                        </div>
                        <p>${movie.title} <c:if test="${not empty movie.ageLimit}">(${movie.ageLimit})</c:if></p>
                        <div class="button-group">
                            <c:if test="${not empty movie.trailerUrl}">
                                <a class="movie-btn trailer" target="_blank" href="${movie.trailerUrl}">Trailer</a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <button class="carousel-btn next" onclick="scrollUpcoming(1)">&#10095;</button>
        </div>
    </div>
</div>

<script>
    let currentSlide = 0;
    const slideImages = [
        'https://colour.vn/wp-content/uploads/mau-banner-quang-cao-khuyen-mai.jpg',
        'https://vending-cdn.kootoro.com/torov-cms/upload/image/1669367204523-gi%C3%A1%20qu%E1%BA%A3ng%20c%C3%A1o%20t%E1%BA%A1i%20r%E1%BA%A1p%20chi%E1%BA%BFu%20phim%20cgv.jpg',
        'https://hnm.1cdn.vn/2025/07/17/f65a255838/poster-mua-do.jpg'
    ];

    function changeSlide(direction) {
        currentSlide = (currentSlide + direction + slideImages.length) % slideImages.length;
        document.getElementById('bannerImg').src = slideImages[currentSlide];
    }

    setInterval(() => {
        currentSlide = (currentSlide + 1) % slideImages.length;
        document.getElementById('bannerImg').src = slideImages[currentSlide];
    }, 3000);

    function scrollCarousel(direction) {
        const container = document.querySelector('.now-showing-posters');
        const scrollAmount = 315;
        container.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
    }

    function scrollUpcoming(direction) {
        const container = document.querySelector('.upcoming-posters');
        const scrollAmount = 315;
        container.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
    }
</script>

</body>
<%@ include file="../../common/footer.jsp" %>
</html>