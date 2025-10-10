package service;

import dao.ArticleDAO;
import model.Article;

import java.sql.SQLException;
import java.util.List;

public class ArticleService {
    private ArticleDAO articleDAO = new ArticleDAO();
    public List<Article> getArticles() throws SQLException {
        return articleDAO.getAllArticles();
    }
}