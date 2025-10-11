package dao;

import model.Account;
import service.AccountService;
import util.DBConnection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import javax.persistence.*;
import java.sql.SQLException;

public class AccountDAO {
    private EntityManager em = DBConnection.getEmFactory().createEntityManager();;

    public void addAccount(Account account) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
    public Account getAccountByUsername(String username) {
        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public Account findAccountByPartnerId(long partnerId) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            String query = "SELECT a FROM Account a " +
                    "JOIN a.user u " +
                    "WHERE u.id = :partnerId";
            TypedQuery<Account> query1 = em.createQuery(query, Account.class);
            query1.setParameter("partnerId", partnerId);
            List<Account> accounts = query1.getResultList();
            return accounts.isEmpty() ? null : accounts.get(0);
        } finally {
            em.close();
        }
            return em.createQuery(
                            "SELECT a FROM Account a LEFT JOIN FETCH a.user WHERE a.userName = :userName",
                            Account.class)
                    .setParameter("userName", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // không tìm thấy user
        }
    }

    public void addAccount(Account account) throws SQLException
    {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }
        }