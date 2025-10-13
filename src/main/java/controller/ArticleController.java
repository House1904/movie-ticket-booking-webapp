package controller;

import model.Article;
import service.ArticleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/admin/articles")
public class ArticleController extends HttpServlet {
    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String url = request.getParameter("url");

        // Always load articles list for display
        List<Article> articles = articleService.getAllArticles();
        request.setAttribute("articles", articles);

        if ("edit".equals(action) && url != null) {
            // Hiển thị form chỉnh sửa
            Article article = articles.stream()
                    .filter(a -> a != null && a.getUrl() != null && a.getUrl().equals(url))
                    .findFirst()
                    .orElse(null);
            if (article != null) {
                request.setAttribute("article", article);
            } else {
                request.setAttribute("errorMessage", "Không tìm thấy bài viết với URL: " + url);
            }
        }

        request.getRequestDispatcher("/view/admin/article.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                String url = request.getParameter("url");
                if (url != null && !url.isEmpty()) {
                    articleService.deleteArticle(url);
                }
                doGet(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Lỗi khi xóa bài viết: " + e.getMessage());
                doGet(request, response);
            }
        } else if ("update".equals(action)) {
            try {
                String url = request.getParameter("url");
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String imageUrl = request.getParameter("imageUrl");
                if (url == null || url.isEmpty()) {
                    request.setAttribute("errorMessage", "Thiếu URL bài viết để cập nhật.");
                    doGet(request, response);
                    return;
                }

                Article existing = articleService.getArticleByUrl(url);
                if (existing == null) {
                    request.setAttribute("errorMessage", "Không tìm thấy bài viết để cập nhật.");
                    doGet(request, response);
                    return;
                }

                existing.setTitle(title);
                existing.setContent(content);
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    existing.setImage(imageUrl);
                }

                articleService.updateArticle(existing);
                doGet(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Lỗi khi cập nhật bài viết: " + e.getMessage());
                doGet(request, response);
            }
        } else {
            try {
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String url = request.getParameter("url");
                String imageUrl = request.getParameter("imageUrl");
                LocalDateTime createdAt = LocalDateTime.now();

                Article article = new Article(title, content, url, createdAt);
                article.setImage(imageUrl);

                articleService.addArticle(article);
                doGet(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Lỗi khi thêm bài viết: " + e.getMessage());
                doGet(request, response);
            }
        }
    }
}