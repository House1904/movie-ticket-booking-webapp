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

        <div class="movie-rating-summary">
            <span class="average-stars">
                <c:forEach begin="1" end="${averageRatingFloor}" var="i">⭐</c:forEach>
                <c:if test="${hasHalfStar}">✰</c:if>
                <c:if test="${ratingCount == 0}">⭐☆☆☆☆</c:if>
            </span>
            <span class="average-score">
                <c:choose>
                    <c:when test="${ratingCount > 0}">(${averageRating}/5)</c:when>
                    <c:otherwise>(0/5)</c:otherwise>
                </c:choose>
            </span>
            <span class="rating-count">
                <c:choose>
                    <c:when test="${ratingCount > 0}">- ${ratingCount} lượt đánh giá</c:when>
                    <c:otherwise>- Chưa có đánh giá</c:otherwise>
                </c:choose>
            </span>
        </div>

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

<div class="rating-section">
    <h3>💬 Đánh giá từ khán giả
        <c:choose>
            <c:when test="${canRate}">
                <button id="openRatingBtn"
                        class="btn btn-sm ${canRate ? 'btn-primary' : 'btn-secondary'} ms-2"
                        ${canRate ? '' : 'disabled'}>
                    ${canRate ? 'Đánh giá' : 'Xem phim để được đánh giá'}
                </button>
            </c:when>
            <c:otherwise>
                <button class="btn btn-sm btn-secondary ms-2" disabled>
                    Xem phim để được đánh giá
                </button>
            </c:otherwise>
        </c:choose>
    </h3>
    <div class="rating-list">
        <c:choose>
            <c:when test="${not empty ratings}">
                <c:forEach var="r" items="${ratings}">
                    <div class="rating-item">
                        <div class="rating-header">
                            <strong>${r.customer.fullName}</strong>
                            <span class="stars">
                                <c:forEach begin="1" end="${r.rating}" var="s">⭐</c:forEach>
                            </span>
                            <span class="rating-date">
                                <fmt:formatDate value="${r.createdAtDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                        <div class="rating-body">
                            <p>${r.content}</p>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Hiện chưa có đánh giá nào cho phim này.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/view/customer/writeRating.jsp" />

<script>
document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('rating-popup');
    const openBtn = document.getElementById('openRatingBtn');
    const closeBtn = popup?.querySelector('.close-popup');

    if(openBtn && popup && !openBtn.disabled) {
        openBtn.addEventListener('click', () => {
            popup.style.display = 'flex';
            document.getElementById('ticketId').value = '${movie.id}';
        });
    }

    if(closeBtn) closeBtn.addEventListener('click', () => popup.style.display = 'none');

    // Đóng popup khi click ngoài
    window.addEventListener('click', (e) => {
        if (e.target === popup) popup.style.display = 'none';
    });

    // Gửi đánh giá
    const submitBtn = document.getElementById('submit-rating-btn');
    if(submitBtn){
        submitBtn.addEventListener('click', function() {
            const movieId = document.getElementById('ticketId').value;
            const comment = document.getElementById('comment').value;

            if(selectedRating === 0){
                alert('Vui lòng chọn số sao!');
                return;
            }

            fetch('${pageContext.request.contextPath}/rating', {
                method:'POST',
                headers:{'Content-Type':'application/x-www-form-urlencoded'},
                body: 'movieId=' + movieId + '&rating=' + selectedRating + '&comment=' + encodeURIComponent(comment)
            })
            .then(res=>res.json())
            .then(data=>{
                if(data.success){
                    alert('Đã gửi đánh giá!');
                    popup.style.display='none';
                    location.reload();
                } else {
                    alert('Lỗi: '+(data.error || 'Không xác định'));
                }
            }).catch(err=>alert('Lỗi gửi đánh giá!'));
        });
    }
});
</script>

<%@ include file="/common/footer.jsp" %>
</body>
</html>
