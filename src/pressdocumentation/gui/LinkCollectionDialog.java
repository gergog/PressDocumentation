/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LinkCollectionDialog.java
 *
 * Created on May 26, 2012, 2:22:44 PM
 */
package pressdocumentation.gui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;

/**
 *
 * @author ETHGGY
 */
public class LinkCollectionDialog extends javax.swing.JDialog {

    private CollectionDialogCallback callback;
    
    /** Creates new form LinkCollectionDialog */
    public LinkCollectionDialog(java.awt.Frame parent, CollectionDialogCallback callback, boolean modal) {
        super(parent, modal);
    
        initComponents();
        
        this.callback = callback;
        
        jTable1.setDragEnabled(true);
  
        jTable1.setDropTarget(new DropTarget() {

            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                try {
                    Point point = dtde.getLocation();
                    int column = jTable1.columnAtPoint(point);
                    int row = jTable1.rowAtPoint(point);
                    
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable t = dtde.getTransferable();
                    String s = (String) t.getTransferData(DataFlavor.stringFlavor);

                    addLink(s, row);
                    
//                    super.drop(dtde);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        });
        jScrollPane1.setDropTarget(new DropTarget(){

            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable t = dtde.getTransferable();
                    String s = (String) t.getTransferData(DataFlavor.stringFlavor);

                    addLink(s, jTable1.getRowCount());
                    
                    
//                    super.drop(dtde);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        /*
        TransferHandler th  = new TransferHandler() {

            
            
            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY;
            }
            
            @Override
            public boolean canImport(TransferSupport support) {
                
                // we only import Strings
                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    return false;
                } else {
                    try {
                        String s = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                        
                        URL u = new URL(s);
                        
                    }  catch (MalformedURLException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (UnsupportedFlavorException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (IOException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                    
                    return true;
                }
            }
                
            public boolean importData(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) {
                    return false;
                }

                
                // Check for String flavor
                if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                    displayDropLocation("Table doesn't accept a drop of this type.");
                    System.out.println("Not supported");
                    return false;
                }

                JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
                int index = dl.getRow();
                boolean insert = dl.isInsertRow();
                // Get the current string under the drop.
//                String value = (String) tableModel.getElementAt(index);
                
                // Get the string that is being dropped.
                Transferable t = info.getTransferable();
                String data;
                try {
                    data = (String) t.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    return false;
                }
                

                
                if (insert) {
                    try {
//                        URL secondURL = new URL(data);
                        URL url = new URL(data);
                        HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
                        ucon.setInstanceFollowRedirects(false);
                        URL secondURL = new URL(ucon.getHeaderField("Location"));
                        URLConnection conn = secondURL.openConnection();                    

                        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        
                        if (tableModel.getRowCount() == 1) {
//                            if (true) {
                            if ((tableModel.getValueAt(0, 0) ==  null)) {
                                tableModel.setValueAt(secondURL.toString(),0, 0);
                                
                            } else {
                                tableModel.insertRow(index,new Object[] {secondURL.toString(), false});
                                
                            }
                            
                            
                        } else {
                                tableModel.insertRow(index,new Object[] {secondURL.toString(), false});
                        
                        }

                                               
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                } else {
//                    tableModel.set(index, data);
                }
                return true;
                 
            }
                
            
            
        };
        jTable1.setTransferHandler(th);
         * 
         */
        jTable1.setDropMode(DropMode.INSERT_ROWS);
        

        
        
    }

    public List<URL> getURLs() {
        
        List<URL> l = new ArrayList<URL>();
        
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        
        int length = tableModel.getRowCount();
        
        for (int i = 0; i < length; i++) {
            try {
                l.add(new URL((String)tableModel.getValueAt(i, 0)));
            } catch (MalformedURLException ex) {
                Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        return  l;
        
    }
    
    private void addLink(String s, int row) {
        try {
            
            URL url = new URL(s);
            HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
            ucon.setInstanceFollowRedirects(false);
            URL secondURL = new URL(ucon.getHeaderField("Location"));
            URLConnection conn = secondURL.openConnection();                    

            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

            tableModel.insertRow(row,new Object[] {secondURL.toString(), false});
        } catch (MalformedURLException ex) {
            Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    
    /*
    class LinkCollectionDnDHandler extends DropTargetAdapter {

        public void drop(DropTargetDropEvent dtde) {
                // Check for String flavor

            if (!dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                    displayDropLocation("Table doesn't accept a drop of this type.");
                    System.out.println("Not supported");

                    return;
            }
                

                JTable.DropLocation dl = (JTable.DropLocation) dtde.get;
                int index = dl.getRow();
                boolean insert = dl.isInsertRow();
                // Get the current string under the drop.
//                String value = (String) tableModel.getElementAt(index);
                
                // Get the string that is being dropped.
                Transferable t = info.getTransferable();
                String data;
                try {
                    data = (String) t.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    return false;
                }
                
      
                
                if (insert) {
                    try {
                        URL url = new URL(data);
                        URL secondURL = new URL(data);
//                        HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
//                        ucon.setInstanceFollowRedirects(false);
//                        URL secondURL = new URL(ucon.getHeaderField("Location"));
//                        URLConnection conn = secondURL.openConnection();                    

                        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        
                        if (tableModel.getRowCount() == 1) {
//                            if (true) {
                            if ((tableModel.getValueAt(0, 0) ==  null)) {
                                tableModel.setValueAt(secondURL.toString(),0, 0);
                                
                            } else {
                                tableModel.insertRow(index,new Object[] {secondURL.toString(), false});
                                
                            }
                            
                            
                        } else {
                                tableModel.insertRow(index,new Object[] {secondURL.toString(), false});
                        
                        }

                                               
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(LinkCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                } else {
//                    tableModel.set(index, data);
                }
        }
        
    }
    
*/
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pressdocumentation.PressDocumentationApp.class).getContext().getActionMap(LinkCollectionDialog.class, this);
        jButton1.setAction(actionMap.get("changeBack")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pressdocumentation.PressDocumentationApp.class).getContext().getResourceMap(LinkCollectionDialog.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Link"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void changeBack() {
        callback.hideArticleCollectionDialog();
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
