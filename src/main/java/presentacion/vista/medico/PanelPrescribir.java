package presentacion.vista.medico;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.TableModelPrincipal;
import logica.entidades.*;
import logica.entidades.lista.Lista;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Panel de Prescribir para Médicos - Vista MVC
 */
public class PanelPrescribir {
    // Componentes del formulario (declarados en el .form) - TODOS LOS CAMPOS NECESARIOS
    private JButton buscarPaciente;
    private JButton agregarMedicamento;
    private JSpinner fechaRetiro;
    private JTable list;                 // CAMPO CORREGIDO PARA BINDING
    private JButton guardar;
    private JButton Limpiar;             // Nota: mantener la mayúscula como está en el form
    private JButton descartarMedicamento;
    private JButton modificarDetalles;
    private JPanel panelPrincipal;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Paciente pacienteSeleccionado = null;
    private DetalleReceta detalleSeleccionado = null;
    private int filaSeleccionada = -1;

    // Labels informativos (si no están en el form, se pueden agregar programáticamente)
    private JLabel lblPacienteInfo;

    public PanelPrescribir(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        limpiarFormulario();
    }

    private void inicializarComponentes() {
        // Configurar tabla de detalles de receta
        tableModel = TableModelPrincipal.crearModeloDetallesReceta();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(100); // Código Medicamento
        list.getColumnModel().getColumn(1).setPreferredWidth(80);  // Cantidad
        list.getColumnModel().getColumn(2).setPreferredWidth(200); // Indicaciones
        list.getColumnModel().getColumn(3).setPreferredWidth(80);  // Duración

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar fecha de retiro (fecha del día siguiente por defecto)
        LocalDate fechaManana = LocalDate.now().plusDays(1);
        Date fechaDate = Date.from(fechaManana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fechaDate);

        // Configurar estado inicial de botones
        actualizarEstadoBotones();
    }

