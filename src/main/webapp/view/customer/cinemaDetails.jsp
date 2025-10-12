<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Chi tiết rạp phim</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cinema.css">
</head>
<body>
<div class="cinema-detail-container">
    <h2 class="cinema-detail-title">${cinema.name}</h2>
    <p><strong>Địa chỉ:</strong> ${cinema.address}</p>
    <p><strong>Liên hệ:</strong> ${cinema.phone}</p>
    <hr>

    <h3 class="cinema-detail-title">Phim đang chiếu</h3>

    <div class="date-carousel-wrap" aria-label="Chọn ngày">
        <div class="date-carousel" id="dateCarousel" role="listbox" tabindex="0"></div>

        <form id="dateForm" action="showtime" method="post" style="margin-top: 10px;">
            <input type="hidden" id="selectedDate" name="selectedDate" value="">
            <input type="hidden" name="id" value="${cinema.id}">
            <input type="hidden" name="action" value="filter">
            <input type="hidden" name="from" value="detail">
        </form>
    </div>

    <div class="movie-grid">
        <c:forEach var="entry" items="${movieShowtimes}">
            <c:set var="movie" value="${entry.key}" />
            <c:set var="showtimes" value="${entry.value}" />

            <div class="movie-card">
                <img src="${movie.posterUrl}" alt="${movie.title}" class="movie-poster">
                <div class="movie-info">
                    <h4>${movie.title}</h4>
                    <p><strong>Thể loại:</strong></p>
                    <p><strong>Thời lượng:</strong> ${movie.duration} phút</p>
                </div>

                <div class="showtime-buttons">
                    <c:forEach var="s" items="${showtimes}">
                        <form action="booking" method="post" class="showtime-form" style="display:inline;">
                            <input type="hidden" name="showtimeID" value="${s.id}">
                            <input type="hidden" name="selectedDate" value="">
                            <input type="hidden" name="action" value="showtimeSl">
                            <button type="submit" class="time-btn">
                                    ${fn:substring(s.startTime, 11, 16)}
                            </button>
                        </form>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="back-btn">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">← Quay lại</a>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/dateSelector.js"></script>
</body>
</html>
