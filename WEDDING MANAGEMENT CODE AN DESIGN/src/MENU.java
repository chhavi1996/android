/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MENU.java
 *
 * Created on Dec 22, 2013, 7:24:59 PM
 */

/**
 *
 * @author ATULE
 */
public class MENU extends javax.swing.JFrame {

    /** Creates new form MENU */
    public MENU() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "WHAT DO YOU WANT", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Brandy", 3, 60), new java.awt.Color(51, 51, 255))); // NOI18N
        jPanel2.setOpaque(false);

        jRadioButton1.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton1.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton1.setText("ENTRY FORM");
        jRadioButton1.setOpaque(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton1);

        jRadioButton2.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton2.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton2.setText("ENTER DETAILS");
        jRadioButton2.setOpaque(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton2);

        jRadioButton3.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton3.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton3.setText("SEARCH");
        jRadioButton3.setOpaque(false);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton3);

        jRadioButton6.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton6.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton6.setText(" SEE BIODATA");
        jRadioButton6.setOpaque(false);
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton6);

        jRadioButton5.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton5.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton5.setText("WEDDING SHOPPING");
        jRadioButton5.setOpaque(false);
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton5);

        jRadioButton4.setFont(new java.awt.Font("Backslash", 3, 48));
        jRadioButton4.setForeground(new java.awt.Color(255, 51, 51));
        jRadioButton4.setText("UPDATE");
        jRadioButton4.setOpaque(false);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jRadioButton4);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 590, 530));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 620));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/4.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 0, 1020, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
          ENTRYFORM E=new ENTRYFORM ();
        E.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
         DETAILS D=new  DETAILS();
        D.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
         SEARCH S=new SEARCH ();
        S.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
         BIODATA B=new BIODATA ();
        B.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
         CHOICE C=new CHOICE ();
        C.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
         UPDATE U=new UPDATE ();
        U.setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_jRadioButton4ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MENU().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    // End of variables declaration//GEN-END:variables

}