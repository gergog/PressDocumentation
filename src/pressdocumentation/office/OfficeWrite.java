/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.office;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.PageStyleLayout;
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
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import ooo.connector.BootstrapSocketConnector;
import pressdocumentation.PrefsHandler;
import pressdocumentation.pojos.Article;

/**
 *
 * @author ETHGGY
 */
public class OfficeWrite {

    private XTextDocument aTextDocument;

    private XText xText;
    
    public OfficeWrite() {
        try {
            String oooExeFolder = "C:/Program Files/OpenOffice.org 3/program/";
            // Get the remote office component context
    //            XComponentContext xContext = Bootstrap.bootstrap();
            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
            System.out.println("elso");

            // Get the remote office service manager
            XMultiComponentFactory xMCF = xContext.getServiceManager();
            System.out.println("ketto");

            // Get the root frame (i.e. desktop) of openoffice framework.
            Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
            System.out.println("harom");

            // Desktop has 3 interfaces. The XComponentLoader interface provides ability to load components.
            XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, oDesktop);
            System.out.println("negy");

            // URL of the component to be loaded
            PrefsHandler ph = PrefsHandler.getInstance();
            
            StringBuilder sb = new StringBuilder("file:///");
            sb.append(ph.getDefaultDocumentDirectory());
            
            String sUrl = sb.toString();
            
            System.out.println("surl : " + sUrl);

            // Load the document, which will be displayed. More param info in apidoc
            XComponent xComp = xComponentLoader.loadComponentFromURL(sUrl, "_blank", 0, new PropertyValue[0]);

            // Get the textdocument
            aTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, xComp);
            // Get its text
            xText = aTextDocument.getText();
            
        } catch (BootstrapException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void addTable(Article a) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");        
        
        
        try {
            XMultiServiceFactory xMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, aTextDocument);

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
            xHeaderText.setString(a.getPaper().getName());

            xCellHeader = xCellRangeHeader.getCellByPosition(1, 1); // cols, rows
            xHeaderText = (XText) UnoRuntime.queryInterface(XText.class, xCellHeader);
            xHeaderText.setString(sdf.format(a.getDate()));
        } catch (UnknownPropertyException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * @param args the command line arguments
     */
    public void addArticle(Article a) {
        try {


            XTextRange xTextRange = xText.createTextCursor();
            ((XTextCursor) xTextRange).gotoEnd(true);
            
            XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(
                    XPropertySet.class, xTextRange);
            xTextProps.setPropertyValue("CharFontName", "Times New Roman");
            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(12));
            
            XMultiServiceFactory xMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, aTextDocument);            
            

            addTable(a);
            
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            StringBuilder sb = new StringBuilder("file:///");
            sb.append(a.getCropURL());
            System.out.println("cropped image : " + sb.toString());

            BufferedImage originalImage = ImageIO.read(new URL(sb.toString()));

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();            
            int imageWidth = 18000;
            
            int imageHeight = (originalHeight * imageWidth)/originalWidth;
            
            System.out.println(originalWidth + "/" + originalHeight);

            
            XNameContainer xBitmapContainer = null;
            XTextContent xImage = null;
            String internalURL = null;


//            try {
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
                
//                File f = new File (a.getCropURL());
                
                if (!xBitmapContainer.hasByName("mypic")) {
                    xBitmapContainer.insertByName("mypic", sb.toString());
                }
                // get interface
                XNameAccess bitmapAccess = (XNameAccess)UnoRuntime.queryInterface(XNameAccess.class, xBitmapContainer);
                internalURL = AnyConverter.toString(bitmapAccess.getByName("mypic"));

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
                xBitmapContainer.removeByName("mypic");
//            } catch (Exception e) {
//                System.out.println("Failed to insert Graphic : " + e.getMessage());
//            }

            // Adding text to document
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);

            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(14));
            xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD)); 

            xText.insertString(xText.getEnd(), a.getTitle(), false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);

            xTextProps.setPropertyValue("CharFontStyleName", "Ragular");
            xTextProps.setPropertyValue("CharHeight", new Float(12));
            xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD)); 

            xText.insertString(xText.getEnd(), a.getPreface(), false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);

            xTextProps.setPropertyValue("CharFontStyleName", "Regular");
            xTextProps.setPropertyValue("CharHeight", new Float(12));
            xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL)); 

            xText.insertString(xText.getEnd(), a.getBody(), false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);
            xText.insertControlCharacter(xText.getEnd(), ControlCharacter.PARAGRAPH_BREAK, false);


        } catch (MalformedURLException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OfficeWrite.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
