package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {
    boolean isLoggedIn = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //alap bejelentkezes meg regisztracio scene
        if (isLoggedIn == false) {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            primaryStage.setTitle("Juhturo 0.1");
            root.getStylesheets().add(getClass().getResource("./media/stylesheet.css").toExternalForm());
            primaryStage.setScene(new Scene(root, 900, 500));
            primaryStage.show();
        }

        //videomegoszto scene, ha be van lépve ez fusson le vagy külön függvénybe legyen meghívva
        // most ha ez ifen kívül van akkor ez a scene lesz a fő scene
        else {
            Parent kezdolap = FXMLLoader.load(getClass().getResource("kezdolap.fxml"));
            primaryStage.setTitle("Juhturo 0.1");
            kezdolap.getStylesheets().add(getClass().getResource("./media/stylesheet.css").toExternalForm());
            primaryStage.setScene(new Scene(kezdolap, 1024, 768));
            primaryStage.show();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
