package org.example.controller;

import io.javalin.Javalin;
import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.service.AuthService;

import java.util.Map;

public class AuthController {
    public static void register(Javalin app){
        UserDAO userDAO = new UserDAO();

        // signup API
        app.post("/signup", ctx ->  {
            User user = ctx.bodyAsClass(User.class);

            AuthService authService = new AuthService();

            boolean createUser = authService.signup(
                    user.getName(),
                    user.getEmail(),
                    user.getPassword()
            );

            if(createUser){
                ctx.status(201).result("User create successfully");
            }else{
                ctx.status(400).result("User creation failed");
            }
        });

        // Login API
        app.post("/login", ctx -> {
            // we're getting the email and password from body as JSON content type.
            Map<String, String> body = ctx.bodyAsClass(Map.class);

            String email = body.get("email");
            String password = body.get("password");

            User user = userDAO.login(email, password);
            // this user returns the user object data if it matches the email and password

            if(user == null){
                ctx.status(401).result("Invalid Credentials");
                return;
            }

            // create session
            ctx.sessionAttribute("userId", user.getId());
            ctx.sessionAttribute("role", user.getRole());

            ctx.json(Map.of("message", "Login Successful", "role", user.getRole()));

        });

        // to check which user is logged in for role based dashboard in frontend
        app.get("/me", ctx -> {
            Integer userId = ctx.sessionAttribute("userId");
            String role =  ctx.sessionAttribute("role");

            if(userId == null){
                ctx.status(401).result("Invalid Credentials");
                return;
            }

            ctx.json(Map.of("userId", userId, "role", role));
        });

        // logout endpoint
        app.post("/logout", ctx -> {
            ctx.req().getSession().invalidate();
            ctx.status(200).result("Logged out successfully");
        });
    }
}
