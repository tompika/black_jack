/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.model;


import javafx.collections.ObservableList;

/**
 *
 * @author szilvacsku
 */
public interface RoundDAO {
    
    public void addRound(Round round);
    public ObservableList<Round> getAllRound();
    
}
