package logica;

import datos.fabricas.DAOFactory;
import datos.interfaces.*;
import modelo.*;
import logica.excepciones.PrescripcionException;
import modelo.lista.Lista;
import utilidades.GeneradorIds;
import java.time.LocalDate;

public class GestorPrescripcion {
    private IRecetaDAO recetaDAO;
    private IPacienteDAO pacienteDAO;
    private IMedicamentoDAO medicamentoDAO;
    private ValidadorNegocio validador;

    public GestorPrescripcion() {
        DAOFactory factory = DAOFactory.getInstance();
        this.recetaDAO = factory.getRecetaDAO();
        this.pacienteDAO = factory.getPacienteDAO();
        this.medicamentoDAO = factory.getMedicamentoDAO();
        // Se obtienen las listas desde los DAOs
        this.validador = new ValidadorNegocio(
                pacienteDAO.obtenerTodos(),
                medicamentoDAO.obtenerTodos()
        );
    }

    public Receta iniciarReceta(String idPaciente, String idMedico) throws PrescripcionException {
        // Validar que el paciente existe
        Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
        if (paciente == null) {
            throw new PrescripcionException("Paciente no encontrado: " + idPaciente);
        }

        // Generar número de receta único
        String numeroReceta = GeneradorIds.generarNumeroReceta();

        // Crear receta con fecha de retiro por defecto (mañana)
        LocalDate fechaRetiro = LocalDate.now().plusDays(1);

        Receta receta = new Receta(numeroReceta, idPaciente, idMedico, fechaRetiro);
        return receta;
    }

    public boolean agregarMedicamentoAReceta(Receta receta, String codigoMedicamento,
                                             int cantidad, String indicaciones, int duracionDias)
            throws PrescripcionException {

        // Validar que el medicamento existe
        Medicamento medicamento = medicamentoDAO.buscarPorCodigo(codigoMedicamento);
        if (medicamento == null) {
            throw new PrescripcionException("Medicamento no encontrado: " + codigoMedicamento);
        }

        // Crear detalle de receta
        DetalleReceta detalle = new DetalleReceta(codigoMedicamento, cantidad, indicaciones, duracionDias);

        // Validar detalle
        if (!detalle.esValidoPrescripcion()) {
            throw new PrescripcionException("Datos de medicamento inválidos");
        }

        // Verificar que no esté ya prescrito en esta receta
        if (receta.getDetalles().buscarPorId(codigoMedicamento) != null) {
            throw new PrescripcionException("Medicamento ya está prescrito en esta receta");
        }

        receta.agregarDetalle(detalle);
        return true;
    }

    public boolean modificarMedicamentoEnReceta(Receta receta, int indice,
                                                int nuevaCantidad, String nuevasIndicaciones,
                                                int nuevaDuracion) throws PrescripcionException {

        if (indice < 0 || indice >= receta.getDetalles().getTam()) {
            throw new PrescripcionException("Índice de medicamento inválido");
        }

        DetalleReceta detalleExistente = receta.getDetalles().obtenerPorPos(indice);
        if (detalleExistente == null) {
            throw new PrescripcionException("Medicamento no encontrado en la receta");
        }

        // Crear nuevo detalle con los cambios
        DetalleReceta nuevoDetalle = new DetalleReceta(
                detalleExistente.getCodigoMedicamento(),
                nuevaCantidad,
                nuevasIndicaciones,
                nuevaDuracion
        );

        if (!nuevoDetalle.esValidoPrescripcion()) {
            throw new PrescripcionException("Datos modificados son inválidos");
        }

        receta.modificarDetalle(indice, nuevoDetalle);
        return true;
    }

    public boolean eliminarMedicamentoDeReceta(Receta receta, String codigoMedicamento) {
        return receta.eliminarDetalle(codigoMedicamento);
    }

    public boolean finalizarReceta(Receta receta) throws PrescripcionException {
        if (!receta.tieneDetalles()) {
            throw new PrescripcionException("La receta debe tener al menos un medicamento");
        }

        // Validar todos los detalles
        for (int i = 0; i < receta.getDetalles().getTam(); i++) {
            DetalleReceta detalle = receta.getDetalles().obtenerPorPos(i);
            if (!detalle.esValidoPrescripcion()) {
                throw new PrescripcionException("Hay medicamentos con datos inválidos");
            }
        }

        receta.setEstado(EstadoReceta.CONFECCIONADA);
        return recetaDAO.guardar(receta);
    }

    // Métodos de consulta
    public Lista<Receta> buscarRecetasPorMedico(String idMedico) {
        return recetaDAO.buscarPorMedico(idMedico);
    }

    public Lista<Paciente> buscarPacientesPorNombre(String patron) {
        return pacienteDAO.buscarPorNombreAproximado(patron);
    }

    public Lista<Medicamento> buscarMedicamentosPorDescripcion(String patron) {
        return medicamentoDAO.buscarPorDescripcionAproximada(patron);
    }
}
