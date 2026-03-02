package org.example.controller;

import io.javalin.Javalin;
import org.example.model.Leave;
import org.example.service.LeaveService;

import java.util.List;

public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    public void registerRoutes(Javalin app){
        // Apply Leave
        app.post("/leave", ctx -> {
           Integer userId = ctx.sessionAttribute("userId");
           if(userId == null){
               ctx.status(401).result("LOGIN REQUIRED");
               return;
           }

           Leave leave = ctx.bodyAsClass(Leave.class);
           leave.setUser_id(userId);

           String result = leaveService.applyLeave(leave);

           if(result.equals("Leave applied successfully")){
               ctx.status(201).result("Leave applied successfully");
           }else{
               ctx.status(400).json(result);
           }
        });

        // Get My Leaves
        app.get("/leave/my", ctx -> {
            Integer userId = ctx.sessionAttribute("userId");
            if(userId == null){
                ctx.status(401).result("LOGIN REQUIRED");
                return;
            }

            List<Leave> leaves = leaveService.getMyLeaves(userId);
            ctx.json(leaves);
        });

        // Admin: Get all leaves
        app.get("/admin/leaves", ctx ->{
           String role = ctx.sessionAttribute("role");
           if(role == null || !role.equals("ADMIN")){
               ctx.status(403).result("ADMIN ACCESS ONLY");
               return;
           }

           List<Leave> leaves = leaveService.getAllLeave();
           ctx.json(leaves);
        });

        // Admin: update leave status
        app.put("/admin/leaves/{id}", ctx -> {
            String role = ctx.sessionAttribute("role");
            if(role == null || !role.equals("ADMIN")){
                ctx.status(403).result("ADMIN ACCESS ONLY");
                return;
            }

            int leaveId = Integer.parseInt(ctx.pathParam("id"));
            Leave request = ctx.bodyAsClass(Leave.class);

            String result = leaveService.updateLeaveStatus(leaveId, request.getStatus());

            if(result.equals("Leave status updated successfully")){
                ctx.json(result);
            }else{
                ctx.status(400).json(result);
            }
        });
    }
}
