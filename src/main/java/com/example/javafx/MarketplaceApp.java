// MarketplaceApp.java
package com.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MarketplaceApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MarketplaceData.loadData(); // Load data first
        FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("My Marketplace");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}