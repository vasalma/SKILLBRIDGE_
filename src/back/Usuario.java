package back;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.DBConnection;

public class Usuario {

    //  Atributos//
    protected String id;
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String contraseña;
    protected String rol;
    protected String telefono;

   
    public Usuario() {
    }

    //Constructor usuario //
    public Usuario(String id, String nombre, String apellido, String correo, String contraseña, String rol, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.rol = rol;
        this.telefono = telefono;
    }

    // ✅ Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // ✅ Verificar si el correo ya existe
    public boolean existeCorreo(String correo) {
        String sql = "SELECT id FROM usuarios WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("⚠️ Error al verificar correo: " + e.getMessage());
            return false;
        }
    }

    // ✅ Registrar usuario
    public boolean registrarUsuarioSQLite(String id, String nombre, String apellido,
            String correo, String contraseña, String rol, String telefono) {
        String sql = "INSERT INTO usuarios (id, nombre, apellido, correo, contraseña, rol, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, correo);
            pstmt.setString(5, contraseña);
            pstmt.setString(6, rol);
            pstmt.setString(7, telefono);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // ✅ Validar login por ID y contraseña
    public boolean validarLoginPorID(String idTexto, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND contraseña = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idTexto);
            pstmt.setString(2, contraseña);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getString("id");
                this.nombre = rs.getString("nombre");
                this.apellido = rs.getString("apellido");
                this.correo = rs.getString("correo");
                this.contraseña = rs.getString("contraseña");
                this.rol = rs.getString("rol");
                this.telefono = rs.getString("telefono");
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al validar login: " + e.getMessage());
            return false;
        }
    }

    // ✅ Obtener usuario completo por ID
    public static Usuario obtenerUsuarioPorID(String idTexto) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idTexto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        rs.getString("telefono")
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario: " + e.getMessage());
            return null;
        }
    }

    // ✅ Obtener usuario completo por correo (para login)
    public static Usuario obtenerUsuarioPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        rs.getString("telefono")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por correo: " + e.getMessage());
            return null;
        }
    }

    // ✅ Actualizar información del usuario
    public boolean actualizarUsuarioSQLite(String id, String nombre, String apellido,
            String correo, String telefono) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, correo=?, telefono=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, correo);
            pstmt.setString(4, telefono);
            pstmt.setString(5, id);
            pstmt.executeUpdate();

            // Actualiza los datos también en el objeto actual
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.telefono = telefono;

            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // ✅ Cambiar contraseña del usuario
    public boolean actualizarContraseña(String id, String nuevaContraseña) {
        String sql = "UPDATE usuarios SET contraseña=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaContraseña);
            pstmt.setString(2, id);
            pstmt.executeUpdate();

            this.contraseña = nuevaContraseña;
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }

    // ✅ Verificar llave de acceso (para monitores o roles especiales)
    public boolean verificarLlaveAcceso(String id, String llaveIngresada) {
        String sql = "SELECT * FROM llaves_acceso WHERE id = ? AND llave = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.trim());
            pstmt.setString(2, llaveIngresada.trim());

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // ✅ True si coincide

        } catch (SQLException e) {
            System.err.println("❌ Error verificando llave: " + e.getMessage());
            return false;
        }
    }

    // ✅ Verifica si ya existe un usuario con ese ID o correo
    public boolean existeUsuarioSQLite(String id, String correo) {
        String sql = "SELECT id FROM usuarios WHERE id = ? OR correo = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, correo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 

}
