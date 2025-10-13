<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chọn Suất Chiếu - ${selectedMovie.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/selectShowtime.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
</head>
<body>
<%@ include file="../../common/header.jsp" %>

<div class="main-content">
    <div class="movie-showtime-container">
        <!-- Movie Info Section (Left) -->
        <div class="movie-info">
            <h2>${selectedMovie.title}</h2>
            <img src="${selectedMovie.posterUrl}" alt="${selectedMovie.title} Poster" class="movie-poster">

            <div class="action-buttons">
                <!-- Nút yêu thích -->
                <button id="favorite-btn"
                        class="favorite-btn ${isFavorite ? 'favorited' : ''}"
                        data-id="${selectedMovie.id}">
                    <i class="fa fa-heart"></i>
                    <span class="favorite-text">
                        ${isFavorite ? 'Bỏ yêu thích' : 'Yêu thích'}
                    </span>
                </button>


                <!-- Nút đánh giá -->
                <button id="rating-btn" class="rating-btn">
                    <i class="fa fa-star"></i> Đánh giá
                </button>
            </div>
        </div>

        <script>
            const favoriteBtn = document.getElementById('favorite-btn');
            if (favoriteBtn) {
                const favoriteText = favoriteBtn.querySelector('.favorite-text');

                favoriteBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const movieId = this.dataset.id;

                    fetch('<%=request.getContextPath()%>/favorite', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'movieId=' + movieId
                    })
                    .then(res => res.json())
                    .then(data => {
                        if (data.status === 'added') {
                            favoriteBtn.classList.add('favorited');
                            favoriteText.textContent = 'Bỏ yêu thích';
                        } else if (data.status === 'removed') {
                            favoriteBtn.classList.remove('favorited');
                            favoriteText.textContent = 'Yêu thích';
                        } else if (data.message === 'not_logged_in') {
                            alert("Vui lòng đăng nhập để thêm yêu thích!");
                            window.location.href = '<%=request.getContextPath()%>/common/login.jsp';
                        }
                    })
                    .catch(err => console.error(err));
                });
            }
        </script>

        <!-- Showtime Selection Section (Right) -->
        <div class="showtime-selection">
            <div class="date-selection">
                <form action="${pageContext.request.contextPath}/selectShowtime" method="get">
                    <input type="hidden" name="movieId" value="${selectedMovie.id}">
                    <label for="date">Chọn ngày:</label>
                    <input type="date" id="date" name="date" value="${selectedDate}" onchange="this.form.submit()">
                </form>
            </div>

            <c:if test="${not empty cinemas}">
                <div class="cinema-selection">
                    <h3>Chọn Rạp</h3>
                    <form action="${pageContext.request.contextPath}/selectShowtime" method="get">
                        <input type="hidden" name="movieId" value="${selectedMovie.id}">
                        <input type="hidden" name="date" value="${selectedDate}">
                        <select name="cinemaId" id="cinemaId" onchange="this.form.submit()">
                            <option value="">-- Chọn rạp --</option>
                            <c:forEach var="cinema" items="${cinemas}">
                                <option value="${cinema.id}" ${cinema.id == selectedCinemaId ? 'selected' : ''}>
                                        ${cinema.name} - ${cinema.address}
                                </option>
                            </c:forEach>
                        </select>
                    </form>

                    <c:if test="${not empty selectedCinemaId}">
                        <c:set var="showtimes" value="${sessionScope['showtimes_'.concat(selectedCinemaId)]}" />
                        <c:choose>
                            <c:when test="${not empty showtimes}">
                                <h5>Suất Chiếu</h5>
                                <div class="showtime-list">
                                    <c:forEach var="showtime" items="${showtimes}">
                                        <c:if test="${showtime.movie.id == selectedMovie.id}">
                                            <form action="booking" method="post" style="display:inline;">
                                                <input type="hidden" name="showtimeID" value="${showtime.id}">
                                                <input type="hidden" name="action" value="showtimeSl">
                                                <button type="submit" class="time-btn">
                                                        ${fn:substring(showtime.startTime, 11, 16)}
                                                </button>
                                            </form>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p>Không có suất chiếu nào cho rạp này vào ngày ${selectedDate}.</p>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </c:if>
            <c:if test="${empty cinemas}">
                <p>Không có rạp nào khả dụng.</p>
            </c:if>
        </div>
    </div>
</div>

<!-- Include popup đánh giá -->
<jsp:include page="ratingPopup.jsp" />

<script>
document.addEventListener('DOMContentLoaded', function() {
    const ratingBtn = document.getElementById('rating-btn');
    const ratingPopup = document.getElementById('rating-popup');
    const closePopup = ratingPopup.querySelector('.close-popup');
    const ratingList = document.getElementById('rating-list');
    const movieId = ${selectedMovie.id};

    if (ratingBtn) {
        ratingBtn.addEventListener('click', function() {
            ratingPopup.style.display = 'flex';
            // Xóa cũ trước khi load mới
            ratingList.innerHTML = '<p>Đang tải đánh giá...</p>';

            fetch('<%=request.getContextPath()%>/rating?movieId=' + movieId)
                .then(res => res.json())
                .then(data => {
                    ratingList.innerHTML = '';
                    if (data.length === 0) {
                        ratingList.innerHTML = '<p>Chưa có đánh giá nào.</p>';
                    } else {
                        data.forEach(r => {
                            const div = document.createElement('div');
                            div.className = 'rating-item';
                            div.innerHTML = `
                                <div class="rating-stars">${'★'.repeat(r.stars)}${'☆'.repeat(10 - r.stars)}</div>
                                <p><strong>${r.username}</strong>: ${r.comment}</p>
                            `;
                            ratingList.appendChild(div);
                        });
                    }
                })
                .catch(() => {
                    ratingList.innerHTML = '<p>Lỗi khi tải đánh giá.</p>';
                });
        });
    }

    // Đóng popup
    closePopup.addEventListener('click', () => ratingPopup.style.display = 'none');
    window.addEventListener('click', (e) => {
        if (e.target === ratingPopup) ratingPopup.style.display = 'none';
    });
});
</script>

<%@ include file="../../common/footer.jsp" %>
</body>
</html>