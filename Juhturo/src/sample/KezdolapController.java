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

public class KezdolapController extends Controller{
    @FXML
    Label label_username;
    @FXML
    Label label_username1;

    @FXML
    public void initialize(){

        db = new DB();
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
    public void display(String username){
        label_username.setText(username);
        label_username1.setText(username);
    }

    public void pressLogout(ActionEvent e) throws IOException {
        System.out.println("Kilépés...");
        isLoggedIn=false;
        System.out.println(isLoggedIn);
        Parent asd = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene asdscene = new Scene(asd);
        Stage app_stage =(Stage) ((Node) e.getSource()).getScene().getWindow();
        app_stage.setScene(asdscene);
        app_stage.show();

    }
    public void pressDel(ActionEvent e) throws IOException {
        System.out.println("Deleting...");
        ArrayList<Felhasznalok> felhasznalok = db.read();
        for (Felhasznalok employee : felhasznalok) {
            System.out.println(label_username.getText()+" cuccizé "+ employee.getFh_nev());
            if(employee.getFh_nev().equals(label_username.getText()))
            {
                db.delete(employee);
                break;
            }
        }
        isLoggedIn=false;
        System.out.println(isLoggedIn);
        Parent asd = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene asdscene = new Scene(asd);
        Stage app_stage =(Stage) ((Node) e.getSource()).getScene().getWindow();
        app_stage.setScene(asdscene);
        app_stage.show();

    }

}
