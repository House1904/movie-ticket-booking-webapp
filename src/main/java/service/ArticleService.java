package service;

import dao.ArticleDAO;
import model.Article;
import java.util.List;

public class ArticleService {
    private final ArticleDAO articleDAO = new ArticleDAO();

    public List<Article> getAllArticles() {
        return articleDAO.getAllArticles();
    }

    public Article getArticleByUrl(String url) {
        return articleDAO.getByUrl(url);
    }

    public void addArticle(Article article) {
        articleDAO.addArticle(article);
    }

    public void deleteArticle(String url) {
        articleDAO.deleteArticle(url);
    }

    public void updateArticle(Article article) {
        articleDAO.updateArticle(article);
    }
}