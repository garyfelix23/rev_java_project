package org.example.dao;

import org.example.config.DBConfig;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    public boolean signup(User user){
        String sql = "INSERT INTO users (name, email, password, role) VALUES(?, ?, ?, ?)";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            // By default, it is employee, because, if we give choice to choose ADMIN or EMPLOYEE, the user can choose ADMIN even if they aren't eligible for ADMIN.
            ps.setString(4, "EMPLOYEE");

            return ps.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public User login(String email, String password){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try(Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
