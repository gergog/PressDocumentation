/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.pojos;

import java.util.Date;

/**
 *
 * @author ETHGGY
 */
public class Article {

    
    private int status = 0;
    
    private String link;
    private String title = null;
    private String preface = null;
    private String body = null;
    private Date date = null;
    
    private String announcement;
    private Newspaper paper;
    private boolean printScreen;
    private String fileURL = null;
    private String cropURL = null;

    
    public boolean allDataCollected() {
        return true;
    }
    
    public void removeStatus(int s) {
        status = (status & ~s);
    }
    
    public void setStatus(int s) {
        status = status | s;
    }
    
    public boolean isStatusValid(int s) {
        return ((status & s) == s);
    }
    
    public String getCropURL() {
        return cropURL;
    }

    public void setCropURL(String cropURL) {
        this.cropURL = cropURL;
    }

    
    
    
    public String getFileURL() {
        return fileURL;
    }


    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    
    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Newspaper getPaper() {
        return paper;
    }

    public void setPaper(Newspaper paper) {
        this.paper = paper;
    }

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public boolean isPrintScreen() {
        return printScreen;
    }

    public void setPrintScreen(boolean printScreen) {
        this.printScreen = printScreen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    
    
}
