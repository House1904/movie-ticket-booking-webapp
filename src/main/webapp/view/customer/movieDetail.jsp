<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="/common/header.jsp" %>

<html>
<head>
    <title>${movie.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movieDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
</head>
<body>
<div class="movie-detail-container">
    <div class="poster-section">
        <img src="${movie.posterUrl}" alt="${movie.title}" class="poster">
    </div>

    <div class="movie-info">
        <h2>${movie.title}</h2>

        <p><strong>Giới hạn độ tuổi:</strong>
            <c:choose>
                <c:when test="${not empty movie.ageLimit}">
                    ${movie.ageLimit}
                </c:when>
                <c:otherwise>Đang cập nhật</c:otherwise>
            </c:choose>
        </p>

        <p><strong>Thể loại:</strong>
            <c:choose>
                <c:when test="${not empty movie.genre}">
                    <c:forEach var="g" items="${movie.genre}" varStatus="status">
                        ${g}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>Đang cập nhật</c:otherwise>
            </c:choose>
        </p>

        <p><strong>Năm phát hành:</strong>
            <c:choose>
                <c:when test="${not empty movie.releaseDate}">
                    ${movie.releaseDate.year}
                </c:when>
                <c:otherwise>Chưa xác định</c:otherwise>
            </c:choose>
        </p>

        <p><strong>Thời lượng:</strong>
            <c:choose>
                <c:when test="${movie.duration > 0}">
                    ${movie.duration} phút
                </c:when>
                <c:otherwise>Đang cập nhật</c:otherwise>
            </c:choose>
        </p>

        <p><strong>Ngôn ngữ:</strong>
            <c:out value="${movie.language != null ? movie.language : 'Đang cập nhật'}" />
        </p>

        <p><strong>Diễn viên:</strong>
            <c:out value="${movie.actor != null ? movie.actor : 'Đang cập nhật'}" />
        </p>

        <p class="desc">${movie.description}</p>

        <div class="movie-actions">
            <button class="favorite-btn ${isFavorited ? 'favorited' : ''}" data-id="${movie.id}">
                <i class="fa fa-heart"></i> <span>${isFavorited ? 'Bỏ yêu thích' : 'Yêu thích'}</span>
            </button>
            <a href="${movie.trailerUrl}" target="_blank" class="trailer-btn">
                📽️ Trailer
            </a>
            <a href="${pageContext.request.contextPath}/selectShowtime?movieId=${movie.id}" class="book-ticket">
                🎟️ Đặt Vé
            </a>
        </div>
    </div>
</div>

<script>
    // Xử lý sự kiện click nút yêu thích
    document.querySelector('.favorite-btn').addEventListener('click', function() {
        const btnEl = this;
        const movieId = this.dataset.id;

        fetch('${pageContext.request.contextPath}/favorite', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'movieId=' + movieId
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'added') {
                btnEl.classList.add('favorited');
                btnEl.querySelector('span').textContent = 'Bỏ yêu thích';
            } else if (data.status === 'removed') {
                btnEl.classList.remove('favorited');
                btnEl.querySelector('span').textContent = 'Yêu thích';
            } else if (data.message === 'not_logged_in') {
                alert("Vui lòng đăng nhập để thêm yêu thích!");
                window.location.href = '${pageContext.request.contextPath}/common/login.jsp';
            }
        })
        .catch(err => console.error(err));
    });
</script>

<%@ include file="/common/footer.jsp" %>
</body>
</html>
