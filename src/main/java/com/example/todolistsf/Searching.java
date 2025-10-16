/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.todolistsf;

/**
 *
 * @author GohSF
 */
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
public class Searching implements Initializable {
    @FXML
    private TableView<TableInfo> tableInfo;

    @FXML
    private TableColumn<TableInfo, String> title;

    @FXML
    private TableColumn<TableInfo, String> description;

    @FXML
    private TableColumn<TableInfo, String> status;

    @FXML
    private TableColumn<TableInfo, String> duedate;

    @FXML
    private TextField textInput;

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


        description.setPrefWidth(60);

        status.setPrefWidth(80);

        duedate.setPrefWidth(80);

        title.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("priority"));
        status.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("status"));
        duedate.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("duedate"));

        refreshTableData();
    }
    private void refreshTableData() {
        ObservableList<TableInfo> list = FXCollections.observableArrayList();

        String selectSQL = "SELECT no, title, description, completion, dueDate FROM tasks";

        try (Connection connection = Cnx.getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("no");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("completion");
                String duedate = rs.getString("dueDate");

                list.add(new TableInfo(id, title, description, status, duedate));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        tableInfo.getItems().clear();  // Clear existing items
        tableInfo.setItems(list);  // Update the table with the new list
    }


    @FXML
    protected void onSearchButtonClick() {
        String keyWord = textInput.getText();;

        String query = "SELECT * FROM tasks WHERE title LIKE ? OR description LIKE ?";

        try (Connection connection = Cnx.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the value for the parameter
            statement.setString(1, "%" + keyWord + "%");
            statement.setString(2, "%" + keyWord + "%");

            // Execute the query after setting the parameter
            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    failText.setVisible(true);
                    sucessText.setVisible(false);
                } else {
                    ObservableList<TableInfo> list = FXCollections.observableArrayList();
                    do {


                        int id = 5;
                        String title = resultSet.getString("title");
                        String description = resultSet.getString("description");
                        String status = resultSet.getString("completion");
                        String duedate = resultSet.getString("dueDate");

                        list.add(new TableInfo(id, title, description, status, duedate));

                    } while (resultSet.next());
                    tableInfo.getItems().clear();
                    tableInfo.setItems(list);
                    failText.setVisible(false);
                    sucessText.setVisible(true);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
