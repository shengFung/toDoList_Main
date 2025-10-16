/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.todolistsf;

/*
 *
 * @author GohSF
 */
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ManageTask implements Initializable {

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
    private Label previousDpd;

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

    @FXML
    protected void onManageButtonClick() {
        // Initialize variable for the row to delete
        int markComplete;

        // Parse input to get the row ID
        try {
            markComplete = Integer.parseInt(idInput.getText());
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }



        // SQL query for deletion
        String selectSQL = "SELECT dependencies FROM tasks WHERE no = ?";
        String dependSQL = "SELECT completion FROM tasks WHERE no = ?";
        String updateSQL = "UPDATE tasks SET completion = 'Complete' WHERE no = ?";

        // Get the connection to the database
        try (Connection connection = Cnx.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            // Prepare the DELETE query
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
                 PreparedStatement dependStatement = connection.prepareStatement(dependSQL);
                 PreparedStatement updateStatement = connection.prepareStatement(updateSQL)
            ) {
                selectStatement.setInt(1, markComplete);
                ResultSet resultSelect = selectStatement.executeQuery();
                try {
                    if (resultSelect.next()) {
                        int valueFromResult = resultSelect.getInt(1); // Try to get value from column 1
                        dependStatement.setInt(1, valueFromResult);
                        ResultSet resultDepend = dependStatement.executeQuery();
                        if (resultDepend.next()) {
                            String valueFromComplete = resultDepend.getString(1); // Try to get value from column 1
                            if ("Incomplete".equals(valueFromComplete)) { // Safely compare strings
                                previousDpd.setText("Dependency Error - Previous Task ID "+valueFromResult+" is Incomplete!");
                                previousDpd.setVisible(true);
                                failText.setVisible(false);
                                sucessText.setVisible(false);
                                return;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                // Set the ID parameter for the DELETE query
                updateStatement.setInt(1, markComplete);

                // Execute the DELETE query
                int rowsAffected = updateStatement.executeUpdate();

                // Provide feedback based on the result
                if (rowsAffected > 0) {
                    Platform.runLater(this::refreshTableData);
                    failText.setVisible(false);
                    previousDpd.setVisible(false);
                    sucessText.setVisible(true);
                } else {
                    failText.setVisible(true);
                    previousDpd.setVisible(false);
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













//    static Scanner input = new Scanner(System.in);
//    public static void main (String [] args){
//        System.out.println("=== Mark Task as Complete ===");
//        System.out.print("Enter the task number you want to mark as complete: ");
//        int markComplete = input.nextInt();
//
//        String updateSQL = "UPDATE tasks SET completion = 'Complete' WHERE no = ?";
//        String selectSQL = "SELECT title FROM tasks WHERE no = ?";
//
//        // Get the connection to the database
//        try (Connection connection = Cnx.getConnection()) {
//            // Start a transaction
//            connection.setAutoCommit(false);
//
//            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
//                 PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
//
//                // Set the ID parameter for the SELECT query
//                selectStatement.setInt(1, markComplete);
//
//                // Execute the SELECT query and get the result
//                ResultSet resultSet = selectStatement.executeQuery();
//
//                // Check if the result contains data (i.e., a row was found)
//                if (resultSet.next()) {
//                    // Get the title from the result set
//                    String title = resultSet.getString("title");
//
//                    // Set the ID parameter for the DELETE query
//                    updateStatement.setInt(1, markComplete);
//
//                    // Execute the DELETE query
//                    int rowsAffected = updateStatement.executeUpdate();
//                    if (rowsAffected > 0) {
////                        System.out.println("Task with ID " + deleteRow + " deleted successfully.");
//                          System.out.println("Task \""+ title +"\" marked as complete!");
//                    } else {
//                        System.out.println("No task found with ID " + markComplete + " to delete.");
//                    }
//
//                    // Commit the transaction
//                    connection.commit();
//                } else {
//                    System.out.println("No task found with ID " + markComplete);
//                    connection.rollback(); // Rollback if no data found to delete
//                }
//
//            } catch (SQLException e) {
//                connection.rollback(); // Rollback the transaction in case of error
//                System.out.println("Error: " + e.getMessage());
//            } finally {
//                connection.setAutoCommit(true); // Reset auto-commit mode
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Database connection error: " + e.getMessage());
//        }
//}



}
