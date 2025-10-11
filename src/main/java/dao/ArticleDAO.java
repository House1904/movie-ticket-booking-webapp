package dao;

import model.Article;
import model.Cinema;
import util.DBConnection;

import javax.persistence.EntityManager;
import java.util.List;

public class ArticleDAO {
    public List<Article> getAllArticles() {
        EntityManager entity = DBConnection.getEmFactory().createEntityManager();
        List<Article> articles = null;

        try {
            articles = entity.createQuery("SELECT a FROM Article a", Article.class)
                    .getResultList();
        } finally {
            entity.close();
        }

        return articles;
    }
}