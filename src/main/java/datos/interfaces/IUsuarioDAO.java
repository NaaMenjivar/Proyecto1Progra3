package datos.interfaces;

import modelo.Usuario;
import modelo.TipoUsuario;
import java.util.List;

public interface IUsuarioDAO {
    // CRUD básico
    boolean guardar(Usuario usuario);
    Usuario buscarPorId(String id);
    List<Usuario> obtenerTodos();
    boolean actualizar(Usuario usuario);
    boolean eliminar(String id);

    // Métodos específicos del negocio
    Usuario autenticar(String id, String clave, TipoUsuario tipo);
    List<Usuario> buscarPorTipo(TipoUsuario tipo);
    boolean existeUsuario(String id);
    boolean cambiarClave(String id, String claveNueva);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
}
