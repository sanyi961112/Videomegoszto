package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    private TableView tv1;

    public void pressRead(ActionEvent e) {
        System.out.println("Reading all data...");
    }
}
