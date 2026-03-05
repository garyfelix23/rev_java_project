package org.example.service;

import org.example.dao.LeaveDAO;
import org.example.model.Leave;

import java.time.LocalDate;
import java.util.List;

public class LeaveService {
    private LeaveDAO leaveDAO = new LeaveDAO();

    public String applyLeave(Leave leave){
        // Start date should not be in the past
        if(leave.getStart_date().isBefore(LocalDate.now())){
            return "Error: Start Date should not be in the past";
        }

        // end date should not be before start date
        if(leave.getEnd_date().isBefore(leave.getStart_date())){
            return "Error: End Date should not be before start date";
        }

        // reason should not be empty
        if(leave.getReason() == null || leave.getReason().trim().isEmpty()){
            return "Error: Reason must not be empty";
        }

        boolean success = leaveDAO.applyLeave(leave);

        if(success){
            return "Leave applied successfully";
        } else{
            return "Failed to apply leave";
        }
    }

    // Get leave for logged-in user
    public List<Leave> getMyLeaves(int userId){
        return leaveDAO.getLeavesByUser(userId);
    }

    // Admin: Get all leaves
    public List<Leave> getAllLeave(){
        return leaveDAO.getAllLeaves();
    }

    // Admin: Update status
    public String updateLeaveStatus(int leaveId, String status){
        if(!status.equalsIgnoreCase("APPROVED") &&
            !status.equalsIgnoreCase("REJECTED")
        ){
            return "Error: Invalid status";
        }

        boolean updatedStatus = leaveDAO.updateLeaveStatus(leaveId, status.toUpperCase());

        if(updatedStatus){
            return "Leave status updated successfully";
        }else{
            return "Failed to update leave status";
        }
    }
}
