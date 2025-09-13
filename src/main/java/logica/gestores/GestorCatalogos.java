package logica.gestores;

import data.*;
import logica.entidades.*;
import logica.entidades.lista.*;
import logica.excepciones.CatalogoException;

/**
 * Gestor único para todos los catálogos del sistema
 * Basado en tu implementación existente pero completado
 */
public class GestorCatalogos {
    private ListaMedicos listaMedicos;
    private ListaFarmaceutas listaFarmaceutas;
    private ListaPacientes listaPacientes;
    private ListaUsuarios listaUsuarios;

    private CatalogoMedicamentos medicamentos;
    private Lista<Receta> recetas; // Para prescripciones y despacho

    public GestorCatalogos() {
        this.listaMedicos = new ListaMedicos();
        this.listaFarmaceutas = new ListaFarmaceutas();
        this.listaPacientes = new ListaPacientes();
        this.listaUsuarios = new ListaUsuarios();
        this.medicamentos = new CatalogoMedicamentos();
        this.recetas = new Lista<>();
    }

    public void guardarDatos(){
        XmlPersisterMedicos.guardar(listaMedicos,"medicos.xml");
        XmlPersisterFarmaceutas.guardar(listaFarmaceutas, "farmaceutas.xml");
        XmlPersisterPacientes.guardar(listaPacientes,"pacientes.xml");
        XmlPersisterUsuarios.guardar(listaUsuarios,"usuarios.xml");
        XmlPersisterMedicamentos.guardar(medicamentos,"medicamentos.xml");
        XmlPersisterRecetas.guardar(recetas,"recetas.xml");
    }

    public void cargarDatos(){
        listaMedicos = XmlPersisterMedicos.cargar("medicos.xml");
        listaFarmaceutas = XmlPersisterFarmaceutas.cargar("farmaceutas.xml");
        listaPacientes = XmlPersisterPacientes.cargar("pacientes.xml");
        listaUsuarios = XmlPersisterUsuarios.cargar("usuarios.xml");
        medicamentos = XmlPersisterMedicamentos.cargar("medicamentos.xml");
        recetas = XmlPersisterRecetas.cargar("recetas.xml");
    }

    // ================================
    // GESTIÓN DE USUARIOS (MÉDICOS)
    // ================================

    public boolean agregarMedico(Medico medico) throws CatalogoException {
        if (medico == null) {
            throw new CatalogoException("Médico no puede ser null");
        }

        if (!esValidoMedico(medico)) {
            throw new CatalogoException("Datos del médico son inválidos");
        }

        if (existeUsuario(medico.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + medico.getId());
        }

        // Establecer clave igual al ID por defecto
        medico.setClave(medico.getId());
        listaMedicos.agregarMedico(medico);
        listaUsuarios.agregarUsuario(medico);
        return true;
    }

    public void agregarUsuario(Usuario usuario) {
        listaUsuarios.agregarUsuario(usuario);
    }

    public boolean agregarFarmaceuta(Farmaceuta farmaceuta) throws CatalogoException {
        if (farmaceuta == null) {
            throw new CatalogoException("Farmaceuta no puede ser null");
        }

        if (!esValidoFarmaceuta(farmaceuta)) {
            throw new CatalogoException("Datos del farmaceuta son inválidos");
        }

        if (existeUsuario(farmaceuta.getId())) {
            throw new CatalogoException("Ya existe un usuario con el ID: " + farmaceuta.getId());
        }

        farmaceuta.setClave(farmaceuta.getId());
        listaFarmaceutas.agregarFarmaceuta(farmaceuta);
        listaUsuarios.agregarUsuario(farmaceuta);
        return true;
    }

    public boolean cambiarClave(Usuario user) throws CatalogoException {
        return listaUsuarios.cambiarClave(user);
    }

    public Usuario autenticarUsuario(String id, String clave) {
        return listaUsuarios.autenticarUsuario(id,clave);
    }

    public Usuario buscarUsuarioId(String id) {
        return listaUsuarios.getUsuarioId(id);
    }

    public ListaMedicos buscarMedicos() {
        return listaMedicos;
    }

    public Medico buscarMedicoId(String id){
        return listaMedicos.buscarMedicoId(id);
    }

    public ListaFarmaceutas buscarFarmaceutas() {
        return listaFarmaceutas;
    }

    public Medico buscarMedicosPorNombre(String nombre) {
        return listaMedicos.buscarMedicoNombre(nombre);
    }

