package back;

public class Estudiante extends Usuario {

  
    public Estudiante() {
        super();
        this.rol = "Estudiante";
    }

    public Estudiante(String id, String nombre, String apellido, String correo, String contraseña, String telefono) {
        super(id, nombre, apellido, correo, contraseña, "Estudiante", telefono);
    }
}
