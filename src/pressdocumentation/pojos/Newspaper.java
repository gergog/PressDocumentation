/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ETHGGY
 */
public class Newspaper {
    
    
    private String link;
    private String name;
    private String media;
    private String type;
    
    // image
    private int screenUpperLeftX = -1;
    private int screenUpperLeftY = -1;
    private int screenLowerRightX = -1;
    private int screenLowerRightY = -1;
    private String exampleImageURL;
    
    private HashMap<String, String> parts;

    public Newspaper() {
        parts = new HashMap<String, String>();
        
        
    }
    
    public void resetDescriptors() {

        parts.clear();
        
    }
    
    
    
    
    public String getCondition(String type) {
        
        if (parts.get(type) == null) {
            return null;
        }
        
        return parts.get(type);
        
    }
    
    
    public void addCondition(String type, String value) {
        
        parts.put(type, value);
       
        
    }
    
    
    
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int getScreenLowerRightX() {
        return screenLowerRightX;
    }

    public void setScreenLowerRightX(int screenLowerRightX) {
        this.screenLowerRightX = screenLowerRightX;
    }

    public int getScreenLowerRightY() {
        return screenLowerRightY;
    }

    public void setScreenLowerRightY(int screenLowerRightY) {
        this.screenLowerRightY = screenLowerRightY;
    }

    public int getScreenUpperLeftX() {
        return screenUpperLeftX;
    }

    public void setScreenUpperLeftX(int screenUpperLeftX) {
        this.screenUpperLeftX = screenUpperLeftX;
    }

    public int getScreenUpperLeftY() {
        return screenUpperLeftY;
    }

    public void setScreenUpperLeftY(int screenUpperLeftY) {
        this.screenUpperLeftY = screenUpperLeftY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
