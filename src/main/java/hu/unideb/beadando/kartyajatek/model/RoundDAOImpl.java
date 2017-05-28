/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.beadando.kartyajatek.model;

import hu.unideb.beadando.kartyajatek.controller.Controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author szilvacsku
 */
public class RoundDAOImpl implements RoundDAO{
    
    private static final Logger logger = LogManager.getLogger(RoundDAOImpl.class);

    private ObservableList<Round> roundData = FXCollections.observableArrayList();

    public RoundDAOImpl() {
    }
    
   
    @Override
    public void addRound(Round _round) {
        
        
        DateFormat dateFormatInTable = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");;
        Calendar cal = Calendar.getInstance();
        String dateInTable = dateFormatInTable.format(cal.getTime());
                
        
        
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

        Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getRoundsDataFileName());
        File saveFile = path.toFile();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        if (!_round.playerProperty().toString().isEmpty() 
                && !_round.osztoProperty().toString().isEmpty() && !saveFile.exists()) {
            logger.info("A kimeneti fajl nem letezik. - Letrehozva!\n - " + saveFile.getPath());
            try {

                builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                Element rootElement = doc.createElement("rounds");
                doc.appendChild(rootElement);

                Element round = doc.createElement("round");
                rootElement.appendChild(round);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(_round.nameProperty().toString()));
                round.appendChild(name);

                Element playerCard = doc.createElement("playercard");
                playerCard.appendChild(doc.createTextNode(_round.playerProperty().toString()));
                round.appendChild(playerCard);

                Element pcCard = doc.createElement("pccard");
                pcCard.appendChild(doc.createTextNode(_round.osztoProperty().toString()));
                round.appendChild(pcCard);

                Element datum = doc.createElement("datum");
                datum.appendChild(doc.createTextNode(dateInTable));
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
                name.appendChild(doc.createTextNode(_round.nameProperty().toString()));
                round.appendChild(name);

                Element playerCard = doc.createElement("playercard");
                playerCard.appendChild(doc.createTextNode(_round.playerProperty().toString()));
                round.appendChild(playerCard);

                Element pcCard = doc.createElement("pccard");
                pcCard.appendChild(doc.createTextNode(_round.osztoProperty().toString()));
                round.appendChild(pcCard);
                round.appendChild(pcCard);

                Element datum = doc.createElement("datum");
                datum.appendChild(doc.createTextNode(dateInTable));
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
        logger.info("Kimeneti fajl path: " + saveFile.getPath());
    }

    @Override
    public ObservableList<Round> getAllRound() {        
        
        Controller controller = Controller.getInstance();
        String playerNickName = controller.getManager().getGameModel().getPlayer().getNickname();
        
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Path path = Paths.get(System.getProperty("user.home"), ".blackjack", Controller.getInstance().getRoundsDataFileName());
            File loadFile = path.toFile();
            
            logger.info("Koroket tartalmazo fajl letezik-e: " + loadFile.exists());
            
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
                        
                        if(playerNickName.equals("admin")){
                        
                        }
                        else if(!playerNickName.equals(name)){
                            continue;                        
                        }
                        
                        Round round = new Round(name, jatekos, oszto, datum);
                        roundData.add(round);
                        
                    }
                }
                logger.info("Lejatszott korok betoltve!");
            } else {
                logger.error("Nincs beolvashato fajl!");
                
            }
            
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            logger.warn(RoundDAOImpl.class.getName() + ex);
        }
        return roundData;
    }
}
