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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// Import SQL libraries
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateTask implements Initializable {

    // Initialize all elements in stage name
    @FXML
    private TextField titleText;

    @FXML
    private TextArea descriptionText;

    @FXML
    private DatePicker dateText;

    @FXML
    private ComboBox<String> categoryTexts;


    @FXML
    private ComboBox<String> priorityTexts;

    @FXML
    private ComboBox<String> reccurence;

    @FXML
    private Label sucessText;

    @FXML
    private Label failText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryTexts.setItems(FXCollections.observableArrayList("Homework", "Personal","Work"));
        priorityTexts.setItems(FXCollections.observableArrayList("Low", "Medium","High"));
        reccurence.setItems(FXCollections.observableArrayList("None", "Daily","Weekly", "Monthly"));
    }



    // When back button pressed
    @FXML
    protected void goToMainPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) sucessText.getScene().getWindow();  // Get the current stage
            stage.setScene(scene);  // Change the scene to the new one


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // When Create Task button prssed
    @FXML
    protected void onCreateButtonClick() {
        // Check whether there is empty spaces before proccssed
        if (!titleText.getText().trim().isEmpty() && !descriptionText.getText().trim().isEmpty() && dateText.getValue() != null && (categoryTexts.getValue() != null) && (priorityTexts.getValue() != null)&& (reccurence.getValue() != null)) {

            // Print the label green success
            failText.setVisible(false);
            sucessText.setVisible(true);
            // Initialize all vars for textbox values
            String title = titleText.getText();
            String description = descriptionText.getText();
            LocalDate mydate = dateText.getValue();
            String date = String.valueOf(mydate);
            String category = categoryTexts.getValue();
            String priority = priorityTexts.getValue();
            String recurrence = reccurence.getValue();
            String completion = "Incomplete";
            String rccDate = null;
            if (!recurrence.equals("None")){
                rccDate = LocalDate.now().toString();
            }




            // SQL query
            String insertSQL = "INSERT INTO tasks (title,description,dueDate,category,priority,completion, recurrence, rccDate) VALUES(?,?,?,?,?,?,?,?)";

            // Insert Data
            try (Connection connection = Cnx.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                // Set parameters for the query
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, description);
                preparedStatement.setDate(3, java.sql.Date.valueOf(date));
                preparedStatement.setString(4, category);
                preparedStatement.setString(5, priority);
                preparedStatement.setString(6, completion);
                preparedStatement.setString(7, recurrence);
                preparedStatement.setString(8, rccDate);
                int rowsAffected = preparedStatement.executeUpdate();
                // Print log if no error
                System.out.print("\nTask \"" + title + "\" added successfully!");
            } catch (SQLException e) {
                // Catch any error
                System.out.println("Error inserting data: " + e.getMessage());
            }
        } else {
            // If got spaces print red warning
            failText.setVisible(true);
            sucessText.setVisible(false);
            dateText.setValue(null);
        }
    }

}

