package hu.unideb.beadando.kartyajatek.controller;


import hu.unideb.beadando.kartyajatek.manager.GameManagerImpl;
import hu.unideb.beadando.kartyajatek.model.GameModel;
import hu.unideb.beadando.kartyajatek.model.Player;
import hu.unideb.beadando.kartyajatek.model.PlayerDAOImpl;
import javafx.scene.control.Alert;
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

    public GameManagerImpl getManager() {
        return gameManager;
    }

    public String getPlayerDataFileName() {
        return playerDataFile;
    }

    public String getRoundsDataFileName() {
        return roundsFileName;
    }

    public void setPlayer(Player player) {
        GameModel gm = gameManager.getGameModel();
        gm.setPlayer(player);
    }

    public boolean verifyPlayer(Player player) {

        PlayerDAOImpl playerImp = new PlayerDAOImpl();
        Player temp = playerImp.getRegisteredPlayer(player.getNickname(), player.getPassword());

        if (temp != null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean RegisterPlayer(String name, String password) {

                
        PlayerDAOImpl playerImp = new PlayerDAOImpl();
        boolean searchSuccess = playerImp.searchPlayer(name);

        if (!searchSuccess) {
            playerImp.registerPlayer(new Player(name, password));

            Alert alert = alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Információ");
            alert.setHeaderText(null);
            alert.setContentText("Sikeres regisztráció!");
            alert.showAndWait();

            return true;

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Hiba");
            alert.setHeaderText("Hibás felhasználói név");
            alert.setContentText("Ezt a felhasználói név már használatban van!");
            alert.showAndWait();

            return false;

        }

    }

}
