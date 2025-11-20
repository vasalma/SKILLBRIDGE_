/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frontMon;

import Cursos.panelCurso;
import Cursos.panelNotif;
import Materia.Asignatura;
import back.Session;
import back.Usuario;

import front.login;
import frontEs.profile;
import frontEs.dashboard;
import javax.swing.UIManager;
import back.Actualizable;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JPanel;
import main.DBConnection;

/**
 *
 * @author Mi PC
 */
public class cursosDashMon extends javax.swing.JFrame implements Actualizable {

   
    public cursosDashMon() {
        initComponents();

        if (jPanel2 != null) {
            jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        }

        if (notifs != null) {
            notifs.setLayout(new javax.swing.BoxLayout(notifs, javax.swing.BoxLayout.Y_AXIS));
        }

        cargarUsuario();

        cargarCursosDisponibles();
        cargarHorariosDeExcepcion();
    }
    
    // El bloque initComponents() autogenerado por NetBeans ha sido movido a su lugar correcto al final del código.
    // Solo queda una única definición.

    private void cargarUsuario() {
        Usuario u = Session.getUsuario();

        if (u != null) {
            userName.setText(u.getNombre() + " " + u.getApellido());
        } else {
            userName.setText("Usuario");
        }
    }

    private void cargarCursosDisponibles() {
        try {
            System.out.println("⏳ Cargando cursos disponibles para el estudiante...");

            // NOTA: Si solo quieres mostrar los cursos INSCRITOS por el estudiante,
            // deberías usar un método que filtre por Session.getUsuario().getId().
            // Por ahora, se mantiene obtenerTodasLasAsignaturas (el catálogo completo).
            List<Asignatura> listaCursos = DBConnection.obtenerTodasLasAsignaturas();

            mostrarAsignaturas(listaCursos);

            System.out.println("✅ Cursos cargados exitosamente: " + listaCursos.size());

        } catch (Exception e) {
            System.err.println("❌ Error al intentar cargar los cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // FUNCIÓN CLAVE CORREGIDA: Cargar y Mostrar el Horario de Excepción (Notificaciones)
    // -------------------------------------------------------------
    public void cargarHorariosDeExcepcion() {
        Usuario usuarioActual = Session.getUsuario();
        if (usuarioActual == null) {
            return;
        }

        String idBusqueda = usuarioActual.getId();

        if (notifs != null) {
            notifs.removeAll();
        } else {
            System.err.println("Error: El contenedor 'notifs' no está inicializado.");
            return;
        }

        try {
            // Retorna 7 campos: {idDocente, salon, fecha, hora_inicio, hora_fin, idMateria, nombreAsignatura}
            List<String[]> horarios = DBConnection.consultarHorarioExcepcion(idBusqueda);

            if (!horarios.isEmpty()) {

                for (String[] horarioData : horarios) {
                    // 🔥 Mapeo de datos CORREGIDO (Índices 0 a 6)
                    String idDocenteEnHorario = horarioData[0];
                    String salon = horarioData[1];
                    String dia = horarioData[2];
                    String inicio = horarioData[3];
                    String fin = horarioData[4];
                    String idMateria = horarioData[5];
                    String nombreAsignatura = horarioData[6]; // <--- ¡Nuevo campo que viene de la BD!

                    // Obtener nombre del docente
                    String nombreDocente = DBConnection.obtenerNombreCompletoUsuario(idDocenteEnHorario);
                    
                    // Si el nombre de la asignatura es "Materia Desconocida" (por si el join falló)
                    // se puede optar por llamar al método, pero ya viene en la consulta.
                    // Si prefieres usar el método anterior:
                    // String nombreAsignatura = DBConnection.obtenerNombreMateria(idMateria);

                    // Crear la tarjeta e inyectar los datos
                    panelNotif nuevaTarjeta = new panelNotif();
                    nuevaTarjeta.setHorarioData(
                            nombreAsignatura,
                            nombreDocente,
                            salon,
                            dia,
                            inicio,
                            fin
                    );

                    // Configuración para BoxLayout
                    nuevaTarjeta.setAlignmentX(LEFT_ALIGNMENT);
                    nuevaTarjeta.setMaximumSize(new Dimension(270, 160));

                    notifs.add(nuevaTarjeta);

                    // Añadir un pequeño separador visual
                    JPanel separator = new JPanel();
                    separator.setPreferredSize(new Dimension(270, 10));
                    separator.setBackground(notifs.getBackground());
                    separator.setAlignmentX(LEFT_ALIGNMENT);
                    notifs.add(separator);
                }
            } else {
                notifs.add(new javax.swing.JLabel("No hay notificaciones de horarios."));
            }

            // Refrescar el panel 
            notifs.revalidate();
            notifs.repaint();

        } catch (Exception e) {
            System.err.println("Error general en cargarHorariosDeExcepcion: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // Métodos Actualizable
    // -------------------------------------------------------------
    @Override
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

    // -------------------------------------------------------------
    // MÉTODO PRINCIPAL: Dibujar las asignaturas como tarjetas
    // -------------------------------------------------------------
    public void mostrarAsignaturas(List<Asignatura> listaAsignaturas) {

        if (jPanel2 == null) {
            System.err.println("❌ ERROR: jPanel2 no está inicializado. No se pueden mostrar asignaturas.");
            return;
        }

        // Limpiar el panel antes de dibujar
        jPanel2.removeAll();

        if (listaAsignaturas == null || listaAsignaturas.isEmpty()) {
            jPanel2.add(new javax.swing.JLabel("No hay asignaturas disponibles en el catálogo."));
        } else {

            for (Asignatura asig : listaAsignaturas) {

                panelCurso tarjeta = new panelCurso(asig);

                // Configuración para BoxLayout 
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
                tarjeta.setAlignmentX(LEFT_ALIGNMENT);

                jPanel2.add(tarjeta);

                // Agregar un separador visual
                JPanel separator = new JPanel();
                separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, 10));
                separator.setBackground(jPanel2.getBackground());
                separator.setAlignmentX(LEFT_ALIGNMENT);
                jPanel2.add(separator);
            }
        }

        // Actualizar la Interfaz 
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        docBtn = new javax.swing.JPanel();
        docTxt = new javax.swing.JLabel();
        docIcon = new javax.swing.JLabel();
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
        mainCont = new javax.swing.JPanel();
        coursesHead = new javax.swing.JLabel();
        cont1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        cont2 = new javax.swing.JPanel();
        panelNotif = new javax.swing.JPanel();
        nuevasTitle = new javax.swing.JLabel();
        moniandtutoTitle = new javax.swing.JLabel();
        bellicon = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        notifs = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        docBtn.setBackground(new java.awt.Color(255, 255, 255));
        docBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                docBtnMouseClicked(evt);
            }
        });
        docBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        docTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        docTxt.setForeground(new java.awt.Color(0, 0, 0));
        docTxt.setText("Docente");
        docBtn.add(docTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, -1, -1));

        docIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/caseBlack.png"))); // NOI18N
        docBtn.add(docIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        getContentPane().add(docBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 260, 40));

        menuBar.setBackground(new java.awt.Color(255, 255, 255));
        menuBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        appName.setFont(new java.awt.Font("Questrial", 0, 30)); // NOI18N
        appName.setForeground(new java.awt.Color(0, 0, 0));
        appName.setText("Skillbridge");
        menuBar.add(appName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        dashBtn.setBackground(new java.awt.Color(255, 255, 255));
        dashBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashBtnMouseClicked(evt);
            }
        });
        dashBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        dashTxt.setForeground(new java.awt.Color(0, 0, 0));
        dashTxt.setText("Dashboard");
        dashBtn.add(dashTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 80, -1));

        dashIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/houseBicon.png"))); // NOI18N
        dashBtn.add(dashIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        menuBar.add(dashBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 260, 40));

        coursesBtn.setBackground(new java.awt.Color(255, 255, 255));
        coursesBtn.setForeground(new java.awt.Color(0, 0, 0));
        coursesBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        coursesTxt.setBackground(new java.awt.Color(255, 255, 255));
        coursesTxt.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        coursesTxt.setForeground(new java.awt.Color(4, 174, 178));
        coursesTxt.setText("Cursos");
        coursesBtn.add(coursesTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, 50, -1));

        coursesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hatGicon.png"))); // NOI18N
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

        mainCont.setBackground(new java.awt.Color(255, 255, 255));
        mainCont.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        coursesHead.setFont(new java.awt.Font("Questrial", 0, 35)); // NOI18N
        coursesHead.setForeground(new java.awt.Color(0, 0, 0));
        coursesHead.setText("Mis cursos");
        mainCont.add(coursesHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        cont1.setBackground(new java.awt.Color(102, 102, 102));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout cont1Layout = new javax.swing.GroupLayout(cont1);
        cont1.setLayout(cont1Layout);
        cont1Layout.setHorizontalGroup(
            cont1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        cont1Layout.setVerticalGroup(
            cont1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );

        mainCont.add(cont1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 700, 570));

        cont2.setBackground(new java.awt.Color(204, 204, 204));
        cont2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNotif.setBackground(new java.awt.Color(255, 255, 255));
        panelNotif.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelNotif.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nuevasTitle.setFont(new java.awt.Font("Poppins SemiBold", 0, 22)); // NOI18N
        nuevasTitle.setForeground(new java.awt.Color(92, 225, 230));
        nuevasTitle.setText("Nuevas");
        panelNotif.add(nuevasTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        moniandtutoTitle.setFont(new java.awt.Font("Poppins Light", 0, 20)); // NOI18N
        moniandtutoTitle.setForeground(new java.awt.Color(0, 0, 0));
        moniandtutoTitle.setText("Monitorias/Tutorías");
        panelNotif.add(moniandtutoTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        bellicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bellicon.png"))); // NOI18N
        panelNotif.add(bellicon, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        panelNotif.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 290, 20));

        notifs.setBackground(new java.awt.Color(153, 153, 153));
        notifs.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelNotif.add(notifs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 270, 500));

        cont2.add(panelNotif, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 290, 600));

        mainCont.add(cont2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 310, 630));

        getContentPane().add(mainCont, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 1010, 630));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dashBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        dashboardMon nuevaventana = new dashboardMon();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_dashBtnMouseClicked

    private void logoutBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        login nuevaventana = new login();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_logoutBtnMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // Abrir el perfil pasando la referencia de esta ventana (dashboard)
        profileMon nuevaventana = new profileMon(this);
        nuevaventana.setVisible(true);

        // Ocultar dashboard (no cerrarlo) para poder volver cuando el perfil cierre
        this.setVisible(false);
    }//GEN-LAST:event_jPanel1MouseClicked

    private void docBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_docBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva
        docente nuevaventana = new docente();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_docBtnMouseClicked

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
            java.util.logging.Logger.getLogger(cursosDashMon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cursosDashMon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cursosDashMon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cursosDashMon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new cursosDashMon().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appName;
    private javax.swing.JLabel bellicon;
    private javax.swing.JLabel configArrow;
    private javax.swing.JPanel cont1;
    private javax.swing.JPanel cont2;
    private javax.swing.JPanel coursesBtn;
    private javax.swing.JLabel coursesHead;
    private javax.swing.JLabel coursesIcon;
    private javax.swing.JLabel coursesTxt;
    private javax.swing.JPanel dashBtn;
    private javax.swing.JLabel dashIcon;
    private javax.swing.JLabel dashTxt;
    private javax.swing.JPanel docBtn;
    private javax.swing.JLabel docIcon;
    private javax.swing.JLabel docTxt;
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel logoutBtn;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel logoutTxt;
    private javax.swing.JPanel mainCont;
    private javax.swing.JPanel menuBar;
    private javax.swing.JLabel moniandtutoTitle;
    private javax.swing.JPanel notifs;
    private javax.swing.JLabel nuevasTitle;
    private javax.swing.JPanel panelNotif;
    private javax.swing.JLabel profilePic;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
}
