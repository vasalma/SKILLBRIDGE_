package frontEs;

import back.Session;
import back.Usuario;
import front.login;
import back.Actualizable;
import java.awt.Desktop;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import main.DBConnection;
import Dashboard.actDash;
//

public class dashboard extends javax.swing.JFrame implements Actualizable {

    public dashboard() {
        initComponents();
        System.out.println("📌 Constructor dashboard iniciado");

        try {
            System.out.println("✔ initComponents ejecutado");

            cargarUsuario();
            System.out.println("✔ cargarUsuario ejecutado");

            cargarVideosRecientes();
            System.out.println("✔ cargarVideosRecientes ejecutado");

            cargarActividadesRecientes();  // ← Corregida para rutas absolutas
            System.out.println("✔ cargarActividadesRecientes ejecutado");

            System.out.println("✔ Mostrando ventana del dashboard...");
            this.setVisible(true);

            System.out.println("✅ Dashboard completamente cargado");

        } catch (Exception e) {
            System.out.println("❌ ERROR dentro del constructor del dashboard:");
            e.printStackTrace();
        }
    }

    private void cargarUsuario() {
        Usuario u = Session.getUsuario();

        if (u != null) {
            userName.setText(u.getNombre() + " " + u.getApellido());
        } else {
            userName.setText("Usuario");
        }

    }

