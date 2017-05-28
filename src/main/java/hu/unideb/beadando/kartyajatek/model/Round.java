package hu.unideb.beadando.kartyajatek.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A lejátszott játszámák megjelenítéséhez szükséges osztály, a TableView
 * feltöltéséhez szükséges.
 *
 *
 * @author Szilvácsku Péter
 *
 */
public class Round {

    private final StringProperty name;
    private final StringProperty player;
    private final StringProperty oszto;
    private final StringProperty datum;

    /**
     * Az eredmények menüpont alatt található táblázat köreinek
     * reprezentációjához.
     *
     * @param name játékos neve
     * @param player játékos lapjai
     * @param oszto osztó lapjai
     * @param datum dátum
     */
    public Round(String name, String player, String oszto, String datum) {

        this.name = new SimpleStringProperty(name);
        this.player = new SimpleStringProperty(player);
        this.oszto = new SimpleStringProperty(oszto);
        this.datum = new SimpleStringProperty(datum);

    }
      

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty playerProperty() {
        return player;
    }

    public StringProperty osztoProperty() {
        return oszto;
    }

    public StringProperty datumProperty() {
        return datum;
    }

}
