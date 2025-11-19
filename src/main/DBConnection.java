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

        // Selecciona solo el nombre y la descripción para el catálogo
        String sql = "SELECT DISTINCT asignatura, descripcion FROM docente";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String nombre = rs.getString("asignatura");

                String descripcion = rs.getString("descripcion");

                // Nota: Aquí se usa el constructor de 2 parámetros, por lo que el ID en el objeto Asignatura
                // será 'nombre' (si se aplicó la corrección en Asignatura.java) o 'null'.
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

        // Asumimos que la tabla 'docente' no tiene un campo id propio, solo la info de la materia
        String sql = "SELECT asignatura, descripcion FROM docente WHERE idDocente = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDocente);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    String nombre = rs.getString("asignatura");

                    String descripcion = rs.getString("descripcion");

                    // Usa el constructor de 2 parámetros que, idealmente, asigna el 'nombre' como 'id'.
                    asignaturas.add(new Asignatura(nombre, descripcion));

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
    // Ejecutar UPDATE genérico
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
    // Insertar actividad
    // Se usa PreparedStatement directo para un mejor manejo de excepciones de BD (como NOT NULL)
    // ------------------------------
    public static boolean insertarActividad(String idDocente, String titulo, String descripcion, String idMateria, String actividadurl) {

        String sql = "INSERT INTO actividades (idDocente, titulo, descripcion, idMateria, actividadurl) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDocente);

            stmt.setString(2, titulo);

            stmt.setString(3, descripcion);

            stmt.setString(4, idMateria); // Aquí el valor que debe ser NOT NULL

            stmt.setString(5, actividadurl);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println("✔ Actividad '" + titulo + "' insertada.");

                return true;

            } else {

                System.out.println("❌ Error al insertar actividad (0 filas afectadas).");

                return false;

            }

        } catch (SQLException e) {

            System.out.println("❌ Error al insertar actividad: " + e.getMessage());

            return false;

        }

    }

    // ------------------------------
    // Insertar video
    // Se usa PreparedStatement directo para un mejor manejo de excepciones de BD (como NOT NULL)
    // ------------------------------
    public static boolean insertarVideo(String idDocente, String titulo, String descripcion, String idMateria, String videourl) {

        String sql = "INSERT INTO videos (idDocente, titulo, descripcion, idMateria, videourl) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idDocente);

            stmt.setString(2, titulo);

            stmt.setString(3, descripcion);

            stmt.setString(4, idMateria); // Aquí el valor que debe ser NOT NULL

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
    // Obtener actividades por materia
    // ------------------------------
    public static List<String[]> obtenerActividadesPorMateria(String idMateria) {

        List<String[]> actividades = new ArrayList<>();

        String sql = "SELECT titulo, descripcion, actividadurl FROM actividades WHERE idMateria = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

}