    private void cargarActividadesRecientes() {
        System.out.println("📌 Ejecutando cargarActividadesRecientes()...");

        actRecientesExcel.removeAll();

        final String rutaBase = System.getProperty("user.dir");
        System.out.println("Ruta Base del Proyecto (CWD): " + rutaBase);

        List<String[]> lista = DBConnection.obtenerActividadesRecientes();

        for (String[] a : lista) {

            String titulo = a[0];
            String nombreDocente = a[1];
            String materia = a[2];
            String rutaRelativa = a[3];

            final Dashboard.actDash item = new Dashboard.actDash();

            item.setActividadData(titulo, nombreDocente, materia, rutaRelativa);

            item.getDownloadBtn1().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    String rutaDB = item.getActividadURL();

                    String rutaAjustada = rutaDB
                            .replace('/', File.separatorChar)
                            .replace('\\', File.separatorChar);

                    String rutaCompleta = rutaBase + File.separator + rutaAjustada;

                    File archivo = new File(rutaCompleta);

                    if (!archivo.exists()) {
                        JOptionPane.showMessageDialog(null,
                                "❌ Archivo NO encontrado en la ruta:\n" + archivo.getAbsolutePath()
                                + "\nVerifique que la carpeta 'uploads' esté en la raíz del proyecto.");
                        return;
                    }

                    try {
                        System.out.println("⬇ Abriendo archivo: " + archivo.getAbsolutePath());
                        Desktop.getDesktop().open(archivo);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al abrir archivo. Asegúrese de tener un programa asociado (PDF, Word, etc.).");
                        ex.printStackTrace();
                    }
                }
            });

            actRecientesExcel.add(item);
        }

        actRecientesExcel.revalidate();
        actRecientesExcel.repaint();
        System.out.println("✅ " + lista.size() + " actividades cargadas.");
    }

    private void cargarVideosRecientes() {

        System.out.println("📌 Ejecutando cargarVideosRecientes()...");

        vidRecientes.removeAll();
        vidRecientes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));

        List<String[]> lista = DBConnection.obtenerVideosRecientes();

        for (String[] v : lista) {

            String titulo = v[0];
            String descripcion = v[1];
            String asignatura = v[2];
            String rutaEnBD = v[3];

            File f = new File(rutaEnBD);
            final String rutaFinal = f.getAbsolutePath();

            Dashboard.videoDash item = new Dashboard.videoDash();
            item.setVideoData(titulo, descripcion, asignatura, rutaFinal);

            item.getPlayBtn().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    File videoFile = new File(rutaFinal);

                    if (!videoFile.exists()) {
                        JOptionPane.showMessageDialog(null,
                                "No se encontró el archivo del video:\n" + rutaFinal);
                        return;
                    }

                    try {
                        System.out.println("▶ Abriendo video: " + videoFile.getAbsolutePath());
                        Desktop.getDesktop().open(videoFile);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "No fue posible abrir el video.");
                        ex.printStackTrace();
                    }
                }
            });

            vidRecientes.add(item);
        }

        vidRecientes.revalidate();
        vidRecientes.repaint();
    }

    public void actualizarNombreEnUI() {

        Usuario u = back.Session.getUsuario();

        if (u != null) {

            userName.setText(u.getNombre() + " " + u.getApellido());
        } else {
            userName.setText("Usuario");
        }
        this.revalidate();
        this.repaint();
        System.out.println("✅ Dashboard: Nombre de usuario recargado.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainCont = new javax.swing.JPanel();
        actsHead = new javax.swing.JLabel();
        videosHead = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vidRecientes = new javax.swing.JPanel();
        actRecientes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        actRecientesExcel = new javax.swing.JPanel();
        actRecientesHeader = new javax.swing.JPanel();
        actividad = new javax.swing.JLabel();
        asignatura = new javax.swing.JLabel();
        docente = new javax.swing.JLabel();
        menuBar = new javax.swing.JPanel();
        appName = new javax.swing.JLabel();
        dashBtn = new javax.swing.JPanel();
        dashTxt = new javax.swing.JLabel();
        dashIcon = new javax.swing.JLabel();
        coursesBtn = new javax.swing.JPanel();
        coursesTxt = new javax.swing.JLabel();
        coursesIcon = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JPanel();
        logoutTxt = new javax.swing.JLabel();
        logoutIcon = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        profilePic = new javax.swing.JLabel();
        userName = new javax.swing.JLabel();
        configArrow = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainCont.setBackground(new java.awt.Color(255, 255, 255));
        mainCont.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actsHead.setFont(new java.awt.Font("Questrial", 0, 35)); // NOI18N
        actsHead.setForeground(new java.awt.Color(0, 0, 0));
        actsHead.setText("Actividades recientes");
        mainCont.add(actsHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        videosHead.setFont(new java.awt.Font("Questrial", 0, 35)); // NOI18N
        videosHead.setForeground(new java.awt.Color(0, 0, 0));
        videosHead.setText("Seguir viendo...");
        mainCont.add(videosHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        vidRecientes.setBackground(new java.awt.Color(255, 255, 255));
        vidRecientes.setLayout(new javax.swing.BoxLayout(vidRecientes, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPane1.setViewportView(vidRecientes);

        mainCont.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 33, 980, 280));

        actRecientes.setBackground(new java.awt.Color(255, 255, 255));
        actRecientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        actRecientesExcel.setBackground(new java.awt.Color(255, 255, 255));
        actRecientesExcel.setLayout(new javax.swing.BoxLayout(actRecientesExcel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(actRecientesExcel);

        actRecientes.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 36, 990, 211));

        actRecientesHeader.setBackground(new java.awt.Color(255, 255, 255));
        actRecientesHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actividad.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        actividad.setForeground(new java.awt.Color(145, 145, 145));
        actividad.setText("Actividad");
        actRecientesHeader.add(actividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 30));

        asignatura.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        asignatura.setForeground(new java.awt.Color(145, 145, 145));
        asignatura.setText("Asignatura");
        actRecientesHeader.add(asignatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, -1, 30));

        docente.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        docente.setForeground(new java.awt.Color(145, 145, 145));
        docente.setText("Docente/Monitor");
        actRecientesHeader.add(docente, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, -1, 30));

        actRecientes.add(actRecientesHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 30));

        mainCont.add(actRecientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 380, -1, -1));

        getContentPane().add(mainCont, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 1010, 630));

        menuBar.setBackground(new java.awt.Color(255, 255, 255));
        menuBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        appName.setFont(new java.awt.Font("Questrial", 0, 30)); // NOI18N
        appName.setForeground(new java.awt.Color(0, 0, 0));
        appName.setText("Skillbridge");
        menuBar.add(appName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        dashBtn.setBackground(new java.awt.Color(255, 255, 255));
        dashBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashTxt.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        dashTxt.setForeground(new java.awt.Color(4, 174, 178));
        dashTxt.setText("Dashboard");
        dashBtn.add(dashTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 80, -1));

        dashIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/houseGicon.png"))); // NOI18N
        dashBtn.add(dashIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        menuBar.add(dashBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 260, 40));

        coursesBtn.setBackground(new java.awt.Color(255, 255, 255));
        coursesBtn.setForeground(new java.awt.Color(0, 0, 0));
        coursesBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                coursesBtnMouseClicked(evt);
            }
        });
        coursesBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        coursesTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        coursesTxt.setForeground(new java.awt.Color(0, 0, 0));
        coursesTxt.setText("Cursos");
        coursesBtn.add(coursesTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 50, -1));

        coursesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hatBicon.png"))); // NOI18N
        coursesBtn.add(coursesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        menuBar.add(coursesBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 260, 40));

        logoutBtn.setBackground(new java.awt.Color(255, 255, 255));
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutBtnMouseClicked(evt);
            }
        });
        logoutBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutTxt.setFont(new java.awt.Font("Open Sans", 0, 11)); // NOI18N
        logoutTxt.setForeground(new java.awt.Color(0, 0, 0));
        logoutTxt.setText("Log out");
        logoutBtn.add(logoutTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 13, -1, -1));

        logoutIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        logoutBtn.add(logoutIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        menuBar.add(logoutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 260, 40));

        getContentPane().add(menuBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 710));

        header.setBackground(new java.awt.Color(255, 255, 255));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        profilePic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/men.png"))); // NOI18N
        header.add(profilePic, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 20, -1, -1));

        userName.setFont(new java.awt.Font("Questrial", 0, 12)); // NOI18N
        userName.setForeground(new java.awt.Color(0, 0, 0));
        userName.setText("Martin Berrio");
        header.add(userName, new org.netbeans.lib.awtextra.AbsoluteConstraints(883, 36, 70, -1));

        configArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrowProfile.png"))); // NOI18N
        header.add(configArrow, new org.netbeans.lib.awtextra.AbsoluteConstraints(946, 34, 40, 20));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        header.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 20, 150, 40));

        getContentPane().add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 1010, 80));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void coursesBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_coursesBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        cursosDash nuevaventana = new cursosDash();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_coursesBtnMouseClicked

    private void logoutBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtnMouseClicked

        this.dispose();

        login nuevaventana = new login();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_logoutBtnMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

        profile nuevaventana = new profile(this);
        nuevaventana.setVisible(true);

        this.setVisible(false);

    }//GEN-LAST:event_jPanel1MouseClicked

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
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new dashboard().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actRecientes;
    private javax.swing.JPanel actRecientesExcel;
    private javax.swing.JPanel actRecientesHeader;
    private javax.swing.JLabel actividad;
    private javax.swing.JLabel actsHead;
    private javax.swing.JLabel appName;
    private javax.swing.JLabel asignatura;
    private javax.swing.JLabel configArrow;
    private javax.swing.JPanel coursesBtn;
    private javax.swing.JLabel coursesIcon;
    private javax.swing.JLabel coursesTxt;
    private javax.swing.JPanel dashBtn;
    private javax.swing.JLabel dashIcon;
    private javax.swing.JLabel dashTxt;
    private javax.swing.JLabel docente;
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel logoutBtn;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel logoutTxt;
    private javax.swing.JPanel mainCont;
    private javax.swing.JPanel menuBar;
    private javax.swing.JLabel profilePic;
    private javax.swing.JLabel userName;
    private javax.swing.JPanel vidRecientes;
    private javax.swing.JLabel videosHead;
    // End of variables declaration//GEN-END:variables

}
