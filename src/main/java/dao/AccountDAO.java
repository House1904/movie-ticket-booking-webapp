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
    public Account getAccountByUsername(String username) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        return em.createQuery(
                        "SELECT a FROM Account a LEFT JOIN FETCH a.user WHERE a.userName = :userName",
                        Account.class)
                .setParameter("userName", username)
                .getSingleResult();
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
    }

    public void addAccount(Account account) throws SQLException
    {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
        em.close();
    }

    public  void updateAccount(Account account) throws SQLException
    {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();
        em.close();
    }
}