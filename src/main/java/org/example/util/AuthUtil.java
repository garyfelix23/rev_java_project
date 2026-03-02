package org.example.util;

import io.javalin.http.Context;

public class AuthUtil {
    public static boolean requireLogin(Context ctx){
        Integer userId = ctx.sessionAttribute("userId");

        if (userId == null){
            ctx.status(401).result("Login first");
            return false;
        }

        return true;
    }

    public static boolean requireRole(Context ctx, String requiredRole) {
        String role = ctx.sessionAttribute("role");

        if (role == null) {
            ctx.status(401).result("Please login first");
            return false;
        }

        if (!requiredRole.equals(role)) {
            ctx.status(403).result("Access denied. " + requiredRole + " only.");
            return false;
        }

        return true;
    }
}
