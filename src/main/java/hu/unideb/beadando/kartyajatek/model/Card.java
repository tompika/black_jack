package hu.unideb.beadando.kartyajatek.model;

import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A kártyalapokat reprezentáló osztály, mely tartalmazza a lap tulajdonságait.
 *
 * @author Szilvácsku Péter
 *
 */
public class Card {

    private static final Logger logger = LogManager.getLogger(Card.class);

    private int value;
    private boolean ace = false;
    private Image img;
    private String color;
    private String type;

    public Card(int value, Image img, boolean ace, String color, String type) {
        this.value = value;
        this.img = img;
        this.ace = ace;
        this.color = color;
        this.type = type;
    }

    public boolean isAce() {
        return ace;
    }

    public int getValue() {
        return value;
    }

    public Image getImg() {
        return img;
    }

    public String getColor(){
        return color;
    }
    
    public String getType(){
        return type;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value) + "" + type;
    }

}
