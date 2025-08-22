package datos.interfaces;

import modelo.Receta;
import modelo.EstadoReceta;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IRecetaDAO {
    // CRUD básico
    boolean guardar(Receta receta);
    Receta buscarPorNumero(String numeroReceta);
    List<Receta> obtenerTodas();
    boolean actualizar(Receta receta);
    boolean eliminar(String numeroReceta);

    // Métodos específicos del negocio
    List<Receta> buscarPorPaciente(String idPaciente);
    List<Receta> buscarPorMedico(String idMedico);
    List<Receta> buscarPorEstado(EstadoReceta estado);
    List<Receta> buscarPorFechaRetiro(LocalDate fecha);
    List<Receta> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    List<Receta> obtenerRecetasDespachables(LocalDate fecha);

    // Métodos para reportes y estadísticas
    int contarPorEstado(EstadoReceta estado);
    int contarMedicamentosEnMes(String codigoMedicamento, int año, int mes);
    List<Receta> obtenerRecetasDelMes(int año, int mes);

    // Métodos de utilidad
    String generarNumeroReceta();
    boolean existeReceta(String numeroReceta);
    void cargarDatos();
    boolean guardarDatos();
    int contarRecetas();
}
