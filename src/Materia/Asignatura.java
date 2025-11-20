package Materia;

public class Asignatura {
    
    private String id;
    private String nombre;
    private String descripcion;

  
    public Asignatura(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
 
    public Asignatura(String nombre, String descripcion) {
      
        this.id = nombre; 
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}