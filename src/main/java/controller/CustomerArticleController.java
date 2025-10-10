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
    private static final int PAGE_SIZE = 7;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        int page = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        List<Article> allArticles = articleService.getAllArticles();
        int totalArticles = allArticles != null ? allArticles.size() : 0;
        int totalPages = (int) Math.ceil((double) totalArticles / PAGE_SIZE);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, totalArticles);
        List<Article> articles = (allArticles != null && start < totalArticles) ? allArticles.subList(start, end) : new ArrayList<>();

        req.setAttribute("articles", articles);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/view/customer/article.jsp").forward(req, resp);
    }
}