package Materia;

import Materia.Asignatura;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class vistaPrevia extends JPanel {

    private final Asignatura asignatura;
    private boolean seleccionada = false;
    private Runnable onClickeador;
    private Runnable onVolverMenu;

    public vistaPrevia() {
        this(new Asignatura("0", "Sin nombre", "Sin descripción"));
    }

    public vistaPrevia(Asignatura asignatura) {
        this(asignatura, null);
    }

    public vistaPrevia(Asignatura asignatura, Runnable onClickeador) {
        this.asignatura = asignatura;
        this.onClickeador = onClickeador;

        initComponents();

        asigName.setText(asignatura.getNombre());
        descrpTxt.setText(asignatura.getDescripcion());

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionada = !seleccionada;
                setBorder(seleccionada
                        ? BorderFactory.createLineBorder(new Color(4, 174, 178), 2)
                        : BorderFactory.createEmptyBorder());
            }
        });

        accTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onClickeador != null) {
                    onClickeador.run();
                }
            }
        });
    }

    // 🔥 NUEVO MÉTODO: Obtener ID único de la materia
    public String getMateriaId() {
        return asignatura.getId();
    }

    public void setOnAcceder(Runnable r) {
        this.onClickeador = r;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    // ⚠️ No escribas initComponents() manualmente si usas el diseñador
    // Asegúrate de que el .form tenga:
    // - JLabel llamado asigName
    // - JTextArea llamado descrpTxt
    // - JLabel llamado accTxt
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        img = new javax.swing.JPanel();
        asigName = new javax.swing.JLabel();
        descrpTxt = new javax.swing.JLabel();
        accBtn = new javax.swing.JPanel();
        accTxt = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(247, 247, 247));
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

        asigName.setFont(new java.awt.Font("Poppins", 1, 25)); // NOI18N
        asigName.setForeground(new java.awt.Color(0, 0, 0));
        asigName.setText("Asignaturra");
        background.add(asigName, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 17, -1, -1));

        descrpTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        descrpTxt.setForeground(new java.awt.Color(0, 0, 0));
        descrpTxt.setText("Descripción");
        background.add(descrpTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 62, 85, -1));

        accBtn.setBackground(new java.awt.Color(64, 174, 178));

        accTxt.setFont(new java.awt.Font("Questrial", 0, 18)); // NOI18N
        accTxt.setForeground(new java.awt.Color(255, 255, 255));
        accTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        accTxt.setText("Acceder");
        accTxt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                accTxtMouseMoved(evt);
            }
        });
        accTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accTxtMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout accBtnLayout = new javax.swing.GroupLayout(accBtn);
        accBtn.setLayout(accBtnLayout);
        accBtnLayout.setHorizontalGroup(
            accBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(accTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
        );
        accBtnLayout.setVerticalGroup(
            accBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(accTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        background.add(accBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(824, 126, -1, -1));

        add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 180));
    }// </editor-fold>//GEN-END:initComponents

    private void accTxtMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accTxtMouseMoved
        accBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_accTxtMouseMoved

    private void accTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accTxtMouseExited
        accBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_accTxtMouseExited

    private void accTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accTxtMouseClicked
        if (onClickeador != null) {
            onClickeador.run();
        }

    }//GEN-LAST:event_accTxtMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accBtn;
    private javax.swing.JLabel accTxt;
    private javax.swing.JLabel asigName;
    private javax.swing.JPanel background;
    private javax.swing.JLabel descrpTxt;
    private javax.swing.JPanel img;
    // End of variables declaration//GEN-END:variables
}
