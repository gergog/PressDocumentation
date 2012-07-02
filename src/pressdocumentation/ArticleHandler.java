/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation;

import java.util.Date;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pressdocumentation.gui.CollectionDialogListener;
import pressdocumentation.gui.DialogServices;
import pressdocumentation.office.OfficeWrite;
import pressdocumentation.pojos.Article;
import pressdocumentation.pojos.Newspaper;

/**
 *
 * @author ethggy
 */
public class ArticleHandler implements ArticleListener, CollectionDialogListener {
    
    private CopyOnWriteArrayList<Article> list = null;
    private static ArticleHandler ah = null;
    private List<ArticleListener> listeners;
    
    private DownloadScreenshotThread downloadScreenshot;
//    private DownloadTextThread downloadText;
    
    
    
    
    private ArticleHandler() {
        list = new CopyOnWriteArrayList<Article>();
        listeners = new ArrayList<ArticleListener>();
        
        listeners.add(this);
        
        
        downloadScreenshot = new DownloadScreenshotThread();
        
        downloadScreenshot.start();

//        downloadText = new DownloadTextThread();
        
//        downloadText.start();
    
    }
    
    public void addListener(ArticleListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ArticleListener listener) {
        listeners.remove(listener);
    }
    
    public void notifyListeners(int type, Article a) {
        a.setStatus(type);
        for (Iterator<ArticleListener> it = listeners.iterator(); it.hasNext();) {
            ArticleListener listener = it.next();
            
            listener.articleModified(type, a);
            
        }
    }

    public void clearUp() {
        downloadScreenshot.stopDownloads();
//        downloadText.stopDownloads();
    }
    
    public static ArticleHandler createInstance() {
        if (ah == null) {
            ah = new ArticleHandler();
        } else {
            
        }
        
        return ah;
    }
    
    public void createArticleFromURL(URL url) {
        URL secondURL = null;
        try {
            Logger.getLogger(ArticleHandler.class.getName()).log(Level.INFO,"url : " + url.toString());
            
            
            HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
            ucon.setInstanceFollowRedirects(false);
            try {
                secondURL = new URL(ucon.getHeaderField("Location"));
            } catch (MalformedURLException malformedURLException) {
                secondURL = url;
            }
            
            
            Logger.getLogger(ArticleHandler.class.getName()).log(Level.INFO,"second url : " + secondURL.toString());
            //                        URLConnection conn = secondURL.openConnection();                    
            Article a = new Article(); 
            a.setLink(secondURL.toString());

            addArticle(a);
            PrefsHandler ph = PrefsHandler.getInstance();
            Newspaper p = ph.getPress(secondURL);

            if (p == null) {

                p = new Newspaper();
                a.setPaper(p);
                p.setLink(secondURL.getHost());
   
                DialogServices dialogServices = DialogServices.getDialogServices();
                dialogServices.showNewspaperDataDialog(a);

            } else {
                a.setPaper(p);
                
                notifyListeners(ArticleListener.NEWSPAPER_MODIFIED, a);
//                addNewsPaper(a);
            }

        } catch (IOException ex) {
            Logger.getLogger(PressDocumentationView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    public int size() {
        return list.size();
    }
    
    public Article getArticle(int i) {
        return list.get(i);
    }
    
    public void addArticle(Article a) {
        list.add(a);
        
        notifyListeners(ArticleListener.ARTICLE_CREATED, a);
        
    }
    
    public void removeArticle(String url) {
        for (int i = 0; i < list.size(); i++) {
            Article a = list.get(i);
            
            if (a.getLink().equalsIgnoreCase(url)) {
                list.remove(i);
                
                break;
            }
            
        }
    }
    
    public void removeArticle(Article a) {
        removeArticle(a.getLink());
        
        notifyListeners(ArticleListener.ARTICLE_REMOVED, a);
    }

    public void articleModified(int type, Article a) {
        if (type == ArticleListener.NEWSPAPER_MODIFIED) {
            Newspaper p = a.getPaper();

            PrefsHandler ph = PrefsHandler.getInstance();
            if (ph.getPress(p.getLink()) == null) {
                ph.addPress(p);
                ph.store();
            }
            
            if ((p.getCondition("header") != null) && (!p.getCondition("header").isEmpty())) {
                notifyListeners(ArticleListener.TEXT_EXTRACTION_DATA_PROVIDED, a);
            }
            
            
            
            // we start the screenshot downloadwhen everything is ready
            /*
            if ((p.getCondition("header") != null) && (!p.getCondition("header").isEmpty())) {
                downloadScreenshot.addArticle(a);
                    
//                downloadText.addArticle(a);
                
            }
             * 
             */
        } else if (type == ArticleListener.TEXT_EXTRACTION_TO_MAKE) {
            
            Newspaper p = a.getPaper();
            if ((p.getCondition("header") != null) && (!p.getCondition("header").isEmpty())) {
                downloadScreenshot.addArticle(a);
                    
//                downloadText.addArticle(a);
                
            }
            
            
        } else {
            
        }
        
    }

    void printDoc(Date date) {
        
        
        /*
        OfficeWrite ow = new OfficeWrite();
        
        for (int i = 0; i < list.size(); i++) {
            Article a = list.get(i);
        
            if (a == null) {
                a.setDate(date);
            }
            ow.addArticle(a);
                    
        }
        */
        
    }

    public void collectedLinks(List<URL> l) {
        for (Iterator<URL> it = l.iterator(); it.hasNext();) {
            URL u = it.next();
            
            createArticleFromURL(u);
            
        }
    }
    
    
}
