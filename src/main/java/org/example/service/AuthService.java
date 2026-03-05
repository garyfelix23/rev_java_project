package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public boolean signup(String name, String email, String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return userDAO.signup(user);
    }
}
