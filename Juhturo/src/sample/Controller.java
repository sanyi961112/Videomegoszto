package sample;


import com.sun.org.apache.xpath.internal.compiler.FuncLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Controller extends Main{

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
    @FXML
    private TextField Bfelhasznalo;
    @FXML
    private TextField Bjelszo;
    @FXML
    private Label nope;
    @FXML
    public Label label_username;

    protected DB db;
    public String nev=null;
    private Stage stage;
    private Scene scene;
    private Parent root;

 @FXML
 public void initialize(){
     db= new DB();
     TableColumn fh_nevCollum = new TableColumn("fh_nev");
     fh_nevCollum.setCellFactory(new PropertyValueFactory<>("fh_nev"));
     TableColumn jelszoCollum = new TableColumn("jelszo");
     jelszoCollum.setCellFactory(new PropertyValueFactory<>("jelszo"));
     TableColumn teljes_nevCollum = new TableColumn("teljes_nev");
     teljes_nevCollum.setCellFactory(new PropertyValueFactory<>("teljes_nev"));
     TableColumn emailCollum = new TableColumn("email");
     emailCollum.setCellFactory(new PropertyValueFactory<>("email"));

     //tv1.getColumns().addAll(fh_nevCollum, jelszoCollum, teljes_nevCollum, emailCollum);
//     System.out.println("asd");
 }

    public void pressRead(ActionEvent e) {
        System.out.println("Reading all data...");
        ArrayList<Felhasznalok> felhasznalok = db.read();
        tv1.getItems().clear();
        for (Felhasznalok employee : felhasznalok){
            tv1.getItems().add(employee);
        }
    }

    public void pressReg(ActionEvent e) {
        System.out.println("Registration...");
        Felhasznalok felhasznalok = new Felhasznalok(
                Rfelhasznalo.getText(),
                Rjelszo.getText(),
                Rteljes_nev.getText(),
                Remail.getText());
        db.insert(felhasznalok);
    }


    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void pressLog(ActionEvent e) throws IOException, NoSuchAlgorithmException {
        System.out.println("Belépés...");

        ArrayList<Felhasznalok> felhasznalok = db.read();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashed = md.digest(Bjelszo.getText().getBytes(StandardCharsets.UTF_8));
        String hashedJelszo = bytesToHex(hashed);
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] encodedHash = digest.digest(Bjelszo.getText().getBytes(StandardCharsets.UTF_8));
//        String hashedJelszo = new String(encodedHash);

        for (Felhasznalok employee : felhasznalok) {

            if (Bfelhasznalo.getText().equals(employee.getFh_nev()) && hashedJelszo.equals(employee.getJelszo())){
                isLoggedIn=true;
                String username= Bfelhasznalo.getText();
                FXMLLoader loader=new FXMLLoader((getClass().getResource("kezdolap.fxml")));
                root = loader.load();

                KezdolapController kezdolapController = loader.getController();
                kezdolapController.display(username);
                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                nev=employee.getFh_nev();
                System.out.println(isLoggedIn);


                System.out.println(nev+" Itt a neved");
                //label_username.setText(nev);

                break;
            }
         else
         {
             nope.textProperty().set("Belépési adatok nem megfelelőek");    //Blank Label
             System.out.println(employee.getFh_nev()+"-"+Bfelhasznalo.getText());
             System.out.println(hashedJelszo+"----------"+employee.getJelszo());
             }
          }
        }
}