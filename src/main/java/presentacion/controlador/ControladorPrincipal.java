package presentacion.controlador;

import logica.entidades.*;
import logica.excepciones.*;
import presentacion.modelo.*;
import presentacion.vista.sistema.VentanaLogin;
import presentacion.vista.sistema.VentanaCambiarClave;
import presentacion.vista.principal.VentanaPrincipal;
import presentacion.vista.principal.VentanaMedico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Controlador principal del sistema que maneja toda la lógica de aplicación
 * Implementa el patrón MVC coordinando Modelos y Vistas
 */
public class ControladorPrincipal {

    // Modelo principal del sistema
    private ModeloPrincipal modelo;

    // Vistas principales
    private VentanaLogin ventanaLogin;
    private VentanaPrincipal ventanaPrincipal;
    private VentanaMedico ventanaMedico;

    // Estado del controlador
    private boolean sistemaIniciado;

    public ControladorPrincipal() {
        inicializarSistema();
    }

    // ================================
    // INICIALIZACIÓN DEL SISTEMA
    // ================================

    private void inicializarSistema() {
        try {
            // Crear modelo principal
            modelo = new ModeloPrincipal();

            // Crear ventana de login
            //ventanaLogin = new VentanaLogin(this);

            // Mostrar ventana de login
            /*SwingUtilities.invokeLater(() -> {
                ventanaLogin.setVisible(true);
            });*/

            sistemaIniciado = true;

        } catch (Exception e) {
            mostrarError("Error al inicializar el sistema: " + e.getMessage());
            System.exit(1);
        }
    }

    // ================================
    // GESTIÓN DE AUTENTICACIÓN
    // ================================

    public boolean autenticarUsuario(String id, String clave) {
        try {
            if (modelo.autenticarUsuario(id, clave)) {
                Usuario usuario = modelo.getUsuarioActual();

                // Ocultar ventana de login
                //ventanaLogin.setVisible(false);

                // Abrir ventana apropiada según tipo de usuario
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
                abrirVentanaMedico();
                break;
            case FARMACEUTA:
            case ADMINISTRADOR:
                abrirVentanaPrincipal();
                break;
        }
    }

    private void abrirVentanaPrincipal() {
        ventanaPrincipal = new VentanaPrincipal(this);
        ventanaPrincipal.setVisible(true);
    }

    private void abrirVentanaMedico() {
        ventanaMedico = new VentanaMedico(this);
        ventanaMedico.setVisible(true);
    }

    public void cerrarSesion() {
        // Cerrar ventanas abiertas
        if (ventanaPrincipal != null) {
            ventanaPrincipal.dispose();
            ventanaPrincipal = null;
        }

        if (ventanaMedico != null) {
            ventanaMedico.dispose();
            ventanaMedico = null;
        }

        // Cerrar sesión en modelo
        modelo.cerrarSesion();

        // Mostrar login nuevamente
        //ventanaLogin.limpiarCampos();
        //ventanaLogin.setVisible(true);
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
                mostrarMensaje("Clave cambiada exitosamente");
                return true;
            } else {
                mostrarError("La clave actual es incorrecta");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al cambiar clave: " + e.getMessage());
            return false;
        }
    }

    // ================================
    // GESTIÓN DE MÉDICOS
    // ================================

