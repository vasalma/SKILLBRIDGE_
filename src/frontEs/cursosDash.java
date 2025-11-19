package frontEs;

// -------------------------------------------------------------------
// 🔥 IMPORTS NECESARIOS (Asegúrate de que estas clases existan)
// -------------------------------------------------------------------
import Cursos.panelCurso; // Tu tarjeta de curso individual
import back.Session;
import back.Usuario;
import back.Actualizable;
import main.DBConnection; // Importa tu clase de conexión a la BD
import Materia.Asignatura; // Importa tu clase de modelo
import front.login;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
// -------------------------------------------------------------------

/**
 * Clase principal que actúa como el dashboard de cursos (vista del Estudiante).
 * Se ha cambiado para cargar automáticamente todos los cursos disponibles.
 */
public class cursosDash extends javax.swing.JFrame implements Actualizable {

    // Asegúrate de que estas variables existan en tus componentes (diseñador):
    // private javax.swing.JLabel userName;
    // private javax.swing.JPanel jPanel5; // El contenedor de las tarjetas

    public cursosDash() {
        initComponents();
        
        // 1. Configurar el layout del contenedor (jPanel5) para que apile las tarjetas verticalmente
        if (jPanel2 != null) {
            jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        }
        
        cargarUsuario();
        
        // 🔥 LÍNEA CRÍTICA: Llamar a la función que carga los cursos para el ESTUDIANTE
        cargarCursosDisponibles(); 
    }

    private void cargarUsuario() {
        Usuario u = Session.getUsuario();

        if (u != null) {
            userName.setText(u.getNombre() + " " + u.getApellido());
        } else {
            userName.setText("Usuario");
        }
    }

