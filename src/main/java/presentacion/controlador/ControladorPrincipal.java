package presentacion.controlador;

import logica.entidades.*;
import logica.entidades.lista.CatalogoMedicamentos;
import logica.entidades.lista.Lista;
import logica.entidades.lista.ListaPacientes;
import logica.excepciones.CatalogoException;
import presentacion.modelo.*;
import presentacion.vista.sistema.VentanaLogin;
import presentacion.vista.principal.VentanaPrincipal;

import javax.swing.*;
import java.time.LocalDate;

public class ControladorPrincipal {

    private ModeloPrincipal modelo;
    private ControladorLogin controladorLogin;
    private VentanaPrincipal ventanaPrincipal;

    public ControladorPrincipal() {
        inicializarSistema();
    }

    private void inicializarSistema() {
        try {
            modelo = new ModeloPrincipal();
            //System.out.println(modelo.generarReporteCompleto()); aqui se cae con un null en actual xd
            controladorLogin = new ControladorLogin(new VentanaLogin(),this);
            controladorLogin.iniciarLogin();

        } catch (Exception e) {
            mostrarError("Error al inicializar el sistema: " + e.getMessage());
            System.exit(1);
        }
    }

    public boolean autenticarUsuario(String id, String clave) {
        try {
            if (modelo.autenticarUsuario(id, clave)) {
                Usuario usuario = modelo.getUsuarioActual();
                abrirVentanaSegunTipoUsuario(usuario);
                return true;
            } else {
                mostrarError("Credenciales incorrectas");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error durante autenticación: " + e.getMessage());
            return false;
        }
    }

    private void abrirVentanaSegunTipoUsuario(Usuario usuario) {
        switch (usuario.getTipo()) {
            case MEDICO:
                abrirVentanaPrincipal(TipoUsuario.MEDICO);
                break;
            case FARMACEUTA:
                abrirVentanaPrincipal(TipoUsuario.FARMACEUTA);
            case ADMINISTRADOR:
                abrirVentanaPrincipal(TipoUsuario.ADMINISTRADOR);
                break;
        }
    }

    private void abrirVentanaPrincipal(TipoUsuario tipoUsuario) {
        ventanaPrincipal = new VentanaPrincipal(this, tipoUsuario);
        ventanaPrincipal.setVisible(true);
    }

    public void cerrarSesion() {
        if (ventanaPrincipal != null) {
            ventanaPrincipal.dispose();
            ventanaPrincipal = null;
        }

        modelo.cerrarSesion();

        controladorLogin.iniciarLogin();
    }

    public boolean cambiarClave(String claveActual, String claveNueva, String confirmarClave) {
        try {
            // Validar que las claves nuevas coincidan
            if (!claveNueva.equals(confirmarClave)) {
                mostrarError("Las nuevas claves no coinciden");
                return false;
            }

            // Validar longitud mínima
            if (claveNueva.length() < 3) {
                mostrarError("La nueva clave debe tener al menos 3 caracteres");
                return false;
            }

            if (modelo.cambiarClave(claveActual, claveNueva)) {
                mostrarMensaje("La contraseña se cambio exitosamente");
                return true;
            } else {
                mostrarError("No se pudo cambiar la contraseña. Verifique la clave actual");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }

    // ================================
    // GESTIÓN DE MÉDICOS
    // ================================

    public boolean agregarMedico(String id, String nombre, String especialidad) {
        try {
            /*if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar médicos");
                return false;
            }*/

            if (modelo.agregarMedico(id, nombre, especialidad)) {
                mostrarMensaje("Médico agregado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo agregar el médico");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar médico: " + e.getMessage());
            return false;
        }
    }

    public Medicamento buscarMedicamentoPorDescripcion(String descripcion) {
        return modelo.buscarMedicamentosPorDescripcion(descripcion);
    }

    public boolean eliminarFarmaceuta(String id) {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar el Medicamento con el ID: " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modelo.eliminarFarmaceuta(id)) {
                    mostrarMensaje("Farmaceuta eliminado exitosamente");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            mostrarError("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }
    public boolean eliminarMedicamento(String codigo){
        try {
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar el Medicamento con el Codigo: " + codigo + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modelo.eliminarMedicamento(codigo)) {
                    mostrarMensaje("Médico eliminado exitosamente");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            mostrarError("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarMedico(String id) {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar el médico con ID: " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modelo.eliminarMedico(id)) {
                    mostrarMensaje("Médico eliminado exitosamente");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            mostrarError("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPaciente(String id) {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro de eliminar el Paciente con ID: " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modelo.eliminarPaciente(id)) {
                    mostrarMensaje("Paciente eliminado exitosamente");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            mostrarError("Error al eliminar médico: " + e.getMessage());
            return false;
        }
    }

    // ================================
    // GESTIÓN DE FARMACEUTAS
    // ================================

    public boolean agregarFarmaceuta(String id, String nombre) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar farmaceutas");
                return false;
            }

            if (id == null || id.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                mostrarError("ID y nombre del farmaceuta son obligatorios");
                return false;
            }

            if (modelo.agregarFarmaceuta(id, nombre)) {
                mostrarMensaje("Farmaceuta agregado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo agregar el farmaceuta");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar farmaceuta: " + e.getMessage());
            return false;
        }
    }


    public boolean agregarPaciente(String id, String nombre, LocalDate fechaNacimiento, String telefono) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar pacientes");
                return false;
            }

            if (id == null || id.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                mostrarError("ID y nombre del paciente son obligatorios");
                return false;
            }

            if (modelo.agregarPaciente(id, nombre, fechaNacimiento, telefono)) {
                mostrarMensaje("Paciente agregado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo agregar el paciente");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar paciente: " + e.getMessage());
            return false;
        }
    }

    public ListaPacientes getListaPacientes() {
        return modelo.obtenerPacientes();
    }


    public boolean agregarMedicamento(String codigo, String nombre, String presentacion, int stock) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar medicamentos");
                return false;
            }

            if (codigo == null || codigo.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                mostrarError("Código y nombre del medicamento son obligatorios");
                return false;
            }

            if (stock < 0) {
                mostrarError("El stock no puede ser negativo");
                return false;
            }

            if (modelo.agregarMedicamento(codigo, nombre, presentacion, stock)) {
                mostrarMensaje("Medicamento agregado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo agregar el medicamento");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar medicamento: " + e.getMessage());
            return false;
        }
    }

    public CatalogoMedicamentos getMedicamentos(){
        return modelo.obtenerMedicamentos();
    }

    public void agregarReceta(Receta receta) throws CatalogoException {
        modelo.agregarReceta(receta);
    }

    public boolean iniciarNuevaReceta(String idPaciente, LocalDate fechaRetiro) {
        try {
            if (!modelo.puedePrescribir()) {
                mostrarError("No tiene permisos para prescribir recetas");
                return false;
            }

            ListaPacientes pacientes = modelo.obtenerPacientes();
            Paciente pacienteEncontrado = null;

            for (Paciente paciente : pacientes) {
                if (paciente.getId().equals(idPaciente)) {
                    pacienteEncontrado = paciente;
                    break;
                }
            }

            if (pacienteEncontrado == null) {
                mostrarError("Paciente no encontrado con ID: " + idPaciente);
                return false;
            }

            modelo.setPacienteSeleccionado(pacienteEncontrado);
            modelo.iniciarNuevaReceta(fechaRetiro);
            return true;

        } catch (Exception e) {
            mostrarError("Error al iniciar receta: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarMedicamentoAReceta(String codigoMedicamento, int cantidad,
                                             String indicaciones, int duracionDias) {
        try {
            if (!modelo.puedePrescribir()) {
                mostrarError("No tiene permisos para prescribir");
                return false;
            }

            if (modelo.agregarMedicamentoAReceta(codigoMedicamento, cantidad, indicaciones, duracionDias)) {
                return true;
            } else {
                mostrarError("No se pudo agregar el medicamento a la receta");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean guardarReceta() {
        try {
            if (!modelo.puedePrescribir()) {
                mostrarError("No tiene permisos para prescribir");
                return false;
            }

            if (modelo.getRecetaActual() == null) {
                mostrarError("No hay una receta activa");
                return false;
            }

            if (!modelo.getRecetaActual().tieneDetalles()) {
                mostrarError("La receta debe tener al menos un medicamento. " +
                        "Verifique que tenga al menos un medicamento");
                return false;
            }

            if (modelo.guardarReceta()) {
                mostrarMensaje("Receta guardada exitosamente");
                return true;
            } else {
                mostrarError("No se pudo guardar la receta. " +
                        "Verifique que tenga al menos un medicamento");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al guardar receta: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoReceta(String numeroReceta, EstadoReceta nuevoEstado) {
        try {
            if (!modelo.puedeDespachar()) {
                mostrarError("No tiene permisos para despachar recetas");
                return false;
            }

            if (modelo.cambiarEstadoReceta(numeroReceta, nuevoEstado)) {
                mostrarMensaje("Estado de receta cambiado a: " + nuevoEstado.getDescripcion());
                return true;
            } else {
                mostrarError("No se pudo cambiar el estado de la receta");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al cambiar estado de receta: " + e.getMessage());
            return false;
        }
    }

    public Lista<Receta> obtenerRecetasDelMedicoActual() {
        try {
            Usuario usuarioActual = modelo.getUsuarioActual();
            if (usuarioActual == null || usuarioActual.getTipo() != TipoUsuario.MEDICO) {
                return new Lista<Receta>();
            }

            return modelo.obtenerRecetasPorMedico(usuarioActual.getId());
        } catch (Exception e) {
            mostrarError("Error al obtener recetas del médico: " + e.getMessage());
            return new Lista<Receta>();
        }
    }

    public Lista<Receta> buscarRecetasDelMedicoActualPorNumero(String numeroReceta) {
        try {
            Usuario usuarioActual = modelo.getUsuarioActual();
            if (usuarioActual == null || usuarioActual.getTipo() != TipoUsuario.MEDICO) {
                return new Lista<Receta>();
            }

            return modelo.buscarRecetasPorMedicoYNumero(usuarioActual.getId(), numeroReceta);
        } catch (Exception e) {
            mostrarError("Error al buscar recetas: " + e.getMessage());
            return new Lista<Receta>();
        }
    }

    public String obtenerDetallesReceta(Receta receta) {
        try {
            if (receta == null) {
                return "No hay receta seleccionada";
            }

            return modelo.generarDetallesReceta(receta);
        } catch (Exception e) {
            mostrarError("Error al obtener detalles de receta: " + e.getMessage());
            return "Error al cargar detalles";
        }
    }

    public Lista<Receta> obtenerTodasLasRecetas() {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para ver todas las recetas");
                return new Lista<Receta>();
            }

            return modelo.obtenerTodasLasRecetas();
        } catch (Exception e) {
            mostrarError("Error al obtener recetas: " + e.getMessage());
            return new Lista<Receta>();
        }
    }

    public Lista<Receta> buscarRecetasPorCriterio(String criterio) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para buscar recetas");
                return new Lista<Receta>();
            }

            return modelo.buscarRecetasPorCriterio(criterio);
        } catch (Exception e) {
            mostrarError("Error al buscar recetas: " + e.getMessage());
            return new Lista<Receta>();
        }
    }

    public ModeloPrincipal getModelo() {
        return modelo;
    }

    public Usuario getUsuarioActual() {
        return modelo.getUsuarioActual();
    }

    public String generarReporteCompleto() {
        return modelo.generarReporteCompleto();
    }


    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cerrarAplicacion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de cerrar la aplicación?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public Lista<Receta> obtenerTodasRecetas() {
        return modelo.obtenerTodasLasRecetas();
    }

    public CatalogoMedicamentos obtenerMedicamentos() {
        return modelo.obtenerMedicamentos();
    }
}