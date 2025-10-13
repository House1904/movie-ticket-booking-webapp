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
      if (validAccount != null && (BCrypt.checkpw(password, validAccount.getPassword())))
          return validAccount;
      else
          return null;
    }

    public Account CheckAccount(long id) {
        return accountDAO.findAccountByPartnerId(id);
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
    public void addAccount(Account account) throws SQLException {
        accountDAO.addAccount(account);
    }
}
