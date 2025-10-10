package controller;

import model.Account;
import model.Partner;
import model.enums.Role;
import util.DBConnection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginController {

    public boolean authenticate(HttpServletRequest request, String username, String password) {
        EntityManager em = DBConnection.getEmFactory().createEntityManager();
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT a FROM Account a WHERE a.userName = :username AND a.password = :password",
                    Account.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            Account account = query.getSingleResult();

            if (account != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", account.getId());
                session.setAttribute("role", account.getRole());

                if (account.getRole() == Role.PARTNER) {
                    TypedQuery<Partner> partnerQuery = em.createQuery(
                            "SELECT p FROM Partner p WHERE p.id = :id",
                            Partner.class
                    );
                    partnerQuery.setParameter("id", account.getId());
                    Partner partner = partnerQuery.getSingleResult();
                    session.setAttribute("partner", partner);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}