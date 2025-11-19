package crearLlave;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import javax.swing.JOptionPane;

public class Privada {

    // ... (Método conectar() y generarLlaveUnica() permanecen igual) ...
    
    // Método para obtener la conexión a la base de datos
    private static Connection conectar() throws Exception {
        String url = "jdbc:sqlite:database/skillbridge.db";
        return DriverManager.getConnection(url);
    }

    // Método para generar una llave de acceso única de 6 dígitos
    private static String generarLlaveUnica() throws Exception {
        String llaveGenerada = "";
        boolean esUnica = false;
        Random random = new Random();
        String sqlVerificarLlave = "SELECT 1 FROM llaves_acceso WHERE llave = ?";

        try (Connection conn = conectar()) { 
            for (int i = 0; i < 100; i++) {
                int num = random.nextInt(900000) + 100000; 
                llaveGenerada = String.valueOf(num); 

                try (PreparedStatement verificar = conn.prepareStatement(sqlVerificarLlave)) {
                    verificar.setString(1, llaveGenerada);
                    ResultSet rs = verificar.executeQuery();
                    
                    if (!rs.next()) {
                        esUnica = true;
                        break; 
                    }
                }
            }
        }
        if (!esUnica) {
            throw new RuntimeException("No se pudo generar una llave única después de varios intentos.");
        }
        return llaveGenerada;
    }

    // Método para insertar la llave en la base de datos (AHORA RECIBE LA MATERIA)
    public static void insertarLlave(String id, String llave) {
        String sqlVerificarId = "SELECT 1 FROM llaves_acceso WHERE id = ?";
        // ⚠ La sentencia INSERT ahora incluye el campo 'materia'
        String sqlInsertar = "INSERT INTO llaves_acceso (id, llave) VALUES (?, ?)";

        try (Connection conn = conectar();
             PreparedStatement verificarId = conn.prepareStatement(sqlVerificarId);
             PreparedStatement insertar = conn.prepareStatement(sqlInsertar)) {

            // 1. Verificar si ya existe el ID
            verificarId.setString(1, id);
            ResultSet rs = verificarId.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "⚠ Ya existe un registro con el ID '" + id + "'. Intente con otro ID.");
                return;
            }

            // 2. Insertar el registro (ID, LLAVE y MATERIA)
            insertar.setString(1, id);
            insertar.setString(2, llave);
      
            
            int filasAfectadas = insertar.executeUpdate();
            if (filasAfectadas > 0) {
                // Mensaje final mostrando la llave y la materia
                JOptionPane.showMessageDialog(null, 
                        "✅ Llave de acceso insertada correctamente.\n\n"
                        + "Id " + id + "\n"
                        + "Llave Generada: " + llave); 
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error al insertar la llave de acceso.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error en la inserción: " + e.getMessage());
        }
    }

    // Método principal modificado
    public static void main(String[] args) {
        // --- 1. Solicitar el ID ---
        String id = JOptionPane.showInputDialog("Ingrese el ID del profesor o monitor:");
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "⚠ El ID no puede estar vacío.");
            return;
        }
        
        // --- 2. Mostrar el Menú de Materias ---
        
        
        // Si el usuario cancela o cierra la ventana
        

        // --- 3. Generar e Insertar ---
        try {
            String llaveGenerada = generarLlaveUnica();
            
            // Llamamos a la función con el tercer parámetro: la materia
            insertarLlave(id, llaveGenerada);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al generar la llave: " + e.getMessage());
        }
    }
}