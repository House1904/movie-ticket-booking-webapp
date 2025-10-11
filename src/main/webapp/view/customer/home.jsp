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
            <c:forEach var="banner" items="${banners}" varStatus="status">
                <c:if test="${banner.start_at <= now && banner.end_at >= now}">
                    <div class="banner-slide ${status.index == 0 ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}${banner.link_url}">
                            <img src="${banner.image_url}" alt="${banner.title}">
                        </a>
                    </div>
                </c:if>
            </c:forEach>
            <c:if test="${empty banners}">
                <!-- Default banner if no valid banners -->
                <div class="banner-slide active">
                    <a href="${pageContext.request.contextPath}/home">
                        <img src="https://colour.vn/wp-content/uploads/mau-banner-quang-cao-khuyen-mai.jpg" alt="Default Banner">
                    </a>
                </div>
            </c:if>
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
    const slides = document.querySelectorAll('.banner-slide');
    const totalSlides = slides.length;

    function changeSlide(direction) {
        currentSlide = (currentSlide + direction + totalSlides) % totalSlides;
        slides.forEach((slide, index) => {
            slide.style.display = index === currentSlide ? 'block' : 'none';
        });
    }

    if (totalSlides > 0) {
        slides.forEach(slide => slide.style.display = 'none');
        slides[0].style.display = 'block';
        setInterval(() => {
            currentSlide = (currentSlide + 1) % totalSlides;
            slides.forEach((slide, index) => {
                slide.style.display = index === currentSlide ? 'block' : 'none';
            });
        }, 3000);
    }
</script>

<%@ include file="../../common/footer.jsp" %>
</body>
</html>