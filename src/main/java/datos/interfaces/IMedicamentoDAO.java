package datos.interfaces;

import modelo.Medicamento;
import java.util.List;

public interface IMedicamentoDAO {
    // CRUD básico
    boolean guardar(Medicamento medicamento);
    Medicamento buscarPorCodigo(String codigo);
    List<Medicamento> obtenerTodos();
    boolean actualizar(Medicamento medicamento);
    boolean eliminar(String codigo);

    // Métodos específicos del negocio
    List<Medicamento> buscarPorNombre(String nombre);
    List<Medicamento> buscarPorDescripcion(String descripcion);
    List<Medicamento> buscarPorDescripcionAproximada(String patron);
    List<Medicamento> obtenerMedicamentosBajoStock(int umbral);
    boolean existeMedicamento(String codigo);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
    int contarMedicamentos();
}
