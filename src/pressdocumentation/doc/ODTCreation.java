/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.doc;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ooo.connector.BootstrapSocketConnector;
import pressdocumentation.pojos.Article;

/**
 *
 * @author ETHGGY
 */
public class ODTCreation extends DocumentCreation  {

    private XComponentLoader xComponentLoader;
    private XTextDocument aTextDocument;
    private XText xText;
    private XMultiServiceFactory xMSF;
    private XPropertySet xTextProps;
    
    
    public ODTCreation() {
        try {
            String oooExeFolder = "C:/Program Files/OpenOffice.org 3/program/";
            // Get the remote office component context
//            XComponentContext xContext = Bootstrap.bootstrap();
            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);

            // Get the remote office service manager
            XMultiComponentFactory xMCF = xContext.getServiceManager();

            // Get the root frame (i.e. desktop) of openoffice framework.
            Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);

            // Desktop has 3 interfaces. The XComponentLoader interface provides ability to load components.
            xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, oDesktop);
        } catch (BootstrapException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void openDoc(String url) throws IOException {
        try {
            // Load the document, which will be displayed. More param info in apidoc
            XComponent xComp = xComponentLoader.loadComponentFromURL(url, "_blank", 0, new PropertyValue[0]);

            // Get the textdocument
            aTextDocument = (XTextDocument) UnoRuntime.queryInterface(
                    XTextDocument.class, xComp);

            // Get its text
            xText = aTextDocument.getText();
            
            XTextRange xTextRange = xText.createTextCursor();
            ((XTextCursor) xTextRange).gotoEnd(true);

            xTextProps = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xTextRange);
            xTextProps.setPropertyValue("CharFontName", "Times New Roman");
            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(12));
            
            xMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, aTextDocument);
            
            
        } catch (UnknownPropertyException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    protected void addTable(Article a) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            
            
            
            // Creating a table with 3 rows and 4 columns
             XTextTable xTextTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xMSF.createInstance("com.sun.star.text.TextTable"));
             xTextTable.initialize(2, 2); // rows, cols
             XPropertySet xTableTextProps = (XPropertySet) UnoRuntime.queryInterface(
                     XPropertySet.class, xTextTable);
             xTableTextProps.setPropertyValue("BackColor", 6724095);
             xTableTextProps.setPropertyValue("RelativeWidth", 10);

             // insert table  in the xText
             xText.insertTextContent(xText.getEnd(), xTextTable, false);
             
            XCellRange xCellRangeHeader = (XCellRange) UnoRuntime.queryInterface(XCellRange.class, xTextTable);
            XCell xCellHeader = null;
            XText xHeaderText = null;

            xCellHeader = xCellRangeHeader.getCellByPosition(0, 0); // cols, rows
//            XPropertySet xCellTextProps = (XPropertySet) UnoRuntime.queryInterface(
//                XPropertySet.class, xCellHeader);
//            xCellTextProps.setPropertyValue("CharFontStyleName", "Regular");

            xHeaderText = (XText) UnoRuntime.queryInterface(XText.class, xCellHeader);
            xHeaderText.setString("Média");

            xCellHeader = xCellRangeHeader.getCellByPosition(1, 0); // cols, rows
            xHeaderText = (XText) UnoRuntime.queryInterface(XText.class, xCellHeader);
            xHeaderText.setString("Dátum");

            xCellHeader = xCellRangeHeader.getCellByPosition(0, 1); // cols, rows
            xHeaderText = (XText) UnoRuntime.queryInterface(XText.class, xCellHeader);
