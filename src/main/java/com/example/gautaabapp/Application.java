package com.example.gautaabapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("GAUTAAB program");
        stage.setResizable(false);
        stage.setScene(scene);
        Image icon = new Image("C:/gautaabApp/src/main/resources/Image/icon3.png");
        stage.getIcons().add(icon);
        stage.show();
        //stage.getIcons().add(("file:satelliteIcon.png"));


    }

    public static void main(String[] args) {
        launch();
    }
}