package Materia;

public class Asignatura {
    
    private String id;
    private String nombre;
    private String descripcion;

    // -------------------------------------------------------------
    // CONSTRUCTOR 1: Completo (id, nombre, descripcion)
    // Usado si obtienes el ID de la tabla 'materias'
    // -------------------------------------------------------------
    public Asignatura(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // -------------------------------------------------------------
    // 🔥 CONSTRUCTOR 2: Requerido (nombre, descripcion)
    // Usado por DBConnection al consultar la tabla 'docente'
    // -------------------------------------------------------------
    public Asignatura(String nombre, String descripcion) {
        // Inicializa 'id' como null o un valor predeterminado, ya que no se proporciona.
        this.id = null; 
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // -------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    
    // -------------------------------------------------------------
    // Setters (Opcional, pero útil si necesitas modificar datos)
    // -------------------------------------------------------------
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}