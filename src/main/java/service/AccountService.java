package service;

import dao.AccountDAO;
import model.Account;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public Account login(String username, String password) throws SQLException
    {
      Account validAccount = accountDAO.getAccountByUsername(username);
      System.out.println(validAccount.getUserName());
      if (validAccount != null && (password.equals(validAccount.getPassword())))
          return validAccount;
      else
          return null;
    }

    public boolean register(Account account) {
        try {
            if (accountDAO.getAccountByUsername(account.getUserName()) == null)
            {
                accountDAO.addAccount(account);
                return true;
            }
                return false; //account với username này đã được đăng ý trước đó
        }
        catch (SQLException e) {
            return false;
        }
    }
}
