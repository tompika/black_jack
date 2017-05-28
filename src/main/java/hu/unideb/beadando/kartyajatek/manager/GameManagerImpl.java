/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.manager;

import hu.unideb.beadando.kartyajatek.model.Card;
import hu.unideb.beadando.kartyajatek.model.GameModel;
import hu.unideb.beadando.kartyajatek.model.Oszto;
import hu.unideb.beadando.kartyajatek.model.Player;
import hu.unideb.beadando.kartyajatek.model.RoundDAOImpl;
import hu.unideb.beadando.kartyajatek.model.Round;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.scene.image.Image;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A játék irányitásáért felelős osztály.
 *
 * @author Szilvácsku Péter
 */
public class GameManagerImpl implements GameManager {

    private static final Logger logger = LogManager.getLogger(GameManagerImpl.class);

    private GameModel gameModel;

    public GameManagerImpl() {
        this.gameModel = new GameModel();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void loadCard(int packOfCardsCount) {

        List<Card> packCards = new ArrayList<>();

        List<Card> tempCardList = new ArrayList<>();

        File folder = new File(Card.class.getClassLoader().getResource("cards/").getFile());
        List<String> fileAll = new ArrayList<>();

        logger.info("Folder isDir: " + folder.isDirectory());
        logger.info("Folder exists: " + folder.exists());

        if (!folder.exists()) {

            CodeSource src = Card.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                ZipInputStream zip = null;
                try {
                    URL jar = src.getLocation();
                    zip = new ZipInputStream(jar.openStream());
                    while (true) {
                        ZipEntry e;
                        try {
                            e = zip.getNextEntry();

                            if (e == null) {
                                break;
                            }
                            String name = e.getName();

                            if (name.startsWith("cards/") && name.endsWith(".png")) {
                                name = name.replace("cards/", "");
                                fileAll.add(name);

                            }
                        } catch (IOException ex) {
                            logger.warn(ex.getStackTrace());
                        }

                    }
                } catch (IOException ex) {
                    logger.warn(ex.getStackTrace());
                } finally {
                    try {
                        zip.close();
                    } catch (IOException ex) {
                        logger.warn(ex.getStackTrace());
                    }
                }
            } else {

                logger.info("BDSRSTN!");
            }

        } else {

            for (File file : folder.listFiles()) {
                fileAll.add(file.getName());
            }

        }

        int val = 0;
        Image im;
        boolean ace;
        String color = "";
        String type = "";

        for (String filename : fileAll) {
            InputStream is;

            is = Card.class.getClassLoader().getResourceAsStream("cards/" + filename);

            ace = false;

            im = new Image(is);

            String[] str = filename.split("_");

            //System.out.println(str[0] + " " + str[1] + " "+ str.length);
            if (str[0].matches("\\d+")) {
                switch (Integer.valueOf(str[0])) {
                    case 2:
                        val = 2;
                        break;
                    case 3:
                        val = 3;
                        break;
                    case 4:
                        val = 4;
                        break;
                    case 5:
                        val = 5;
                        break;
                    case 6:
                        val = 6;
                        break;
                    case 7:
                        val = 7;
                        break;
                    case 8:
                        val = 8;
                        break;
                    case 9:
                        val = 9;
                        break;
                    case 10:
                        val = 10;
                        break;

                    default:
                        break;
                }

            } else {
                switch (str[0]) {
                    case "ace":
                        val = 1;
                        ace = true;
                        type = "A";
                        break;
                    case "jack":
                        val = 10;
                        type = "J";
                        break;
                    case "king":
                        val = 10;
                        type = "K";
                        break;
                    case "queen":
                        val = 10;
                        type = "Q";
                        break;

                    default:
                        break;
                }
            }

            if (str.length == 3) {
                switch (str[2].replace(".png", "").replace("2", "")) {
                    case "clubs":
                        color = "black";
                        //type = "clubs";
                        break;
                    case "diamonds":
                        color = "red";
                        //type = "diamonds";
                        break;
                    case "hearts":
                        color = "red";
                        //type = "hearts";
                        break;
                    case "spades":
                        color = "black";
                        //type = "spades";
                        break;

                    default:
                        break;

                }
            }

            Card c = new Card(val, im, ace, color, type);
            if (!filename.startsWith("0_")) {
                tempCardList.add(c);
            }

            type = "";
        }

        packCards = tempCardList;

        if (packOfCardsCount > 0) {
            List<Card> temp = packCards;

            for (int i = 0; i < packOfCardsCount - 1; i++) {
                tempCardList.addAll(temp);
            }

            Collections.shuffle(tempCardList);
        }

        Collections.shuffle(tempCardList);
        logger.info("Cards loaded!");
        logger.info("Cards shuffle: OK");
        logger.info(tempCardList.size() + " db kartya beolvasva.");

        gameModel.setAllCards(tempCardList);

    }

