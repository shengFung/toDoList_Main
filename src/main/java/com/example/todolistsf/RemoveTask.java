/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.todolistsf;

/**
 *
 * @author GohSF
 */

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;

import java.util.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.Initializable;
public class RemoveTask implements Initializable   {

    @FXML
    private TableView<TableInfo> tableInfo;

    @FXML
    private TableColumn<TableInfo, Integer> id;

    @FXML
    private TableColumn<TableInfo, String> title;

    @FXML
    private TableColumn<TableInfo, String> priority;

    @FXML
    private TableColumn<TableInfo, String> status;

    @FXML
    private TableColumn<TableInfo, String> duedate;

    @FXML
    private TextField idInput;

    @FXML
    private Label sucessText;

    @FXML
    private Label failText;


    @FXML
    protected void goToMainPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) tableInfo.getScene().getWindow();  // Get the current stage
            stage.setScene(scene);  // Change the scene to the new one


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setMinWidth(30);
        id.setMaxWidth(30);
        id.setPrefWidth(30);

        priority.setPrefWidth(60);

        status.setPrefWidth(80);

        duedate.setPrefWidth(80);

        id.setCellValueFactory(new PropertyValueFactory<TableInfo, Integer>("id"));
        title.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("title"));
        priority.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("priority"));
        status.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("status"));
        duedate.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("duedate"));

        refreshTableData();
    }

    @FXML
    protected void onRemoveButtonClick() {
        // Initialize variable for the row to delete
        int deleteRow;

        // Parse input to get the row ID
        try {
            deleteRow = Integer.parseInt(idInput.getText());
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }

        // SQL query for deletion
        String deleteSQL = "DELETE FROM tasks WHERE no = ?";

        // Get the connection to the database
        try (Connection connection = Cnx.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            // Prepare the DELETE query
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL)) {

                // Set the ID parameter for the DELETE query
                deleteStatement.setInt(1, deleteRow);

                // Execute the DELETE query
                int rowsAffected = deleteStatement.executeUpdate();

                // Provide feedback based on the result
                if (rowsAffected > 0) {
                    Platform.runLater(this::refreshTableData);
                    failText.setVisible(false);
                    sucessText.setVisible(true);
                } else {
                    failText.setVisible(true);
                    sucessText.setVisible(false);
                }

                // Commit the transaction
                connection.commit();

            } catch (SQLException e) {
                // Rollback the transaction in case of error
                connection.rollback();
                System.out.println("Error during deletion: " + e.getMessage());
            } finally {
                // Reset auto-commit mode
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void refreshTableData() {
        ObservableList<TableInfo> list = FXCollections.observableArrayList();

        String selectSQL = "SELECT no, title, priority, completion, dueDate FROM tasks";

        try (Connection connection = Cnx.getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("no");
                String title = rs.getString("title");
                String priority = rs.getString("priority");
                String status = rs.getString("completion");
                String duedate = rs.getString("dueDate");

                list.add(new TableInfo(id, title, priority, status, duedate));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        tableInfo.getItems().clear();  // Clear existing items
        tableInfo.setItems(list);  // Update the table with the new list
    }






}

