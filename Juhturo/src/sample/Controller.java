package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.util.ArrayList;

public class Controller {

    @FXML
    private TableView tv1;
    @FXML
    private TextField Rfelhasznalo;
    @FXML
    private TextField Rjelszo;
    @FXML
    private TextField Rteljes_nev;
    @FXML
    private TextField Remail;

    private DB db;

    @FXML
    public void initialize(){
        db= new DB();
        TableColumn fh_nevCollum = new TableColumn("fh_nev");
        fh_nevCollum.setCellFactory(new PropertyValueFactory<>("fh_nev"));
        TableColumn jelszoCollum = new TableColumn("jelszo");
        fh_nevCollum.setCellFactory(new PropertyValueFactory<>("jelszo"));
        TableColumn teljes_nevCollum = new TableColumn("teljes_nev");
        fh_nevCollum.setCellFactory(new PropertyValueFactory<>("teljes_nev"));
        TableColumn emailCollum = new TableColumn("email");
        fh_nevCollum.setCellFactory(new PropertyValueFactory<>("email"));
        tv1.getColumns().addAll(fh_nevCollum, jelszoCollum, teljes_nevCollum, emailCollum);
        System.out.println("asd");
    }

    public void pressRead(ActionEvent e) {
        System.out.println("Reading all data...");
        ArrayList<Felhasznalok> fh_adat = db.read();
        tv1.getItems().clear();
        for (Felhasznalok felhasznalok : fh_adat){
            tv1.getItems().add(felhasznalok);
        }
    }
    public void pressReg(ActionEvent e) {
        System.out.println("Registrating...");
        Felhasznalok felhasznalok = new Felhasznalok(
                Rfelhasznalo.getText(),
                Rjelszo.getText(),
                Rteljes_nev.getText(),
                Remail.getText());
        db.insert(felhasznalok);
    }

}