package logica;

import modelo.ListaUsuarios;
import modelo.Usuario;

public class GestorAutenticacion {
    private ListaUsuarios usuarios;

    public GestorAutenticacion() {
        usuarios = new ListaUsuarios();
    }

    public boolean login(String id, String clave) {
        Usuario usuario = usuarios.buscarPorId(id);
        if (usuario != null && usuario.validarCredenciales(id, clave)) {
            usuario.setSesionActiva(true);
            return true;
        }
        return false;
    }

    public void logout(String id) {
        Usuario usuario = usuarios.buscarPorId(id);
        if (usuario != null) {
            usuario.setSesionActiva(false);
        }
    }

    public boolean cambioClave(String id, String claveActual, String claveNueva) {
        Usuario usuario = usuarios.buscarPorId(id);
        if (usuario != null && usuario.getClave().equals(claveActual)) {
            usuario.setClave(claveNueva);
            return true;
        }
        return false;
    }

    public boolean agregarUsuario(Usuario usuario) {
        if (usuarios.buscarPorId(usuario.getId()) != null) return false;
        usuarios.agregarFinal(usuario);
        return true;
    }

    public ListaUsuarios getUsuarios() {
        return usuarios;
    }
}
