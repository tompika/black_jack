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

 /*
    private void checkWinner(List<Card> cardsPlayer, List<Card> cardsPc) {

        
        String nyertes = "";
        
        int player = cardsPlayer.stream()
                .mapToInt(e -> e.getValue())
                .sum();

        int pc = cardsPc.stream()
                .mapToInt(e -> e.getValue())
                .sum();

        int playerAce = player;
        int pcAce = pc;

        int ace = (int) cardsPlayer.stream()
                .filter(e -> e.isAce())
                .count();

        int acePc = (int) cardsPc.stream()
                .filter(e -> e.isAce())
                .count();

        if (ace > 0) {
            playerAce += 11;
        }
        if (acePc > 0) {
            pcAce += 11;
        }

        if (pc == player) {
            logger.info("Dontetlen!");
            cont.calculateEgyenleg(0);
            nyertes = "Dontetlen!";
        }
        
         else if (pc <= 21 && player <= 21) {
            if ( (21 - player < 21 - pc ) || (21-playerAce < 21 - pcAce) ) {
                logger.info("Nyertes jatekos: " + getPlayerName());
                calculateEgyenleg(1);
                nyertes = getPlayerName();
            }
            else if ( (21 - player > 21 - pc ) || (21 - playerAce > 21 - pcAce) ){
                logger.info("Nyertes jatekos: " + "Oszto");
                calculateEgyenleg(-1);
                nyertes = "Oszto";
            }
        }
       
        
        else if (pc > 21 && player <= 21) {
            logger.info("Nyertes jatekos: " + getPlayerName());
            calculateEgyenleg(1);
            nyertes = getPlayerName();
         
        }

        else if (player > 21 && pc <= 21) {
            logger.info("Nyertes jatekos: OSZTO");
            calculateEgyenleg(-1);
            nyertes = "Oszto";
   

        }
        
        
                
        if (nyertes.equals("Dontetlen"))
        {
            labelInfo.setText("Senki sem nyert!");
            
            labelEgyenleg.setText(String.valueOf(cont.getEgyenleg()));
            
            tart.setDisable(true);
            lap.setDisable(true);
            buttonTet.setDisable(true);
            buttonUjKor.setDisable(false);
            
            cont.roundToFile(cardsPlayer, cardsPc);
        }
         else if (!nyertes.isEmpty()) {
            labelInfo.setText("Nyertes: " + nyertes);
            
            labelEgyenleg.setText(String.valueOf(cont.getEgyenleg()));
                       
            tart.setDisable(true);
            lap.setDisable(true);
            buttonTet.setDisable(true);
            buttonUjKor.setDisable(false);
            
            cont.roundToFile(cardsPlayer, cardsPc);

        }

*/

}
