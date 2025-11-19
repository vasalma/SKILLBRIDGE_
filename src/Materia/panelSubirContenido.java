package Materia;

import back.Session;
import back.Usuario;
import java.awt.Color;
import main.DBConnection;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

public class panelSubirContenido extends javax.swing.JPanel {

    private panelAsig panelAsigRef;
    private File videoFile;
    private File actividadFile;
    private final String idDocente;
    private final String idMateria;

    // Constructor ORIGINAL - para mantener compatibilidad HOLIIIIIIII
    public panelSubirContenido(Asignatura materiaActual) {
        this(materiaActual, null); // Llama al nuevo constructor con null
    }

    // Constructor NUEVO - con referencia a panelAsig
    public panelSubirContenido(Asignatura materiaActual, panelAsig panelAsigRef) {
        initComponents();
        this.panelAsigRef = panelAsigRef;

        // Obtener datos de sesión
        Usuario usuarioActual = Session.getUsuario();
        if (usuarioActual != null) {
            idDocente = usuarioActual.getId();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No hay usuario logueado.");
            idDocente = null;
        }

        // Obtener ID de la materia
        this.idMateria = materiaActual.getId();

        // Configuración de eventos
        setListeners();
    }

    // Método para establecer la referencia después de la creación (opcional)
    public void setPanelAsigRef(panelAsig panelAsigRef) {
        this.panelAsigRef = panelAsigRef;
    }

