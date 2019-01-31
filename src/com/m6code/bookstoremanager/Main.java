package com.m6code.bookstoremanager;

import com.m6code.bookstoremanager.model.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/sample.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Bookstore Manager");
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!Datasource.getInstance().open()){
            System.out.println("Error: couldn't connect to db ");
            Platform.exit();
        }

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }
}
