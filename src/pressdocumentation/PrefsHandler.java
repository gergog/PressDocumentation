/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation;

import pressdocumentation.gui.AddNewNewspaperDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pressdocumentation.pojos.Newspaper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pressdocumentation.pojos.PageElementDescriptor;

/**
 *
 * @author ETHGGY
 */
public class PrefsHandler {

    private static PrefsHandler prefsHandler = null;
    private Document doc;

    public static PrefsHandler getInstance() {
        if (prefsHandler == null) {
            return (prefsHandler = new PrefsHandler());
        } else {
            return prefsHandler;
        }
    }

    
    private PrefsHandler() {
        DocumentBuilder dBuilder = null;
        try {
            File fXmlFile = new File("pressdocumentation.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

        } catch (FileNotFoundException ex) {
            doc = dBuilder.newDocument();
            initDocuments(doc);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PrefsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PrefsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrefsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    
    
    private void initDocuments(Document doc) {
        Element root = doc.createElement("pressdocumentation");
        Element defaults = doc.createElement("defaults");

        Element nameElement = doc.createElement("media");
        nameElement.appendChild(doc.createTextNode("ingatlan"));
        defaults.appendChild(nameElement);
        nameElement = doc.createElement("media");
        nameElement.appendChild(doc.createTextNode("gazd/közélet"));
        defaults.appendChild(nameElement);

        nameElement = doc.createElement("type");
        nameElement.appendChild(doc.createTextNode("online"));
        defaults.appendChild(nameElement);
        nameElement = doc.createElement("type");
        nameElement.appendChild(doc.createTextNode("offline"));
        defaults.appendChild(nameElement);
        nameElement = doc.createElement("type");
        nameElement.appendChild(doc.createTextNode("elektronikus"));
        defaults.appendChild(nameElement);
        root.appendChild(defaults);

        Element press = doc.createElement("presssettings");
        
        root.appendChild(press);
        
        Element settings = doc.createElement("settings");
        
        root.appendChild(settings);
        
        
        
        
//        Element settings = doc.createElement("settings");
//        nameElement = doc.createElement("CurrentDocument");
//        nameElement.appendChild(doc.createTextNode("ingatlan"));
        
        
        
        doc.appendChild(root);
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("pressdocumentation.xml"));
            transformer.transform(source, result);

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(AddNewNewspaperDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AddNewNewspaperDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    private Element initBaseElement(String name) {
        Node pn = doc.getElementsByTagName("pressdocumentation").item(0);

        Element settings;
        
        NodeList nl = doc.getElementsByTagName(name);
        
        if (nl.getLength() == 0) {
            settings = doc.createElement(name);
            
            if (pn != null) {
                pn.appendChild(settings);
            } else {
                doc.appendChild(settings);
            }
            
        } else {
            settings = (Element) nl.item(0);
        }

        return settings;
        
    }
    
    
    
    private Element initSettings() {
        return initBaseElement("settings");
    }
    
    
    private Element initDefaults() {
        return initBaseElement("defaults");
        
    }
    
    
    private Element initPressSettings() {
        return initBaseElement("presssettings");
    }
    

    private Element getCompanyElement(String company) {
        Element settings = initSettings();
        
        NodeList nl = settings.getElementsByTagName("company");

        boolean found = false;
        
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            
            Element e = (Element) n;
            
            if (e.getAttribute("name").equalsIgnoreCase(company)) {
                found = true;
                return e;
            }
            
        }
        
        if (!found) {
            Element companyElement = doc.createElement("company");
            companyElement.setAttribute("name", company);
            settings.appendChild(companyElement);
            
            return companyElement;
        }
        
        
        return null;
    }
    
    private void addCompanySetting(String company, String attributeName, String attributeValue) {
        Element companyElement = getCompanyElement(company);
        
        NodeList nl = companyElement.getElementsByTagName(attributeName);

        if (nl.getLength() > 0 ) {
            Node n = nl.item(0);
            
            Element e = (Element) n;
            
            e.setTextContent(attributeValue);
            
        } else {
            Element attributeElement = doc.createElement(attributeName);
            attributeElement.setTextContent(attributeValue);
            companyElement.appendChild(attributeElement);
            
            
        }
                
    }
    
    private void addOtherSetting(String attributeName, String attributeValue) {
        Element settings = initSettings();

        NodeList nl = settings.getElementsByTagName(attributeName);

        if (nl.getLength() > 0 ) {
            Node n = nl.item(0);
            
            Element e = (Element) n;
            
            e.setTextContent(attributeValue);
            
        } else {
            Element attributeElement = doc.createElement(attributeName);
            attributeElement.setTextContent(attributeValue);
            settings.appendChild(attributeElement);
            
            
        }
        
        
    }
    
    
    
    public void addDocumentDirectory(String company, String dir) {
        
       
        addCompanySetting(company, "documentdirectory", dir);
                
    }
    
    
    public void addSpreadsheetDirectory(String company, String dir) {
        
        addCompanySetting(company, "spreadsheetdirectory", dir);

        
    }
    public void addDefaultCompany(String company) {
        
        addOtherSetting("defaultcompany", company);
            
  
    }
    
    public String getSettings(String s) {
        String p;
        doc.getDocumentElement().normalize();

        Element settings = initSettings();
        
//          System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = settings.getElementsByTagName(s);

        if (nList.getLength() == 0) {
            return null;
        } else {
            Node nNode = nList.item(0);

            Element eElement = (Element) nNode;

            return eElement.getTextContent();
            
        }
                
    }
    

    private String getDefaultCompany() {
        
        return getSettings("defaultcompany");
                
    }
    
    
    public String getDefaultDocumentDirectory() {
        String d = getDefaultCompany();
        
        if (d == null) {
            return "C:\\Users\\";
        }
        
        
        return getCompanyDocumentDirectory(d);
        
    }
    
    public String getDefaultSpreadsheetDirectory() {
        String d = getDefaultCompany();
        if (d == null) {
            return "C:\\Users\\";
        }
        
        return getSpreadsheetDocumentDirectory(d);
        
    }

    public String getCompanyDocumentDirectory(String company) {
        
        Element e = getCompanyElement(company);
        
        NodeList nl = e.getElementsByTagName("documentdirectory");

        if (nl.getLength() == 0) {
            return null;
        } else {
            return nl.item(0).getTextContent();
        }
        
    }

    public String getSpreadsheetDocumentDirectory(String company) {
        
        Element e = getCompanyElement(company);
        
        NodeList nl = e.getElementsByTagName("spreadsheetdirectory");

        if (nl.getLength() == 0) {
            return null;
        } else {
            return nl.item(0).getTextContent();
        }
        
    }
    
    
    
    
    public String[] getCompanies() {
        String[] sa = new String[0];
        ArrayList<String> als = new ArrayList<String>();
    
        doc.getDocumentElement().normalize();

        Element settings = initSettings();
        
        NodeList nList = settings.getElementsByTagName("company");
        
        for (int i = 0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            Element e1 = (Element) n;
           
            als.add(e1.getAttribute("name"));
        }
        
        return als.toArray(sa);
    }
    

    
    private Element initPressElement(String webpage) {
        Element press = initPressSettings();

        Element nameElement = doc.createElement("press");
        nameElement.setAttribute("webpage", webpage);
        press.appendChild(nameElement);

        Element webElement = doc.createElement("name");
        nameElement.appendChild(webElement);
        Element mediaElement = doc.createElement("media");
        nameElement.appendChild(mediaElement);
        Element typeElement = doc.createElement("type");
        nameElement.appendChild(typeElement);
        Element imageElement = doc.createElement("cropimageposition");        
        nameElement.appendChild(imageElement);
        /*
        Element headerElement = doc.createElement("header");        
        nameElement.appendChild(headerElement);
        Element prefaceElement = doc.createElement("preface");        
        nameElement.appendChild(prefaceElement);
        Element bodyElement = doc.createElement("body");        
        nameElement.appendChild(bodyElement);
*/
        return nameElement;
        
    }
    
    public Element getPressElement(String webpage) {

        NodeList pn = doc.getElementsByTagName("press");
        
        for (int i = 0; i < pn.getLength(); i++) {
            Element n = (Element) pn.item(i);
            
            if (n.getAttribute("webpage").equalsIgnoreCase(webpage)) {
                return n;
            }
            
        }

        return null;
        
    }

    
    private Element getPressAttributeElement(Element press, String attributeName) {
        
        NodeList nl = press.getElementsByTagName(attributeName);
        
        if (nl.getLength() == 0) {
            Element e = doc.createElement(attributeName);
            press.appendChild(e);
            
            return e;
            
        } else {
            return (Element) nl.item(0);
        }
        
        
    }
    
    public void initPressAttribute(Element press, String attributeName) {
        NodeList nl = press.getElementsByTagName(attributeName);
        
//        System.out.println("PrefsHandler::initPressAttribute(" + attributeName + " node list length : " + nl.getLength());
        for (int i = nl.getLength(); i > 0; i--) {
            Element e = (Element) nl.item(i - 1);
            
            e.getParentNode().removeChild(e);
//            doc.removeChild(nl.item(i - 1));
        }

        doc.normalize();
        nl = press.getElementsByTagName(attributeName);
//        System.out.println("PrefsHandler::initPressAttribute " + "initPressAttribute(" + attributeName + " node list length : " + nl.getLength());
        
    }

    public void addPressCondition(Element press, String type, String value) {
        Element e = doc.createElement(type);
        e.setAttribute("value", value);
        
        press.appendChild(e);
        
    }
    
    
    public void addPress(Newspaper p ) {

//        System.out.println("PrefsHandler::addPress " + "addPress getLink: " + p.getLink());
        Element press = getPressElement(p.getLink());
        
        if (press == null) {
            press = initPressElement(p.getLink());
        }
        
        Element element = getPressAttributeElement(press, "name");
        element.setTextContent(p.getName());

        element = getPressAttributeElement(press, "media");
        element.setTextContent(p.getMedia());
        
        element = getPressAttributeElement(press, "type");
        element.setTextContent(p.getType());

        element = getPressAttributeElement(press, "cropimageposition");
        element.setAttribute("ux", String.valueOf(p.getScreenUpperLeftX()));
        element.setAttribute("uy", String.valueOf(p.getScreenUpperLeftY()));
        element.setAttribute("dx", String.valueOf(p.getScreenLowerRightX()));
        element.setAttribute("dy", String.valueOf(p.getScreenLowerRightY()));

        Logger.getLogger(PrefsHandler.class.getName()).log(Level.INFO, 
                "getPress cropimageposition ux : " + p.getScreenUpperLeftX() + " uy: " + p.getScreenUpperLeftY()+ " dx: " + p.getScreenLowerRightX()+ " dy: " + p.getScreenLowerRightY());
        
        
        List<String> parts = new ArrayList<String>();
        parts.add("header");
        parts.add("date");
        parts.add("preface");
        parts.add("body");
        parts.add("upleft-component");
        parts.add("downright-component");
        
        
        
        for (String s : parts) {
            initPressAttribute(press, s);
  //          System.out.println("PrefsHandler::addPress " + s);

            String ped = p.getCondition(s);

//            System.out.println("PrefsHandler::addPress " + s + " : " + ped);

            addPressCondition(press, s, ped);

            
        
        }        
        
    }
    
    
    
    
    
    public void store() {
        try {


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("pressdocumentation.xml"));
            transformer.transform(source, result);


        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PrefsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PrefsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    

    
    
    public String[] getDefaults(String s) {
        String[] sa = new String[0];
        ArrayList<String> als = new ArrayList<String>();
    
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("defaults");
        
        nList = nList.item(0).getChildNodes();
        
        boolean found = false;
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

//                Element eElement = (Element) nNode;

                if (nNode.getNodeName().equalsIgnoreCase(s)) {
//                    System.out.println("PrefsHandler::getDefaults " + "default " + s + " added : " + nNode.getTextContent());
                    als.add(nNode.getTextContent());
                }
            }
        }
        
        return als.toArray(sa);
        
    }

    private List<Element> getPressCondition(Element press, String attributeName) {
        List<Element> list = new ArrayList<Element>();
        
        NodeList nl = press.getElementsByTagName(attributeName);
        
        if (nl.getLength() == 0) {
//            Element e = doc.createElement(attributeName);
//            press.appendChild(e);

//            list.add(e);
            return list;
            
        } else {
            
            for (int i = 0; i < nl.getLength(); i++) {
                list.add((Element)nl.item(i));
            }
            
            return list;
        }
    }
    

    public Newspaper getPress(String host) {
        Newspaper p = new Newspaper();

        Element eElement = getPressElement(host);

        if (eElement == null) {
            return null;
        }
        
//        System.out.println("PrefsHandler::getPress : " + host);
        
        if (host.contains(eElement.getAttribute("webpage"))) {
            p.setName(getTagValue("name", eElement));
            p.setLink(eElement.getAttribute("webpage"));
            p.setMedia(getTagValue("media", eElement));
            p.setType(getTagValue("type", eElement));
            
            Element pos = getPressAttributeElement(eElement, "cropimageposition");
            p.setScreenUpperLeftX(Integer.parseInt(pos.getAttribute("ux")));
            p.setScreenUpperLeftY(Integer.parseInt(pos.getAttribute("uy")));
            p.setScreenLowerRightX(Integer.parseInt(pos.getAttribute("dx")));
            p.setScreenLowerRightY(Integer.parseInt(pos.getAttribute("dy")));
            

            List<String> parts = new ArrayList<String>();
            parts.add("header");
            parts.add("date");
            parts.add("preface");
            parts.add("body");
            parts.add("upleft-component");
            parts.add("downright-component");
            for (String s : parts) {
                List<Element> list = getPressCondition(eElement, s);

                for (Iterator<Element> it = list.iterator(); it.hasNext();) {
                    Element element = it.next();

                    p.addCondition(s, element.getAttribute("value"));
  //                  System.out.println("PrefsHandler::getPress value : " + element.getAttribute("value"));

                }
    
            }
            
            return p;
        } else {
        }

        return p;
        
    }
    
    public Newspaper getPress(URL u) {

        String host = u.getHost();

        return getPress(host);

    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }

    
    
    
}
