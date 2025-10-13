package dao;

import model.Banner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class BannerDAO {
    private EntityManagerFactory emf;

    public BannerDAO() {
        // Khởi tạo EntityManagerFactory với persistence unit "ProjectLoad" từ persistence.xml
        emf = Persistence.createEntityManagerFactory("ProjectLoad");
    }

    public void addBanner(Banner banner) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            banner.setCreated_at(LocalDateTime.now());
            em.persist(banner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void updateBanner(Banner banner) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(banner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteBanner(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Banner banner = em.find(Banner.class, id);
            if (banner != null) {
                em.remove(banner);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Banner getBannerById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Banner.class, id);
        } finally {
            em.close();
        }
    }

    public List<Banner> getAllBanners() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Banner> query = em.createQuery("SELECT b FROM Banner b", Banner.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}