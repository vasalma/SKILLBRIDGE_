package back;

public class Monitor extends Usuario {

    private String materiaAsignada;

    public Monitor() {
        super();
        this.rol = "Monitor/tutor";
    }

    //Constructor para monitor//
    public Monitor(String id, String nombre, String apellido, String correo, String contraseña, String telefono, String materiaAsignada) {
        super(id, nombre, apellido, correo, contraseña, "Monitor/tutor", telefono);
        this.materiaAsignada = materiaAsignada;
    }

    public String getMateriaAsignada() {
        return materiaAsignada;
    }

    public void setMateriaAsignada(String materiaAsignada) {
        this.materiaAsignada = materiaAsignada;
    }
}