    // Método para ser llamado desde el constructor
    private void cargarCursosDisponibles() {
        try {
            System.out.println("⏳ Cargando cursos disponibles para el estudiante...");
            
            // 1. Llama al método de la BD para obtener TODAS las asignaturas
            List<Asignatura> listaCursos = DBConnection.obtenerTodasLasAsignaturas();
            
            // 2. Llama a tu propio método para dibujar las tarjetas
            mostrarAsignaturas(listaCursos);
            
            System.out.println("✅ Cursos cargados exitosamente: " + listaCursos.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error al intentar cargar los cursos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Métodos Actualizable (se mantienen)
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
    // 🔥 MÉTODO PRINCIPAL: Dibujar las asignaturas como tarjetas
    // -------------------------------------------------------------
    /**
     * Recibe la lista de asignaturas y las dibuja como tarjetas (panelCurso) en jPanel5.
     */
    public void mostrarAsignaturas(List<Asignatura> listaAsignaturas) {

        if (jPanel2 == null) {
            System.err.println("❌ ERROR: jPanel5 no está inicializado. No se pueden mostrar asignaturas.");
            return;
        }

        // 1. Limpiar el panel antes de dibujar
        jPanel2.removeAll();

        if (listaAsignaturas == null || listaAsignaturas.isEmpty()) {
            // Muestra un mensaje si no hay asignaturas
            jPanel2.add(new javax.swing.JLabel("No hay asignaturas disponibles en el catálogo."));
        } else {
            // 2. Iterar sobre la lista y añadir la tarjeta con sus datos
            for (Asignatura asig : listaAsignaturas) {

                // --- INSTANCIACIÓN DE LA TARJETA panelCurso ---
                panelCurso tarjeta = new panelCurso(asig);

                // Configuración para BoxLayout (importante para que se vean bien)
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
                tarjeta.setAlignmentX(LEFT_ALIGNMENT);

                jPanel2.add(tarjeta); // Añadir la tarjeta a jPanel5

                // 3. Agregar un separador visual para espacio entre tarjetas
                JPanel separator = new JPanel();
                separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, 10)); // 10px de espacio
                separator.setBackground(jPanel2.getBackground());
                separator.setAlignmentX(LEFT_ALIGNMENT);
                jPanel2.add(separator);
            }
        }

        // 4. Actualizar la Interfaz (esencial)
        jPanel2.revalidate();
        jPanel2.repaint();
    }    // -------------------------------------------------------------

    // ... El resto de tus métodos (initComponents, listeners, main, y variables)
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        mainCont = new javax.swing.JPanel();
        coursesHead = new javax.swing.JLabel();
        cont1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        menuTab = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        cont2 = new javax.swing.JPanel();
        panelNotif = new javax.swing.JPanel();
        nuevasTitle = new javax.swing.JLabel();
        moniandtutoTitle = new javax.swing.JLabel();
        bellicon = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        notifs = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        menuBar = new javax.swing.JPanel();
        appName = new javax.swing.JLabel();
        dashBtn = new javax.swing.JPanel();
        dashTxt = new javax.swing.JLabel();
        dashIcon = new javax.swing.JLabel();
        coursesBtn = new javax.swing.JPanel();
        coursesTxt = new javax.swing.JLabel();
        coursesIcon = new javax.swing.JLabel();
        actsBtn = new javax.swing.JPanel();
        actsTxt = new javax.swing.JLabel();
        actsIcon = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JPanel();
        logoutTxt = new javax.swing.JLabel();
        logoutIcon = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        searchIcon = new javax.swing.JButton();
        searchBar = new javax.swing.JTextField();
        profilePic = new javax.swing.JLabel();
        userName = new javax.swing.JLabel();
        configArrow = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainCont.setBackground(new java.awt.Color(255, 255, 255));
        mainCont.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        coursesHead.setFont(new java.awt.Font("Questrial", 0, 35)); // NOI18N
        coursesHead.setForeground(new java.awt.Color(0, 0, 0));
        coursesHead.setText("Mis cursos");
        mainCont.add(coursesHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        cont1.setBackground(new java.awt.Color(102, 102, 102));

        menuTab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                menuTabKeyPressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout menuTabLayout = new javax.swing.GroupLayout(menuTab);
        menuTab.setLayout(menuTabLayout);
        menuTabLayout.setHorizontalGroup(
            menuTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        menuTabLayout.setVerticalGroup(
            menuTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jTabbedPane1.addTab("tab1", menuTab);

        javax.swing.GroupLayout cont1Layout = new javax.swing.GroupLayout(cont1);
        cont1.setLayout(cont1Layout);
        cont1Layout.setHorizontalGroup(
            cont1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        cont1Layout.setVerticalGroup(
            cont1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cont1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        notifs.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 270, 50));

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setForeground(new java.awt.Color(0, 0, 0));
        jTextField2.setText("jTextField1");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        notifs.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 270, 50));

        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setForeground(new java.awt.Color(0, 0, 0));
        jTextField3.setText("jTextField1");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        notifs.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 270, 50));

        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setForeground(new java.awt.Color(0, 0, 0));
        jTextField4.setText("jTextField1");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        notifs.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 50));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jTextField5.setForeground(new java.awt.Color(0, 0, 0));
        jTextField5.setText("jTextField5");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 270, 50));

        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setForeground(new java.awt.Color(0, 0, 0));
        jTextField6.setText("jTextField5");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 22, 270, 50));

        notifs.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 270, 220));

        panelNotif.add(notifs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 270, 500));

        cont2.add(panelNotif, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 290, 600));

        mainCont.add(cont2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 310, 630));

        getContentPane().add(mainCont, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 1010, 630));

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

        menuBar.add(dashBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 260, 40));

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

        menuBar.add(coursesBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 260, 40));

        actsBtn.setBackground(new java.awt.Color(255, 255, 255));
        actsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actsBtnMouseClicked(evt);
            }
        });
        actsBtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actsTxt.setFont(new java.awt.Font("Open Sans", 0, 12)); // NOI18N
        actsTxt.setForeground(new java.awt.Color(0, 0, 0));
        actsTxt.setText("Actividades");
        actsBtn.add(actsTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 13, -1, -1));

        actsIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/appleBicon.png"))); // NOI18N
        actsBtn.add(actsIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 40, 40));

        menuBar.add(actsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 260, 40));

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

        searchIcon.setBackground(new java.awt.Color(145, 145, 145));
        searchIcon.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        searchIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/searchIcon.png"))); // NOI18N
        searchIcon.setBorder(null);
        searchIcon.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        searchIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchIconActionPerformed(evt);
            }
        });
        header.add(searchIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 50, 26));

        searchBar.setBackground(new java.awt.Color(145, 145, 145));
        searchBar.setFont(new java.awt.Font("Open Sans", 0, 11)); // NOI18N
        searchBar.setForeground(new java.awt.Color(229, 229, 229));
        searchBar.setText("    Search");
        searchBar.setBorder(null);
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        header.add(searchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 240, 26));

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

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed

    private void searchIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchIconActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchIconActionPerformed

    private void dashBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        dashboard nuevaventana = new dashboard();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_dashBtnMouseClicked

    private void actsBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actsBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        actDash nuevaventana = new actDash();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_actsBtnMouseClicked

    private void logoutBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtnMouseClicked
        //Cierra la ventana actual (login)
        this.dispose();
        //Abre la ventana nueva 
        login nuevaventana = new login();
        nuevaventana.setVisible(true);
    }//GEN-LAST:event_logoutBtnMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // Abrir el perfil pasando la referencia de esta ventana (dashboard)
        profile nuevaventana = new profile(this);
        nuevaventana.setVisible(true);

        // Ocultar dashboard (no cerrarlo) para poder volver cuando el perfil cierre
        this.setVisible(false);
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void menuTabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_menuTabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuTabKeyPressed

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
            java.util.logging.Logger.getLogger(cursosDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cursosDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cursosDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cursosDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
            new cursosDash().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actsBtn;
    private javax.swing.JLabel actsIcon;
    private javax.swing.JLabel actsTxt;
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
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JPanel logoutBtn;
    private javax.swing.JLabel logoutIcon;
    private javax.swing.JLabel logoutTxt;
    private javax.swing.JPanel mainCont;
    private javax.swing.JPanel menuBar;
    private javax.swing.JPanel menuTab;
    private javax.swing.JLabel moniandtutoTitle;
    private javax.swing.JPanel notifs;
    private javax.swing.JLabel nuevasTitle;
    private javax.swing.JPanel panelNotif;
    private javax.swing.JLabel profilePic;
    private javax.swing.JTextField searchBar;
    private javax.swing.JButton searchIcon;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
}
