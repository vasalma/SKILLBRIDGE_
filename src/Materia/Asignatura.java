package Materia;

public class Asignatura {
    
    private String id;
    private String nombre;
    private String descripcion;

    // -------------------------------------------------------------
    // CONSTRUCTOR 1: Completo (id, nombre, descripcion)
    // Usado si obtienes el ID único y dedicado de la tabla 'materias'
    // -------------------------------------------------------------
    public Asignatura(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // -------------------------------------------------------------
    // 🔥 CONSTRUCTOR 2: Corregido (nombre, descripcion)
    // Usado por DBConnection al consultar la tabla 'docente'
    // 
    // Si la tabla 'docente' no tiene una columna 'idMateria', usamos el 'nombre'
    // como ID temporal para evitar el error de BD al subir contenido.
    // -------------------------------------------------------------
    public Asignatura(String nombre, String descripcion) {
        // CORRECCIÓN: Asignamos el nombre al ID para garantizar que no sea NULL
        // cuando se suba a la base de datos (videos.idMateria y actividades.idMateria).
        this.id = nombre; 
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
    // Setters
    // -------------------------------------------------------------
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}