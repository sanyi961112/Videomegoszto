package sample;

import com.sun.org.apache.xpath.internal.compiler.FuncLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.rmi.runtime.NewThreadAction;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class KezdolapController extends Controller {
    @FXML
    Label label_username;
    @FXML
    Label label_username1;
    @FXML
    private TextArea cimke;
    @FXML
    private TextField asd;

    @FXML
    private TextField katfield;
    @FXML
    private ListView<Blob> myListView;
    @FXML
    private ComboBox vidComboBox;
    @FXML
    private ComboBox vidComboBoxDel;
    @FXML
    private TextField mycim;
    @FXML
    private TextArea myleiras;

    long time =System.currentTimeMillis();
    java.sql.Date d =new java.sql.Date(time);
    @FXML
    public void initialize() {


        db = new DB();
        TableColumn fh_nevCollum = new TableColumn("fh_nev");
        fh_nevCollum.setCellFactory(new PropertyValueFactory<>("fh_nev"));
        TableColumn jelszoCollum = new TableColumn("jelszo");
        jelszoCollum.setCellFactory(new PropertyValueFactory<>("jelszo"));
        TableColumn teljes_nevCollum = new TableColumn("teljes_nev");
        teljes_nevCollum.setCellFactory(new PropertyValueFactory<>("teljes_nev"));
        TableColumn emailCollum = new TableColumn("email");
        emailCollum.setCellFactory(new PropertyValueFactory<>("email"));
        ArrayList<Kategoriak> kategoriak = db.comboread();
        for (Kategoriak feltolt : kategoriak) {
            vidComboBox.getItems().add(feltolt.getKategoria_nev());
            vidComboBoxDel.getItems().add(feltolt.getKategoria_nev());
        }
        //tv1.getColumns().addAll(fh_nevCollum, jelszoCollum, teljes_nevCollum, emailCollum);
//     System.out.println("asd");
    }

    public void display(String username) {

        label_username.setText(username);
        label_username1.setText(username);
    }

    public void pressChoose(ActionEvent e) {
        System.out.println("Fájl Kiválasztása...");
        FileChooser fileChooser = new FileChooser();
        File file;
        Scanner fileIn;
        int response;
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        response = chooser.showOpenDialog(null);

        String a = "asd";
        if (response == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();

            try {
                fileIn = new Scanner(file);
                if (file.isFile()) {
                    while (fileIn.hasNextLine()) {
                        String line = fileIn.nextLine();
                        System.out.println(line);
                    }
                    System.out.println(file.getAbsolutePath());
                    a = file.getAbsolutePath();
                    System.out.println(a);
                    asd.setText(a);


                } else {
                    System.out.println("Ez nem egy file");
                }
                fileIn.close();
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            }

        }
        asd.setText(a);
    }

    public void pressUpload(ActionEvent e) {
        System.out.println("Fájl Feltöltése...");

    }

    public void pressLogout(ActionEvent e) throws IOException {
        System.out.println("Kilépés...");
        isLoggedIn = false;
        System.out.println(isLoggedIn);
        Parent asd = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene asdscene = new Scene(asd);
        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        app_stage.setScene(asdscene);
        app_stage.show();

    }

    public void pressDel(ActionEvent e) throws IOException {
        System.out.println("Deleting...");
        ArrayList<Felhasznalok> felhasznalok = db.read();
        for (Felhasznalok employee : felhasznalok) {
            System.out.println(label_username.getText() + " cuccizé " + employee.getFh_nev());
            if (employee.getFh_nev().equals(label_username.getText())) {
                db.delete(employee);
                break;
            }
        }

        isLoggedIn = false;
        System.out.println(isLoggedIn);
        Parent asd = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene asdscene = new Scene(asd);
        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        app_stage.setScene(asdscene);
        app_stage.show();

    }

    public void presskathozaad(ActionEvent e) {

        Kategoriak kategoriak = new Kategoriak(katfield.getText());
        db.insertkat(kategoriak);
        ArrayList<Kategoriak> kategoriaks = db.comboread();
        vidComboBoxDel.getItems().clear();
        vidComboBox.getItems().clear();
        for (Kategoriak feltolt : kategoriaks) {
            vidComboBox.getItems().add(feltolt.getKategoria_nev());
            vidComboBoxDel.getItems().add(feltolt.getKategoria_nev());
        }
    }

    public void kategDel(ActionEvent e) {

        ArrayList<Kategoriak> kategoriaks = db.comboread();
        for (Kategoriak feltolt : kategoriaks) {
            if (feltolt.getKategoria_nev().equals(vidComboBoxDel.getValue()))
            {
                db.katdelete(feltolt);
            }
        }
        vidComboBoxDel.getItems().clear();
        vidComboBox.getItems().clear();
        ArrayList<Kategoriak> kategoriak = db.comboread();
        for (Kategoriak feltolt : kategoriak) {
            vidComboBox.getItems().add(feltolt.getKategoria_nev());
            vidComboBoxDel.getItems().add(feltolt.getKategoria_nev());
        }
    }
public void pressVideoUpload(ActionEvent e){
    System.out.println("Video Feltöltése");
    System.out.println(mycim.getText());
    ArrayList<Video> videos = db.videoread();
    Video video = new Video(null,
            mycim.getText(),
            myleiras.getText(),
            null,
            label_username.getText(),
            asd.getText(),
            vidComboBox.getValue().toString());
    db.insertvideo(video);
    System.out.println(mycim.getText()+" "+myleiras.getText());
    Video video2=null;
     for (Video video1: videos)
     {
         video2=video1;
     }
     int kiíras = 0;
     if (video2.getVideo_id().equals(null))
     {
         kiíras=1;
     }
     else
     {
         kiíras=video2.getVideo_id()+1;
     }
        Cimkek cimkek = new Cimkek(
                kiíras,
                cimke.getText()
        );
        db.insertcimke(cimkek);

}
}
