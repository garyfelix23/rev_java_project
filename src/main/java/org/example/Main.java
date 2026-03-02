package org.example;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.example.controller.AuthController;
import org.example.controller.LeaveController;
import org.example.service.LeaveService;
import org.example.util.AuthUtil;


public class Main {

    public static void main(String[] args){

        JavalinJackson.defaultMapper().registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                    it.allowCredentials = true;
                });
            });
        }).start(7000);

        AuthController.register(app);

        // Leave Controller
        LeaveService leaveService = new LeaveService();
        LeaveController leaveController = new LeaveController(leaveService);

        leaveController.registerRoutes(app);

        // Employee Route
        app.get("/leave/test", ctx -> {

            if (!AuthUtil.requireRole(ctx, "EMPLOYEE")) return;

            ctx.result("Employee route working");
        });

        // Admin Route
        app.get("/admin/test", ctx -> {

            if (!AuthUtil.requireRole(ctx, "ADMIN")) return;

            ctx.result("Admin route working");
        });

        System.out.println("Server started at http://localhost:7000");
    }
}