/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PagePartWidget.java
 *
 * Created on May 19, 2012, 11:09:11 AM
 */
package pressdocumentation.gui;

import pressdocumentation.gui.WorkArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author ETHGGY
 */
public class PagePartWidget extends javax.swing.JPanel {

    /** Creates new form PagePartWidget */
    public PagePartWidget(String type) {
        this.type = type;

        initComponents();
        
        initWidgets();
        
        
        
    }

    
    private String type;
    private JLabel label;
    private JButton button;
    private JTextField jTextField1;
    
    
    private void initWidgets() {
        
        System.out.println("PagePartWidget::initWidgets : " + getType());
        if (label == null) {
            label = new javax.swing.JLabel();
        }
        if (jTextField1 == null) {
            jTextField1 = new javax.swing.JTextField();
        }
        
        if (button == null) {
            button = new javax.swing.JButton();
        }

        
        
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pressdocumentation.PressDocumentationApp.class).getContext().getResourceMap(WorkArea.class);
        
        
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pressdocumentation.PressDocumentationApp.class).getContext().getActionMap(PagePartWidget.class, this);
        java.awt.GridBagConstraints gridBagConstraints;
        
        label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label.setText(resourceMap.getString(type + "Label.text")); // NOI18N
        label.setName("Label"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        add(label, gridBagConstraints);

        jTextField1.setName("jTextField1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        add(jTextField1, gridBagConstraints);
        
        
        
        button.setAction(actionMap.get("check")); // NOI18N
        button.setText(resourceMap.getString("button.text")); // NOI18N
        button.setName("button"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        add(button, gridBagConstraints);
        
        
        invalidate();
        updateUI();
        
        
        
        
        
    }
    
    
    public void setPageElementDescriptor(String descriptor) {
        jTextField1.setText(descriptor);
                    
        
        
    }
    

    public String getType() {
        return type;
    }
    
    
    
    public void reset() {

        
        jTextField1.setText("");
    }
    
        
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public String getText() {
        return jTextField1.getText(); 
    }
}