    public Farmaceuta buscarFarmaceutaId(String id) {
        return listaFarmaceutas.buscarFarmaceutaId(id);
    }

    public Farmaceuta buscarFarmaceutasPorNombre(String nombre) {
        return listaFarmaceutas.buscarFarmaceutaNombre(nombre);
    }

    public boolean eliminarUsuario(String id) throws CatalogoException {
        return listaUsuarios.eliminarUsuario(id);
    }

    public boolean eliminarFarmaceuta(String id) throws CatalogoException {
        return listaFarmaceutas.eliminarFarmaceuta(id);
    }

    public boolean eliminarMedico(String id) throws CatalogoException {
        return listaMedicos.eliminarMedico(id);
    }

    // ================================
    // GESTIÓN DE PACIENTES
    // ================================

    public boolean agregarPaciente(Paciente paciente) throws CatalogoException {
        if (paciente == null) {
            throw new CatalogoException("Paciente no puede ser null");
        }

        if (existePaciente(paciente.getId())) {
            throw new CatalogoException("Ya existe un paciente con el ID: " + paciente.getId());
        }

        listaPacientes.agregarPaciente(paciente);
        return true;
    }

    public Paciente buscarPacientePorId(String id) {
        return listaPacientes.buscarPacienteId(id);
    }

    public boolean actualizarPaciente(Paciente paciente) throws CatalogoException {
        return listaPacientes.actualizarPaciente(paciente);
    }

    public boolean eliminarPaciente(String id) throws CatalogoException {
        if (!existePaciente(id)) {
            throw new CatalogoException("Paciente no existe: " + id);
        }

        return listaPacientes.eliminarPaciente(id);
    }

    public ListaPacientes obtenerTodosPacientes() {
        return listaPacientes;
    }

    public Paciente buscarPacientesPorNombre(String nombre) {
        return listaPacientes.buscarPacienteNombre(nombre);
    }

    // ================================
    // GESTIÓN DE MEDICAMENTOS
    // ================================

    public boolean agregarMedicamento(Medicamento medicamento) throws CatalogoException {
        if (medicamento == null) {
            throw new CatalogoException("Medicamento no puede ser null");
        }

        if (existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Ya existe un medicamento con el código: " + medicamento.getCodigo());
        }

        medicamentos.agregarMedicamento(medicamento);
        return true;
    }

    public Medicamento buscarMedicamentoPorCodigo(String codigo) {
        return medicamentos.buscarMedicamentoCodigo(codigo);
    }

    public Medicamento buscarMedicamentosPorDescripcion(String descripcion) {
        return medicamentos.buscarMedicamentoDescripcion(descripcion);
    }

    public boolean actualizarMedicamento(Medicamento medicamento) throws CatalogoException {
        if (medicamento == null) {
            throw new CatalogoException("Medicamento no puede ser null");
        }

        if (!existeMedicamento(medicamento.getCodigo())) {
            throw new CatalogoException("Medicamento no existe: " + medicamento.getCodigo());
        }

        return medicamentos.modificarMedicamento(medicamento.getCodigo(), medicamento);
    }

    public boolean eliminarMedicamento(String codigo) throws CatalogoException {
        if (!existeMedicamento(codigo)) {
            throw new CatalogoException("Medicamento no existe: " + codigo);
        }

        return medicamentos.eliminarMedicamento(codigo);
    }

    public CatalogoMedicamentos obtenerTodosMedicamentos() {
        return medicamentos;
    }

    public CatalogoMedicamentos obtenerMedicamentosBajoStock(int umbral) {
        return medicamentos.medicamentosBajoStock(umbral);
    }

    // ================================
    // GESTIÓN DE RECETAS (Prescripción y Despacho)
    // ================================

    public Lista<Receta> obtenerTodasRecetas() {
        return recetas;
    }

    public Receta buscarReceta(String numeroReceta) {
        return recetas.buscarPorId(numeroReceta);
    }

