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
</head>
<body>
<%@ include file="../../common/header.jsp" %>

<div class="main-content">
    <div class="movie-showtime-container">
        <!-- Movie Info Section (Left) -->
        <div class="movie-info">
            <h2>${selectedMovie.title}</h2>
            <img src="${selectedMovie.posterUrl}" alt="${selectedMovie.title} Poster" class="movie-poster">
        </div>

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

<%@ include file="../../common/footer.jsp" %>
</body>
</html>