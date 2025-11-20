package Materia;

import back.Session;
import java.awt.Color;
import javax.swing.JOptionPane;
import main.DBConnection;


public class registrarAsig extends javax.swing.JFrame {

    public registrarAsig() {
        initComponents();

    }

    private Asignatura resultado;

    public Asignatura getResultado() {
        return resultado;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        img = new javax.swing.JPanel();
        accBtn = new javax.swing.JPanel();
        regisABtn = new javax.swing.JLabel();
        asigName = new javax.swing.JTextField();
        descrpTxt = new javax.swing.JTextField();
        IDAsig = new javax.swing.JTextField();
        exit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(980, 180));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(247, 247, 247));
        background.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img.setBackground(new java.awt.Color(64, 174, 178));

        javax.swing.GroupLayout imgLayout = new javax.swing.GroupLayout(img);
        img.setLayout(imgLayout);
        imgLayout.setHorizontalGroup(
            imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );
        imgLayout.setVerticalGroup(
            imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
        );

        background.add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 17, -1, -1));

        accBtn.setBackground(new java.awt.Color(64, 174, 178));

        regisABtn.setFont(new java.awt.Font("Questrial", 0, 18)); // NOI18N
        regisABtn.setForeground(new java.awt.Color(255, 255, 255));
        regisABtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        regisABtn.setText("Registrar");
        regisABtn.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                regisABtnMouseMoved(evt);
            }
        });
        regisABtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regisABtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                regisABtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                regisABtnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout accBtnLayout = new javax.swing.GroupLayout(accBtn);
        accBtn.setLayout(accBtnLayout);
        accBtnLayout.setHorizontalGroup(
            accBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(regisABtn, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );
        accBtnLayout.setVerticalGroup(
            accBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(regisABtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        background.add(accBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(833, 119, -1, -1));

        asigName.setBackground(new java.awt.Color(247, 247, 247));
        asigName.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        asigName.setForeground(new java.awt.Color(145, 145, 145));
        asigName.setText("Asignatura");
        asigName.setBorder(null);
        asigName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                asigNameMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asigNameMousePressed(evt);
            }
        });
        background.add(asigName, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 214, 33));

        descrpTxt.setBackground(new java.awt.Color(247, 247, 247));
        descrpTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        descrpTxt.setForeground(new java.awt.Color(145, 145, 145));
        descrpTxt.setText("Descripción");
        descrpTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                descrpTxtMousePressed(evt);
            }
        });
        background.add(descrpTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 59, 410, 108));

        IDAsig.setBackground(new java.awt.Color(247, 247, 247));
        IDAsig.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        IDAsig.setForeground(new java.awt.Color(145, 145, 145));
        IDAsig.setText("ID de asignatura");
        IDAsig.setBorder(null);
        IDAsig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                IDAsigMousePressed(evt);
            }
        });
        background.add(IDAsig, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 20, 190, 33));

        exit.setFont(new java.awt.Font("Questrial", 0, 24)); // NOI18N
        exit.setForeground(new java.awt.Color(0, 0, 0));
        exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exit.setText("x");
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitMouseClicked(evt);
            }
        });
        background.add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(952, 1, 47, 46));

        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 180));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void regisABtnMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regisABtnMouseMoved
        accBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_regisABtnMouseMoved

    private void regisABtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regisABtnMouseExited
        accBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_regisABtnMouseExited

    private void regisABtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regisABtnMouseClicked
        String id = IDAsig.getText().trim();
        String nombre = asigName.getText().trim();
        String descripcion = descrpTxt.getText().trim();

      
        if (id.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        String idDocente = Session.getUsuario().getId();

        resultado = DBConnection.registrarAsignaturaEnBD(id, nombre, descripcion);

        if (resultado == null) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la asignatura.");
            return;
        }

        DBConnection.guardarAsignaturaDocente(idDocente, nombre, descripcion);

        JOptionPane.showMessageDialog(this, "Asignatura registrada y asociada al docente correctamente.");

        this.dispose();

    }//GEN-LAST:event_regisABtnMouseClicked

    private void regisABtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regisABtnMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_regisABtnMouseEntered

    private void exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseClicked
        this.dispose();
    }//GEN-LAST:event_exitMouseClicked

    private void asigNameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asigNameMousePressed
        if (asigName.getText().equals("Asignatura")) {
            asigName.setText("");
        }
    }//GEN-LAST:event_asigNameMousePressed

    private void IDAsigMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IDAsigMousePressed
        if (IDAsig.getText().equals("ID de asignatura")) {
            IDAsig.setText("");
        }
    }//GEN-LAST:event_IDAsigMousePressed

    private void descrpTxtMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descrpTxtMousePressed
        if (descrpTxt.getText().equals("Descripción")) {
            descrpTxt.setText("");
        }
    }//GEN-LAST:event_descrpTxtMousePressed

    private void asigNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asigNameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_asigNameMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(registrarAsig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(registrarAsig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(registrarAsig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(registrarAsig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new registrarAsig().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDAsig;
    private javax.swing.JPanel accBtn;
    private javax.swing.JTextField asigName;
    private javax.swing.JPanel background;
    private javax.swing.JTextField descrpTxt;
    private javax.swing.JLabel exit;
    private javax.swing.JPanel img;
    private javax.swing.JLabel regisABtn;
    // End of variables declaration//GEN-END:variables
}
