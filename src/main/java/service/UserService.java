package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean userRegister(User user) {
        if (userDAO.getUserByEmail(user.getEmail()) == null) {
            userDAO.addUser(user);
            return true;
        }
        return false;
    }
}
