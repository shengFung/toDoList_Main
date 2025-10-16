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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class EditTask implements Initializable {

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
    private ComboBox<String> selectChange;

    @FXML
    private TextField textChange;


    @FXML
    private Label sucessText;

    @FXML
    private Label failText;

    @FXML
    private Label previousDpd;


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

        selectChange.setItems(FXCollections.observableArrayList("Title", "Description", "Due Date", "Category", "Priority", "Status", "Dependency","Recurrence"));
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
        title.setText("Title");
        priority.setText("Priority");
        id.setVisible(true);
        status.setVisible(true);
        duedate.setVisible(true);
        tableInfo.getItems().clear();  // Clear existing items
        tableInfo.setItems(list);  // Update the table with the new list
    }

    @FXML
    protected void refresh() {
        refreshTableData();
    }




    @FXML
    protected void onIdSearchClick() {
        int idValue;

        // Parse input to get the row ID
        try {
            idValue = Integer.parseInt(idInput.getText());
        } catch (NumberFormatException e) {
            System.out.print("Id doesn't Exist");
            return; // Exit the method if input is invalid
        }

        String query = "SELECT * FROM tasks WHERE no = ?";
        try (Connection connection = Cnx.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the value for the parameter
            statement.setInt(1, idValue);

            // Execute the query after setting the parameter
            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    System.out.print("Id doesn't Exist");
                } else {
                    ObservableList<TableInfo> list = FXCollections.observableArrayList();
                    do {

                        String title;
                        String description;
                        int id = 5;
                        String status = resultSet.getString("completion");
                        String duedate = resultSet.getString("dueDate");


                        title = "Title >>";
                        description = resultSet.getString("title");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Description >>";
                        description = resultSet.getString("description");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Due Date >>";
                        description = resultSet.getString("dueDate");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Category >>";
                        description = resultSet.getString("category");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Priority >>";
                        description = resultSet.getString("priority");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Status >>";
                        description = resultSet.getString("completion");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Dependency >>";
                        description = resultSet.getString("dependencies");
                        list.add(new TableInfo(id, title, description, status, duedate));

                        title = "Recurrence >>";
                        description = resultSet.getString("recurrence");
                        list.add(new TableInfo(id, title, description, status, duedate));



                    } while (resultSet.next());
                    title.setText("Details");
                    priority.setText("Values");
                    id.setVisible(false);
                    status.setVisible(false);
                    duedate.setVisible(false);
                    tableInfo.getItems().clear();
                    tableInfo.setItems(list);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onEditButtonClick() {
        int idValue;

        // Parse input to get the row ID
        try {
            idValue = Integer.parseInt(idInput.getText());
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            previousDpd.setVisible(false);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }

        if (selectChange.getValue() == null){
            failText.setVisible(true);
            previousDpd.setVisible(false);
            sucessText.setVisible(false);
            return;
        }
        String selecttext = selectChange.getValue();
        selecttext = switch (selecttext) { case "Title" -> "title"; case "Description" -> "description";case "Due Date" -> "dueDate";case "Category" -> "category";case "Priority" -> "priority";case "Status" -> "completion";case "Dependency" -> "dependencies";case "Recurrence" -> "recurrence";default -> selecttext; };


        String editText = textChange.getText();

        // SQL query for deletion
        String updateSQL = "UPDATE tasks SET " + selecttext + " = ? WHERE no = ?";

        // Get the connection to the database
        try (Connection connection = Cnx.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            if ("dependencies".equals(selecttext)) {
                String checkDependencySQL = "SELECT COUNT(*) FROM tasks WHERE no = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkDependencySQL)) {
                    checkStatement.setString(1, editText);

                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) == 0 || editText.equals(String.valueOf(idValue))) {
                            failText.setVisible(true);
                            previousDpd.setVisible(false);
                            sucessText.setVisible(false);
                            connection.rollback(); // Rollback in case anything was started
                            return;
                        }
                    }
                }
            }
            // Check date format
            if ("dueDate".equals(selecttext)) {
                String dateToCheck = editText; // The date value to check

                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false); // Strict parsing (no leniency on invalid dates)

                try {
                    // Try parsing the date. If invalid, it will throw a ParseException
                    dateFormat.parse(dateToCheck);
                } catch (ParseException e) {
                    failText.setVisible(true);
                    previousDpd.setVisible(false);
                    sucessText.setVisible(false);
                    return;
                }
            }
            if ("category".equals(selecttext) && !editText.equals("Homework")&& !editText.equals("Personal")&& !editText.equals("Work")) {
                failText.setVisible(true);
                previousDpd.setVisible(false);
                sucessText.setVisible(false);
                return;
            }
            if ("priority".equals(selecttext) && !editText.equals("Low")&& !editText.equals("Medium")&& !editText.equals("High")) {
                failText.setVisible(true);
                previousDpd.setVisible(false);
                sucessText.setVisible(false);
                return;
            }
            if ("recurrence".equals(selecttext) && !editText.equals("None")&& !editText.equals("Daily")&& !editText.equals("Weekly")&& !editText.equals("Monthly")) {
                failText.setVisible(true);
                previousDpd.setVisible(false);
                sucessText.setVisible(false);
                return;
            }
            if ("completion".equals(selecttext)) {
                if (editText.equals("Incomplete") || editText.equals("Complete")){
                    if(editText.equals("Complete") && checkDependencies(connection, idValue)){
                        return;
                    }
                }else{
                    failText.setVisible(true);
                    previousDpd.setVisible(false);
                    sucessText.setVisible(false);
                    return;
                }
            }


            // Prepare the UPDATE query
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

                // Set the ID parameter for the DELETE query
                updateStatement.setString(1, editText);
                updateStatement.setInt(2, idValue);

                // Execute the DELETE query
                int rowsAffected = updateStatement.executeUpdate();

                // Provide feedback based on the result
                if (rowsAffected > 0) {
                    Platform.runLater(this::onIdSearchClick);
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

    private boolean checkDependencies(Connection connection, int markComplete ){
        // SQL query for deletion
        String selectSQL = "SELECT dependencies FROM tasks WHERE no = ?";
        String dependSQL = "SELECT completion FROM tasks WHERE no = ?";

        // Prepare the DELETE query
        try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
             PreparedStatement dependStatement = connection.prepareStatement(dependSQL);
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
                            previousDpd.setText("Dependency Error - Previous Task ID " + valueFromResult + " is Incomplete!");
                            previousDpd.setVisible(true);
                            failText.setVisible(false);
                            sucessText.setVisible(false);
                            return true;
                        }
                    }
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






}













