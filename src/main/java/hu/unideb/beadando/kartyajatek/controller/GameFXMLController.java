package hu.unideb.beadando.kartyajatek.controller;

import hu.unideb.beadando.kartyajatek.manager.GameManagerImpl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import hu.unideb.beadando.kartyajatek.model.Card;
import hu.unideb.beadando.kartyajatek.model.GameModel;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class GameFXMLController implements Initializable {

    private static final Logger logger = LogManager.getLogger(GameFXMLController.class);

    private Controller controller = Controller.getInstance();
    
    private GameManagerImpl gameManager = controller.getManager();
    private GameModel gameModel = gameManager.getGameModel();
    
    
    Card cardPlayer, cardPc;

    List<Card> cardsPlayer = new ArrayList<>();
    List<Card> cardsPc = new ArrayList<>();
    

    ImageView imView;
    ImageView imView2;

    public GameFXMLController() {
    }

    @FXML
    private BorderPane gameBorder;

    @FXML
    private HBox hboxPcCard;

    @FXML
    private HBox hboxPlayerCard;

    @FXML
    private Label labelPlayerPoint;

    @FXML
    private Label labelOsztoPoint;

    @FXML
    private Label labelInfo;


    @FXML
    private Button btnHit;
    
    @FXML
    private Button btnTet;

    @FXML
    private TextField textFieldTet;

    @FXML
    private Button btnAdmin;

    @FXML
    private Label labelTet;

    @FXML
    private Label labelName;

    @FXML
    private Button btnStand;

    @FXML
    private Button btnNewRound;

    @FXML
    private Label labelBalance;

    @FXML
    private MenuItem btnHistory;
    
    
    @FXML
    void btnAdminOnAction(ActionEvent event) throws IOException {
        
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/AdminFxml.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = new Stage();

        stage.setTitle("Felhasznalok");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnHistoryOnAction(ActionEvent event) throws IOException {
        
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/HistoryFxml.fxml"));
        Scene scene = new Scene(parent);

        Stage stage = new Stage();
        stage.setTitle("Eredmények");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }

    @FXML
    void btnLapOnAction(ActionEvent event) {
        
        moveLap(hboxPlayerCard, false, cardsPlayer);
        
       
        setPointPlayer(calculatePoint(cardsPlayer));

        btnStand.setDisable(false);
        checkToMuch();
    }

    @FXML
    void btnNewRoundOnAction(ActionEvent event) {

        hboxPcCard.getChildren().clear();
        hboxPlayerCard.getChildren().clear();

        cardsPc.clear();
        cardsPlayer.clear();

        setPointPlayer(0);
        setPointOszto(0);

        setLabelInfo("New round!");

        btnHit.setDisable(false);
        
        startState();

    }

    @FXML
    void btnTartOnAction(ActionEvent event) {

        hboxPcCard.getChildren().remove(0);
        cardsPc.remove(0);
              

        try {
           
            //amíg az oszto lapjaniak értéke nem éri el a 17-et, lapot húz
            while( cardsPc.stream().mapToInt(e -> e.getValue()).sum() < 17){
                Thread.sleep(300);
                moveLap(hboxPcCard, false, cardsPc);
                
            }
            
         
            setPointOszto(calculatePoint(cardsPc));
            

        } catch (InterruptedException e) {
            logger.warn(e.getStackTrace());
        }
        //checkWinner();
        
    }

    @FXML
    void btnTetOnAction(ActionEvent event) {
        
     
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);

        if (!textFieldTet.getText().isEmpty()) {
            if (Integer.parseInt(textFieldTet.getText()) < 0 
                  /*controller.getEgyenleg()  <= Integer.parseInt(textFieldTet.getText() */ ) {
                //labelInfo.setText("Töltsd fel az egyenleged!");
                alert.setContentText("NO CASH!");
                alert.show();

            } else {

                labelTet.setText(textFieldTet.getText());
                //controller.setTet(Integer.valueOf(labelTet.getText()));

                btnHit.setDisable(false);
                btnStand.setDisable(false);
                btnTet.setDisable(true);
                btnNewRound.setDisable(true);
                
                moveLap(hboxPcCard, true, cardsPc);
                moveLap(hboxPcCard, false, cardsPc);
                
                moveLap(hboxPlayerCard, false, cardsPlayer);
                moveLap(hboxPlayerCard, false, cardsPlayer);
                
                
                setPointOszto(calculatePoint(cardsPc));
                setPointPlayer(calculatePoint(cardsPlayer));
                
                
            }
        } else {

            alert.setContentText("Kérem ellenőrizze a beírt adatot!\nA mező nem lehet üres!");
            alert.show();
        }
        
        
         
        
        
    }
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        gameManager.loadCard(1);

        //cardsPc = gameModel.getOszto().getCards();
        //cardsPlayer = gameModel.getPlayer().getCards();
        
        
        
        //   try {
        btnAdmin.setDisable(true);
        btnAdmin.setVisible(false);

        logger.info(gameModel);
        
        logger.info(gameModel.getPlayer().toString());
        
        if (gameModel.getPlayer().getNickname().equals("admin")) {
            btnAdmin.setDisable(false);
            btnAdmin.setVisible(true);
        }
        
                
        setPointPlayer(0);

        hboxPlayerCard.setAlignment(Pos.CENTER);
        hboxPcCard.setAlignment(Pos.CENTER);

        btnStand.setDisable(true);
        btnHit.setDisable(true);
        btnNewRound.setDisable(true);

        setLabelPlayer(gameModel.getPlayer().getNickname());
        
        setLabelBalance(10000);

        //    controller.setEgyenleg(10000);
        BackgroundImage bgimage = new BackgroundImage(new Image("tableBackground.png", 0, 0, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        gameBorder.setBackground(new Background(bgimage));

        labelInfo.setText("Sok szerencsét!");

        //  } catch (IOException ex) {
        //      logger.error(ex.getStackTrace());
        //  }
    }



    private void moveLap(HBox hbox, boolean isBack, List<Card> list) {

        Card card;
        if (isBack) {
            card = gameManager.getBackCard();
        } else {
            card = gameManager.getRandomCard();
        }

        list.add(card);

        Image img = card.getImg();

        imView = new ImageView();
        imView.setImage(img);
        imView.setFitWidth(80);
        imView.setFitHeight(150);

        hbox.getChildren().add(imView);

    }

    private void checkToMuch() {
        
        int player = cardsPlayer.stream()
                .mapToInt(e -> e.getValue())
                .sum();

        int pc = cardsPc.stream()
                .mapToInt(e -> e.getValue())
                .sum();
       
        if(player > 21){
            
            labelInfo.setText("Vesztettél!\nLapjaid összege nagyobb mint 21!");
            //controller.roundToFile(cardsPlayer, cardsPc);
            
            //controller.calculateEgyenleg(-1);
            //labelEgyenleg.setText(String.valueOf(controller.getEgyenleg()));
            
            //setLabelBalance(pc);
            
            
            
            btnStand.setDisable(true);
            btnHit.setDisable(true);
            btnNewRound.setDisable(false);
            
        }
         
    }

  
    private void startState() {

        /*  hboxPcCard.getChildren().clear();
        hboxPlayerCard.getChildren().clear();

        cardsPc.clear();
        cardsPlayer.clear();

        setPlayerPoint(0);
        setOsztoPoint(0);
        
        
        setInfoLabel("Uj kor kezdodott!");*/
        

        btnStand.setDisable(false);
        btnHit.setDisable(false);

        moveLap(hboxPcCard, true, cardsPc);
        moveLap(hboxPcCard, false, cardsPc);

        
        setPointOszto(calculatePoint(cardsPc));

        moveLap(hboxPlayerCard, false, cardsPlayer);
        moveLap(hboxPlayerCard, false, cardsPlayer);

        setPointPlayer(calculatePoint(cardsPlayer));

    }

    private String calculatePoint(List<Card> list) {

        int num = 0;
        boolean ace = false;

        for (Card card : list) {
            num += card.getValue();
            if (card.isAce()) {
                ace = true;
            }
        }

        return ace ? Integer.toString(num) + " / " + Integer.toString(num + 11) : Integer.toString(num);
    }

    /**
     * Beallitja az informacio label szoveget.
     * @param value - szoveg
     */
    private void setLabelInfo(String value) {
        labelInfo.setText(value);
    }

    /**
     * Beallitja a jatekos nevet.
     * @param value - jatekos neve
     */
    private void setLabelPlayer(String value) {
        labelName.setText(value);
    }

    /**
     * Beallitja az oszto pontszamat
     * @param value pontszam
     */
    private void setPointOszto(int value) {
        labelOsztoPoint.setText(String.valueOf(value));
    }

    private void setPointOszto(String value){
        labelOsztoPoint.setText(value);
    }
    
    /**
     * Beallitja a jatekos pontszamat.
     * @param value  pontszam
     */
    private void setPointPlayer(int value) {
        labelPlayerPoint.setText(String.valueOf(value));
    }

    private void setPointPlayer(String value){
        labelPlayerPoint.setText(value);
    }
    
    
    
    /**
     * Beallitja a jatekos egyenleget.
     * @param value egyenleg
     */
    private void setLabelBalance(int value) {
        labelBalance.setText(String.valueOf(value));
    }

}
