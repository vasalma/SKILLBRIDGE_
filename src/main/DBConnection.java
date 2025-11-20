package main;

import Materia.Asignatura;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; 

public class DBConnection {

    // ------------------------------
    // Configuración Singleton
    // ------------------------------
    private static DBConnection instance = null;

    private static Connection conn = null;

    private static final String DB_PATH = "database/skillbridge.db";

    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    private DBConnection() {
        // Constructor privado
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // ------------------------------
    // Conexión
    // ------------------------------
    public static Connection getConnection() {
        try {
            File dbDir = new File("database");
            if (!dbDir.exists()) {
                dbDir.mkdir();
                System.out.println("📁 Carpeta 'database' creada.");
            }

            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL);
                conn.createStatement().execute("PRAGMA busy_timeout = 5000;");
                System.out.println("✅ Conexión establecida con la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
        return conn;
    }

    // ------------------------------
    // Cerrar conexión
    // ------------------------------
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // OBTENER TODO EL CATÁLOGO ÚNICO DESDE 'DOCENTE'
    // -------------------------------------------------------------
    public static List<Asignatura> obtenerTodasLasAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>();
        String sql = "SELECT DISTINCT asignatura, descripcion FROM docente";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("asignatura");
                String descripcion = rs.getString("descripcion");
                asignaturas.add(new Asignatura(nombre, descripcion));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener todas las asignaturas desde 'docente': " + e.getMessage());
        }
        return asignaturas;
    }

