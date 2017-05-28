package hu.unideb.beadando.kartyajatek.model;

import hu.unideb.beadando.kartyajatek.controller.Controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.transform.OutputKeys;

/**
 * A fájlkezeléshez tartozó műveleteket megvalósító osztály.
 *
 */
public class Data {

    private static final Logger logger = LogManager.getLogger(Data.class);

    private final String LOGIN_DATA_XML = "data.xml";
    private final String ROUNDS_DATA_XML = "rounds.xml";

    private String fileName;
    private final DateFormat dateFormatInTable;
    private final Calendar cal;
    private final String dateInTable;

    Controller cont = Controller.getInstance();

    private ObservableList<Round> roundData = FXCollections.observableArrayList();
    private ObservableList<PlayerRecord> recordData = FXCollections.observableArrayList();

    public Data() {

        this.cal = Calendar.getInstance();
        this.dateFormatInTable = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        this.dateInTable = dateFormatInTable.format(cal.getTime());

    }

    public ObservableList<Round> getRoundData() throws IOException, ParserConfigurationException, SAXException {
        load();
        return roundData;
    }
    
    public ObservableList<PlayerRecord> getPlayerRecordData() throws ParserConfigurationException, SAXException, IOException{
        loadRecordData();
        return recordData;
    }

    public void addPlayer(String nickName, String _password) {

        String folder_path = System.getProperty("user.home") + File.separator;
        folder_path += File.separator + ".blackjack";
        File customDir = new File(folder_path);

        if (customDir.exists()) {
            logger.info(customDir + " mappa letezik.");
        } else if (customDir.mkdirs()) {
            logger.info(customDir + " mappa letrehozva.");
        } else {
            logger.info(customDir + " mappa letrehozasa nem sikerult.");
        }

        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", LOGIN_DATA_XML);
        File saveFile = path.toFile();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        if (!_password.isEmpty() && !nickName.isEmpty() && !saveFile.exists()) {
            logger.info("A kimeneti fajl nem letezik. - Letrehozva!\n - " + saveFile.getPath());
            try {

                builder = factory.newDocumentBuilder();

                Document doc = builder.newDocument();

                Element rootElement = doc.createElement("players");
                doc.appendChild(rootElement);

                Element player = doc.createElement("player");

                rootElement.appendChild(player);

                Element name = doc.createElement("nickname");
                name.appendChild(doc.createTextNode(nickName));
                player.appendChild(name);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(_password));
                player.appendChild(password);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);

                transformer.transform(source, result);

            } catch (ParserConfigurationException | TransformerException e) {
                logger.warn(e.getStackTrace());
            }

        } else {
            logger.info("Az adatokat tartalmazo fajl letezik!");
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc;

                doc = documentBuilder.parse(saveFile);

                Element root = doc.getDocumentElement();
                Element player = doc.createElement("player");

                Element name = doc.createElement("nickname");
                name.appendChild(doc.createTextNode(nickName));
                player.appendChild(name);

                Element playerCard = doc.createElement("password");
                playerCard.appendChild(doc.createTextNode(_password));
                player.appendChild(playerCard);

                root.appendChild(player);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);
                // new File("target/" + datee + ".xml"));

                transformer.transform(source, result);

            } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
                logger.warn(e.getStackTrace());
            }

        }
        logger.info("Kimeneti fajl: " + saveFile.getPath());

    }

    
      public void deleteUser(String _nickName) {

        

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Path path = Paths.get(System.getProperty("user.home"), ".blackjack", LOGIN_DATA_XML);

            File loadFile = path.toFile();

            if (loadFile.exists()) {
                Document document;

                document = builder.parse(loadFile);

                NodeList nodeList = document.getElementsByTagName("player");

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                                        
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

                        String nickName = e.getElementsByTagName("nickname").item(0).getTextContent();
                        String password = e.getElementsByTagName("password").item(0).getTextContent();

                        if (_nickName.equals(nickName)) {
                            while (node.hasChildNodes())
                                node.removeChild(node.getFirstChild());
                            logger.info(nickName + " sikeresen torolve!");
                        }

                    }
                }
            } else {
                logger.warn("Nincs beolvashato fajl!");
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            logger.info(Data.class.getName());
        }

    }
      
   
    
    public void load() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", ROUNDS_DATA_XML);
        File loadFile = path.toFile();

        logger.info("Load fajl letezik: " + loadFile.exists());

        if (loadFile.exists()) {
            logger.info("Lejatszott korok betoltese...");
            Document document = builder.parse(loadFile);

            NodeList nodeList = document.getElementsByTagName("round");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    String name = e.getElementsByTagName("name").item(0).getTextContent();
                    String jatekos = e.getElementsByTagName("playercard").item(0).getTextContent();
                    String oszto = e.getElementsByTagName("pccard").item(0).getTextContent();
                    String datum = e.getElementsByTagName("datum").item(0).getTextContent();

                  /*  if(cont.getPlayerName().equals("admin")){
                       
                    }
                    else if(!cont.getPlayerName().equals(name)){
                        continue;
                        
                    }*/
                    
                    Round round = new Round(name, jatekos, oszto, datum);
                    roundData.add(round);

                }
            }
            logger.info("Lejatszott korok betoltve!");
        } else {
            logger.warn("Nincs beolvashato fajl!");
        }

    }

    public void loadRecordData() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        EncryptDecrypt crypto = new EncryptDecrypt();
        
        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", LOGIN_DATA_XML);
        File loadFile = path.toFile();

        logger.info("Load fajl letezik: " + loadFile.exists());

        if (loadFile.exists()) {
            logger.info("Felhasznlo adatainak betoltese!");
            Document document = builder.parse(loadFile);

            NodeList nodeList = document.getElementsByTagName("player");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    String nickname = e.getElementsByTagName("nickname").item(0).getTextContent();
                    String password = e.getElementsByTagName("password").item(0).getTextContent();


                    PlayerRecord playerRec = new PlayerRecord(nickname, crypto.decrypt(password));

                    recordData.add(playerRec);

                }
            }
            logger.info(recordData.size() + " felhasznalo betoltve.");
        } else {
            logger.warn("Nincs beolvashato fajl!");
        }

    }

    public void addRoundToFile(List<Card> playerIn, List<Card> pcIn, String nickName) {
/*
        String folder_path = System.getProperty("user.home") + File.separator;
        folder_path += File.separator + ".blackjack";
        File customDir = new File(folder_path);

        if (customDir.exists()) {
            logger.info(customDir + " mappa letezik.");
        } else if (customDir.mkdirs()) {
            logger.info(customDir + " mappa letrehozva.");
        } else {
            logger.warn(customDir + " mappa letrehozasa nem sikerult!");
        }

        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", ROUNDS_DATA_XML);
        File saveFile = path.toFile();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        if (!playerIn.isEmpty() && !pcIn.isEmpty() && !saveFile.exists()) {
            logger.info("A kimeneti fajl nem letezik. - Letrehozva!\n - " + saveFile.getPath());
            try {

                builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                Element rootElement = doc.createElement("rounds");
                doc.appendChild(rootElement);

                Element round = doc.createElement("round");
                rootElement.appendChild(round);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(nickName));
                round.appendChild(name);

                Element playerCard = doc.createElement("playercard");
                playerCard.appendChild(doc.createTextNode(printCards(playerIn)));
                round.appendChild(playerCard);

                Element pcCard = doc.createElement("pccard");
                pcCard.appendChild(doc.createTextNode(printCards(pcIn)));
                round.appendChild(pcCard);

                Element datum = doc.createElement("datum");
                datum.appendChild(doc.createTextNode(this.dateInTable));
                round.appendChild(datum);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);
                // (new File("target/" + datee + ".xml"));

                transformer.transform(source, result);

            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }

        } else {
            logger.info("A kimeneti fajl letezik!");
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc;

                doc = documentBuilder.parse(saveFile);
                

                Element root = doc.getDocumentElement();
                Element round = doc.createElement("round");

                Element name = doc.createElement("name");
//                name.appendChild(doc.createTextNode(cont.getPlayerName()));
                round.appendChild(name);

                Element playerCard = doc.createElement("playercard");
                playerCard.appendChild(doc.createTextNode(printCards(playerIn)));
                round.appendChild(playerCard);

                Element pcCard = doc.createElement("pccard");
                pcCard.appendChild(doc.createTextNode(printCards(pcIn)));
                round.appendChild(pcCard);

                Element datum = doc.createElement("datum");
                datum.appendChild(doc.createTextNode(this.dateInTable));
                round.appendChild(datum);

                root.appendChild(round);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);
                // new File("target/" + datee + ".xml"));

                transformer.transform(source, result);

            } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
                logger.warn(e.getStackTrace());
            }

        }
        logger.info("Kimeneti fajl: " + saveFile.getPath());
        */
    }
    

}
