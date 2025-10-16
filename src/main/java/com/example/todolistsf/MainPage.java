package com.example.todolistsf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 650);
        stage.setResizable(false);
        Image icon = new Image("logo.png");
        stage.setTitle("To Do List");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

}