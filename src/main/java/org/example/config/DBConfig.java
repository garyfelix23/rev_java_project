package org.example.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConfig {
    private static final String PROPERTIES_FILE = "db.properties";
    public static Connection getConnection() throws SQLException {
        try(InputStream input = DBConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        try(Connection con = DBConfig.getConnection()){
            if (con != null){
                System.out.println("Success");
            }else{
                System.out.println("Failed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
