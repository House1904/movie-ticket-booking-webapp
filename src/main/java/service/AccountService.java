package service;

import dao.AccountDAO;
import model.Account;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();
    public Account CheckAccount(long id) {
        return accountDAO.findAccountByPartnerId(id);
    }
    public void addAccount(Account account) {
        accountDAO.addAccount(account);
    }
}
