/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Dashboard;

/**
 *
 * @author Mi PC
 */
public class videoDash extends javax.swing.JPanel {

    /**
     * Creates new form videoDash
     */

    public videoDash() {
        initComponents();
        }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARN
     */
    // ==================== GETTERS PARA EL DASHBOARD ====================
    public javax.swing.JLabel getPlayBtn() {
        return bigplayBtn;
    }

    public javax.swing.JLabel getVideoName() {
        return videoName;
    }

    public javax.swing.JLabel getDescripTxt() {
        return descripTxt;
    }

    public javax.swing.JLabel getAsigName() {
        return asigName;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        bigvideoPanel = new javax.swing.JPanel();
        bigplayBtn = new javax.swing.JLabel();
        videoName = new javax.swing.JLabel();
        descripTxt = new javax.swing.JLabel();
        asigBtn = new javax.swing.JPanel();
        asigName = new javax.swing.JLabel();

        background.setBackground(new java.awt.Color(247, 247, 247));

        bigvideoPanel.setBackground(new java.awt.Color(92, 225, 230));

        bigplayBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bigplayBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/playBigIcon.png"))); // NOI18N

        javax.swing.GroupLayout bigvideoPanelLayout = new javax.swing.GroupLayout(bigvideoPanel);
        bigvideoPanel.setLayout(bigvideoPanelLayout);
        bigvideoPanelLayout.setHorizontalGroup(
            bigvideoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bigplayBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
        bigvideoPanelLayout.setVerticalGroup(
            bigvideoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bigvideoPanelLayout.createSequentialGroup()
                .addComponent(bigplayBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        videoName.setFont(new java.awt.Font("Poppins SemiBold", 0, 26)); // NOI18N
        videoName.setForeground(new java.awt.Color(0, 0, 0));
        videoName.setText("Título");

        descripTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        descripTxt.setForeground(new java.awt.Color(0, 0, 0));
        descripTxt.setText("Descripción...");

        asigBtn.setBackground(new java.awt.Color(221, 224, 229));

        asigName.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        asigName.setForeground(new java.awt.Color(145, 145, 145));
        asigName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        asigName.setText("Asignatura");

        javax.swing.GroupLayout asigBtnLayout = new javax.swing.GroupLayout(asigBtn);
        asigBtn.setLayout(asigBtnLayout);
        asigBtnLayout.setHorizontalGroup(
            asigBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(asigName, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        asigBtnLayout.setVerticalGroup(
            asigBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(asigName, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(bigvideoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(videoName, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(asigBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(descripTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(bigvideoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(videoName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(asigBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(descripTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel asigBtn;
    private javax.swing.JLabel asigName;
    private javax.swing.JPanel background;
    private javax.swing.JLabel bigplayBtn;
    private javax.swing.JPanel bigvideoPanel;
    private javax.swing.JLabel descripTxt;
    private javax.swing.JLabel videoName;
    // End of variables declaration//GEN-END:variables
}
