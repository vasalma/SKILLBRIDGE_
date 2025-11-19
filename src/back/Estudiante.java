package back;

public class Estudiante extends Usuario {

    // 🔹 Constructor vacío
    public Estudiante() {
        super();
        this.rol = "Estudiante";
    }

    // 🔹 Constructor completo (ahora usa String id)
    public Estudiante(String id, String nombre, String apellido, String correo, String contraseña, String telefono) {
        super(id, nombre, apellido, correo, contraseña, "Estudiante", telefono);
    }
}
