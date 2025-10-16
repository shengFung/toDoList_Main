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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Dependency implements Initializable {

    @FXML
    private TableView<TableInfo> tableInfo;

    @FXML
    private TableColumn<TableInfo, Integer> id;

    @FXML
    private TableColumn<TableInfo, String> title;

    @FXML
    private TableColumn<TableInfo, String> priority;

    @FXML
    private TableColumn<TableInfo, String> dependency;


    @FXML
    private TextField idInput;

    @FXML
    private TextField dependencyText;

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

        priority.setPrefWidth(80);

        title.setPrefWidth(90);

        dependency.setPrefWidth(50);

        id.setCellValueFactory(new PropertyValueFactory<TableInfo, Integer>("id"));
        title.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("title"));
        priority.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("priority"));
        dependency.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("status"));

        refreshTableData();
    }

    private void refreshTableData() {
        ObservableList<TableInfo> list = FXCollections.observableArrayList();

        String selectSQL = "SELECT no, title, priority, dependencies FROM tasks";

        try (Connection connection = Cnx.getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("no");
                String title = rs.getString("title");
                String priority = rs.getString("priority");
                String dependency = rs.getString("dependencies");
                if (dependency == null) {
                    dependency = "N/A";
                }
                list.add(new TableInfo(id, title, priority, dependency, ""));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        tableInfo.getItems().clear();  // Clear existing items
        tableInfo.setItems(list);  // Update the table with the new list
    }

    @FXML
    protected void onSetButtonClick() {
        int selectText, setText;


        // Parse input to get the row ID
        try {
            selectText = Integer.parseInt(idInput.getText());
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }

        try {
            setText = Integer.parseInt(dependencyText.getText());
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }


        // SQL query for deletion
        String updateSQL = "UPDATE tasks SET dependencies = ? WHERE no = ?";

        // Get the connection to the database
        try (Connection connection = Cnx.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            String checkDependencySQL = "SELECT COUNT(*) FROM tasks WHERE no = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkDependencySQL)) {
                checkStatement.setString(1, String.valueOf(setText));

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0 ||String.valueOf(setText).equals(String.valueOf(selectText))) {
                        failText.setVisible(true);
                        sucessText.setVisible(false);
                        connection.rollback(); // Rollback in case anything was started
                        return;
                    }
                }
            }






            // Prepare the DELETE query
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

                // Set the ID parameter for the DELETE query
                updateStatement.setInt(1, setText);
                updateStatement.setInt(2, selectText);

                // Execute the DELETE query
                int rowsAffected = updateStatement.executeUpdate();

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













}
