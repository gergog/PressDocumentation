/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pressdocumentation.doc;

import pressdocumentation.pojos.Article;

/**
 *
 * @author ETHGGY
 */
public abstract class DocumentCreation {

    public DocumentCreation() {
        
    }

    public void addArticle(Article a) {
        addTable(a);
        if (a.isPrintScreen()) {
            addImage(a);
        }
        addArticleBody(a);
    }

    protected void closeMontlyDocument() {
        
    }
    
    
    protected abstract void addArticleBody(Article a);

    protected abstract void addImage(Article a);

    protected abstract void addTable(Article a);

    public abstract void openDoc(String url);
    
}
