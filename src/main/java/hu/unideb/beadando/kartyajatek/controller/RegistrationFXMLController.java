package hu.unideb.beadando.kartyajatek.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;

import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author szilvacsku
 */
public class RegistrationFXMLController implements Initializable {

    Controller controller = Controller.getInstance();
    private static Logger logger = Logger.getLogger(StartFXMLController.class.getName());
    
    @FXML
    private TextField tfNickName;

    @FXML
    private PasswordField pwfPassword;

    @FXML
    private PasswordField pwfPassword2;

    @FXML
    private Button btnRegistration;

    
    
 
   
    @FXML
    void registrationExecute(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Hiba");
        alert.setHeaderText(null);

        if (tfNickName.getText().isEmpty()) {
            
            tfNickName.setId("empty");    
            
            Platform.runLater(() -> tfNickName.requestFocus());
            
            alert.setContentText("A felhasználói név megadása kötelező!");
            alert.show();
            return;

        } else if (pwfPassword.getText().isEmpty() || pwfPassword2.getText().isEmpty()) {

            if( pwfPassword.getText().isEmpty()){
                pwfPassword.setId("empty");
            }
            
            if( pwfPassword2.getText().isEmpty()){
                pwfPassword2.setId("empty");
            }
            
            Platform.runLater(() -> pwfPassword.requestFocus());
            alert.setContentText("A jelszó megadása kötelező!");
            alert.show();
            return;

        }
        
        if( !pwfPassword.getText().equals(pwfPassword2.getText())){
            Platform.runLater(() -> pwfPassword.requestFocus());
            alert.setContentText("A két jelszó nem egyezik meg!");
            alert.show();
            return;
        }
        
        if(controller.RegisterPlayer(tfNickName.getText(), pwfPassword.getText()) ){
        
            tfNickName.setText("");
            pwfPassword.setText("");
            pwfPassword2.setText("");
        }           
        
        

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