    @Override
    public Card getRandomCard() {
        int cardsSize = gameModel.getAllCard().size();

        int index = new Random().nextInt(cardsSize);
        if (cardsSize > 0) {
            logger.info("Lapok szama a pakliban: " + Integer.toString(cardsSize - 1));

            return gameModel.getAllCard().remove(index);

        } else {
            logger.warn("Nincs beolvasott kartyalap!");
            return getBackCard();
        }
    }

    @Override
    public Card getBackCard() {

        Card back = new Card(0, new Image(Card.class.getClassLoader().getResourceAsStream("cards/0_back.png")), false, "", "");

        return back;

    }

    @Override
    public void calculatePoint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getWinnerName(Player _player, Oszto _oszto) {

        List<Card> playerCards = _player.getCards();
        List<Card> osztoCards = _oszto.getCards();

        String playerNickName = _player.getNickname();
        String osztoName = _oszto.getName();

        String nyertes = "";

        int player = playerCards.stream()
                .mapToInt(e -> e.getValue())
                .sum();

        int oszto = osztoCards.stream()
                .mapToInt(e -> e.getValue())
                .sum();

        int playerAce = player;
        int osztoAce = oszto;

        int ace = (int) playerCards.stream()
                .filter(e -> e.isAce())
                .count();

        int aceOszto = (int) osztoCards.stream()
                .filter(e -> e.isAce())
                .count();

        if (ace > 0) {
            playerAce += 11;
        }
        if (aceOszto > 0) {
            osztoAce += 11;
        }

        if (oszto == player) {
            logger.info("Dontetlen!");
            //cont.calculateEgyenleg(0);
            nyertes = "Dontetlen!";
        } else if (oszto <= 21 && player <= 21) {
            if ((21 - player < 21 - oszto) || (21 - playerAce < 21 - osztoAce)) {
                logger.info("Nyertes jatekos: " + playerNickName);
                //calculateEgyenleg(1);
                nyertes = playerNickName;
            } else if ((21 - player > 21 - oszto) || (21 - playerAce > 21 - osztoAce)) {
                logger.info("Nyertes jatekos: " + "Oszto");
                //calculateEgyenleg(-1);
                nyertes = "Oszto";
            }
        } else if (oszto > 21 && player <= 21) {
            logger.info("Nyertes jatekos: " + playerNickName);
            //calculateEgyenleg(1);
            nyertes = playerNickName;

        } else if (player > 21 && oszto <= 21) {
            logger.info("Nyertes jatekos: " + osztoName);
            //calculateEgyenleg(-1);
            nyertes = osztoName;

        }

        return nyertes;

    }

    public String calculatePoint(List<Card> list) {

        int num = 0;
        boolean ace = false;

        for (Card card : list) {
            num += card.getValue();
            if (card.isAce()) {
                ace = true;
            }
        }

        return ace ? Integer.toString(num) + " / " + Integer.toString(num + 11) : Integer.toString(num);

    }

    public void roundToFile(Round _round) {

        RoundDAOImpl roundimpl = new RoundDAOImpl();

        roundimpl.addRound(_round);

        logger.info("Az adott kor kiirasra kerul!");

    }

    private String cardsToString(List<Card> list) {
        String res = "[";
        res = list.stream().map((card) -> card + " ").reduce(res, String::concat);
        res += "]";
        return res;
    }

}
