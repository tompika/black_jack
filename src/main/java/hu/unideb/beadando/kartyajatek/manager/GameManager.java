/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.manager;

import hu.unideb.beadando.kartyajatek.model.Card;


/**
 *
 * @author szilvacsku
 */
public interface GameManager {
    
    public void loadCard();
    public Card getRandomCard();
    public Card getBackCard();
    
    public String getWinnerName();
    
}
