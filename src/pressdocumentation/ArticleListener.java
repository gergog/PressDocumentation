/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation;

import pressdocumentation.pojos.Article;

/**
 *
 * @author ETHGGY
 */
public interface ArticleListener {
    public void articleModified(int type, Article a);
    
    public static final int ARTICLE_CREATED = 1;
    public static final int NEWSPAPER_MODIFIED = 2;
    public static final int IMAGECROP_FIELD_DETERMINED = 4;
    public static final int IMAGE_CROPPED = 8;
    public static final int TEXT_EXTRACTED = 16;
    public static final int ARTICLE_REMOVED = 32;
    public static final int IMAGE_DOWNLOADED = 64;
    public static final int TEXT_EXTRACTION_DATA_PROVIDED = 128;
    public static final int TEXT_EXTRACTION_TO_MAKE = 256;
    
    
}
