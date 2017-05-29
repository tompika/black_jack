/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.model;


import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author szilvacsku
 */
public interface RoundDAO {
    
    public void addRound(String nickName, List<Card> playerCards, List<Card> osztoCards);
    public ObservableList<Round> getAllRound();
    
}
