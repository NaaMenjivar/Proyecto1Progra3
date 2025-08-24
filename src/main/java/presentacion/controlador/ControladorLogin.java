package presentacion.controlador;

import modelo.TipoUsuario;
import presentacion.vista.principales.VentanaCambiarClave;
import presentacion.vista.principales.VentanaLogin;

import java.awt.*;

public class ControladorLogin {
    private VentanaLogin ventanaLogin;

    public ControladorLogin() {
        ventanaLogin = new VentanaLogin(null);
        ventanaLogin.setControlador(this);
    }

    public void mostrarLogin() {
        ventanaLogin.setVisible(true);
    }

    public void procesarLogin(String id, String clave, TipoUsuario tipo){
        // Aquí va la lógica real: consultar BD, modelo, etc.
        // Por ahora simulamos con un if
        if ("admin".equals(id) && "admin".equals(clave)) {
            ventanaLogin.mostrarLoginExitoso("Bienvenido " + tipo.getDescripcion());
        } else {
            ventanaLogin.mostrarErrorLogin("Credenciales inválidas.");
        }
    }
    public void cancelarLogin() {
        ventanaLogin.dispose();
    }

    public void cambiarClave() {
        VentanaCambiarClave ventanaCambiarClave = new VentanaCambiarClave(null);
        ventanaCambiarClave.setVisible(true);
    }

}
