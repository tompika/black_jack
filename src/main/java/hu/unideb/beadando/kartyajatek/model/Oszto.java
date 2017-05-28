package hu.unideb.beadando.kartyajatek.model;

import java.util.ArrayList;
import java.util.List;

public class Oszto {

	private final List<Card> cards;
	private final String name;

	
	public Oszto(){
		this.cards = new ArrayList<>();
		this.name = "Oszto";
	}

	
	public List<Card> getCards() {
		return cards;
	}
        
        public String getName(){
            return name;
        }
	
	
}
