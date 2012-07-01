/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pressdocumentation.pojos.Article;
import pressdocumentation.pojos.Newspaper;
import pressdocumentation.util.DateConverter;

/**
 *
 * @author ethggy
 */
public class DownloadScreenshotThread extends Thread {
    
    private ArrayList<Article> articleList;
    private volatile boolean downloads = true;
    private volatile boolean stopped = false;

    private ChromeDriver driver;
    private String directory;
    
    public DownloadScreenshotThread() {
        articleList = new ArrayList<Article>();
        
        PrefsHandler ph = PrefsHandler.getInstance();
        
        File f = new File(ph.getDefaultDocumentDirectory());
        f = f.getParentFile();
        
        directory = f.toString();
        
        
        
    }

    public void addArticle(Article a) {
        articleList.add(a);
    }
    
    public void stopDownloads() {
        downloads = false;
        
        while (!stopped);
        
        System.out.println("Download stopped");
        
    }
    
    
    public void run() {
        
        Article a = null;
        while (downloads) {
            
            if (articleList.size() > 0) {
                if (driver == null) {
                    driver = new ChromeDriver();
                }

                a = null;

                synchronized (articleList) {
                    if (articleList.size() > 0) {
                        a = articleList.remove(0);
                    }
                }

                if (a != null) {
                    ArticleHandler ah = ArticleHandler.createInstance();
                    if (takeScreenshot(a)) {
                        ah.notifyListeners(ArticleListener.IMAGE_DOWNLOADED, a);
                    }
                    textExtraction(a);
                    ah.notifyListeners(ArticleListener.TEXT_EXTRACTED, a);
                }
            }
            
            
        }
        

        if (driver != null) {
            driver.quit();
        }
        stopped = true;
        
        
    }
    
    private boolean takeScreenshot(Article a) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat hm = new SimpleDateFormat("HHmmss");

        driver.get(a.getLink());


        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        try {

            StringBuilder sb = new StringBuilder(directory);
            sb.append("\\" + sdf.format(new Date()) + "\\");
            String dirStr = sb.toString();
            sb.append(hm.format(new Date()));
            sb.append(".jpg");

            a.setFileURL(sb.toString());

//            showPic(screenshot);

//            FileUtils.copyFile(screenshot, new File(sb.toString()));

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(sb.toString());
            } catch (FileNotFoundException fileNotFoundException) {
                if (new File(dirStr).mkdir()) {
                    fos = new FileOutputStream(sb.toString());

                } else {
                    throw fileNotFoundException;
                }
            }
            InputStream in = new ByteArrayInputStream(screenshot);
            BufferedImage bImageFromConvert = ImageIO.read(in);         
            ImageIO.write(bImageFromConvert, "jpg", fos);
            
            
 //           fos.write(screenshot);


            return true;

        } catch (IOException ex) {
            Logger.getLogger(PressDocumentationView.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    
    }


    public void textExtraction(Article a) {



//        storePageExtractionCondition();
//        System.out.println("starting driver : " + a.getLink());
//        driver.get(a.getLink());

//        System.out.println("driver started");

        Newspaper newspaper = a.getPaper();



        List<String> parts = new ArrayList<String>();
        parts.add("header");
        parts.add("date");
        parts.add("preface");
        parts.add("body");


        for (String s : parts) {
            StringBuilder headerString = new StringBuilder();
            System.out.println("part : " + s);


            String descriptor = newspaper.getCondition(s);

            if (!descriptor.isEmpty()) {
                Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "element to find : " + descriptor);

                List<WebElement> lista = null;
                lista = driver.findElements(By.cssSelector(descriptor));


                for (Iterator<WebElement> it1 = lista.iterator(); it1.hasNext();) {
                    WebElement webElement = it1.next();

                    headerString.append(webElement.getText());
                }
            }



//            headerText = findByDescriptor(descriptor, i, n);
//            WebElement selectheaderText = findByDescriptor(descriptor, i, n);
            if (headerString.length() > 0) {

                Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "str to print : " + headerString.toString());
                if (s.equalsIgnoreCase("header")) {
                    a.setTitle(headerString.toString());
                    Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, s + " : " + headerString);
                } else if (s.equalsIgnoreCase("preface")) {
                    a.setPreface(headerString.toString());
                    Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, s + " : " + headerString);
                } else if (s.equalsIgnoreCase("date")) {
                    a.setDate(DateConverter.convert(headerString.toString()));
                    Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, s + " : " + headerString);
                } else if (s.equalsIgnoreCase("upleft-component")) {
//                    a.setPreface(headerString.toString());
                } else if (s.equalsIgnoreCase("downright-component")) {
//                    a.setPreface(headerString.toString());
                } else {
                    a.setBody(headerString.toString());
                    Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, s + " : " + headerString);

                }

            }

        }

        parts.clear();
        parts.add("upleft-component");
        parts.add("downright-component");

        for (String s : parts) {
            Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "part : " + s);


            String descriptor = newspaper.getCondition(s);

            Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "element to find : " + descriptor);
            
            WebElement findElement = driver.findElement(By.cssSelector(descriptor));
        
            Point p = findElement.getLocation();
            Dimension d = findElement.getSize();
            if (s.equalsIgnoreCase("upleft-component")) {
                a.getPaper().setScreenUpperLeftX(p.x);
                a.getPaper().setScreenUpperLeftY(p.y);
                int x = p.x;
                int y = p.y;
                Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "upleft x : " + x + " y : " + y);
            } else if (s.equalsIgnoreCase("downright-component")) {
                int x = p.x + d.width;
                int y = p.y + d.height;
                a.getPaper().setScreenLowerRightX(x);
                a.getPaper().setScreenLowerRightY(y);
                Logger.getLogger(DownloadScreenshotThread.class.getName()).log(Level.INFO, "downright x : " + x + " y : " + y);
            }               
           
        
            
        }    
        
    }    
    
    
}