    // -------------------------------------------------------------
    // OBTENER ASIGNATURAS ASIGNADAS AL DOCENTE 
    // -------------------------------------------------------------
    public static List<Asignatura> obtenerAsignaturasDocente(String idDocente) {
        List<Asignatura> asignaturas = new ArrayList<>();
        String sql = "SELECT m.id, m.nombre, m.descripcion FROM materias m "
                + "INNER JOIN docente d ON m.nombre = d.asignatura AND m.descripcion = d.descripcion "
                + "WHERE d.idDocente = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDocente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    asignaturas.add(new Asignatura(id, nombre, descripcion));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener asignaturas del docente: " + e.getMessage());
        }
        return asignaturas;
    }

    // ------------------------------
    // Registrar asignatura en tabla materias (CATÁLOGO)
    // ------------------------------
    public static Asignatura registrarAsignaturaEnBD(String id, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO materias (id, nombre, descripcion) VALUES (?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                System.out.println("✔ Asignatura registrada en materias (Catálogo).");
                return new Asignatura(id, nombre, descripcion);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("❌ Error: La asignatura con ID " + id + " ya existe en el catálogo.");
            } else {
                System.out.println("❌ Error registrando asignatura: " + e.getMessage());
            }
        }
        return null;
    }

    // ------------------------------
    // Guardar asignatura en tabla docente (ASIGNACIÓN)
    // ------------------------------
    public static void guardarAsignaturaDocente(String idDocente, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO docente (idDocente, asignatura, descripcion) VALUES (?, ?, ?)"
        )) {
            stmt.setString(1, idDocente);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            stmt.executeUpdate();
            System.out.println("✅ Asignatura asociada al docente: " + idDocente + " (Nombre: " + nombre + ")");
        } catch (SQLException e) {
            System.out.println("❌ Error guardando asignatura en docente: " + e.getMessage());
        }
    }

    // ------------------------------
    // Eliminar asignatura SOLO del docente (DESASIGNAR)
    // ------------------------------
    public static void eliminarAsignaturaDocente(String idDocente, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM docente WHERE idDocente = ? AND asignatura = ? AND descripcion = ?"
        )) {
            stmt.setString(1, idDocente);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑 Asignatura eliminada SOLO del docente " + idDocente + " (Nombre: " + nombre + ").");
            } else {
                System.out.println("⚠️ Advertencia: No se encontró la asignación para eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error eliminando asignatura del docente: " + e.getMessage());
        }
    }

    // ------------------------------
    // Ejecutar UPDATE genérico
    // ------------------------------
    public static boolean ejecutarUpdate(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer parámetros
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error en ejecutarUpdate: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertarActividad(String idDocente, String titulo, String descripcion, String idMateria, String actividadurl) {
        String sql = "INSERT INTO actividades (idDocente, titulo, descripcion, idMateria, actividadurl) VALUES (?, ?, ?, ?, ?)";
        boolean exito = ejecutarUpdate(sql, idDocente, titulo, descripcion, idMateria, actividadurl);

        if (exito) {
            System.out.println("✔ Actividad '" + titulo + "' insertada.");
        } else {
            System.out.println("❌ Error al insertar actividad.");
        }
        return exito;
    }

    // ------------------------------
    // Insertar Horario de Excepción
    // ------------------------------
    public static boolean insertarHorarioExcepcion(String idDocente, String salon, String fecha, String horaInicio, String horaFin, String idMateria) {
        // Generar un ID único tipo TEXT (UUID) para la PRIMARY KEY.
        String uniqueId = UUID.randomUUID().toString();
        
        String sql = "INSERT INTO horarios (id, idDocente, salon, fecha, hora_inicio, hora_fin, idMateria) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uniqueId); 
            stmt.setString(2, idDocente);
            stmt.setString(3, salon);
            stmt.setString(4, fecha);
            stmt.setString(5, horaInicio);
            stmt.setString(6, horaFin);
            stmt.setString(7, idMateria);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✔ Excepción de Horario insertada (ID: " + uniqueId + ") para Docente " + idDocente + " en Materia " + idMateria + ".");
                return true;
            } else {
                System.out.println("❌ Error al insertar excepción de horario (0 filas afectadas).");
                return false;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("❌ Error de Horario: La clave única ID falló.");
            } else {
                System.out.println("❌ Error al insertar excepción de horario: " + e.getMessage());
            }
            return false;
        }
    }

    // ------------------------------
    // Insertar video
    // ------------------------------
    public static boolean insertarVideo(String idDocente, String titulo, String descripcion, String idMateria, String videourl) {
        String sql = "INSERT INTO videos (idDocente, titulo, descripcion, idMateria, videourl) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDocente);
            stmt.setString(2, titulo);
            stmt.setString(3, descripcion);
            stmt.setString(4, idMateria);
            stmt.setString(5, videourl);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✔ Video '" + titulo + "' insertado.");
                return true;
            } else {
                System.out.println("❌ Error al insertar video (0 filas afectadas).");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar video: " + e.getMessage());
            return false;
        }
    }

    public static List<String[]> obtenerVideosRecientes() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT titulo, descripcion, idMateria, videourl FROM videos";

        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String idMateria = rs.getString("idMateria");
                String rutaVideo = rs.getString("videourl");
                lista.add(new String[]{titulo, descripcion, idMateria, rutaVideo});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ------------------------------
    // Obtener actividades por materia
    // ------------------------------
    public static List<String[]> obtenerActividadesPorMateria(String idMateria) {
        List<String[]> actividades = new ArrayList<>();
        String sql = "SELECT titulo, descripcion, actividadurl FROM actividades WHERE idMateria = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] actividad = {
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("actividadurl")
                };
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo actividades: " + e.getMessage());
        }
        return actividades;
    }

    // ------------------------------
    // Obtener videos por materia
    // ------------------------------
    public static List<String[]> obtenerVideosPorMateria(String idMateria) {
        List<String[]> videos = new ArrayList<>();
        String sql = "SELECT titulo, descripcion, videourl FROM videos WHERE idMateria = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] video = {
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("videourl")
                };
                videos.add(video);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo videos: " + e.getMessage());
        }
        return videos;
    }

    public static List<String[]> obtenerActividadesRecientes() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT a.titulo, a.descripcion, m.nombre as materia, a.actividadurl, u.nombre, u.apellido "
                + "FROM actividades a "
                + "LEFT JOIN materias m ON a.idMateria = m.id "
                + "LEFT JOIN usuarios u ON a.idDocente = u.id";

        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String materia = rs.getString("materia");
                String rutaActividad = rs.getString("actividadurl");
                String nombreDocente = rs.getString("nombre") + " " + rs.getString("apellido");

                lista.add(new String[]{titulo, nombreDocente, materia, rutaActividad});
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en obtenerActividadesRecientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // =========================================================================
    // FUNCIONES ADICIONALES (Consultas)
    // =========================================================================

    /**
     * Consulta y retorna TODAS las excepciones de horario publicadas.
     * Muestra todos los horarios de excepción, ya que son visibles para todos los estudiantes.
     *
     * @param idBusqueda ID del ESTUDIANTE logueado (No se usa, se mantiene la firma).
     * @return List<String[]> con {idDocente, salon, fecha, hora_inicio, hora_fin, idMateria, nombreAsignatura}
     */
    public static List<String[]> consultarHorarioExcepcion(String idBusqueda) {
        List<String[]> horarios = new ArrayList<>();
        
        // CONSULTA CORREGIDA: Se eliminó el JOIN a 'inscripciones' y la cláusula WHERE por idEstudiante.
        String sql = "SELECT h.idDocente, h.salon, h.fecha, h.hora_inicio, h.hora_fin, h.idMateria, m.nombre AS nombreAsignatura "
                + "FROM horarios h "
                + "INNER JOIN materias m ON h.idMateria = m.id "
                + "ORDER BY h.fecha, h.hora_inicio DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // NO se necesita establecer parámetros
            
            try(ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String[] horarioData = {
                        rs.getString("idDocente"),      // Índice 0 (Docente ID)
                        rs.getString("salon"),          // Índice 1
                        rs.getString("fecha"),          // Índice 2
                        rs.getString("hora_inicio"),    // Índice 3
                        rs.getString("hora_fin"),       // Índice 4
                        rs.getString("idMateria"),      // Índice 5 (Materia ID)
                        rs.getString("nombreAsignatura")// Índice 6 (Nombre para la tarjeta)
                    };
                    horarios.add(horarioData);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error consultando horarios de excepción: " + e.getMessage());
        }
        return horarios;
    }

    /**
     * Obtiene el nombre completo (nombre + apellido) de un usuario por su ID.
     */
    public static String obtenerNombreCompletoUsuario(String idUsuario) {
        String nombreCompleto = "Usuario Desconocido";
        String sql = "SELECT nombre, apellido FROM usuarios WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                nombreCompleto = nombre + " " + apellido;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo nombre de usuario: " + e.getMessage());
        }
        return nombreCompleto;
    }

    /**
     * Obtiene el nombre de la materia a partir de su ID.
     */
    public static String obtenerNombreMateria(String idMateria) {
        String nombreMateria = "Materia Desconocida";
        String sql = "SELECT nombre FROM materias WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idMateria);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombreMateria = rs.getString("nombre");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo nombre de materia: " + e.getMessage());
        }
        return nombreMateria;
    }

}