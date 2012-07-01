/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import pressdocumentation.pojos.Article;
import pressdocumentation.pojos.Newspaper;
import pressdocumentation.pojos.PageElementDescriptor;

/**
 *
 * @author ethggy
 */
public class DownloadTextThread extends Thread {

    private ArrayList<Article> articleList;
    private volatile boolean downloads = true;
    private volatile boolean stopped = false;
//    private HtmlUnitDriver driver;
    private ChromeDriver driver;

    public DownloadTextThread() {
        articleList = new ArrayList<Article>();




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
//                    driver = new HtmlUnitDriver();
                    driver = new ChromeDriver();
                }

                a = null;

                synchronized (articleList) {
                    if (articleList.size() > 0) {
                        a = articleList.remove(0);
                    }
                }


                if (a != null) {

                    textExtraction(a);
                    ArticleHandler ah = ArticleHandler.createInstance();
                    ah.notifyListeners(ArticleListener.TEXT_EXTRACTED, a);

                }
            }


        }



        driver.quit();

        stopped = true;


    }

    public void textExtraction(Article a) {



//        storePageExtractionCondition();
        System.out.println("starting driver : " + a.getLink());
        driver.get(a.getLink());

        System.out.println("driver started");

        Newspaper newspaper = a.getPaper();



        List<String> parts = new ArrayList<String>();
        parts.add("header");
        parts.add("date");
        parts.add("preface");
        parts.add("body");
        parts.add("upleft-component");
        parts.add("downright-component");


        for (String s : parts) {
            StringBuilder headerString = new StringBuilder();
            System.out.println("part : " + s);


            String descriptor = newspaper.getCondition(s);


            System.out.println("element to find : " + descriptor);

            List<WebElement> lista = null;
            lista = driver.findElements(By.tagName(descriptor));


            for (Iterator<WebElement> it1 = lista.iterator(); it1.hasNext();) {
                WebElement webElement = it1.next();

                headerString.append(webElement.getText());
            }




//            headerText = findByDescriptor(descriptor, i, n);
//            WebElement selectheaderText = findByDescriptor(descriptor, i, n);
            if (headerString.length() > 0) {

                System.out.println("str to print : " + headerString.toString());
                if (s.equalsIgnoreCase("header")) {
                    a.setTitle(headerString.toString());
                } else if (s.equalsIgnoreCase("preface")) {
                    a.setPreface(headerString.toString());
                } else if (s.equalsIgnoreCase("date")) {
//                    a.setPreface(headerString.toString());
                } else if (s.equalsIgnoreCase("upleft-component")) {
//                    a.setPreface(headerString.toString());
                } else if (s.equalsIgnoreCase("downright-component")) {
//                    a.setPreface(headerString.toString());
                } else {
                    a.setBody(headerString.toString());

                }

            }

        }

    }
}
