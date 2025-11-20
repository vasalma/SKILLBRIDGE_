package Materia;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import main.DBConnection;
import Materia.video;
import Materia.actividad;

public class panelAsig extends javax.swing.JPanel {

    private String idMateria;

    public panelAsig() {
        initComponents();

        if (videos == null) {
            videos = new javax.swing.JPanel();
        }
        if (acts == null) {
            acts = new javax.swing.JPanel();
        }

        configurarContenedorVideos();
        configurarContenedorActividades();
    }

    public panelAsig(String idMateria) {
        initComponents();
        this.idMateria = idMateria;

        if (videos == null) {
            videos = new javax.swing.JPanel();
        }
        if (acts == null) {
            acts = new javax.swing.JPanel();
        }

        configurarContenedorVideos();
        configurarContenedorActividades();

        if (idMateria != null) {
            cargarVideos();
            cargarActividades();
        }
    }

    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
        if (idMateria != null) {
            cargarVideos();
            cargarActividades();
        }
    }

    private void configurarContenedorVideos() {
        videos.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        videos.setBackground(new Color(240, 240, 240));
    }

    private void configurarContenedorActividades() {
        acts.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        acts.setBackground(new Color(240, 240, 240));
    }

    public void cargarVideos() {
        if (idMateria == null) {
            System.out.println("idMateria es null, no se cargan videos");
            return;
        }

        videos.removeAll();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT titulo, descripcion, videoUrl FROM videos WHERE idMateria = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idMateria);

            ResultSet rs = pst.executeQuery();

            int contador = 1;
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String videoUrl = rs.getString("videoUrl");

                video videoComponente = new video(titulo, descripcion, contador);
                videoComponente.setVideoFilePath(videoUrl);

                final String tituloFinal = titulo;
                videoComponente.setOnEliminarListener(() -> {
                    eliminarVideo(tituloFinal);
                });

                videos.add(videoComponente);
                contador++;
            }

            if (contador == 1) {
                JLabel mensaje = new JLabel("No hay videos disponibles");
                mensaje.setForeground(Color.GRAY);
                mensaje.setFont(new Font("Poppins", Font.ITALIC, 14));
                videos.add(mensaje);
            }

            videos.revalidate();
            videos.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar videos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargarActividades() {
        if (idMateria == null) {
            System.out.println("idMateria es null, no se cargan actividades");
            return;
        }

        acts.removeAll();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT titulo, descripcion, actividadurl FROM actividades WHERE idMateria = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idMateria);

            ResultSet rs = pst.executeQuery();

            int contador = 1;
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String actividadUrl = rs.getString("actividadurl");

                String[] actividadData = {titulo, descripcion, actividadUrl};

                actividad actividadComponente = new actividad(actividadData);

                final String tituloFinal = titulo;

                actividadComponente.setOnEliminarListener(() -> {
                    eliminarActividad(tituloFinal);
                });

                acts.add(actividadComponente);
                contador++;
            }

            if (contador == 1) {
                JLabel mensaje = new JLabel("No hay actividades disponibles");
                mensaje.setForeground(Color.GRAY);
                mensaje.setFont(new Font("Poppins", Font.ITALIC, 14));
                acts.add(mensaje);
            }

            acts.revalidate();
            acts.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar actividades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void eliminarVideo(String titulo) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM videos WHERE titulo = ? AND idMateria = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, titulo);
            pst.setString(2, idMateria);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Video eliminado correctamente");
                cargarVideos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el video");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar video: " + e.getMessage());
        }
    }

    private void eliminarActividad(String titulo) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM actividades WHERE titulo = ? AND idMateria = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, titulo);
            pst.setString(2, idMateria);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Actividad eliminada correctamente");
                cargarActividades();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la actividad");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar actividad: " + e.getMessage());
        }
    }

    public void refrescarVideos() {
        cargarVideos();
    }

    public void refrescarActividades() {
        cargarActividades();
    }

    private Runnable onIrExtra;

    public void setOnIrExtra(Runnable r) {
        this.onIrExtra = r;
    }

    public javax.swing.JLabel getAsigName() {
        return asigName;
    }

    public javax.swing.JPanel getBackBtn() {
        return backBtn;
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
        actsSection = new javax.swing.JPanel();
        actsTitle = new javax.swing.JLabel();
        loadactsBtn = new javax.swing.JPanel();
        loadIcon = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        acts = new javax.swing.JPanel();
        videosSection = new javax.swing.JPanel();
        videosTitle = new javax.swing.JLabel();
        loadvidBtn = new javax.swing.JPanel();
        loadIcon1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        videos = new javax.swing.JPanel();
        asigName = new javax.swing.JLabel();
        slash = new javax.swing.JLabel();

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

        actsSection.setBackground(new java.awt.Color(255, 255, 255));
        actsSection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        actsTitle.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        actsTitle.setForeground(new java.awt.Color(0, 0, 0));
        actsTitle.setText("Actividades");
        actsSection.add(actsTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        loadactsBtn.setBackground(new java.awt.Color(64, 174, 178));

        loadIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loadIcon.png"))); // NOI18N
        loadIcon.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                loadIconMouseMoved(evt);
            }
        });
        loadIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadIconMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loadIconMouseExited(evt);
            }
        });

        javax.swing.GroupLayout loadactsBtnLayout = new javax.swing.GroupLayout(loadactsBtn);
        loadactsBtn.setLayout(loadactsBtnLayout);
        loadactsBtnLayout.setHorizontalGroup(
            loadactsBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        loadactsBtnLayout.setVerticalGroup(
            loadactsBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
        );

        actsSection.add(loadactsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 37, -1, -1));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        acts.setBackground(new java.awt.Color(255, 255, 255));
        acts.setLayout(new javax.swing.BoxLayout(acts, javax.swing.BoxLayout.X_AXIS));
        jScrollPane2.setViewportView(acts);

        actsSection.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 6, 796, 190));

        background.add(actsSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 950, 210));

        videosSection.setBackground(new java.awt.Color(255, 255, 255));
        videosSection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        videosTitle.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        videosTitle.setForeground(new java.awt.Color(0, 0, 0));
        videosTitle.setText("Videos");
        videosSection.add(videosTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        loadvidBtn.setBackground(new java.awt.Color(64, 174, 178));

        loadIcon1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loadIcon.png"))); // NOI18N
        loadIcon1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                loadIcon1MouseMoved(evt);
            }
        });
        loadIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadIcon1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loadIcon1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout loadvidBtnLayout = new javax.swing.GroupLayout(loadvidBtn);
        loadvidBtn.setLayout(loadvidBtnLayout);
        loadvidBtnLayout.setHorizontalGroup(
            loadvidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        loadvidBtnLayout.setVerticalGroup(
            loadvidBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
        );

        videosSection.add(loadvidBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 37, -1, -1));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        videos.setBackground(new java.awt.Color(255, 255, 255));
        videos.setLayout(new javax.swing.BoxLayout(videos, javax.swing.BoxLayout.X_AXIS));
        jScrollPane1.setViewportView(videos);

        videosSection.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 6, 796, 190));

        background.add(videosSection, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 950, 220));

        asigName.setFont(new java.awt.Font("Questrial", 0, 26)); // NOI18N
        asigName.setForeground(new java.awt.Color(221, 224, 229));
        asigName.setText("// Asignatura");
        background.add(asigName, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 50, -1, -1));

        slash.setFont(new java.awt.Font("Questrial", 0, 26)); // NOI18N
        slash.setForeground(new java.awt.Color(221, 224, 229));
        slash.setText("//");
        background.add(slash, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 560));
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backBtnMouseClicked


    }//GEN-LAST:event_backBtnMouseClicked

    private void loadIcon1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIcon1MouseMoved
        loadvidBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_loadIcon1MouseMoved

    private void loadIcon1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIcon1MouseExited
        loadvidBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_loadIcon1MouseExited

    private void loadIconMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIconMouseMoved
        loadactsBtn.setBackground(new Color(38, 114, 116));
    }//GEN-LAST:event_loadIconMouseMoved

    private void loadIconMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIconMouseExited
        loadactsBtn.setBackground(new Color(4, 174, 178));
    }//GEN-LAST:event_loadIconMouseExited

    private void loadIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIcon1MouseClicked
        if (onIrExtra != null) {
            onIrExtra.run();
        }

    }//GEN-LAST:event_loadIcon1MouseClicked

    private void loadIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadIconMouseClicked
        if (onIrExtra != null) {
            onIrExtra.run();
        }
    }//GEN-LAST:event_loadIconMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel acts;
    private javax.swing.JPanel actsSection;
    private javax.swing.JLabel actsTitle;
    private javax.swing.JLabel asigName;
    private javax.swing.JPanel backBtn;
    private javax.swing.JPanel background;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel loadIcon;
    private javax.swing.JLabel loadIcon1;
    private javax.swing.JPanel loadactsBtn;
    private javax.swing.JPanel loadvidBtn;
    private javax.swing.JLabel slash;
    private javax.swing.JPanel videos;
    private javax.swing.JPanel videosSection;
    private javax.swing.JLabel videosTitle;
    // End of variables declaration//GEN-END:variables
}

