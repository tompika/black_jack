package hu.unideb.beadando.kartyajatek.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author szilvacsku
 */
public class GameModel {

    private static final Logger logger = LogManager.getLogger(GameModel.class);
    
    private Player player;
    private Oszto oszto;
    
    private static List<Card> allCard = new ArrayList<Card>();

    private int tet;

    
    public GameModel(){
        this.oszto = new Oszto();
    }
    
    public void setPlayer(Player player){
        this.player = player;
    }
    
    public void setTet(int tet) {
        this.tet = tet;
    }

    public int getTet() {
        return tet;
    }

    public void setAllCards(List<Card> list){
        allCard = list;
    }

    public Player getPlayer(){
        return player;
    }
    
    public Oszto getOszto(){
        return oszto;
    }
            
    public List<Card> getAllCard() {
        return allCard;

    }


}
