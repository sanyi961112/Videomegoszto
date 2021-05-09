package sample;

import com.sun.org.apache.xpath.internal.compiler.FuncLoader;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class KezdolapController extends Controller {
    @FXML
    private TextField comment;
    @FXML
    Label label_username;
    @FXML
    private Label feltoltesdatuma;
    @FXML
    Label label_username1;

    @FXML
    private TabPane tabpane;
    @FXML
    private TextArea cimke;
    @FXML
    private Label valassz;
    @FXML
    private ListView myListView;
    @FXML
    private TextField asd;
    @FXML
    private TextField katfield;
    @FXML
    private Label videocimlabel;
    @FXML
    private ComboBox vidComboBox;
    @FXML
    private ComboBox vidComboBoxDel;
    @FXML
    private TextField mycim;
    @FXML
    private VBox vboxParent;
    @FXML
    private MediaView mvVideo;
    private MediaPlayer mpVideo;
    private Media mediaVideo;
    @FXML
    private HBox hBoxControls;
    @FXML
    private HBox hboxVolume;
    @FXML
    private Button buttonPPR;
    @FXML
    private Label labelCurrentTime;
    @FXML
    private Label labelTotalTime;
    @FXML
    private Label labelFullScreen;
    @FXML
    private Label labelSpeed;
    @FXML
    private Label labelVolume;
    @FXML
    private Slider sliderVolume;
    @FXML
    private Tab tab_vid;
    @FXML
    private ListView ALLListView;
    @FXML
    private Slider sliderTime;

    // Checks if the video is at the end.
    private boolean atEndOfVideo = false;
    // Video is not playing when GUI starts.
    private boolean isPlaying = true;
    // Checks if the video is muted or not.
    private boolean isMuted = true;

    // ImageViews for the buttons and labels.
    private ImageView ivPlay;
    private ImageView ivPause;
    private ImageView ivRestart;
    private ImageView ivVolume;
    private ImageView ivFullScreen;
    private ImageView ivMute;
    private ImageView ivExit;
    @FXML
    private TextArea myleiras;
    ListView<String> listView;
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

        ALLListView.getItems().clear();
        ArrayList<Video> videos = db.videoread();
        for (Video video: videos)
        {
            ALLListView.getItems().add(video.getCim());
        }

    }

    public String getTime(Duration time) {

        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();


        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;
        if (hours > 59) hours = hours % 60;

        if (hours > 0) return String.format("%d:%02d:%02d",
                hours,
                minutes,
                seconds);
        else return String.format("%02d:%02d",
                minutes,
                seconds);
    }

    public void labelsMatchEndVideo(String labelTime, String labelTotalTime) {
        for (int i = 0; i < labelTotalTime.length(); i++) {
            if (labelTime.charAt(i) != labelTotalTime.charAt(i)) {
                atEndOfVideo = false;
                if (isPlaying) buttonPPR.setGraphic(ivPause);
                else buttonPPR.setGraphic(ivPlay);
                break;
            } else {
                atEndOfVideo = true;
                buttonPPR.setGraphic(ivRestart);
            }
        }
    }
    public void bindCurrentTimeLabel() {
        labelCurrentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(mpVideo.getCurrentTime()) + " / ";
            }
        }, mpVideo.currentTimeProperty()));
    }
    public void display(String username) {

        label_username.setText(username);
        label_username1.setText(username);
    }

    public void displayVideo(){

    }

    public void pressComment(ActionEvent e){
        try{
            System.out.println("Insert comment...");
            Timestamp ts = Timestamp.from(Instant.now());


            Komment komment = new Komment(
                    mvVideo.getId(),
                    label_username.getText(),
                    ts,
                    comment.getText());
            db.insertKomment(komment);
        } catch (Error err) {
            System.out.println(err);
        }

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
//            System.out.println(label_username.getText() + " cuccizé " + employee.getFh_nev());
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
    myListView.getItems().clear();

    System.out.println(mycim.getText()+" "+myleiras.getText());
    Video video2=null;
    int max=0;
    ArrayList<Video> videos1 = db.videoread();
     for (Video video1: videos)
     {

         if (video1.getVideo_id()>max)
         {
             max=video1.getVideo_id();
         }
     }
        Cimkek cimkek = new Cimkek(
                max,
                cimke.getText()
        );
        db.insertcimke(cimkek);

    ArrayList<Video> videos4 = db.videoread();
    for (Video video3: videos4)
    {
        if (video.getFeltolto().equals(label_username.getText()))
        {
            myListView.getItems().add(video3.getCim());
        }

    }

    ArrayList<VIDEOMEGOSZTOK> videomegosztoks = db.readVideoMegosztok();
    int szamlalo=0;
    for (VIDEOMEGOSZTOK vid: videomegosztoks)
    {
        if (vid.getFh_nev().equals(label_username.getText()))
        {
            //db.videomegosztokupdate(vid);
            szamlalo++;
            break;
        }

    }
    if (szamlalo==0)
    {
        VIDEOMEGOSZTOK asd= new VIDEOMEGOSZTOK(label_username.getText(),1);
        db.insertVideoMegosztok(asd);
    }
szamlalo=0;
}

    public void pressVidDel(ActionEvent e) {

        String a =myListView.getSelectionModel().getSelectedItems().toString();
        int b = a.length();

        ArrayList<Video> videos = db.videoread();
        for (Video video : videos)
        {
            System.out.println(video.getCim()+" aaaaaa "+a.subSequence(1,b-1));
            if (video.getCim().equals(a.subSequence(1,b-1)))
            {
                db.viddelete(video);
                break;
            }

        }

        myListView.getItems().clear();
        ArrayList<Video> videos4 = db.videoread();
        for (Video video3: videos4)
        {

            if (video3.getFeltolto().equals(label_username.getText())) {
                myListView.getItems().add(video3.getCim());
            }
        }
    }
    public void pressSelectVideo(ActionEvent e) {
        String a =myListView.getSelectionModel().getSelectedItems().toString();
        String c =ALLListView.getSelectionModel().getSelectedItems().toString();
        int b = a.length();
        int d = c.length();

        String videoURL="";
        String cim="";
        ArrayList<Video> videos = db.videoread();
        for (Video video : videos)
        {
            if (video.getCim().equals(a.subSequence(1,b-1)) || video.getCim().equals(c.subSequence(1,d-1)))
            {
                videoURL=video.getForras_fajl();
                cim=video.getCim().toString();
                feltoltesdatuma.setText(video.getFeltoltes_datuma().toString());
                break;
            }
        }
        if (myListView.getSelectionModel().getSelectedItem()!=null || ALLListView.getSelectionModel().getSelectedItem()!=null)
        {
            tabpane.getSelectionModel().select(tab_vid);
            mediaVideo = new Media(new File(videoURL).toURI().toString());
            mpVideo = new MediaPlayer(mediaVideo);
            mvVideo.setMediaPlayer(mpVideo);

            Image imagePlay = new Image(new File("src/sample/media/play-btn.png").toURI().toString());
            ivPlay = new ImageView(imagePlay);
            ivPlay.setFitWidth(35);
            ivPlay.setFitHeight(35);

            Image imageStop = new Image(new File("src/sample/media/stop-btn.png").toURI().toString());
            ivPause = new ImageView(imageStop);
            ivPause.setFitHeight(35);
            ivPause.setFitWidth(35);

            Image imageRestart = new Image(new File("src/sample/media/restart-btn.png").toURI().toString());
            ivRestart = new ImageView(imageRestart);
            ivRestart.setFitWidth(35);
            ivRestart.setFitHeight(35);

            Image imageVol = new Image(new File("src/sample/media/volume.png").toURI().toString());
            ivVolume = new ImageView(imageVol);
            ivVolume.setFitWidth(35);
            ivVolume.setFitHeight(35);

            Image imageFull = new Image(new File("src/sample/media/fullscreen.png").toURI().toString());
            ivFullScreen = new ImageView(imageFull);
            ivFullScreen.setFitHeight(35);
            ivFullScreen.setFitWidth(35);

            Image imageMute = new Image(new File("src/sample/media/mute.png").toURI().toString());
            ivMute = new ImageView(imageMute);
            ivMute.setFitWidth(35);
            ivMute.setFitHeight(35);

            Image imageExit = new Image(new File("src/sample/media/exitscreen.png").toURI().toString());
            ivExit = new ImageView(imageExit);
            ivExit.setFitHeight(35);
            ivExit.setFitWidth(35);

            buttonPPR.setGraphic(ivPause);
            labelVolume.setGraphic(ivMute);
            labelSpeed.setText("1X");
            labelFullScreen.setGraphic(ivFullScreen);



            hboxVolume.getChildren().remove(sliderVolume);

            buttonPPR.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Button buttonPlay = (Button) actionEvent.getSource();
                    bindCurrentTimeLabel();

                    if (atEndOfVideo) {
                        sliderTime.setValue(0);
                        atEndOfVideo = false;
                        isPlaying = false;
                    }

                    if (isPlaying) {
                        buttonPlay.setGraphic(ivPlay);
                        mpVideo.pause();
                        isPlaying = false;
                    } else {
                        buttonPlay.setGraphic(ivPause);
                        mpVideo.play();
                        isPlaying = true;
                    }
                }
            });

            mpVideo.volumeProperty().bindBidirectional(sliderVolume.valueProperty());
            sliderVolume.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mpVideo.setVolume(sliderVolume.getValue());
                    if (mpVideo.getVolume() != 0.0) {
                        labelVolume.setGraphic(ivVolume);
                        isMuted = false;
                    } else {
                        labelVolume.setGraphic(ivMute);
                        isMuted = true;
                    }
                }
            });

            mpVideo.play();

            labelSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (labelSpeed.getText().equals("1X")) {
                        labelSpeed.setText("2X");
                        mpVideo.setRate(2.0);
                    } else {
                        labelSpeed.setText("1X");
                        mpVideo.setRate(1.0);
                    }
                }
            });

            labelVolume.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (isMuted) {
                        labelVolume.setGraphic(ivVolume);
                        sliderVolume.setValue(0.2);
                        isMuted = false;
                    }
                    else {
                        labelVolume.setGraphic(ivMute);
                        sliderVolume.setValue(0);
                        isMuted = true;
                    }
                }
            });


            labelVolume.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (hboxVolume.lookup("#sliderVolume") == null) {
                        hboxVolume.getChildren().add(sliderVolume);
                        sliderVolume.setValue(mpVideo.getVolume());
                    }
                }
            });

            hboxVolume.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    hboxVolume.getChildren().remove(sliderVolume);
                }
            });


            vboxParent.sceneProperty().addListener(new ChangeListener<Scene>() {
                @Override
                public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene newScene) {
                    if (scene == null && newScene != null) {
                        mvVideo.fitHeightProperty().bind(newScene.heightProperty().subtract(hBoxControls.heightProperty().add(20)));
                    }
                }
            });


            labelFullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Label label = (Label) mouseEvent.getSource();
                    Stage stage = (Stage) label.getScene().getWindow();


                    if (stage.isFullScreen()) {
                        stage.setFullScreen(false);
                        labelFullScreen.setGraphic(ivFullScreen);
                    } else {
                        stage.setFullScreen(true);
                        labelFullScreen.setGraphic(ivExit);
                        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent keyEvent) {
                                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                                    labelFullScreen.setGraphic(ivFullScreen);
                                }
                            }
                        });
                    }
                }
            });

            mpVideo.totalDurationProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                    sliderTime.setMax(newDuration.toSeconds());
                    labelTotalTime.setText(getTime(newDuration));

                }
            });


            sliderTime.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean isChanging) {
                    bindCurrentTimeLabel();
                    if (!isChanging) {
                        mpVideo.seek(Duration.seconds(sliderTime.getValue()));
                    }
                }
            });


            sliderTime.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                    bindCurrentTimeLabel();
                    // Get the current time of the video in seconds.
                    double currentTime = mpVideo.getCurrentTime().toSeconds();
                    if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                        mpVideo.seek(Duration.seconds(newValue.doubleValue()));
                    }
                    labelsMatchEndVideo(labelCurrentTime.getText(), labelTotalTime.getText());
                }
            });

            mpVideo.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                    bindCurrentTimeLabel();
                    if (!sliderTime.isValueChanging()) {
                        sliderTime.setValue(newTime.toSeconds());
                    }
                    labelsMatchEndVideo(labelCurrentTime.getText(), labelTotalTime.getText());
                }
            });

            mpVideo.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    buttonPPR.setGraphic(ivRestart);
                    atEndOfVideo = true;
                    if (!labelCurrentTime.textProperty().equals(labelTotalTime.textProperty())) {
                        labelCurrentTime.textProperty().unbind();
                        labelCurrentTime.setText(getTime(mpVideo.getTotalDuration()) + " / ");
                    }
                }
            });

        }
        else {
            valassz.setText("Válassz videót");
        }
        videocimlabel.setText(cim);
    }

    public void pressSelectVideoim(Event event) {
        myListView.getItems().clear();
            ArrayList<Video> videos = db.videoread();
            for (Video video: videos)
            {
                if (video.getFeltolto().equals(label_username.getText()))
                {
                    myListView.getItems().add(video.getCim());
                }
                System.out.println(video.getFeltolto()+"nyomorék"+label_username.getText());
            }
    }
}
