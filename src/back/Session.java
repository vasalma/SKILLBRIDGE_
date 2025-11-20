package back;

public class Session {

    private static Usuario usuarioLogueado;

    public static void setUsuario(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}
