/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package back;

/**
 *
 * @author PC
 */
public class Session {

    private static Usuario usuarioLogueado;

    // Guarda el usuario cuando inicia sesión
    public static void setUsuario(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    // Retorna el usuario guardado
    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    // Limpia la sesión al cerrar sesión
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}