    public Lista<Receta> buscarRecetasPorPaciente(String idPaciente) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getIdPaciente().equals(idPaciente)) {
                resultado.agregarFinal(receta);
            }
        }
        return resultado;
    }

    public Lista<Receta> buscarRecetasPorMedico(String idMedico) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getIdMedico().equals(idMedico)) {
                resultado.agregarFinal(receta);
            }
        }
        return resultado;
    }

    public Lista<Receta> buscarRecetasPorEstado(EstadoReceta estado) {
        Lista<Receta> resultado = new Lista<>();
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getEstado() == estado) {
                resultado.agregarFinal(receta);
            }
        }
        return resultado;
    }

    public boolean cambiarEstadoReceta(String numeroReceta, EstadoReceta nuevoEstado) throws CatalogoException {
        Receta receta = buscarReceta(numeroReceta);
        if (receta == null) {
            throw new CatalogoException("Receta no encontrada: " + numeroReceta);
        }

        if (!receta.puedeAvanzarAEstado(nuevoEstado)) {
            throw new CatalogoException("Transición de estado no válida");
        }

        receta.setEstado(nuevoEstado);
        return true;
    }


    public int contarPacientes() {
        return listaPacientes.getTam();
    }

    public int contarMedicamentos() {
        return medicamentos.getTam();
    }

    public int contarMedicos() {
        return listaMedicos.getTam();
    }

    public int contarFarmaceutas() {
        return listaFarmaceutas.getTam();
    }

    public int contarRecetas() {
        return recetas.getTam();
    }


    public String generarReporteGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS GENERALES DEL SISTEMA ===\n\n");
        sb.append("Médicos registrados: ").append(contarMedicos()).append("\n");
        sb.append("Farmaceutas registrados: ").append(contarFarmaceutas()).append("\n");
        sb.append("Pacientes registrados: ").append(contarPacientes()).append("\n");
        sb.append("Medicamentos en catálogo: ").append(contarMedicamentos()).append("\n");
        sb.append("Recetas en el sistema: ").append(contarRecetas()).append("\n\n");

        // Medicamentos con stock bajo
        CatalogoMedicamentos bajoStock = obtenerMedicamentosBajoStock(10);
        sb.append("Medicamentos con stock bajo (≤10): ").append(bajoStock.getTam()).append("\n");
        sb.append(bajoStock.toString());

        return sb.toString();
    }

    // ================================
    // MÉTODOS DE UTILIDAD Y VALIDACIÓN
    // ================================

    private boolean esValidoMedico(Medico medico) {
        return medico.getId() != null && !medico.getId().trim().isEmpty() &&
                medico.getNombre() != null && !medico.getNombre().trim().isEmpty() &&
                medico.getEspecialidad() != null && !medico.getEspecialidad().trim().isEmpty();
    }

    private boolean esValidoFarmaceuta(Farmaceuta farmaceuta) {
        return farmaceuta.getId() != null && !farmaceuta.getId().trim().isEmpty() &&
                farmaceuta.getNombre() != null && !farmaceuta.getNombre().trim().isEmpty();
    }

    private boolean existeUsuario(String id) {
        return listaUsuarios.existeUsuarioId(id);
    }

    private boolean existePaciente(String id) {
        return listaPacientes.buscarPacienteId(id) != null;
    }

    private boolean existeMedicamento(String codigo) {
        return medicamentos.buscarMedicamentoCodigo(codigo) != null;
    }

    /**
     * Obtiene todas las recetas del sistema
     */
    public Lista<Receta> obtenerTodasLasRecetas() {
        return recetas; // Retorna la lista completa de recetas
    }

    /**
     * Obtiene las recetas de un médico específico
     */
    public Lista<Receta> obtenerRecetasPorMedico(String idMedico) {
        Lista<Receta> recetasMedico = new Lista<>();

        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getIdMedico().equals(idMedico)) {
                recetasMedico.agregarFinal(receta);
            }
        }

        return recetasMedico;
    }

    /**
     * Busca una receta por su número
     */
    public Receta buscarRecetaPorNumero(String numeroReceta) {
        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getNumeroReceta().equals(numeroReceta)) {
                return receta;
            }
        }
        return null;
    }

    /**
     * Obtiene recetas por estado
     */
    public Lista<Receta> obtenerRecetasPorEstado(String estado) {
        Lista<Receta> recetasPorEstado = new Lista<>();

        for (int i = 0; i < recetas.getTam(); i++) {
            Receta receta = recetas.obtenerPorPos(i);
            if (receta.getEstado().equals(estado)) {
                recetasPorEstado.agregarFinal(receta);
            }
        }

        return recetasPorEstado;
    }


    public boolean agregarReceta(Receta receta) throws CatalogoException {
        if (receta == null) {
            throw new CatalogoException("La receta no puede ser null");
        }

        if (buscarRecetaPorNumero(receta.getNumeroReceta()) != null) {
            throw new CatalogoException("Ya existe una receta con el número: " + receta.getNumeroReceta());
        }

        recetas.agregarFinal(receta);
        return true;
    }
}