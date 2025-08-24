package datos.interfaces;

import modelo.Receta;
import modelo.EstadoReceta;
import modelo.lista.Lista;
import java.time.LocalDate;

public interface IRecetaDAO {
    // CRUD básico
    boolean guardar(Receta receta);
    Receta buscarPorNumero(String numeroReceta);
    Lista<Receta> obtenerTodas();  // ✅ Cambio aquí
    boolean actualizar(Receta receta);
    boolean eliminar(String numeroReceta);

    // Métodos específicos del negocio
    Lista<Receta> buscarPorPaciente(String idPaciente);  // ✅ Cambio aquí
    Lista<Receta> buscarPorMedico(String idMedico);  // ✅ Cambio aquí
    Lista<Receta> buscarPorEstado(EstadoReceta estado);  // ✅ Cambio aquí
    Lista<Receta> buscarPorFechaRetiro(LocalDate fecha);  // ✅ Cambio aquí
    Lista<Receta> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);  // ✅ Cambio aquí
    Lista<Receta> obtenerRecetasDespachables(LocalDate fecha);  // ✅ Cambio aquí

    // Métodos para reportes y estadísticas
    int contarPorEstado(EstadoReceta estado);
    int contarMedicamentosEnMes(String codigoMedicamento, int año, int mes);
    Lista<Receta> obtenerRecetasDelMes(int año, int mes);  // ✅ Cambio aquí

    // Métodos de utilidad
    String generarNumeroReceta();
    boolean existeReceta(String numeroReceta);
    void cargarDatos();
    boolean guardarDatos();
    int contarRecetas();
}
