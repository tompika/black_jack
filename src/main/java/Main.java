
import hu.unideb.beadando.kartyajatek.controller.Controller;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Main osztály - A projekt belépési pontja, létrehozza a játékhoz tartozó
 * modell és fxml objektumokat.
 *
 * @author Szilvácsku Péter
 *
 */
public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private Stage primaryStage;

    @SuppressWarnings("unused")
    private BorderPane rootPane;
    private GridPane rootGrid;

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Controller cont = Controller.getInstance();
       
        logger.info("Controller loaded");
        cont.init();
        launch(args);

        logger.info("A program leallt.");

    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("BlackJack v2.0");
        createBorderPane();       
        
    }

    private void createBorderPane() {

       try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/StartFxml.fxml"));
            rootGrid = (GridPane) loader.load();
            
            Scene scene = new Scene(rootGrid);
          //  scene.getStylesheets().add("../styles/Styles.css");


            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            logger.error(e);

        }
    }

    @Override
    public void stop() {
        logger.info("A gui leallt");
    }

}
