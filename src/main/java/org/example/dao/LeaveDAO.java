package org.example.dao;

import org.example.config.DBConfig;
import org.example.model.Leave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {
    public boolean applyLeave(Leave leave){
        String sql = "INSERT INTO leaves (user_id, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setInt(1, leave.getUser_id());
            ps.setDate(2, Date.valueOf(leave.getStart_date()));
            ps.setDate(3, Date.valueOf(leave.getEnd_date()));
            ps.setString(4, leave.getReason());
            ps.setString(5, "PENDING");

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // get leaves by user
    public List<Leave> getLeavesByUser(int userId){
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ?";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setInt(1, userId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    leaves.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaves;
    }

    // ADMIN: Get all Leaves
    public List<Leave> getAllLeaves(){
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()){
                leaves.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaves;
    }

    public boolean updateLeaveStatus(int leaveId, String status){
        String sql = "UPDATE leaves SET status = ? WHERE id = ?";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, status);
            ps.setInt(2, leaveId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Row mapper
    private Leave mapRow(ResultSet rs) throws SQLException{
        Leave leave = new Leave();

        leave.setId(rs.getInt("id"));
        leave.setUser_id(rs.getInt("user_id"));
        leave.setStart_date(rs.getDate("start_date").toLocalDate());
        leave.setEnd_date(rs.getDate("end_date").toLocalDate());
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));

        return leave;

    }
}
