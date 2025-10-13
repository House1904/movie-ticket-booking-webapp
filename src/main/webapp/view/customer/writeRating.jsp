<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<div id="rating-popup" class="rating-popup" style="display:none;">
    <div class="popup-content">
        <span class="close-popup">&times;</span>
        <h3>Đánh giá phim</h3>
        <input type="hidden" id="ticketId" value="">
        <label>Đánh giá:</label>
            <div id="star-rating">
                <span class="star" data-value="1">☆</span>
                <span class="star" data-value="2">☆</span>
                <span class="star" data-value="3">☆</span>
                <span class="star" data-value="4">☆</span>
                <span class="star" data-value="5">☆</span>
            </div>
        <label>Bình luận:</label>
        <textarea id="comment" rows="4"></textarea>
        <button id="submit-rating-btn">Gửi đánh giá</button>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const stars = document.querySelectorAll('#star-rating .star');
    let selectedRating = 0;

    stars.forEach(star => {
        star.addEventListener('mouseover', () => {
            const val = parseInt(star.dataset.value);
            highlightStars(val);
        });

        star.addEventListener('mouseout', () => {
            highlightStars(selectedRating);
        });

        star.addEventListener('click', () => {
            selectedRating = parseInt(star.dataset.value);
            highlightStars(selectedRating);
        });
    });

    function highlightStars(rating) {
        stars.forEach(star => {
            if(parseInt(star.dataset.value) <= rating) {
                star.textContent = '★'; // sao vàng
            } else {
                star.textContent = '☆'; // sao trắng
            }
        });
    }

    // Submit rating
    const submitBtn = document.getElementById('submit-rating-btn');
    submitBtn.addEventListener('click', function() {
        const movieId = document.getElementById('ticketId').value;
        const comment = document.getElementById('comment').value;

        if(selectedRating === 0) {
            alert('Vui lòng chọn số sao!');
            return;
        }

        fetch('${pageContext.request.contextPath}/rating', {
            method: 'POST',
            headers: {'Content-Type':'application/x-www-form-urlencoded'},
            body: 'movieId=' + movieId + '&rating=' + selectedRating + '&comment=' + encodeURIComponent(comment)
        })
        .then(res => res.json())
        .then(data => {
            if(data.success) {
                alert('Đã gửi đánh giá!');
                document.getElementById('rating-popup').style.display = 'none';
                location.reload();
            } else {
                alert('Lỗi: ' + (data.error || 'Không xác định'));
            }
        })
        .catch(err => alert('Lỗi gửi đánh giá!'));
    });
});
</script>
