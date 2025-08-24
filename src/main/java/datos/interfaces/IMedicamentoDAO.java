package datos.interfaces;

import modelo.Medicamento;
import modelo.lista.Lista;

public interface IMedicamentoDAO {
    // CRUD básico
    boolean guardar(Medicamento medicamento);
    Medicamento buscarPorCodigo(String codigo);
    Lista<Medicamento> obtenerTodos();  // ✅ Cambio aquí
    boolean actualizar(Medicamento medicamento);
    boolean eliminar(String codigo);

    // Métodos específicos del negocio
    Lista<Medicamento> buscarPorNombre(String nombre);  // ✅ Cambio aquí
    Lista<Medicamento> buscarPorDescripcion(String descripcion);  // ✅ Cambio aquí
    Lista<Medicamento> buscarPorDescripcionAproximada(String patron);  // ✅ Cambio aquí
    Lista<Medicamento> obtenerMedicamentosBajoStock(int umbral);  // ✅ Cambio aquí
    boolean existeMedicamento(String codigo);

    // Métodos de utilidad
    void cargarDatos();
    boolean guardarDatos();
    int contarMedicamentos();
}
