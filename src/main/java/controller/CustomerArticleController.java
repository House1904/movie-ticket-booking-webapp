package controller;

import model.Article;
import service.ArticleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/public/articles")
public class CustomerArticleController extends HttpServlet {
    private final ArticleService articleService = new ArticleService();
    private static final int PAGE_SIZE = 5; // Sửa thành 5 bài/trang

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // Xác định trang hiện tại
        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        // Lấy danh sách bài báo
        List<Article> allArticles = articleService.getAllArticles();
        int totalArticles = (allArticles != null) ? allArticles.size() : 0;
        int totalPages = (totalArticles > 0) ? (int) Math.ceil((double) totalArticles / PAGE_SIZE) : 1;

        // Giới hạn trang hợp lệ
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        // Tính toán chỉ số bắt đầu và kết thúc
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalArticles);
        List<Article> articles = (allArticles != null && totalArticles > 0 && start < totalArticles)
                ? allArticles.subList(start, end)
                : new ArrayList<>();

        // Đặt thuộc tính cho JSP
        req.setAttribute("articles", articles);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        // In debug để kiểm tra
        System.out.println("Total articles: " + totalArticles);
        System.out.println("Current page: " + page + ", Articles on page: " + articles.size());

        // Forward đến JSP
        req.getRequestDispatcher("/view/customer/article.jsp").forward(req, resp);
    }
}