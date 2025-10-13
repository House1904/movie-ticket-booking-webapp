<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tin Tức - Movie Ticket Booking</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/customer-article.css">
</head>
<body>
<%@ include file="../../common/header.jsp" %>

<div class="news-container">
    <h1 class="news-title">Tin Tức</h1>

    <!-- Tin Tức Section -->
    <div class="section-container">
        <h2 class="section-title">Tin Tức</h2>
        <div class="articles-list" id="newsArticles">
            <c:choose>
                <c:when test="${empty articles}">
                    <div class="no-articles">
                        <div class="no-articles-icon">📰</div>
                        <h3>Chưa có bài viết nào</h3>
                        <p>Hiện tại chưa có bài viết nào được đăng. Vui lòng quay lại sau!</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="article" items="${articles}">
                        <div class="article-item">
                            <a href="${article.url}" class="article-link" target="_blank">
                                <div class="article-image-container">
                                    <img src="${article.image != null && !article.image.isEmpty() ? article.image : pageContext.request.contextPath + '/assets/images/default-article.jpg'}"
                                         alt="${article.title}" class="article-image">
                                    <div class="article-overlay">
                                        <span class="read-more-btn">Đọc thêm</span>
                                    </div>
                                </div>
                                <div class="article-content">
                                    <h3 class="article-title">${article.title}</h3>
                                    <p class="article-preview">
                                        <c:choose>
                                            <c:when test="${article.content != null && article.content.length() > 200}">
                                                ${article.content.substring(0, 200)}...
                                            </c:when>
                                            <c:when test="${article.content != null}">
                                                ${article.content}
                                            </c:when>
                                            <c:otherwise>Không có nội dung</c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Pagination -->
        <c:if test="${not empty articles}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/public/articles?page=${currentPage - 1}" class="page-btn">« Trước</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="page-btn active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/public/articles?page=${i}" class="page-btn">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/public/articles?page=${currentPage + 1}" class="page-btn">Sau »</a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="../../common/footer.jsp" />

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const articleItems = document.querySelectorAll('.article-item');
        articleItems.forEach(item => {
            item.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-5px)';
            });
            item.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0)';
            });
        });
    });
</script>
</body>
</html>