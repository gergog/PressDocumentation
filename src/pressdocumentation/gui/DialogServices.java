/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import pressdocumentation.ArticleHandler;
import pressdocumentation.pojos.Article;
import pressdocumentation.pojos.Newspaper;

/**
 *
 * @author ETHGGY
 */
public class DialogServices implements WindowListener, CollectionDialogCallback {
    private static DialogServices dialogServices;
    private JFrame frame;
    private LinkCollectionDialog collectionDialog;
    
    
    
    
    public static DialogServices createDialogServices(JFrame frame) {
        if (dialogServices == null) {
            dialogServices = new DialogServices();
            dialogServices.init();
        } else {
            
        }
    
        dialogServices.setFrame(frame);
        
        
        return dialogServices;
        
    }
    
    public static DialogServices getDialogServices() {
        if (dialogServices == null) {
            // at first we need to init
            return null;
        } else {
            return dialogServices;
        }
    }

    private void init() {
        collectionDialog = new LinkCollectionDialog(null, this, false);
        collectionDialog.addWindowListener(this);
        collectionDialog.setAlwaysOnTop(true);
        
    }
    
    
    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
    public void showNewspaperDataDialog(Article a) {
        AddNewNewspaperDialog annd = new AddNewNewspaperDialog(this, true);
        annd.setTitle("Sajtótermék adatok");
        annd.setArticle(a);
        annd.setVisible(true);
        
    }
    
    @Override
    public void showArticleCollectionDialog() {
        Logger.getLogger(DialogServices.class.getName()).log(Level.INFO, "dialog shown");
        collectionDialog.setVisible(true);
        getFrame().toBack();
        
        
    }
    
    @Override
    public void hideArticleCollectionDialog() {
        Logger.getLogger(DialogServices.class.getName()).log(Level.INFO, "dialog hidden");
        collectionDialog.setVisible(false);
        
        ArticleHandler ah = ArticleHandler.createInstance();
        CollectionDialogListener dialogListener = ah;
        
        dialogListener.collectedLinks(collectionDialog.getURLs());
        
        getFrame().toFront();        
   
        
        
        
    }
    
    

    public void windowOpened(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosed(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
