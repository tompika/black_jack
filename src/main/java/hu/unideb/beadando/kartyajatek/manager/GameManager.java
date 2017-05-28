/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.manager;

import hu.unideb.beadando.kartyajatek.model.Card;
import hu.unideb.beadando.kartyajatek.model.Oszto;
import hu.unideb.beadando.kartyajatek.model.Player;

/**
 *
 * @author szilvacsku
 */
public interface GameManager {
    
    public void loadCard(int packOfCardsCount);
    public Card getRandomCard();
    public Card getBackCard();
    
    public String getWinnerName(Player player, Oszto oszto);
    public void calculatePoint();
    
}
