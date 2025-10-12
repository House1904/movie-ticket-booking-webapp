package dao;

import model.Account;
import service.AccountService;
import util.DBConnection;

import javax.persistence.*;
import java.sql.SQLException;

public class AccountDAO {
    private EntityManager em = DBConnection.getEmFactory().createEntityManager();;

    public Account getAccountByUsername(String username) {
        try {
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
