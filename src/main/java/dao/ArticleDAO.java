package dao;

import model.Article;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class ArticleDAO {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ProjectLoad");

    public List<Article> getAllArticles() {
        EntityManager em = emf.createEntityManager();
        List<Article> articles = null;
        try {
            TypedQuery<Article> query = em.createQuery("SELECT a FROM Article a ORDER BY a.created_at DESC", Article.class);
            articles = query.getResultList();
        } finally {
            em.close();
        }
        return articles;
    }

    public Article getByUrl(String url) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Article.class, url);
        } finally {
            em.close();
        }
    }

    public void addArticle(Article article) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(article);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteArticle(String url) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Article article = em.find(Article.class, url);
            if (article != null) {
                em.remove(article);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void updateArticle(Article article) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (article.getImage() != null && !article.getImage().trim().isEmpty()) {
                em.createQuery(
                                "UPDATE Article a SET a.title = :title, a.content = :content, a.image = :image WHERE a.url = :url")
                        .setParameter("title", article.getTitle())
                        .setParameter("content", article.getContent())
                        .setParameter("image", article.getImage())
                        .setParameter("url", article.getUrl())
                        .executeUpdate();
            } else {
                em.createQuery(
                                "UPDATE Article a SET a.title = :title, a.content = :content WHERE a.url = :url")
                        .setParameter("title", article.getTitle())
                        .setParameter("content", article.getContent())
                        .setParameter("url", article.getUrl())
                        .executeUpdate();
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}