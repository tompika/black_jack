package hu.unideb.beadando.kartyajatek.model;

import java.util.ArrayList;
import java.util.List;

public class Oszto {

	private List<Card> cards =  new ArrayList<>();
	private String name;

	
	public Oszto(){
		this.name = "Oszto";
           
	}

	
	public List<Card> getCards() {
		return cards;
	}
        
        public String getName(){
            return name;
        }
	
        public void setCards(List<Card> list){
            this.cards = list;
        }
	
}
