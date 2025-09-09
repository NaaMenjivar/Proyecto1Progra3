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
    // Componentes del formulario (declarados en el .form)
    private JButton buscarPaciente;
    private JButton agregarMedicamento;
    private JSpinner fechaRetiro;
    private JTable list;
    private JButton guardar;
    private JButton Limpiar;
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

        // Configurar spinner de fecha
        configurarSpinnerFecha();

        // Inicializar estado de botones
        actualizarEstadoBotones();

        // Crear label informativo si no existe
        if (lblPacienteInfo == null) {
            lblPacienteInfo = new JLabel("Seleccione un paciente para iniciar la prescripción");
            lblPacienteInfo.setHorizontalAlignment(SwingConstants.CENTER);
            // Nota: Agregar al panel si es necesario
        }
    }

    private void configurarSpinnerFecha() {
        // Configurar spinner para fechas
        SpinnerDateModel dateModel = new SpinnerDateModel();
        fechaRetiro.setModel(dateModel);

        // Configurar formato de fecha
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaRetiro, "dd/MM/yyyy");
        fechaRetiro.setEditor(dateEditor);

        // Establecer fecha por defecto (mañana)
        LocalDate fechaDefault = LocalDate.now().plusDays(1);
        Date fecha = Date.from(fechaDefault.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fecha);
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

        // Botón Modificar Detalles
        modificarDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaModificarDetalle();
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

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarDetalleMedicamento();
                }
            }
        });

        // Cambio en fecha de retiro
        fechaRetiro.addChangeListener(e -> {
            if (controlador.getModelo().getRecetaActual() != null) {
                Date fechaDate = (Date) fechaRetiro.getValue();
                LocalDate nuevaFecha = fechaDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                controlador.getModelo().getRecetaActual().setFechaRetiro(nuevaFecha);
            }
        });
    }

    private void abrirVentanaBuscarPaciente() {
        try {
            VentanaBuscarPaciente ventana = new VentanaBuscarPaciente(controlador);
            ventana.setModal(true);
            ventana.setLocationRelativeTo(panelPrincipal);
            ventana.setVisible(true);

            // Obtener paciente seleccionado después de cerrar la ventana
            Paciente pacienteElegido = ventana.getPacienteSeleccionado();
            if (pacienteElegido != null) {
                establecerPaciente(pacienteElegido);
            }

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

    private void establecerPaciente(Paciente paciente) {
        this.pacienteSeleccionado = paciente;

        // Actualizar información del paciente
        actualizarInfoPaciente();

        // Iniciar nueva receta
        Date fechaDate = (Date) fechaRetiro.getValue();
        LocalDate fechaRetiroReceta = fechaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (controlador.iniciarNuevaReceta(paciente.getId(), fechaRetiroReceta)) {
            // Limpiar tabla de medicamentos
            tableModel.limpiar();

            // Actualizar estado de botones
            actualizarEstadoBotones();

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Nueva receta iniciada para: " + paciente.getNombre(),
                    "Receta iniciada",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarInfoPaciente() {
        // Los labels ya están en el .form, solo actualizar si es necesario
        if (pacienteSeleccionado != null) {
            // Actualizar título de la ventana padre si es posible
            actualizarTituloVentana();
        }
    }

    private void actualizarTituloVentana() {
        // Actualizar título si la ventana padre lo permite
        try {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(panelPrincipal);
            if (ventanaPadre != null && pacienteSeleccionado != null) {
                ventanaPadre.setTitle("Prescribir - " + pacienteSeleccionado.getNombre());
            }
        } catch (Exception e) {
            // Silenciar error si no se puede actualizar título
        }
    }

    private void agregarDetalleAReceta(DetalleReceta detalle) {
        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual != null) {
            recetaActual.agregarDetalle(detalle);
            actualizarTablaDetalles();

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Medicamento agregado a la receta",
                    "Medicamento agregado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void modificarDetalleEnReceta(DetalleReceta detalleModificado) {
        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual != null && filaSeleccionada >= 0) {
            recetaActual.modificarDetalle(filaSeleccionada, detalleModificado);
            actualizarTablaDetalles();

            JOptionPane.showMessageDialog(panelPrincipal,
                    "Detalle del medicamento modificado",
                    "Medicamento actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void seleccionarDetalleMedicamento() {
        filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof DetalleReceta) {
                detalleSeleccionado = (DetalleReceta) objeto;

                // Habilitar botones de modificación y descarte
                modificarDetalles.setEnabled(true);
                descartarMedicamento.setEnabled(true);
            }
        } else {
            detalleSeleccionado = null;
            modificarDetalles.setEnabled(false);
            descartarMedicamento.setEnabled(false);
        }
    }

    private void descartarMedicamentoSeleccionado() {
        if (detalleSeleccionado == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                "¿Está seguro de eliminar el medicamento: " + detalleSeleccionado.getCodigoMedicamento() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            Receta recetaActual = controlador.getModelo().getRecetaActual();
            if (recetaActual != null) {
                if (recetaActual.eliminarDetalle(detalleSeleccionado.getCodigoMedicamento())) {
                    actualizarTablaDetalles();

                    // Limpiar selección
                    detalleSeleccionado = null;
                    filaSeleccionada = -1;
                    list.clearSelection();

                    JOptionPane.showMessageDialog(panelPrincipal,
                            "Medicamento eliminado de la receta",
                            "Medicamento eliminado",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void guardarReceta() {
        // Validar que hay un paciente seleccionado
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Debe seleccionar un paciente para la receta",
                    "Paciente requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que hay medicamentos en la receta
        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual == null || !recetaActual.tieneDetalles()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Debe agregar al menos un medicamento a la receta",
                    "Medicamentos requeridos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar guardado
        int confirmacion = JOptionPane.showConfirmDialog(panelPrincipal,
                String.format("¿Desea guardar la receta para %s con %d medicamento(s)?",
                        pacienteSeleccionado.getNombre(),
                        recetaActual.getTotalMedicamentos()),
                "Confirmar guardado de receta",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.guardarReceta()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "✅ Receta guardada exitosamente\n" +
                                "Número de receta: " + recetaActual.getNumeroReceta() + "\n" +
                                "Estado: " + recetaActual.getEstado().getDescripcion(),
                        "Receta guardada",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar formulario después de guardar
                limpiarFormulario();
            }
        }
    }

    private void limpiarFormulario() {
        // Limpiar paciente seleccionado
        pacienteSeleccionado = null;
        detalleSeleccionado = null;
        filaSeleccionada = -1;

        // Limpiar tabla
        tableModel.limpiar();

        // Restablecer fecha por defecto
        LocalDate fechaDefault = LocalDate.now().plusDays(1);
        Date fecha = Date.from(fechaDefault.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fecha);

        // Actualizar información
        actualizarInfoPaciente();
        actualizarEstadoBotones();

        // Limpiar selección de tabla
        list.clearSelection();
    }

    private void actualizarTablaDetalles() {
        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual != null) {
            Lista<DetalleReceta> detalles = recetaActual.getDetalles();
            Lista<Object> datosTabla = new Lista<>();

            for (int i = 0; i < detalles.getTam(); i++) {
                datosTabla.agregarFinal(detalles.obtenerPorPos(i));
            }

            tableModel.setDatos(datosTabla);
        }
    }

    private void actualizarEstadoBotones() {
        boolean hayPaciente = pacienteSeleccionado != null;
        boolean hayMedicamentos = false;

        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual != null) {
            hayMedicamentos = recetaActual.tieneDetalles();
        }

        // Habilitar/deshabilitar botones según estado
        agregarMedicamento.setEnabled(hayPaciente);
        guardar.setEnabled(hayPaciente && hayMedicamentos);
        modificarDetalles.setEnabled(false); // Se habilita al seleccionar en tabla
        descartarMedicamento.setEnabled(false); // Se habilita al seleccionar en tabla

        // El botón de buscar paciente y limpiar siempre están habilitados
        buscarPaciente.setEnabled(true);
        Limpiar.setEnabled(true);
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    // Método para refrescar datos desde el exterior
    public void refrescarPanel() {
        limpiarFormulario();
    }

    // Método para verificar si hay trabajo sin guardar
    public boolean tieneTrabajoSinGuardar() {
        Receta recetaActual = controlador.getModelo().getRecetaActual();
        return recetaActual != null && recetaActual.tieneDetalles();
    }

    // Método para obtener resumen de la receta actual
    public String getResumenRecetaActual() {
        if (pacienteSeleccionado == null) {
            return "No hay receta en progreso";
        }

        Receta recetaActual = controlador.getModelo().getRecetaActual();
        if (recetaActual == null) {
            return "No hay receta iniciada";
        }

        return String.format("Receta para %s - %s",
                pacienteSeleccionado.getNombre(),
                recetaActual.getResumenMedicamentos());
    }

    // Método para validar permisos
    private boolean validarPermisos() {
        if (!controlador.getModelo().puedePrescribir()) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "No tiene permisos para prescribir recetas",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Método para mostrar ayuda
    public void mostrarAyuda() {
        String ayuda =
                "CÓMO USAR EL PANEL DE PRESCRIPCIÓN:\n\n" +
                        "1. 🔍 Buscar Paciente: Seleccione el paciente para quien prescribir\n\n" +
                        "2. 💊 Agregar Medicamento: Agregue medicamentos a la receta\n" +
                        "   - Especifique cantidad, duración e indicaciones\n\n" +
                        "3. ✏️ Modificar Detalles: Seleccione un medicamento de la lista y modifique\n\n" +
                        "4. ❌ Descartar Medicamento: Elimine medicamentos de la receta\n\n" +
                        "5. 💾 Guardar: Guarde la receta completa (mínimo 1 medicamento)\n\n" +
                        "6. 🧹 Limpiar: Limpie el formulario para empezar nueva receta\n\n" +
                        "📅 La fecha de retiro por defecto es mañana, pero puede cambiarla.";

        JTextArea textArea = new JTextArea(ayuda);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 350));

        JOptionPane.showMessageDialog(panelPrincipal, scrollPane,
                "Ayuda - Panel de Prescripción",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
