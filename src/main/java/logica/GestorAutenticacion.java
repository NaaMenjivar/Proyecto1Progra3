package logica;

import modelo.Usuario;
import modelo.lista.Lista;

public class GestorAutenticacion {
    private Lista<Usuario> usuarios;

    public GestorAutenticacion() {
        usuarios = new Lista<Usuario>();
    }

    public boolean login(String id, String clave) {
        Usuario usuario = usuarios.buscarPorId(id); //Probar que funcione
        if (usuario != null && usuario.validarCredenciales(id, clave)) {
            usuario.setSesionActiva(true);
            return true;
        }
        return false;
    }

    public void logout(String id) {
        Usuario usuario = usuarios.buscarPorId(id); //Probar que funcione
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

    public Lista<Usuario> getUsuarios() {
        return usuarios;
    }
}
