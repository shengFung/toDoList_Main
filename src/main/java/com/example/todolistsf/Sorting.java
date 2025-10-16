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

public class Sorting implements Initializable {

    @FXML
    private TableView<TableInfo> tableInfo;


    @FXML
    private TableColumn<TableInfo, String> title;

    @FXML
    private TableColumn<TableInfo, String> priority;


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

        title.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("title"));
        priority.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("priority"));
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
    protected void onSortButtonClick() {
        int sortStyle;
        try {
            sortStyle = Integer.parseInt(idInput.getText());
            if (!(sortStyle >=1 && sortStyle <=4 )){
                failText.setVisible(true);
                sucessText.setVisible(false);
                return; // Exit the method if input is invalid
            }
        } catch (NumberFormatException e) {
            failText.setVisible(true);
            sucessText.setVisible(false);
            return; // Exit the method if input is invalid
        }


        switch (sortStyle) {
            case 1:
                dueDateAsc();
                break;
            case 2:
                dueDateDesc();
                break;
            case 3:
                priorityHtoL();
                break;
            case 4:
                priorityLtoH();
                break;
            }
    }

    private void dueDateAsc() {
        String selectSQL = "SELECT title,dueDate FROM tasks";
        String[][] SortingArray = connectandGet(selectSQL);
        for (int i = 0; i < SortingArray.length - 1; i++) {
            for (int j = 0; j < SortingArray.length - i - 1; j++) {
                // Compare by date (second column)
                int dateComparison = SortingArray[j][1].compareTo(SortingArray[j + 1][1]);

                if (dateComparison > 0 || (dateComparison == 0 &&
                        SortingArray[j][0].compareToIgnoreCase(SortingArray[j + 1][0]) > 0)) {
                    // Swap the rows if they are out of order
                    String[] temp = SortingArray[j];
                    SortingArray[j] = SortingArray[j + 1];
                    SortingArray[j + 1] = temp;
                }
            }
        }
        ObservableList<TableInfo> list = FXCollections.observableArrayList();
        for (int i=0;i< SortingArray.length;i++){
            int id = 5;
            String title = SortingArray[i][0];
            String priority = "priority";
            String status = "completion";
            String duedate = SortingArray[i][1];

            list.add(new TableInfo(id, title, priority, status, duedate));
        }
        priority.setVisible(false);
        duedate.setVisible(true);
        tableInfo.getItems().clear();
        tableInfo.setItems(list);
        failText.setVisible(false);
        sucessText.setVisible(true);

    }

    private void dueDateDesc() {
        String selectSQL = "SELECT title,dueDate FROM tasks";
        String[][] SortingArray = connectandGet(selectSQL);
        for (int i = 0; i < SortingArray.length - 1; i++) {
            for (int j = 0; j < SortingArray.length - i - 1; j++) {
                // Compare by date (second column)
                int dateComparison = SortingArray[j][1].compareTo(SortingArray[j + 1][1]);

                if (dateComparison < 0 || (dateComparison == 0 &&
                        SortingArray[j][0].compareToIgnoreCase(SortingArray[j + 1][0]) > 0)) {
                    // Swap the rows if they are out of order
                    String[] temp = SortingArray[j];
                    SortingArray[j] = SortingArray[j + 1];
                    SortingArray[j + 1] = temp;
                }
            }
        }
        ObservableList<TableInfo> list = FXCollections.observableArrayList();
        for (int i=0;i< SortingArray.length;i++){
            int id = 5;
            String title = SortingArray[i][0];
            String priority = "priority";
            String status = "completion";
            String duedate = SortingArray[i][1];

            list.add(new TableInfo(id, title, priority, status, duedate));
        }
        priority.setVisible(false);
        duedate.setVisible(true);
        tableInfo.getItems().clear();
        tableInfo.setItems(list);
        failText.setVisible(false);
        sucessText.setVisible(true);

    }

    private void priorityHtoL (){
        String selectSQL = "SELECT title,priority FROM tasks";
        String [][] SortingArray = connectandGet(selectSQL);
        for (int i = 0; i<SortingArray.length-1;i++){
            for (int j = 0; j<SortingArray.length-1-i;j++){
                if(Integer.valueOf(SortingArray[j][1])<Integer.valueOf(SortingArray[j+1][1])){
                    String tempTitle = SortingArray[j][0];
                    String tempValue = SortingArray[j][1];
                    SortingArray[j][0] = SortingArray[j+1][0];
                    SortingArray[j+1][0] = tempTitle;
                    SortingArray[j][1] = SortingArray[j+1][1];
                    SortingArray[j+1][1] = tempValue;
                }
            }
        }
        prioritySortEnding(SortingArray);
    }

    private void priorityLtoH (){
        String selectSQL = "SELECT title,priority FROM tasks";
        String [][] SortingArray = connectandGet(selectSQL);
        for (int i = 0; i<SortingArray.length-1;i++){
            for (int j = 0; j<SortingArray.length-1-i;j++){
                if(Integer.valueOf(SortingArray[j][1])>Integer.valueOf(SortingArray[j+1][1])){
                    String tempTitle = SortingArray[j][0];
                    String tempValue = SortingArray[j][1];
                    SortingArray[j][0] = SortingArray[j+1][0];
                    SortingArray[j+1][0] = tempTitle;
                    SortingArray[j][1] = SortingArray[j+1][1];
                    SortingArray[j+1][1] = tempValue;
                }
            }
        }
        prioritySortEnding(SortingArray);
    }

        static String[][] connectandGet (String selectSQL){
        try (Connection connection = Cnx.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

        // Execute the query
        ResultSet resultSet = preparedStatement.executeQuery();

        // Dynamically store results in a list first (since we don't know the number of rows beforehand)
        List<String[]> resultList = new ArrayList<>();
        while (resultSet.next()) {
            String title = resultSet.getString("title");
            if (selectSQL.contains("dueDate")){
                String date = resultSet.getString("dueDate");
                resultList.add(new String[]{title, date});
            } else{
                String priority = "0";
                switch (resultSet.getString("priority")){
                    case "High": priority = "3"; break;
                    case "Medium": priority = "2"; break;
                    case "Low": priority = "1"; break;
                }
                resultList.add(new String[]{title, priority});
            }
        }

        // Convert the list to a 2D array
        String[][] resultArray = new String[resultList.size()][2];
        resultArray = resultList.toArray(resultArray);

         //Print the 2D array (optional)
//        for (String[] row : resultArray) {
//            System.out.println("Title: " + row[0] + ", Priority or Date: " + row[1]);
//        }
        return resultArray;
      } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
            return null;
        }
    }
    private void prioritySortEnding(String[][] SortingArray) {
        int k = 1 ;
        int [] section = new int [3];
        section [0]=0;
        for (int i = 0; i<SortingArray.length-1;i++){
            if (SortingArray[i][1] != SortingArray[i+1][1]){
                section [k] = i+1;
                k++;
            }
        }

        List<List<String>> sectionA = new ArrayList<>();
        List<List<String>> sectionB = new ArrayList<>();
        List<List<String>> sectionC = new ArrayList<>();
        List<List<String>> SortingArrayFinal = new ArrayList<>();

        for (int i = 0; i<section[1];i++){
            sectionA.add(Arrays.asList(SortingArray[i]));
        }
        sectionA.sort((row1, row2) -> row1.get(0).compareToIgnoreCase(row2.get(0)));
        for (int i = section[1]; i<section[2];i++){
            sectionB.add(Arrays.asList(SortingArray[i]));
        }
        sectionB.sort((row1, row2) -> row1.get(0).compareToIgnoreCase(row2.get(0)));
        for (int i = section[2]; i<SortingArray.length;i++){
            sectionC.add(Arrays.asList(SortingArray[i]));
        }
        sectionC.sort((row1, row2) -> row1.get(0).compareToIgnoreCase(row2.get(0)));

        SortingArrayFinal.addAll(sectionA);
        SortingArrayFinal.addAll(sectionB);
        SortingArrayFinal.addAll(sectionC);

        SortingArray = new String[SortingArrayFinal.size()][];
        for (int i = 0; i < SortingArrayFinal.size(); i++) {
            SortingArray[i] = SortingArrayFinal.get(i).toArray(new String[0]);
        }


        for (int i = 0; i<SortingArray.length;i++){
            switch(SortingArray[i][1]){
                case "3": SortingArray[i][1] = "High"; break;
                case "2": SortingArray[i][1] = "Medium"; break;
                case "1": SortingArray[i][1] = "Low"; break;
            }
        }
        ObservableList<TableInfo> list = FXCollections.observableArrayList();
        for (int i=0;i< SortingArray.length;i++){
            int id = 5;
            String title = SortingArray[i][0];
            String priority = SortingArray[i][1];
            String status = "completion";
            String duedate = "date";

            list.add(new TableInfo(id, title, priority, status, duedate));
        }
        priority.setVisible(true);
        duedate.setVisible(false);
        tableInfo.getItems().clear();
        tableInfo.setItems(list);
        failText.setVisible(false);
        sucessText.setVisible(true);

    }


}




//

//

//

//
//}
