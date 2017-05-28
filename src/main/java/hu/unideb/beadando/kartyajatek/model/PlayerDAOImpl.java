/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import hu.unideb.beadando.kartyajatek.controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author szilvacsku
 */
public class PlayerDAOImpl implements PlayerDAO{

    private static final Logger logger = LogManager.getLogger(PlayerDAOImpl.class);

    private ObservableList<PlayerRecord> recordData = FXCollections.observableArrayList();
    
    
    public PlayerDAOImpl() {
    }
    
    
    @Override
    public void registerPlayer(Player player) {
        
        String encryptPw = player.getPassword();
        encryptPw = new EncryptDecrypt().encrypt(encryptPw);
        
        player.setPassword(encryptPw);
        
        
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

        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getPlayerDataFileName());
        File saveFile = path.toFile();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        if ( !saveFile.exists() ) {
            logger.info("A kimeneti fajl nem letezik. - Letrehozva!\n - " + saveFile.getPath());
            try {

                builder = factory.newDocumentBuilder();

                Document doc = builder.newDocument();

                Element rootElement = doc.createElement("players");
                doc.appendChild(rootElement);

                Element playerElement = doc.createElement("player");

                rootElement.appendChild(playerElement);

                        
                Element nickName = doc.createElement("nickname");
                nickName.appendChild(doc.createTextNode(player.getNickname()));
                playerElement.appendChild(nickName);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(player.getPassword()));
                playerElement.appendChild(password);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);

                transformer.transform(source, result);

            } catch (ParserConfigurationException | TransformerException e) {
                logger.warn(e.getStackTrace().toString());
            }

        } else {
            logger.info("Az adatokat tartalmazo fajl letezik!");
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc;

                doc = documentBuilder.parse(saveFile);

                Element root = doc.getDocumentElement();
                Element playerElement = doc.createElement("player");

     
                Element nickName = doc.createElement("nickname");
                nickName.appendChild(doc.createTextNode(player.getNickname()));
                playerElement.appendChild(nickName);

                Element playerCard = doc.createElement("password");
                playerCard.appendChild(doc.createTextNode(player.getPassword()));
                playerElement.appendChild(playerCard);

                root.appendChild(playerElement);

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(saveFile);
                // new File("target/" + datee + ".xml"));

                transformer.transform(source, result);

            } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
                logger.warn(e.getStackTrace().toString());
            }

        }
        logger.info("Kimeneti fajl: " + saveFile.getPath());
                
    }

    @Override
    public void removePlayer(Player player) {
        ///Nem 100%
        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getPlayerDataFileName());
        File saveFile = path.toFile();
        
        Document document;
        
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File loadFile = path.toFile();

            if (loadFile.exists()) {
                

                document = builder.parse(loadFile);

                NodeList nodeList = document.getElementsByTagName("player");

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                                        
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

                        String nickName = e.getElementsByTagName("nickname").item(0).getTextContent();
                        String password = e.getElementsByTagName("password").item(0).getTextContent();

                                                
                        if (player.getNickname().equals(nickName)) {
                            while (node.hasChildNodes())
                                node.removeChild(node.getFirstChild());
                            logger.info(nickName + " sikeresen torolve!");
                            break;
                        }

                    }
                }
                
                
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(saveFile);
                // new File("target/" + datee + ".xml"));

                transformer.transform(source, result);
                
                
            } else {
                logger.warn("Nincs beolvashato fajl!");
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            logger.warn(ex.getStackTrace().toString());
        } catch (TransformerConfigurationException ex) {
            logger.warn(ex.getStackTrace().toString());
        } catch (TransformerException ex) {
            logger.warn(ex.getStackTrace().toString());
        }        
        
        
        
    }

    @Override
    public Player getRegisteredPlayer(String _nickName, String _password) {
        
        Player player;

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getPlayerDataFileName());

            File loadFile = path.toFile();

            if (loadFile.exists()) {
                Document document;

                document = builder.parse(loadFile);

                NodeList nodeList = document.getElementsByTagName("player");

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

//                        String name     = e.getElementsByTagName("name").item(0).getTextContent();
                        String nickName = e.getElementsByTagName("nickname").item(0).getTextContent();
                        String password = e.getElementsByTagName("password").item(0).getTextContent();
                        
                        EncryptDecrypt enc = new EncryptDecrypt();
                        
                        password = enc.decrypt(password);
                        
                        if (_nickName.equals(nickName) && _password.equals(password)) {
                            player = new Player("a", _nickName, password);
                            return player;
                        }

                    }
                }
            } else {
                logger.warn("Nincs beolvashato fajl!");
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            logger.info(Data.class.getName());
        }
        logger.warn("Nem talaltunk ilyen felhasznalot!");
        return null;
    }

    @Override
    public String getDecryptPassword(String password) {
        
        EncryptDecrypt encrypt = new EncryptDecrypt();
        
        logger.info("--- Jelszo dekodolva! ---");
        
        return encrypt.decrypt(password);
        
    }

    @Override
    public boolean searchPlayer(String _nickname) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getPlayerDataFileName());

            File loadFile = path.toFile();

            if (loadFile.exists()) {
                Document document;

                document = builder.parse(loadFile);

                NodeList nodeList = document.getElementsByTagName("player");

                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

//                        String name     = e.getElementsByTagName("name").item(0).getTextContent();
                        String nickName = e.getElementsByTagName("nickname").item(0).getTextContent();
                        String password = e.getElementsByTagName("password").item(0).getTextContent();
                        
                      
                        
                        if (_nickname.equals(nickName)) {
                            return true;
                        }

                    }
                }
            } else {
                logger.warn("Nincs beolvashato fajl!");
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            logger.info(Data.class.getName());
        }
        logger.warn("Nem talaltunk ilyen felhasznalot!");
        return false;
    }
    
    
    public ObservableList<PlayerRecord> getAllPlayer() throws SAXException, IOException, ParserConfigurationException {        
        
        Controller controller = Controller.getInstance();
        String playerNickName = controller.getManager().getGameModel().getPlayer().getNickname();
       
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        EncryptDecrypt crypto = new EncryptDecrypt();
        
        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getPlayerDataFileName());
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
            logger.error("Nincs beolvashato fajl!");
        }
        return recordData;
    }
    
    
}
