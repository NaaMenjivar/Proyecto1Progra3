package logica;

import datos.fabricas.DAOFactory;
import datos.interfaces.IUsuarioDAO;
import modelo.Usuario;
import modelo.TipoUsuario;
import logica.excepciones.AutenticacionException;

public class GestorAutenticacion {
    private IUsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    public GestorAutenticacion() {
        this.usuarioDAO = DAOFactory.getInstance().getUsuarioDAO();
    }

    public Usuario login(String id, String clave, TipoUsuario tipo) throws AutenticacionException {
        if (id == null || id.trim().isEmpty()) {
            throw new AutenticacionException("ID de usuario es obligatorio");
        }

        if (clave == null || clave.trim().isEmpty()) {
            throw new AutenticacionException("Clave es obligatoria");
        }

        Usuario usuario = usuarioDAO.autenticar(id.trim(), clave, tipo);
        if (usuario == null) {
            throw new AutenticacionException("Credenciales inválidas o tipo de usuario incorrecto");
        }

        usuario.setSesionActiva(true);
        this.usuarioActual = usuario;
        return usuario;
    }

    public void logout() {
        if (usuarioActual != null) {
            usuarioActual.setSesionActiva(false);
            usuarioActual = null;
        }
    }

    public boolean cambiarClave(String id, String claveActual, String claveNueva) throws AutenticacionException {
        if (claveNueva == null || claveNueva.trim().length() < 4) {
            throw new AutenticacionException("La nueva clave debe tener al menos 4 caracteres");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new AutenticacionException("Usuario no encontrado");
        }

        if (!usuario.getClave().equals(claveActual)) {
            throw new AutenticacionException("Clave actual incorrecta");
        }

        return usuarioDAO.cambiarClave(id, claveNueva);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean hayUsuarioLogueado() {
        return usuarioActual != null && usuarioActual.isSesionActiva();
    }

    public boolean usuarioTienePermiso(TipoUsuario tipoRequerido) {
        return hayUsuarioLogueado() && usuarioActual.getTipo() == tipoRequerido;
    }
}