    public boolean agregarMedico(String id, String nombre, String especialidad) {
        try {
            // Validar permisos
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar médicos");
                return false;
            }

            // Validar datos
            if (id == null || id.trim().isEmpty()) {
                mostrarError("El ID del médico es obligatorio");
                return false;
            }

            if (nombre == null || nombre.trim().isEmpty()) {
                mostrarError("El nombre del médico es obligatorio");
                return false;
            }

            if (especialidad == null || especialidad.trim().isEmpty()) {
                mostrarError("La especialidad del médico es obligatoria");
                return false;
            }

            if (modelo.agregarMedico(id, nombre, especialidad)) {
                mostrarMensaje("Médico agregado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo agregar el médico. Verifique que el ID no exista");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar médico: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarMedico(String id, String nombre, String especialidad) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para actualizar médicos");
                return false;
            }

            if (modelo.actualizarMedico(id, nombre, especialidad)) {
                mostrarMensaje("Médico actualizado exitosamente");
                return true;
            } else {
                mostrarError("No se pudo actualizar el médico");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al actualizar médico: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarMedico(String id) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para eliminar médicos");
                return false;
            }

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
                    mostrarError("No se pudo eliminar el médico");
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

    // ================================
    // GESTIÓN DE PACIENTES
    // ================================

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

            if (fechaNacimiento == null) {
                mostrarError("La fecha de nacimiento es obligatoria");
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

    // ================================
    // GESTIÓN DE MEDICAMENTOS
    // ================================

    public boolean agregarMedicamento(String codigo, String nombre, String presentacion, int stock) {
        try {
            if (!modelo.puedeGestionarCatalogos()) {
                mostrarError("No tiene permisos para gestionar medicamentos");
                return false;
            }

            if (codigo == null || codigo.trim().isEmpty() ||
                    nombre == null || nombre.trim().isEmpty() ||
                    presentacion == null || presentacion.trim().isEmpty()) {
                mostrarError("Código, nombre y presentación son obligatorios");
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

    // ================================
    // GESTIÓN DE PRESCRIPCIONES
    // ================================

    public boolean iniciarNuevaReceta(String idPaciente, LocalDate fechaRetiro) {
        try {
            if (!modelo.puedePrescribir()) {
                mostrarError("No tiene permisos para prescribir recetas");
                return false;
            }

            // Buscar paciente
            Paciente paciente = modelo.obtenerPacientes().buscarPorId(idPaciente);
            if (paciente == null) {
                mostrarError("Paciente no encontrado");
                return false;
            }

            modelo.setPacienteSeleccionado(paciente);
            modelo.iniciarNuevaReceta(fechaRetiro);

            mostrarMensaje("Nueva receta iniciada para: " + paciente.getNombre());
            return true;
        } catch (Exception e) {
            mostrarError("Error al iniciar receta: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarMedicamentoAReceta(String codigoMedicamento, int cantidad,
                                             String indicaciones, int duracionDias) {
        try {
            if (modelo.getRecetaActual() == null) {
                mostrarError("Debe iniciar una receta primero");
                return false;
            }

            if (cantidad <= 0 || duracionDias <= 0) {
                mostrarError("Cantidad y duración deben ser mayores a cero");
                return false;
            }

            if (indicaciones == null || indicaciones.trim().isEmpty()) {
                mostrarError("Las indicaciones son obligatorias");
                return false;
            }

            if (modelo.agregarMedicamentoAReceta(codigoMedicamento, cantidad, indicaciones, duracionDias)) {
                mostrarMensaje("Medicamento agregado a la receta");
                return true;
            } else {
                mostrarError("No se pudo agregar el medicamento a la receta");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al agregar medicamento a receta: " + e.getMessage());
            return false;
        }
    }

    public boolean guardarReceta() {
        try {
            if (modelo.getRecetaActual() == null) {
                mostrarError("No hay receta para guardar");
                return false;
            }

            if (modelo.guardarReceta()) {
                mostrarMensaje("Receta guardada exitosamente");
                return true;
            } else {
                mostrarError("No se pudo guardar la receta. Verifique que tenga al menos un medicamento");
                return false;
            }
        } catch (Exception e) {
            mostrarError("Error al guardar receta: " + e.getMessage());
            return false;
        }
    }

    // ================================
    // GESTIÓN DE DESPACHO
    // ================================

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

    // ================================
    // GETTERS PARA LAS VISTAS
    // ================================

    public ModeloPrincipal getModelo() {
        return modelo;
    }

    public Usuario getUsuarioActual() {
        return modelo.getUsuarioActual();
    }

    public String generarReporteCompleto() {
        return modelo.generarReporteCompleto();
    }

    // ================================
    // MÉTODOS DE UTILIDAD
    // ================================

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
    Arreglar
    public void mostrarVentanaCambiarClave() {
        VentanaCambiarClave ventana = new VentanaCambiarClave(null);
        ventana.setControlador(this);
        ventana.setVisible(true);
    }*/

    // ================================
    // MÉTODOS PARA CERRAR APLICACIÓN
    // ================================

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
}