    private void setListeners() {

        subirVidTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                subirVideo();
            }
        });

        subirActTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                subirActividad();
            }
        });
    }

    // Subir Video - MODIFICADO para refrescar panelAsig si está disponible
    private void subirVideo() {
        if (videoFile == null || vidTitleTxt.getText().isEmpty() || vidDescripTxt.getText().isEmpty()) {

            return;
        }

        if (idDocente == null) {
            JOptionPane.showMessageDialog(this, "Error de Sesión: No se pudo obtener el ID del Docente.");
            return;
        }

        // ⭐ PASO 1: DEFINIR LA RUTA DE DESTINO DEL ARCHIVO FÍSICO ⭐
        // Usaremos una carpeta llamada 'uploads/videos' dentro de tu directorio de proyecto.
        final String BASE_UPLOAD_PATH = "uploads" + File.separator + "videos";

        // Crear la carpeta si no existe
        File uploadDir = new File(BASE_UPLOAD_PATH);
        if (!uploadDir.exists()) {
            // La función mkdirs() crea todos los directorios padre necesarios
            if (!uploadDir.mkdirs()) {
                JOptionPane.showMessageDialog(this, "Error: No se pudo crear el directorio de subida.");
                return;
            }
        }

        // Crear un nombre de archivo único (usando timestamp + nombre original)
        String uniqueFileName = System.currentTimeMillis() + "_" + videoFile.getName();
        File targetFile = new File(uploadDir, uniqueFileName);

        // La RUTA ABSOLUTA que guardaremos como String en la DB
        String dbVideoPath = BASE_UPLOAD_PATH + File.separator + uniqueFileName;

        try {
            // ⭐ PASO 2: MOVER/COPIAR EL ARCHIVO FÍSICAMENTE AL DIRECTORIO DE SUBIDA ⭐
            // Usamos Files.copy para un manejo eficiente de archivos
            Files.copy(videoFile.toPath(), targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            // ⭐ PASO 3: GUARDAR SÓLO LA RUTA (STRING) EN LA BASE DE DATOS ⭐
            try (Connection conn = DBConnection.getConnection()) {

                // CAMBIAR LA SENTENCIA SQL: ya no se usa setBinaryStream
                String sql = "INSERT INTO videos (idDocente, titulo, descripcion, idMateria, videourl) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, idDocente);
                pst.setString(2, vidTitleTxt.getText());
                pst.setString(3, vidDescripTxt.getText());
                pst.setString(4, idMateria);
                // PASAMOS LA RUTA DEL ARCHIVO COMO STRING
                pst.setString(5, dbVideoPath);

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Video subido correctamente.\n" + "Ruta guardada: " + dbVideoPath);
                    limpiarCamposVideo();

                    // Refrescar panelAsig si está disponible
                    if (panelAsigRef != null) {
                        panelAsigRef.refrescarVideos();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al subir video a la base de datos.");
                    // Si la inserción en DB falla, eliminamos el archivo físico copiado
                    targetFile.delete();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
                targetFile.delete(); // Asegura la limpieza si falla la DB
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al copiar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Subir Actividad
    // Archivo: Materia/panelSubirContenido.java
    private void subirActividad() {
        if (actividadFile == null || actTitleTxt.getText().isEmpty() || actDescripTxt.getText().isEmpty()) {

            return;
        }

        final String BASE_UPLOAD_PATH = "uploads" + File.separator + "actividades";
        File uploadDir = new File(BASE_UPLOAD_PATH);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            JOptionPane.showMessageDialog(this, "Error: No se pudo crear el directorio de subida para actividades.");
            return;
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + actividadFile.getName();
        File targetFile = new File(uploadDir, uniqueFileName);
        String dbActividadPath = BASE_UPLOAD_PATH + File.separator + uniqueFileName; // ✅ AÑADIR ESTA LÍNEA String dbActividadPath = targetFile.getAbsolutePath();

        try {
            Files.copy(actividadFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try (Connection conn = DBConnection.getConnection()) {

                // EL CAMPO 'actividadurl' AHORA DEBE SER TEXT
                String sql = "INSERT INTO actividades (idDocente, titulo, descripcion, idMateria, actividadurl) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, idDocente);
                pst.setString(2, actTitleTxt.getText());
                pst.setString(3, actDescripTxt.getText());
                pst.setString(4, idMateria);
                pst.setString(5, dbActividadPath); // Guardamos la RUTA

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Actividad subida correctamente.");
                    limpiarCamposActividad();

                    // ⭐ LÍNEA IMPLEMENTADA AQUÍ: LLAMADA DE REFRESH ⭐
                    if (panelAsigRef != null) {
                        panelAsigRef.refrescarActividades();
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Error al subir actividad.");
                    targetFile.delete(); // Limpia si falla la DB
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
                targetFile.delete();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al copiar el archivo: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public javax.swing.JLabel getTitulo() {
        return vidTitle;
    }

    private Runnable onBack;

    public void setOnBack(Runnable r) {
        this.onBack = r;
    }

    private void limpiarCamposVideo() {
        vidTitleTxt.setText("");
        vidDescripTxt.setText("");
        plusVideos.setText("+");
        plusVideos.setFont(new java.awt.Font("Poppins", 0, 35)); // Restaurar tamaño de fuente
        videoFile = null;
    }

    private void limpiarCamposActividad() {
        actTitleTxt.setText("");
        actDescripTxt.setText("");
        plusActs.setText("+");
        plusActs.setFont(new java.awt.Font("Poppins", 0, 35)); // Restaurar tamaño de fuente
        actividadFile = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        backBtn = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        asigName = new javax.swing.JLabel();
        actsSection = new javax.swing.JPanel();
        actsTitle = new javax.swing.JLabel();
        bigloadactsBtn = new javax.swing.JPanel();
        plusActs = new javax.swing.JLabel();
        subirActBtn = new javax.swing.JPanel();
        subirActTxt = new javax.swing.JLabel();
        actTitle = new javax.swing.JLabel();
        actTitleTxt = new javax.swing.JTextField();
        actDescrip = new javax.swing.JLabel();
        actDescripTxt = new javax.swing.JTextField();
        videosSection = new javax.swing.JPanel();
        videosTitle = new javax.swing.JLabel();
        bigloadvidBtn = new javax.swing.JPanel();
        plusVideos = new javax.swing.JLabel();
        subirVidBtn = new javax.swing.JPanel();
        subirVidTxt = new javax.swing.JLabel();
        vidTitle = new javax.swing.JLabel();
        vidTitleTxt = new javax.swing.JTextField();
        vidDescripTitle = new javax.swing.JLabel();
        vidDescripTxt = new javax.swing.JTextField();
        scheduleSection = new javax.swing.JPanel();
        whiteBack = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        horariosTitle = new javax.swing.JLabel();
        moniandtutoTitle = new javax.swing.JLabel();
        salidaTitle = new javax.swing.JLabel();
        salonTitle = new javax.swing.JLabel();
        diaTxt = new javax.swing.JTextField();
        salidaTxt = new javax.swing.JTextField();
        diaTitle = new javax.swing.JLabel();
        inicioTitle = new javax.swing.JLabel();
        salonTxt = new javax.swing.JTextField();
        inicioTxt = new javax.swing.JTextField();
        subirScheBtn = new javax.swing.JPanel();
        subirScheTxt = new javax.swing.JLabel();
        watchIcon = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1010, 560));
        setPreferredSize(new java.awt.Dimension(1010, 560));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        background.setBackground(new java.awt.Color(255, 255, 255));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backBtn.setBackground(new java.awt.Color(255, 255, 255));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backBtnMouseClicked(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Open Sans", 1, 10)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Retroceder");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backIcon.png"))); // NOI18N

        javax.swing.GroupLayout backBtnLayout = new javax.swing.GroupLayout(backBtn);
        backBtn.setLayout(backBtnLayout);
        backBtnLayout.setHorizontalGroup(
            backBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backBtnLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel23)
                .addGap(0, 0, 0)
                .addComponent(jLabel22)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        backBtnLayout.setVerticalGroup(
            backBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(backBtnLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        background.add(backBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 30));

        asigName.setFont(new java.awt.Font("Questrial", 0, 26)); // NOI18N
        asigName.setForeground(new java.awt.Color(221, 224, 229));
        asigName.setText("// Subir contenido");
        background.add(asigName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        actsSection.setBackground(new java.awt.Color(255, 255, 255));
        actsSection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actsTitle.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        actsTitle.setForeground(new java.awt.Color(0, 0, 0));
        actsTitle.setText("Actividad");
        actsSection.add(actsTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        bigloadactsBtn.setBackground(new java.awt.Color(145, 145, 145));

        plusActs.setFont(new java.awt.Font("Poppins", 0, 35)); // NOI18N
        plusActs.setForeground(new java.awt.Color(255, 255, 255));
        plusActs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plusActs.setText("+");
        plusActs.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                plusActsMouseMoved(evt);
            }
        });
        plusActs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                plusActsMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                plusActsMouseExited(evt);
            }
        });

        javax.swing.GroupLayout bigloadactsBtnLayout = new javax.swing.GroupLayout(bigloadactsBtn);
        bigloadactsBtn.setLayout(bigloadactsBtnLayout);
        bigloadactsBtnLayout.setHorizontalGroup(
            bigloadactsBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(plusActs, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );
        bigloadactsBtnLayout.setVerticalGroup(
            bigloadactsBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(plusActs, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
        );

        actsSection.add(bigloadactsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 37, 240, -1));

        subirActBtn.setBackground(new java.awt.Color(145, 145, 145));
        subirActBtn.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                subirActBtnMouseMoved(evt);
            }
        });
        subirActBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subirActBtnMouseExited(evt);
            }
        });

        subirActTxt.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        subirActTxt.setForeground(new java.awt.Color(255, 255, 255));
        subirActTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subirActTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loadMiniIcon.png"))); // NOI18N
        subirActTxt.setText("Subir contenido");
        subirActTxt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                subirActTxtMouseMoved(evt);
            }
        });
        subirActTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subirActTxtMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subirActTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout subirActBtnLayout = new javax.swing.GroupLayout(subirActBtn);
        subirActBtn.setLayout(subirActBtnLayout);
        subirActBtnLayout.setHorizontalGroup(
            subirActBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subirActBtnLayout.createSequentialGroup()
                .addComponent(subirActTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );
        subirActBtnLayout.setVerticalGroup(
            subirActBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subirActTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)
        );

        actsSection.add(subirActBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 136, -1, -1));

        actTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        actTitle.setForeground(new java.awt.Color(0, 0, 0));
        actTitle.setText("Título");
        actsSection.add(actTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        actTitleTxt.setBackground(new java.awt.Color(247, 247, 247));
        actTitleTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        actTitleTxt.setForeground(new java.awt.Color(145, 145, 145));
        actTitleTxt.setText("Título de la actividad");
        actTitleTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actTitleTxtMouseClicked(evt);
            }
        });
        actTitleTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actTitleTxtActionPerformed(evt);
            }
        });
        actsSection.add(actTitleTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 240, -1));

        actDescrip.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        actDescrip.setForeground(new java.awt.Color(0, 0, 0));
        actDescrip.setText("Descripción");
        actsSection.add(actDescrip, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, -1, -1));

        actDescripTxt.setBackground(new java.awt.Color(247, 247, 247));
        actDescripTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        actDescripTxt.setForeground(new java.awt.Color(145, 145, 145));
        actDescripTxt.setText("Descripción de la actividad");
        actDescripTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actDescripTxtMouseClicked(evt);
            }
        });
        actDescripTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actDescripTxtActionPerformed(evt);
            }
        });
        actsSection.add(actDescripTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 240, 60));

        background.add(actsSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 560, 180));

        videosSection.setBackground(new java.awt.Color(255, 255, 255));
        videosSection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        videosTitle.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        videosTitle.setForeground(new java.awt.Color(0, 0, 0));
        videosTitle.setText("Video");
        videosSection.add(videosTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        bigloadvidBtn.setBackground(new java.awt.Color(64, 174, 178));

        plusVideos.setFont(new java.awt.Font("Poppins", 0, 35)); // NOI18N
        plusVideos.setForeground(new java.awt.Color(255, 255, 255));
        plusVideos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        plusVideos.setText("+");
        plusVideos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                plusVideosMouseMoved(evt);
            }
        });
        plusVideos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                plusVideosMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                plusVideosMouseExited(evt);
            }
        });

        javax.swing.GroupLayout bigloadvidBtnLayout = new javax.swing.GroupLayout(bigloadvidBtn);
        bigloadvidBtn.setLayout(bigloadvidBtnLayout);
        bigloadvidBtnLayout.setHorizontalGroup(
            bigloadvidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(plusVideos, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );
        bigloadvidBtnLayout.setVerticalGroup(
            bigloadvidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(plusVideos, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
        );

        videosSection.add(bigloadvidBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 37, 240, -1));

        subirVidBtn.setBackground(new java.awt.Color(64, 174, 178));
        subirVidBtn.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                subirVidBtnMouseMoved(evt);
            }
        });
        subirVidBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subirVidBtnMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subirVidBtnMouseExited(evt);
            }
        });

        subirVidTxt.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        subirVidTxt.setForeground(new java.awt.Color(255, 255, 255));
        subirVidTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subirVidTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loadMiniIcon.png"))); // NOI18N
        subirVidTxt.setText("Subir contenido");
        subirVidTxt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                subirVidTxtMouseMoved(evt);
            }
        });
        subirVidTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subirVidTxtMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subirVidTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout subirVidBtnLayout = new javax.swing.GroupLayout(subirVidBtn);
        subirVidBtn.setLayout(subirVidBtnLayout);
        subirVidBtnLayout.setHorizontalGroup(
            subirVidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subirVidBtnLayout.createSequentialGroup()
                .addComponent(subirVidTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );
        subirVidBtnLayout.setVerticalGroup(
            subirVidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subirVidTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)
        );

        videosSection.add(subirVidBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 136, -1, -1));

        vidTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        vidTitle.setForeground(new java.awt.Color(0, 0, 0));
        vidTitle.setText("Título");
        videosSection.add(vidTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        vidTitleTxt.setBackground(new java.awt.Color(247, 247, 247));
        vidTitleTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        vidTitleTxt.setForeground(new java.awt.Color(145, 145, 145));
        vidTitleTxt.setText("Título del video");
        vidTitleTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vidTitleTxtMouseClicked(evt);
            }
        });
        vidTitleTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vidTitleTxtActionPerformed(evt);
            }
        });
        videosSection.add(vidTitleTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 240, -1));

        vidDescripTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        vidDescripTitle.setForeground(new java.awt.Color(0, 0, 0));
        vidDescripTitle.setText("Descripción");
        videosSection.add(vidDescripTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, -1, -1));

        vidDescripTxt.setBackground(new java.awt.Color(247, 247, 247));
        vidDescripTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        vidDescripTxt.setForeground(new java.awt.Color(145, 145, 145));
        vidDescripTxt.setText("Descripción del video");
        vidDescripTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vidDescripTxtMouseClicked(evt);
            }
        });
        vidDescripTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vidDescripTxtActionPerformed(evt);
            }
        });
        videosSection.add(vidDescripTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, 240, 60));

        background.add(videosSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 560, 170));

        scheduleSection.setBackground(new java.awt.Color(255, 255, 255));

        whiteBack.setBackground(new java.awt.Color(255, 255, 255));
        whiteBack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        whiteBack.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        whiteBack.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 72, 388, 21));

        horariosTitle.setFont(new java.awt.Font("Poppins Medium", 0, 16)); // NOI18N
        horariosTitle.setForeground(new java.awt.Color(0, 0, 0));
        horariosTitle.setText("Horarios");
        whiteBack.add(horariosTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        moniandtutoTitle.setFont(new java.awt.Font("Poppins Light", 0, 16)); // NOI18N
        moniandtutoTitle.setForeground(new java.awt.Color(0, 0, 0));
        moniandtutoTitle.setText("Monitorias/Tutorías");
        whiteBack.add(moniandtutoTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 15));

        salidaTitle.setBackground(new java.awt.Color(0, 0, 0));
        salidaTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        salidaTitle.setForeground(new java.awt.Color(0, 0, 0));
        salidaTitle.setText("Hora de salida");
        whiteBack.add(salidaTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, -1, -1));

        salonTitle.setBackground(new java.awt.Color(0, 0, 0));
        salonTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        salonTitle.setForeground(new java.awt.Color(0, 0, 0));
        salonTitle.setText("Salón");
        whiteBack.add(salonTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        diaTxt.setBackground(new java.awt.Color(247, 247, 247));
        diaTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        diaTxt.setForeground(new java.awt.Color(145, 145, 145));
        diaTxt.setText("ej: Miércoles, 11/11/25");
        diaTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diaTxtMouseClicked(evt);
            }
        });
        diaTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diaTxtActionPerformed(evt);
            }
        });
        whiteBack.add(diaTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 200, -1));

        salidaTxt.setBackground(new java.awt.Color(247, 247, 247));
        salidaTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        salidaTxt.setForeground(new java.awt.Color(145, 145, 145));
        salidaTxt.setText("ej: 4:00 pm");
        salidaTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salidaTxtMouseClicked(evt);
            }
        });
        salidaTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salidaTxtActionPerformed(evt);
            }
        });
        whiteBack.add(salidaTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 90, -1));

        diaTitle.setBackground(new java.awt.Color(0, 0, 0));
        diaTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        diaTitle.setForeground(new java.awt.Color(0, 0, 0));
        diaTitle.setText("Día ");
        whiteBack.add(diaTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        inicioTitle.setBackground(new java.awt.Color(0, 0, 0));
        inicioTitle.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        inicioTitle.setForeground(new java.awt.Color(0, 0, 0));
        inicioTitle.setText("Hora de inicio");
        whiteBack.add(inicioTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        salonTxt.setBackground(new java.awt.Color(247, 247, 247));
        salonTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        salonTxt.setForeground(new java.awt.Color(145, 145, 145));
        salonTxt.setText("ej: Salon 301, Bloque 7");
        salonTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salonTxtMouseClicked(evt);
            }
        });
        salonTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salonTxtActionPerformed(evt);
            }
        });
        whiteBack.add(salonTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 200, -1));

        inicioTxt.setBackground(new java.awt.Color(247, 247, 247));
        inicioTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        inicioTxt.setForeground(new java.awt.Color(145, 145, 145));
        inicioTxt.setText("ej: 2:00 pm");
        inicioTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inicioTxtMouseClicked(evt);
            }
        });
        inicioTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioTxtActionPerformed(evt);
            }
        });
        whiteBack.add(inicioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 90, -1));

        subirScheBtn.setBackground(new java.awt.Color(64, 174, 178));

        subirScheTxt.setFont(new java.awt.Font("Questrial", 0, 14)); // NOI18N
        subirScheTxt.setForeground(new java.awt.Color(255, 255, 255));
        subirScheTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subirScheTxt.setText("Subir");
        subirScheTxt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                subirScheTxtMouseMoved(evt);
            }
        });
        subirScheTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subirScheTxtMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                subirScheTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout subirScheBtnLayout = new javax.swing.GroupLayout(subirScheBtn);
        subirScheBtn.setLayout(subirScheBtnLayout);
        subirScheBtnLayout.setHorizontalGroup(
            subirScheBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subirScheTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        subirScheBtnLayout.setVerticalGroup(
            subirScheBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subirScheTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        whiteBack.add(subirScheBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 120, 30));

        watchIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/watchCheck.png"))); // NOI18N
        whiteBack.add(watchIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, -1, 40));

        javax.swing.GroupLayout scheduleSectionLayout = new javax.swing.GroupLayout(scheduleSection);
        scheduleSection.setLayout(scheduleSectionLayout);
        scheduleSectionLayout.setHorizontalGroup(
            scheduleSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scheduleSectionLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(whiteBack, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        scheduleSectionLayout.setVerticalGroup(
            scheduleSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(whiteBack, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        background.add(scheduleSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 300, 360));

        add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 560));
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backBtnMouseClicked
        if (onBack != null) {
            onBack.run();
        }
    }//GEN-LAST:event_backBtnMouseClicked

    private void actDescripTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actDescripTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_actDescripTxtActionPerformed

    private void actTitleTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actTitleTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_actTitleTxtActionPerformed

    private void vidTitleTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vidTitleTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vidTitleTxtActionPerformed

    private void vidDescripTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vidDescripTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vidDescripTxtActionPerformed

    private void diaTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diaTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_diaTxtActionPerformed

    private void salidaTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salidaTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salidaTxtActionPerformed

    private void salonTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salonTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salonTxtActionPerformed

    private void inicioTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inicioTxtActionPerformed

    private void subirScheTxtMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirScheTxtMouseMoved
        subirScheBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_subirScheTxtMouseMoved

    private void subirScheTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirScheTxtMouseExited
        subirScheBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_subirScheTxtMouseExited

    private void plusVideosMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusVideosMouseMoved
        bigloadvidBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_plusVideosMouseMoved

    private void plusVideosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusVideosMouseExited
        bigloadvidBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_plusVideosMouseExited

    private void subirVidTxtMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidTxtMouseMoved
        subirVidBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_subirVidTxtMouseMoved

    private void subirVidTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidTxtMouseExited
        subirVidBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_subirVidTxtMouseExited

    private void subirVidBtnMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidBtnMouseMoved
        subirVidBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_subirVidBtnMouseMoved

    private void subirVidBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidBtnMouseExited
        subirVidBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_subirVidBtnMouseExited

    private void plusActsMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusActsMouseMoved
        bigloadactsBtn.setBackground(new Color(114, 112, 112));
    }//GEN-LAST:event_plusActsMouseMoved

    private void plusActsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusActsMouseExited
        bigloadactsBtn.setBackground(new Color(145, 145, 145));
    }//GEN-LAST:event_plusActsMouseExited

    private void subirActTxtMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirActTxtMouseMoved
        subirActBtn.setBackground(new Color(114, 112, 112));
    }//GEN-LAST:event_subirActTxtMouseMoved

    private void subirActTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirActTxtMouseExited
        subirActBtn.setBackground(new Color(145, 145, 145));
    }//GEN-LAST:event_subirActTxtMouseExited

    private void subirActBtnMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirActBtnMouseMoved
        subirActBtn.setBackground(new Color(114, 112, 112));
    }//GEN-LAST:event_subirActBtnMouseMoved

    private void subirActBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirActBtnMouseExited
        subirActBtn.setBackground(new Color(145, 145, 145));
    }//GEN-LAST:event_subirActBtnMouseExited

    private void plusVideosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusVideosMouseClicked
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Videos", "mp4", "avi", "mov"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            videoFile = chooser.getSelectedFile();
            plusVideos.setText(videoFile.getName());
            plusVideos.setFont(new java.awt.Font("Poppins", 0, 14));
        }
    }//GEN-LAST:event_plusVideosMouseClicked

    private void plusActsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusActsMouseClicked
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PDF, DOCX", "pdf", "docx"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            actividadFile = chooser.getSelectedFile();
            plusActs.setText(actividadFile.getName());
            plusActs.setFont(new java.awt.Font("Poppins", 0, 14));
        }

    }//GEN-LAST:event_plusActsMouseClicked

    private void subirVidTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidTxtMouseClicked
        subirVideo();
    }//GEN-LAST:event_subirVidTxtMouseClicked

    private void subirActTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirActTxtMouseClicked
        subirActividad();
    }//GEN-LAST:event_subirActTxtMouseClicked

    private void subirVidBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirVidBtnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_subirVidBtnMouseClicked

    private void vidTitleTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vidTitleTxtMouseClicked
        if (vidTitleTxt.getText().equals("Título del video")) {
            vidTitleTxt.setText("");
        }
    }//GEN-LAST:event_vidTitleTxtMouseClicked

    private void vidDescripTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vidDescripTxtMouseClicked
        if (vidDescripTxt.getText().equals("Descripción del video")) {
            vidDescripTxt.setText("");
        }
    }//GEN-LAST:event_vidDescripTxtMouseClicked

    private void actTitleTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actTitleTxtMouseClicked
        if (actTitleTxt.getText().equals("Título de la actividad")) {
            actTitleTxt.setText("");
        }
    }//GEN-LAST:event_actTitleTxtMouseClicked

    private void actDescripTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actDescripTxtMouseClicked
        if (actDescripTxt.getText().equals("Descripción de la actividad")) {
            actDescripTxt.setText("");
        }
    }//GEN-LAST:event_actDescripTxtMouseClicked

    private void salonTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salonTxtMouseClicked
        if (salonTxt.getText().equals("ej: Salon 301, Bloque 7")) {
            salonTxt.setText("");
        }
    }//GEN-LAST:event_salonTxtMouseClicked

    private void diaTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diaTxtMouseClicked
        if (diaTxt.getText().equals("ej: Miércoles, 11/11/25")) {
            diaTxt.setText("");
        }
    }//GEN-LAST:event_diaTxtMouseClicked

    private void inicioTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inicioTxtMouseClicked
        if (inicioTxt.getText().equals("ej: 2:00 pm")) {
            inicioTxt.setText("");
        }
    }//GEN-LAST:event_inicioTxtMouseClicked

    private void salidaTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salidaTxtMouseClicked
        if (salidaTxt.getText().equals("ej: 4:00 pm")) {
            salidaTxt.setText("");
        }
    }//GEN-LAST:event_salidaTxtMouseClicked

    private void subirScheTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subirScheTxtMouseClicked
        // EVENTOOOO
    }//GEN-LAST:event_subirScheTxtMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actDescrip;
    private javax.swing.JTextField actDescripTxt;
    private javax.swing.JLabel actTitle;
    private javax.swing.JTextField actTitleTxt;
    private javax.swing.JPanel actsSection;
    private javax.swing.JLabel actsTitle;
    private javax.swing.JLabel asigName;
    private javax.swing.JPanel backBtn;
    private javax.swing.JPanel background;
    private javax.swing.JPanel bigloadactsBtn;
    private javax.swing.JPanel bigloadvidBtn;
    private javax.swing.JLabel diaTitle;
    private javax.swing.JTextField diaTxt;
    private javax.swing.JLabel horariosTitle;
    private javax.swing.JLabel inicioTitle;
    private javax.swing.JTextField inicioTxt;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel moniandtutoTitle;
    private javax.swing.JLabel plusActs;
    private javax.swing.JLabel plusVideos;
    private javax.swing.JLabel salidaTitle;
    private javax.swing.JTextField salidaTxt;
    private javax.swing.JLabel salonTitle;
    private javax.swing.JTextField salonTxt;
    private javax.swing.JPanel scheduleSection;
    private javax.swing.JPanel subirActBtn;
    private javax.swing.JLabel subirActTxt;
    private javax.swing.JPanel subirScheBtn;
    private javax.swing.JLabel subirScheTxt;
    private javax.swing.JPanel subirVidBtn;
    private javax.swing.JLabel subirVidTxt;
    private javax.swing.JLabel vidDescripTitle;
    private javax.swing.JTextField vidDescripTxt;
    private javax.swing.JLabel vidTitle;
    private javax.swing.JTextField vidTitleTxt;
    private javax.swing.JPanel videosSection;
    private javax.swing.JLabel videosTitle;
    private javax.swing.JLabel watchIcon;
    private javax.swing.JPanel whiteBack;
    // End of variables declaration//GEN-END:variables
}
