package hu.unideb.beadando.kartyajatek.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * 
 * @author Szilvácsku Péter
 *
 */
public class PlayerRecord {

	
    private final StringProperty nickname;
    private final StringProperty password;


    public PlayerRecord(String nickname, String password) {

            this.nickname = new SimpleStringProperty(nickname);
            this.password = new SimpleStringProperty(password);

    }


    public StringProperty nicknameProperty(){
            return nickname;
    }

    public StringProperty passwordProperty(){
            return password;
    }




	
}
