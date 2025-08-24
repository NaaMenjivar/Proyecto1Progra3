package datos.interfaces;

import modelo.Usuario;
import modelo.TipoUsuario;
import modelo.lista.Lista;

public interface IUsuarioDAO {
    // CRUD básico
    boolean guardar(Usuario usuario);
    Usuario buscarPorId(String id);
    Lista<Usuario> obtenerTodos();  // ✅ Cambio aquí
    boolean actualizar(Usuario usuario);
    boolean eliminar(String id);

    // Métodos específicos del negocio
    Usuario autenticar(String id, String clave, TipoUsuario tipo);
    Lista<Usuario> buscarPorTipo(TipoUsuario tipo);  // ✅ Cambio aquí
    boolean existeUsuario(String id);
    boolean cambiarClave(String id, String claveNueva);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
}
