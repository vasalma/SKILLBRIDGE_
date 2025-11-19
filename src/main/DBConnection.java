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

public class DBConnection {

    // ✅ AÑADIDO para el patrón Singleton (la instancia única)
    private static DBConnection instance = null;

    private static Connection conn = null;
    private static final String DB_PATH = "database/skillbridge.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    // ✅ AÑADIDO: Constructor privado para evitar que la clase se instancie directamente.
    private DBConnection() {
        // La lógica de conexión ya se maneja en el método estático getConnection().
    }

    // ✅ AÑADIDO: El método estático que faltaba para obtener la instancia.
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

    public static List<String[]> obtenerVideosRecientes() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT titulo, descripcion, idMateria, videourl FROM videos";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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

    // ... (El resto de tus métodos se mantienen igual: registrarAsignaturaEnBD, guardarAsignaturaDocente, etc.)
    // ------------------------------
    // Registrar asignatura en tabla materias (CATÁLOGO)
    // ------------------------------
    public static Asignatura registrarAsignaturaEnBD(String id, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
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
    /**
     * Inserta la asignación de una materia a un docente.
     *
     * @param idDocente ID del docente.
     * @param nombre Nombre de la materia (usado como FK implícita).
     * @param descripcion Descripción de la materia.
     */
    public static void guardarAsignaturaDocente(String idDocente, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
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
    /**
     * Elimina el vínculo de la asignatura de la tabla 'docente' (Asignación).
     *
     * @param idDocente ID del docente.
     * @param nombre Nombre de la materia.
     * @param descripcion Descripción de la materia.
     */
    public static void eliminarAsignaturaDocente(String idDocente, String nombre, String descripcion) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
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
    // OBTENER ASIGNATURAS ASIGNADAS AL DOCENTE ACTUAL (¡FILTRADO CORREGIDO!)
    // ------------------------------
    /**
     * Recupera solo las asignaturas que han sido asignadas al ID de docente
     * especificado. La consulta se corrige para unir por Nombre Y Descripción,
     * resolviendo el problema de las asignaturas homónimas.
     *
     * @param idDocente ID del docente logueado.
     * @return Lista de Asignaturas.
     */
    public static List<Asignatura> obtenerAsignaturasDocente(String idDocente) {
        List<Asignatura> asignaturas = new ArrayList<>();

        // 🚀 CONSULTA CORREGIDA: Se añade la DESCRIPCIÓN a la cláusula ON para asegurar que solo una asignatura 
        // de la tabla 'materias' coincida con la asignación del docente.
        String sql = "SELECT m.id, m.nombre, m.descripcion FROM materias m "
                + "INNER JOIN docente d ON m.nombre = d.asignatura AND m.descripcion = d.descripcion "
                + "WHERE d.idDocente = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    // ✅ MÉTODO: Ejecutar UPDATE genérico
    // ------------------------------
    public static boolean ejecutarUpdate(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    // ------------------------------
    // ✅ NUEVO MÉTODO CORREGIDO: Insertar actividad
    // ------------------------------
    /**
     * Inserta una nueva actividad en la base de datos. Coincide con la tabla
     * 'actividades'.
     */
    public static boolean insertarActividad(String idDocente, String titulo, String descripcion, String idMateria, String actividadurl) {
        String sql = "INSERT INTO actividades (idDocente, titulo, descripcion, idMateria, actividadurl) VALUES (?, ?, ?, ?, ?)";

        // Usamos los 5 parámetros que requiere tu tabla
        boolean exito = ejecutarUpdate(sql, idDocente, titulo, descripcion, idMateria, actividadurl);
        if (exito) {
            System.out.println("✔ Actividad '" + titulo + "' insertada.");
        } else {
            System.out.println("❌ Error al insertar actividad.");
        }
        return exito;
    }

    // ------------------------------
    // ✅ NUEVO MÉTODO CORREGIDO: Insertar video
    // ------------------------------
    /**
     * Inserta un nuevo video en la base de datos. Coincide con la tabla
     * 'videos'.
     */
    public static boolean insertarVideo(String idDocente, String titulo, String descripcion, String idMateria, String videourl) {
        // Corregido: Tu tabla usa 'descripcion', no 'description'
        String sql = "INSERT INTO videos (idDocente, titulo, descripcion, idMateria, videourl) VALUES (?, ?, ?, ?, ?)";

        // Usamos los 5 parámetros que requiere tu tabla
        boolean exito = ejecutarUpdate(sql, idDocente, titulo, descripcion, idMateria, videourl);
        if (exito) {
            System.out.println("✔ Video '" + titulo + "' insertado.");
        } else {
            System.out.println("❌ Error al insertar video.");
        }
        return exito;
    }

    // ------------------------------
    // ✅ NUEVO MÉTODO CORREGIDO: Obtener actividades por materia
    // ------------------------------
    /**
     * Obtiene la lista de actividades (título, descripción y URL) para una
     * materia. Devuelve un array de String por cada fila: [titulo, descripcion,
     * actividadurl]
     */
    public static List<String[]> obtenerActividadesPorMateria(String idMateria) {
        List<String[]> actividades = new ArrayList<>();
        // Corregido: Seleccionamos los campos útiles, incluyendo la URL
        String sql = "SELECT titulo, descripcion, actividadurl FROM actividades WHERE idMateria = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] actividad = {
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("actividadurl") // Se necesita la URL para mostrarla/descargarla
                };
                actividades.add(actividad);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo actividades: " + e.getMessage());
        }
        return actividades;
    }

    // ------------------------------
    // ✅ NUEVO MÉTODO CORREGIDO: Obtener videos por materia
    // ------------------------------
    /**
     * Obtiene la lista de videos (título, descripción y URL) para una materia.
     * Devuelve un array de String por cada fila: [titulo, descripcion,
     * videourl]
     */
    public static List<String[]> obtenerVideosPorMateria(String idMateria) {
        List<String[]> videos = new ArrayList<>();
        // Corregido: Tu tabla usa 'descripcion' y necesitamos 'videourl'
        String sql = "SELECT titulo, descripcion, videourl FROM videos WHERE idMateria = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] video = {
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("videourl") // Se necesita la URL para ver el video
                };
                videos.add(video);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo videos: " + e.getMessage());
        }
        return videos;
    }
}
