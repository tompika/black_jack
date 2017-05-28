package hu.unideb.beadando.kartyajatek.controller;

import hu.unideb.beadando.kartyajatek.controller.GameFXMLController;
import hu.unideb.beadando.kartyajatek.controller.StartFXMLController;
import hu.unideb.beadando.kartyajatek.manager.GameManagerImpl;
import hu.unideb.beadando.kartyajatek.model.Card;
import hu.unideb.beadando.kartyajatek.model.GameModel;
import hu.unideb.beadando.kartyajatek.model.Player;
import hu.unideb.beadando.kartyajatek.model.PlayerDAOImpl;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A projekthez tartozó controller osztály melynek feladata a különböző
 * események kezelése.
 *
 * @author Szilvácsku Péter
 *
 */
public class Controller {

    private static Controller instance = null;
    private static final Logger logger = LogManager.getLogger(Controller.class);

 
    private final String playerDataFile = "data.xml";
    private final String roundsFileName = "rounds.xml";
    
    private GameManagerImpl gameManager;
    
    public static Controller getInstance() {

        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    private Controller() {
    }

    public void init() {
        this.gameManager = new GameManagerImpl();
    }

    public GameManagerImpl getManager(){
        return gameManager;
    }
    
    
    public String getPlayerDataFileName(){
        return playerDataFile;
    }
    
    public String getRoundsDataFileName(){
        return roundsFileName;
    }
    
    public void setPlayer(Player player){
        GameModel gm = gameManager.getGameModel();
        gm.setPlayer(player);
    }
    
    
    public void viewGame() throws IOException {

        if ( true ) {
            logger.info("Sikeres belepes!");

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Sikertelen bejelentkezés");
            alert.setContentText("Hibás felhasználónév és/vagy jelszó!");
            alert.show();

            return;
        }

        List<String> choices = new ArrayList<>();
        choices.add("1");
        choices.add("2");
        choices.add("3");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("1", choices);
        dialog.setTitle("Paklik száma");
        dialog.setHeaderText(null);
        dialog.setContentText("Válassza ki, hogy hány paklival szeretne játszani:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            
        }

        Parent login_parent = FXMLLoader.load(getClass().getResource("/fxml/GameFxml.fxml"));
        Scene gameScene = new Scene(login_parent);
        gameScene.getStylesheets().add("styles/Styles.css");


        Stage stage = new Stage();
        stage.setTitle("BlackJack");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(gameScene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
    
    public boolean verifyPlayer(Player player){
        
        PlayerDAOImpl playerImp = new PlayerDAOImpl();
        Player temp = playerImp.getRegisteredPlayer(player.getNickname(), player.getPassword());
        
        
        if( temp != null ){
            return true;
        }
        else{
            return false;
        }
        
    }
    
    
    public boolean RegisterPlayer(String name, String password) {
    
        PlayerDAOImpl playerImp = new PlayerDAOImpl();
        boolean searchSuccess = playerImp.searchPlayer(name);
        
        if( !searchSuccess ){
            playerImp.registerPlayer(new Player(name, password));
            
            Alert alert = alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Információ");
            alert.setContentText("Sikeres regisztráció!");
            alert.showAndWait();
            
            return true;
            
        }
        else{
       
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hibás felhasználói név");
            alert.setContentText("Ezt a felhasználói név már használatban van!");
            alert.showAndWait();
            
            return false;
        
        }

    }

  
}
