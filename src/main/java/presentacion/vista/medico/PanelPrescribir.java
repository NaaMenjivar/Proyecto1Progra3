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
 * Panel de Prescripción para Médicos - Vista MVC
 * Utiliza el .form correspondiente
 */
public class PanelPrescribir {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JButton buscarPaciente;
    private JButton agregarMedicamento;
    private JSpinner fechaRetiro;
    private JTable table1;
    private JButton guardar;
    private JButton limpiar;
    private JButton descartar;
    private JButton detalles;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Paciente pacienteSeleccionado = null;
    private DetalleReceta detalleSeleccionado = null;

    public PanelPrescribir(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        actualizarEstadoBotones();
    }

    private void inicializarComponentes() {
        // Configurar tabla de detalles de receta
        tableModel = TableModelPrincipal.crearModeloDetallesReceta();
        table1.setModel(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        table1.getColumnModel().getColumn(0).setPreferredWidth(120); // Código Medicamento
        table1.getColumnModel().getColumn(1).setPreferredWidth(80);  // Cantidad
        table1.getColumnModel().getColumn(2).setPreferredWidth(200); // Indicaciones
        table1.getColumnModel().getColumn(3).setPreferredWidth(100); // Duración

        // Configurar tabla para mejor visualización
        table1.setRowHeight(25);
        table1.getTableHeader().setReorderingAllowed(false);

        // Configurar spinner de fecha
        fechaRetiro.setModel(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaRetiro, "dd/MM/yyyy");
        fechaRetiro.setEditor(dateEditor);

        // Establecer fecha por defecto (mañana)
        LocalDate manana = LocalDate.now().plusDays(1);
        Date fechaDate = Date.from(manana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fechaDate);
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
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarReceta();
            }
        });

        // Botón Descartar
        descartar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descartarMedicamento();
            }
        });

        // Botón Detalles
        detalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaModificarDetalle();
            }
        });

        // Selección en tabla
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarDetalle();
                }
            }
        });
    }

    private void abrirVentanaBuscarPaciente() {
        try {
            VentanaBuscarPaciente ventana = new VentanaBuscarPaciente(controlador);
            ventana.setModal(true);
            ventana.setLocationRelativeTo(panelPrincipal);
            ventana.setVisible(true);

            // Obtener paciente seleccionado
            Paciente paciente = ventana.getPacienteSeleccionado();
            if (paciente != null) {
                establecerPaciente(paciente);
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
        if (controlador.agregarMedicamentoAReceta(
                detalle.getCodigoMedicamento(),
                detalle.getCantidad(),
                detalle.getIndicaciones(),
                detalle.getDuracionDias())) {

            actualizarTablaDetalles();
            actualizarEstadoBotones();
        }
    }

    private void modificarDetalleEnReceta(DetalleReceta detalleModificado) {
        // TODO: Implementar modificación cuando esté disponible en el controlador
        JOptionPane.showMessageDialog(panelPrincipal,
                "Funcionalidad de modificación en desarrollo",
                "En desarrollo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void descartarMedicamento() {
        if (detalleSeleccionado == null) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Seleccione un medicamento de la lista para descartar",
                    "Medicamento no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                panelPrincipal,
                "¿Está seguro de descartar este medicamento de la receta?",
                "Confirmar descarte",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            // TODO: Implementar eliminación de detalle cuando esté disponible
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Funcionalidad de descarte en desarrollo",
                    "En desarrollo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarReceta() {
        try {
            if (pacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Debe seleccionar un paciente antes de guardar",
                        "Paciente requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Actualizar fecha de retiro si cambió
            Date fechaDate = (Date) fechaRetiro.getValue();
            LocalDate fechaRetiroReceta = fechaDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (controlador.guardarReceta()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Receta guardada exitosamente",
                        "Receta guardada",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarReceta();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al guardar receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarReceta() {
        pacienteSeleccionado = null;
        detalleSeleccionado = null;
        tableModel.limpiar();

        // Resetear fecha a mañana
        LocalDate manana = LocalDate.now().plusDays(1);
        Date fechaDate = Date.from(manana.atStartOfDay(ZoneId.systemDefault()).toInstant());
        fechaRetiro.setValue(fechaDate);

        table1.clearSelection();
        actualizarEstadoBotones();

        // Limpiar título
        try {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(panelPrincipal);
            if (ventanaPadre != null) {
                ventanaPadre.setTitle("Sistema Hospitalario - Módulo Médico");
            }
        } catch (Exception e) {
            // Silenciar error
        }
    }

    private void seleccionarDetalle() {
        int filaSeleccionada = table1.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof DetalleReceta) {
                    detalleSeleccionado = (DetalleReceta) elemento;
                }
            } catch (Exception e) {
                detalleSeleccionado = null;
            }
        } else {
            detalleSeleccionado = null;
        }
        actualizarEstadoBotones();
    }

    private void actualizarTablaDetalles() {
        try {
            Receta recetaActual = controlador.getModelo().getRecetaActual();
            if (recetaActual != null && recetaActual.getDetalles() != null) {
                Lista<DetalleReceta> detalles = recetaActual.getDetalles();
                Lista<Object> datos = new Lista<>();

                for (int i = 0; i < detalles.getTam(); i++) {
                    datos.agregarFinal(detalles.obtenerPorPos(i));
                }

                tableModel.setDatos(datos);
            }
        } catch (Exception e) {
            // Silenciar error de actualización
        }
    }

    private void actualizarEstadoBotones() {
        boolean hayPaciente = pacienteSeleccionado != null;
        boolean hayDetalleSeleccionado = detalleSeleccionado != null;

        try {
            Receta recetaActual = controlador.getModelo().getRecetaActual();
            boolean hayDetalles = recetaActual != null && recetaActual.tieneDetalles();

            agregarMedicamento.setEnabled(hayPaciente);
            guardar.setEnabled(hayPaciente && hayDetalles);
            limpiar.setEnabled(hayPaciente);
            descartar.setEnabled(hayDetalleSeleccionado);
            detalles.setEnabled(hayDetalleSeleccionado);
        } catch (Exception e) {
            agregarMedicamento.setEnabled(hayPaciente);
            guardar.setEnabled(false);
            limpiar.setEnabled(hayPaciente);
            descartar.setEnabled(hayDetalleSeleccionado);
            detalles.setEnabled(hayDetalleSeleccionado);
        }
    }

    /**
     * Obtiene el panel principal para ser añadido a contenedores
     * @return JPanel principal del formulario
     */
    public JPanel getPanel() {
        return panelPrincipal;
    }

    /**
     * Método para refrescar datos desde el exterior
     */
    public void refrescarDatos() {
        limpiarReceta();
    }
}
