/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.pojos;

/**
 *
 * @author ETHGGY
 */
public class PageElementDescriptor {
    private String tag;
    private String attribute;
    private String value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
    
}
