package hu.unideb.beadando.kartyajatek.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private String nickname;
    private String password;

    private int balance;
    private List<Card> cards;
    private int numCardsInHand;

    public Player() {
        this.numCardsInHand = 0;
    }

    public Player(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public Player(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.cards = new ArrayList<>();
        this.numCardsInHand = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Card> getCards() {
        return cards;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Player.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Player other = (Player) obj;
        if ((this.nickname == null) ? (other.nickname != null) : !this.nickname.equals(other.nickname)) {
            return false;
        }
        if (this.password != other.password) {
            return false;
        }
        return true;
    }
@Override
    public String toString() {
        return "Player{" + "name=" + name + ", nickname=" + nickname + ", password=" + password + ", balance=" + balance + ", cards=" + cards + ", numCardsInHand=" + numCardsInHand + '}';
    }
    /*
	public int getCardsPoint(){
		return cards.stream().mapToInt(e ->e.getValue()).sum();
	}*/
}