//            xHeaderText.setString("Űrbeli Önképzőkör");
            xHeaderText.setString(a.getPaper().getName());

            xCellHeader = xCellRangeHeader.getCellByPosition(1, 1); // cols, rows
            xHeaderText = (XText) UnoRuntime.queryInterface(XText.class, xCellHeader);
            xHeaderText.setString(sdf.format(a.getDate()));


            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
             
             
        } catch (Exception ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected void addImage(Article a) {


        try {
            BufferedImage originalImage = ImageIO.read(new URL(a.getFileURL()));

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();            
            int imageWidth = 18000;

            int imageHeight = (originalHeight * imageWidth)/originalWidth;

//            System.out.println(originalWidth + "/" + originalHeight);




            XNameContainer xBitmapContainer = null;
            XTextContent xImage = null;
            String internalURL = null;


            try {
                xBitmapContainer = (XNameContainer) UnoRuntime.queryInterface(
                        XNameContainer.class, xMSF.createInstance(
                        "com.sun.star.drawing.BitmapTable"));
                xImage = (XTextContent) UnoRuntime.queryInterface(
                        XTextContent.class, xMSF.createInstance(
                        "com.sun.star.text.TextGraphicObject"));
                XPropertySet xProps = (XPropertySet) UnoRuntime.queryInterface(
                        XPropertySet.class, xImage);

                // helper-stuff to let OOo create an internal name of the graphic
                // that can be used later (internal name consists of various checksums)
                xBitmapContainer.insertByName("crash", "file:///C:/Users/ethggy/Documents/crash1.jpg");

                internalURL = AnyConverter.toString(xBitmapContainer.getByName("crash"));

                xProps.setPropertyValue("AnchorType",
                        com.sun.star.text.TextContentAnchorType.AS_CHARACTER);
                xProps.setPropertyValue("GraphicURL", internalURL);
                xProps.setPropertyValue("Width", new Integer(imageWidth));
    //                xProps.setPropertyValue("Width", new Integer(450));
                xProps.setPropertyValue("Height", new Integer(imageHeight));
    //                xProps.setPropertyValue("Height", new Integer(271));

                // inser the graphic at the cursor position
                xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
                xText.insertTextContent(xText.getEnd(), xImage, false);

                // remove the helper-entry
                xBitmapContainer.removeByName("crash");
            } catch (Exception e) {
                System.out.println("Failed to insert Graphic");
            }
            
        } catch (java.io.IOException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected void addArticleBody(Article a) {
        try {
            // Adding text to document
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
//            xText.insertString(xText.getEnd(), "Már szerdán meghalt a kulcsi négyes gyilkosság első áldozata, az elkövető bátyja, derült ki a részletes beismerő vallomásból, melyet a pénteken elfogott H. Csanád Jutas tett. A nagyszülőkkel csütörtökön végzett. A többi családtagot először áramütéssel próbálta megölni. A férfi nem bánta meg tettét, családtagjait halálos ellenségeinek gondolta. Kényszergyógykezelésre egyelőre nem küldik, holnap döntenek előzetes letartóztatásáról. A 23 éves H. Csanád Jutast, akit még tegnap elfogtak a TEK munkatársai, sérülései miatt először a Dunaújvárosi Kórházba vitték, onnan szállították át a Dunaújvárosi Rendőrkapitányságra, majd később Székesfehérvárra, a Fejér Megyei Rendőr-főkapitányságra, ahol kihallgatták gyanúsítottként, majd bűnügyi őrizetbe vették. Kezdeményezték előzetes letartóztatását is, erről vasárnap dönt a bíróság. A rendőrségi közlemény szerint a helyszíni szemle adataiból és a vallomásából is az derült ki, hogy a négyes gyilkosságot előre kitervelten, különös kegyetlenséggel követte el.", false);

            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(14));
            xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));            
            xText.insertString(xText.getEnd(), a.getTitle(), false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            
            if (!a.getPreface().isEmpty()) {
                xTextProps.setPropertyValue("CharHeight", new Float(12));
                xText.insertString(xText.getEnd(), a.getPreface(), false);
                xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            }
            
            xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));            
            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(12));
            xText.insertString(xText.getEnd(), a.getBody(), false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
        } catch (UnknownPropertyException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ODTCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
