<!--customer-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Article" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tin Tức & Khuyến Mãi - Movie Ticket Booking</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/styles.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/customer-article.css">
</head>
<body>
    <%@ include file="../../common/header.jsp" %>
    
    <div class="news-container">
        <h1 class="news-title">Tin Tức & Khuyến Mãi</h1>
        
        <!-- Tin Tức Section -->
        <div class="section-container">
            <h2 class="section-title">Tin Tức</h2>
            <div class="articles-list" id="newsArticles">
                <%
                    List<Article> articles = (List<Article>) request.getAttribute("articles");
                    if (articles != null && !articles.isEmpty()) {
                        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
                        int articlesPerPage = 5;
                        int startIndex = (currentPage - 1) * articlesPerPage;
                        int endIndex = Math.min(startIndex + articlesPerPage, articles.size());
                        
                        for (int i = startIndex; i < endIndex; i++) {
                            Article article = articles.get(i);
                            String imageUrl = article.getImage() != null && !article.getImage().isEmpty() 
                                ? article.getImage() 
                                : request.getContextPath() + "/assets/images/default-article.jpg";
                            String contentPreview = article.getContent() != null 
                                ? (article.getContent().length() > 200 
                                    ? article.getContent().substring(0, 200) + "..." 
                                    : article.getContent())
                                : "Không có nội dung";
                %>
                <div class="article-item">
                    <a href="<%= article.getUrl() %>" class="article-link" target="_blank">
                        <div class="article-image-container">
                            <img src="<%= imageUrl %>" alt="<%= article.getTitle() %>" class="article-image">
                            <div class="article-overlay">
                                <span class="read-more-btn">Đọc thêm</span>
                            </div>
                        </div>
                        <div class="article-content">
                            <h3 class="article-title"><%= article.getTitle() %></h3>
                            <p class="article-preview"><%= contentPreview %></p>
                        </div>
                    </a>
                </div>
                <%
                        }
                    } else {
                %>
                <div class="no-articles">
                    <div class="no-articles-icon">📰</div>
                    <h3>Chưa có bài viết nào</h3>
                    <p>Hiện tại chưa có bài viết nào được đăng. Vui lòng quay lại sau!</p>
                </div>
                <%
                    }
                %>
            </div>
            
            <!-- Pagination for News -->
            <%
                if (articles != null && !articles.isEmpty()) {
                    int totalPages = (int) Math.ceil((double) articles.size() / 5);
                    int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            %>
            <div class="pagination">
                <% if (currentPage > 1) { %>
                    <a href="?page=<%= currentPage - 1 %>" class="page-btn">« Trước</a>
                <% } %>
                
                <% for (int i = 1; i <= totalPages; i++) { %>
                    <% if (i == currentPage) { %>
                        <span class="page-btn active"><%= i %></span>
                    <% } else { %>
                        <a href="?page=<%= i %>" class="page-btn"><%= i %></a>
                    <% } %>
                <% } %>
                
                <% if (currentPage < totalPages) { %>
                    <a href="?page=<%= currentPage + 1 %>" class="page-btn">Sau »</a>
                <% } %>
            </div>
            <%
                }
            %>
        </div>
        
        <!-- Khuyến Mãi Section -->
        <div class="section-container" id="promotion">
            <h2 class="section-title">Khuyến Mãi</h2>
            <div class="promotion-placeholder">
                <div class="promotion-icon">🎁</div>
                <h3>Khuyến mãi sắp ra mắt</h3>
                <p>Chúng tôi đang chuẩn bị những ưu đãi hấp dẫn dành cho bạn. Hãy quay lại sau nhé!</p>
            </div>
        </div>
    </div>
    
    <!-- Loading Spinner -->
    <div class="loading-spinner" id="loadingSpinner" style="display: none;">
        <div class="spinner"></div>
        <p>Đang tải...</p>
    </div>
    
    <jsp:include page="../../common/footer.jsp" />
    
    <script>
        // Article hover effects
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
            
            // Scroll to promotion section if URL contains #promotion
            if (window.location.hash === '#promotion') {
                setTimeout(function() {
                    const promotionSection = document.getElementById('promotion');
                    if (promotionSection) {
                        promotionSection.scrollIntoView({ 
                            behavior: 'smooth',
                            block: 'start'
                        });
                    }
                }, 100);
            }
        });
    </script>
</body>
</html>
