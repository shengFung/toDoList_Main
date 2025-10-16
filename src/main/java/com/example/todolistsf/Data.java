/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.todolistsf;

/**
 *
 * @author GohSF
 */
// Import Java FX Libraries

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;






    public class Data implements Initializable {

        // Initialize all elements in stage name
        @FXML
        private Text total;
        @FXML
        private Text complete;
        @FXML
        private Text pending;
        @FXML
        private Text rate;
        @FXML
        private Text homework;
        @FXML
        private Text personal;
        @FXML
        private Text work;


        @FXML
        protected void goToMainPage() {
            try {
                // Load the create.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                AnchorPane root = loader.load();

                // Set the new scene with the loaded FXML
                Scene scene = new Scene(root);
                Stage stage = (Stage) total.getScene().getWindow();  // Get the current stage
                stage.setScene(scene);  // Change the scene to the new one


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            total.setText(getRow("SELECT COUNT(*) AS totalRow FROM tasks"));
            complete.setText(getRow("SELECT COUNT(*) AS totalRow FROM tasks WHERE completion = 'Complete'"));
            pending.setText(getRow("SELECT COUNT(*) AS totalRow FROM tasks WHERE completion = 'Incomplete'"));
            double percentage = (double) Integer.parseInt(complete.getText()) /Integer.parseInt(total.getText())*100;
            rate.setText(String.format("%.2f",percentage));
            homework.setText("Homework: "+getRow("SELECT COUNT(*) AS totalRow FROM tasks WHERE category = 'Homework'"));
            personal.setText("Personal: "+getRow("SELECT COUNT(*) AS totalRow FROM tasks WHERE category = 'Personal'"));
            work.setText("Work: "+getRow("SELECT COUNT(*) AS totalRow FROM tasks WHERE category = 'Work'"));
        }

        public String getRow(String selectSQL){
            try (Connection connection = Cnx.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    int rowCount = resultSet.getInt("totalRow");
                    return String.valueOf(rowCount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "0";
        }



    }






