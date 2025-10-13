<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="/common/header.jsp" %>

<html>
<head>
    <title>${movie.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movieDetail.css">
</head>
<body>
<div class="movie-detail-container">
    <div class="poster-section">
        <img src="${movie.posterUrl}" alt="${movie.title}" class="poster">
    </div>

    <div class="movie-info">
        <h2>${movie.title}</h2>

        <p><strong>Giới hạn Độ tuổi:</strong>
            <c:choose>
                <c:when test="${not empty movie.ageLimit}">
                    ${movie.ageLimit}+
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
            <button class="favorite-btn">
                <i class="fa fa-heart"></i> Yêu thích
            </button>
            <a href="${movie.trailerUrl}" target="_blank" class="trailer-btn">
                📽️ Trailer
            </a>
        </div>
    </div>
</div>

<%@ include file="/common/footer.jsp" %>
</body>
</html>