    private void configurarEventos() {
        // Botón Buscar Paciente
        buscarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaBuscarPaciente();
            }
        });

        // Botón Agregar Medicamento
        agregarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaAgregarMedicamento();
            }
        });

        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarReceta();
            }
        });

        // Botón Limpiar
        Limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        // Botón Descartar Medicamento
        descartarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descartarMedicamentoSeleccionado();
            }
        });

        // Botón Modificar Detalles
        modificarDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaModificarDetalle();
            }
        });

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarSeleccionTabla();
                }
            }
        });

        // Cambio en fecha de retiro
        fechaRetiro.addChangeListener(e -> validarFormulario());
    }

    private void abrirVentanaBuscarPaciente() {
        try {
            // Aquí se abriría la ventana de búsqueda de pacientes
            // Por ahora, mostrar mensaje temporal
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Función de búsqueda de pacientes en desarrollo.\n" +
                            "Use el módulo de administrador para gestionar pacientes.",
                    "Función en desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al abrir ventana de búsqueda: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaAgregarMedicamento() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Debe seleccionar un paciente primero",
                    "Paciente requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            VenatanaAgregarMedicamento ventana = new VenatanaAgregarMedicamento(controlador);
            ventana.setModal(true);
            ventana.setLocationRelativeTo(panelPrincipal);
            ventana.setVisible(true);

            // Obtener detalle del medicamento agregado
            DetalleReceta nuevoDetalle = ventana.getDetalleCreado();
            if (nuevoDetalle != null) {
                agregarDetalleAReceta(nuevoDetalle);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al abrir ventana de medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaModificarDetalle() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un medicamento de la lista para modificar",
                    "Medicamento requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            VentanaModificarDetalle ventana = new VentanaModificarDetalle(controlador, detalleSeleccionado);
            ventana.setModal(true);
            ventana.setLocationRelativeTo(panelPrincipal);
            ventana.setVisible(true);

            // Obtener detalle modificado
            DetalleReceta detalleModificado = ventana.getDetalleModificado();
            if (detalleModificado != null) {
                modificarDetalleEnReceta(detalleModificado);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al abrir ventana de modificación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarReceta() {
        if (!validarRecetaCompleta()) {
            return;
        }

        try {
            if (controlador.guardarReceta()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Receta guardada exitosamente",
                        "Receta guardada",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Error al guardar la receta. Verifique los datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        // Limpiar selecciones
        pacienteSeleccionado = null;
        detalleSeleccionado = null;
        filaSeleccionada = -1;

        // Limpiar tabla
        tableModel.limpiar();

        // Resetear fecha de retiro
        LocalDate fechaManana = LocalDate.now().plusDays(1);
        Date fechaDate = Date.from(fechaManana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fechaDate);

        // Actualizar botones
        actualizarEstadoBotones();

        // Actualizar información del paciente
        actualizarInfoPaciente();
    }

    private void descartarMedicamentoSeleccionado() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un medicamento para descartar",
                    "Medicamento requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Está seguro de eliminar este medicamento de la receta?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            tableModel.eliminarObjeto(filaSeleccionada);
            detalleSeleccionado = null;
            filaSeleccionada = -1;
            actualizarEstadoBotones();
        }
    }

    private void agregarDetalleAReceta(DetalleReceta nuevoDetalle) {
        if (nuevoDetalle != null) {
            tableModel.agregarObjeto(nuevoDetalle);
            validarFormulario();

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Medicamento agregado a la receta",
                    "Medicamento agregado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modificarDetalleEnReceta(DetalleReceta detalleModificado) {
        if (detalleModificado != null && filaSeleccionada >= 0) {
            tableModel.setValueAt(detalleModificado, filaSeleccionada, -1);
            validarFormulario();

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Medicamento modificado exitosamente",
                    "Medicamento modificado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarSeleccionTabla() {
        filaSeleccionada = list.getSelectedRow();

        if (filaSeleccionada >= 0) {
            try {
                detalleSeleccionado = (DetalleReceta) tableModel.getValueAt(filaSeleccionada, -1);
            } catch (Exception e) {
                detalleSeleccionado = null;
            }
        } else {
            detalleSeleccionado = null;
        }

        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean hayPaciente = pacienteSeleccionado != null;
        boolean hayMedicamentos = tableModel.getRowCount() > 0;
        boolean haySeleccion = detalleSeleccionado != null;

        agregarMedicamento.setEnabled(hayPaciente);
        guardar.setEnabled(hayPaciente && hayMedicamentos);
        descartarMedicamento.setEnabled(haySeleccion);
        modificarDetalles.setEnabled(haySeleccion);
    }

    private void actualizarInfoPaciente() {
        // Actualizar información del paciente si hay labels informativos
        if (lblPacienteInfo != null) {
            if (pacienteSeleccionado != null) {
                lblPacienteInfo.setText("Paciente: " + pacienteSeleccionado.getNombre());
            } else {
                lblPacienteInfo.setText("No hay paciente seleccionado");
            }
        }
    }

    private boolean validarRecetaCompleta() {
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Debe seleccionar un paciente",
                    "Paciente requerido",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Debe agregar al menos un medicamento",
                    "Medicamentos requeridos",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Date fechaDate = (Date) fechaRetiro.getValue();
        LocalDate fechaRetiroReceta = fechaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (fechaRetiroReceta.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "La fecha de retiro no puede ser anterior a hoy",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validarFormulario() {
        boolean formularioValido = validarRecetaCompleta();
        actualizarEstadoBotones();
        return formularioValido;
    }

    // Métodos públicos
    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void establecerPaciente(Paciente paciente) {
        this.pacienteSeleccionado = paciente;
        actualizarInfoPaciente();
        actualizarEstadoBotones();

        // Iniciar nueva receta en el controlador
        Date fechaDate = (Date) fechaRetiro.getValue();
        LocalDate fechaRetiroReceta = fechaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        controlador.iniciarNuevaReceta(paciente.getId(), fechaRetiroReceta);
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void limpiar() {
        limpiarFormulario();
    }
}
