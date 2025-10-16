/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.todolistsf;

/**
 *
 * @author GohSF
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connect to the database
public class Cnx {
    private static final String URL = "jdbc:mysql://localhost:3306/toDoList"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = ""; // Replace with your MySQL password
    
    // Get the database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
