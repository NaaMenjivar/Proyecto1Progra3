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

/**
 * Ventana para agregar medicamentos a la receta - Vista Modal
 */
public class VenatanaAgregarMedicamento extends JDialog {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JComboBox atributoMedicamento;
    private JTextField atributoFld;
    private JTable list;
    private JButton cancel;
    private JButton ok;
    private JSpinner cantidadMedicamento;
    private JSpinner duracionMedicamento;
    private JTextField indicacionesMedicamentoFld;
    private JButton cancelar;
    private JButton guardar;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Medicamento medicamentoSeleccionado = null;
    private DetalleReceta detalleCreado = null;
    private boolean confirmado = false;

    public VenatanaAgregarMedicamento(ControladorPrincipal controlador) {
        super((JFrame) null, "Agregar Medicamento", true);
        this.controlador = controlador;

        inicializarVentana();
        inicializarComponentes();
        configurarEventos();
        cargarTodosMedicamentos();
    }

    private void inicializarVentana() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void inicializarComponentes() {
        // Configurar combo de atributos para búsqueda
        atributoMedicamento.addItem("Nombre");
        atributoMedicamento.addItem("Código");
        atributoMedicamento.addItem("Presentación");
        atributoMedicamento.setSelectedIndex(0); // Por defecto: Nombre

        // Configurar tabla de medicamentos
        tableModel = TableModelPrincipal.crearModeloMedicamentos();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        list.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        list.getColumnModel().getColumn(2).setPreferredWidth(150); // Presentación
        list.getColumnModel().getColumn(3).setPreferredWidth(60);  // Stock

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar spinners
        configurarSpinners();

        // Estado inicial de botones
        ok.setEnabled(false);
        guardar.setEnabled(false);

        // Placeholder en campo de búsqueda
        atributoFld.setText("Escriba para buscar medicamentos...");
        atributoFld.setForeground(java.awt.Color.GRAY);
    }

    private void configurarSpinners() {
        // Spinner de cantidad
        SpinnerNumberModel cantidadModel = new SpinnerNumberModel(1, 1, 999, 1);
        cantidadMedicamento.setModel(cantidadModel);

        // Spinner de duración
        SpinnerNumberModel duracionModel = new SpinnerNumberModel(7, 1, 365, 1);
        duracionMedicamento.setModel(duracionModel);

        // Configurar editores para mostrar mejor
        JSpinner.NumberEditor cantidadEditor = new JSpinner.NumberEditor(cantidadMedicamento, "0");
        cantidadMedicamento.setEditor(cantidadEditor);

        JSpinner.NumberEditor duracionEditor = new JSpinner.NumberEditor(duracionMedicamento, "0");
        duracionMedicamento.setEditor(duracionEditor);
    }

    private void configurarEventos() {
        // Botón OK (de la parte superior)
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarMedicamentoParaDetalle();
            }
        });

        // Botón Cancel (de la parte superior)
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        // Botón Guardar (de la sección de medicamento)
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDetalleMedicamento();
            }
        });

        // Botón Cancelar (de la sección de medicamento)
        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        // Selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarMedicamento();
                }
            }
        });

        // Doble clic en tabla para seleccionar
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && medicamentoSeleccionado != null) {
                    seleccionarMedicamentoParaDetalle();
                }
            }
        });

        // Cambio en combo de atributos
        atributoMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPlaceholderBusqueda();
                realizarBusqueda();
            }
        });

        // Eventos del campo de búsqueda
        atributoFld.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (atributoFld.getForeground().equals(java.awt.Color.GRAY)) {
                    atributoFld.setText("");
                    atributoFld.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (atributoFld.getText().trim().isEmpty()) {
                    actualizarPlaceholderBusqueda();
                }
            }
        });

        // Búsqueda en tiempo real
        atributoFld.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { realizarBusqueda(); }
        });

        // ESC para cancelar
        getRootPane().registerKeyboardAction(
                e -> cancelarOperacion(),
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Validación en tiempo real de indicaciones
        indicacionesMedicamentoFld.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validarFormularioDetalle(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validarFormularioDetalle(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validarFormularioDetalle(); }
        });

        // Cambios en spinners
        cantidadMedicamento.addChangeListener(e -> validarFormularioDetalle());
        duracionMedicamento.addChangeListener(e -> validarFormularioDetalle());
    }

    private void actualizarPlaceholderBusqueda() {
        String atributoSeleccionado = (String) atributoMedicamento.getSelectedItem();

        if (atributoFld.getText().trim().isEmpty() ||
                atributoFld.getForeground().equals(java.awt.Color.GRAY)) {

            switch (atributoSeleccionado) {
                case "Nombre":
                    atributoFld.setText("Escriba el nombre del medicamento...");
                    break;
                case "Código":
                    atributoFld.setText("Escriba el código...");
                    break;
                case "Presentación":
                    atributoFld.setText("Escriba la presentación...");
                    break;
                default:
                    atributoFld.setText("Escriba para buscar medicamentos...");
            }
            atributoFld.setForeground(java.awt.Color.GRAY);
        }
    }

    private void realizarBusqueda() {
        String criterio = atributoFld.getText().trim();

        // No buscar si es texto placeholder
        if (criterio.isEmpty() || atributoFld.getForeground().equals(java.awt.Color.GRAY)) {
            cargarTodosMedicamentos();
            return;
        }

        String atributoSeleccionado = (String) atributoMedicamento.getSelectedItem();

        try {
            Lista<Medicamento> medicamentosEncontrados;

            if ("Nombre".equals(atributoSeleccionado) || "Presentación".equals(atributoSeleccionado)) {
                // Usar método específico de búsqueda por descripción
                medicamentosEncontrados = controlador.getModelo().buscarMedicamentosPorDescripcion(criterio);
            } else {
                // Filtrar por código
                Lista<Medicamento> todosMedicamentos = controlador.getModelo().obtenerMedicamentos();
                medicamentosEncontrados = new Lista<>();

                for (int i = 0; i < todosMedicamentos.getTam(); i++) {
                    Medicamento medicamento = todosMedicamentos.obtenerPorPos(i);

                    if ("Código".equals(atributoSeleccionado)) {
                        if (medicamento.getCodigo().toLowerCase().contains(criterio.toLowerCase())) {
                            medicamentosEncontrados.agregarFinal(medicamento);
                        }
                    }
                }
            }

            // Actualizar tabla
            Lista<Object> datos = new Lista<>();
            for (int i = 0; i < medicamentosEncontrados.getTam(); i++) {
                datos.agregarFinal(medicamentosEncontrados.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            // Actualizar título de la ventana con número de resultados
            if (medicamentosEncontrados.getTam() > 0) {
                setTitle("Agregar Medicamento - " + medicamentosEncontrados.getTam() + " encontrado(s)");
            } else {
                setTitle("Agregar Medicamento - Sin resultados");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar medicamentos: " + e.getMessage(),
                    "Error de búsqueda",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarMedicamento() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Object objeto = tableModel.getObjetoEnFila(filaSeleccionada);
            if (objeto instanceof Medicamento) {
                medicamentoSeleccionado = (Medicamento) objeto;
                ok.setEnabled(true);

                // Mostrar información del medicamento seleccionado
                mostrarInfoMedicamentoSeleccionado();
            }
        } else {
            medicamentoSeleccionado = null;
            ok.setEnabled(false);
        }
    }

    private void mostrarInfoMedicamentoSeleccionado() {
        if (medicamentoSeleccionado != null) {
            // Verificar stock
            if (medicamentoSeleccionado.getStock() <= 0) {
                setTitle("⚠️ Medicamento SIN STOCK - " + medicamentoSeleccionado.getNombre());
                JOptionPane.showMessageDialog(this,
                        "ADVERTENCIA: El medicamento seleccionado no tiene stock disponible.\n" +
                                "Stock actual: " + medicamentoSeleccionado.getStock() + " unidades",
                        "Sin stock",
                        JOptionPane.WARNING_MESSAGE);
            } else if (medicamentoSeleccionado.getStock() <= 10) {
                setTitle("⚠️ Medicamento STOCK BAJO - " + medicamentoSeleccionado.getNombre());
            } else {
                setTitle("Agregar Medicamento - " + medicamentoSeleccionado.getNombre());
            }
        }
    }

    private void seleccionarMedicamentoParaDetalle() {
        if (medicamentoSeleccionado != null) {
            // Habilitar sección de detalles
            guardar.setEnabled(true);

            // Enfocar en el campo de indicaciones
            indicacionesMedicamentoFld.requestFocus();

            // Mostrar mensaje informativo
            JOptionPane.showMessageDialog(this,
                    "Medicamento seleccionado: " + medicamentoSeleccionado.getDescripcionCompleta() + "\n" +
                            "Complete los detalles de prescripción abajo.",
                    "Complete detalles",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void validarFormularioDetalle() {
        boolean formularioValido = true;

        // Validar que hay medicamento seleccionado
        if (medicamentoSeleccionado == null) {
            formularioValido = false;
        }

        // Validar indicaciones
        String indicaciones = indicacionesMedicamentoFld.getText().trim();
        if (indicaciones.isEmpty()) {
            formularioValido = false;
        }

        // Validar cantidad y duración (los spinners ya tienen valores válidos por defecto)
        int cantidad = (Integer) cantidadMedicamento.getValue();
        int duracion = (Integer) duracionMedicamento.getValue();

        if (cantidad <= 0 || duracion <= 0) {
            formularioValido = false;
        }

        guardar.setEnabled(formularioValido);
    }

    private void guardarDetalleMedicamento() {
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un medicamento primero",
                    "Medicamento requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener valores del formulario
        int cantidad = (Integer) cantidadMedicamento.getValue();
        int duracion = (Integer) duracionMedicamento.getValue();
        String indicaciones = indicacionesMedicamentoFld.getText().trim();

        // Validaciones
        if (indicaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Las indicaciones son obligatorias",
                    "Indicaciones requeridas",
                    JOptionPane.WARNING_MESSAGE);
            indicacionesMedicamentoFld.requestFocus();
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a cero",
                    "Cantidad inválida",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (duracion <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La duración debe ser mayor a cero",
                    "Duración inválida",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar stock disponible
        if (cantidad > medicamentoSeleccionado.getStock()) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    String.format("ADVERTENCIA: Stock insuficiente\n\n" +
                                    "Cantidad solicitada: %d\n" +
                                    "Stock disponible: %d\n\n" +
                                    "¿Desea continuar de todas formas?",
                            cantidad, medicamentoSeleccionado.getStock()),
                    "Stock insuficiente",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Crear detalle de receta
        detalleCreado = new DetalleReceta(
                medicamentoSeleccionado.getCodigo(),
                cantidad,
                indicaciones,
                duracion
        );

        // Validar que el detalle es válido
        if (!detalleCreado.esValidoPrescripcion()) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear el detalle de prescripción. Verifique los datos.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirmado = true;

        // Mostrar mensaje de confirmación
        String mensaje = String.format(
                "Medicamento agregado exitosamente:\n\n" +
                        "Medicamento: %s\n" +
                        "Código: %s\n" +
                        "Cantidad: %s\n" +
                        "Duración: %s\n" +
                        "Indicaciones: %s",
                medicamentoSeleccionado.getDescripcionCompleta(),
                medicamentoSeleccionado.getCodigo(),
                detalleCreado.getCantidadTexto(),
                detalleCreado.getDuracionTexto(),
                indicaciones
        );

        JOptionPane.showMessageDialog(this, mensaje,
                "Medicamento agregado",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void cancelarOperacion() {
        // Verificar si hay datos ingresados
        boolean hayDatos = medicamentoSeleccionado != null ||
                !indicacionesMedicamentoFld.getText().trim().isEmpty() ||
                !cantidadMedicamento.getValue().equals(1) ||
                !duracionMedicamento.getValue().equals(7);

        if (hayDatos) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cancelar? Se perderán los datos ingresados.",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        detalleCreado = null;
        confirmado = false;
        dispose();
    }

    private void cargarTodosMedicamentos() {
        try {
            Lista<Medicamento> todosMedicamentos = controlador.getModelo().obtenerMedicamentos();
            Lista<Object> datos = new Lista<>();

            for (int i = 0; i < todosMedicamentos.getTam(); i++) {
                datos.agregarFinal(todosMedicamentos.obtenerPorPos(i));
            }

            tableModel.setDatos(datos);

            // Actualizar título
            setTitle("Agregar Medicamento - " + todosMedicamentos.getTam() + " medicamento(s) disponible(s)");

            // Mostrar medicamentos con stock bajo
            verificarMedicamentosBajoStock(todosMedicamentos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarMedicamentosBajoStock(Lista<Medicamento> medicamentos) {
        Lista<Medicamento> medicamentosBajoStock = new Lista<>();

        for (int i = 0; i < medicamentos.getTam(); i++) {
            Medicamento med = medicamentos.obtenerPorPos(i);
            if (med.getStock() <= 10) {
                medicamentosBajoStock.agregarFinal(med);
            }
        }

        if (medicamentosBajoStock.getTam() > 0) {
            // Mostrar advertencia discreta (sin bloquear la interfaz)
            SwingUtilities.invokeLater(() -> {
                setTitle("⚠️ " + medicamentosBajoStock.getTam() + " medicamento(s) con stock bajo");
            });
        }
    }

    // Método público para obtener el detalle creado
    public DetalleReceta getDetalleCreado() {
        return confirmado ? detalleCreado : null;
    }

    // Método para verificar si se confirmó la operación
    public boolean isConfirmado() {
        return confirmado;
    }

    // Método para obtener el medicamento seleccionado (por si se necesita)
    public Medicamento getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    // Método para centrar en pantalla
    public void centrarEnPantalla() {
        setLocationRelativeTo(null);
    }

    // Método para mostrar ayuda
    public void mostrarAyuda() {
        String ayuda =
                "CÓMO AGREGAR UN MEDICAMENTO:\n\n" +
                        "1. 🔍 Busque el medicamento usando el filtro:\n" +
                        "   • Por nombre, código o presentación\n\n" +
                        "2. 📋 Seleccione el medicamento de la lista:\n" +
                        "   • Doble clic o botón OK\n" +
                        "   • Verifique el stock disponible\n\n" +
                        "3. 📝 Complete los detalles:\n" +
                        "   • Cantidad: Unidades a prescribir\n" +
                        "   • Duración: Días del tratamiento\n" +
                        "   • Indicaciones: Cómo tomar el medicamento\n\n" +
                        "4. 💾 Guardar para agregarlo a la receta\n\n" +
                        "⚠️ El sistema alertará si hay stock insuficiente";

        JTextArea textArea = new JTextArea(ayuda);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(450, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Ayuda - Agregar Medicamento",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Override para manejar cierre de ventana
    @Override
    public void dispose() {
        // Si no se confirmó, limpiar
        if (!confirmado) {
            detalleCreado = null;
            medicamentoSeleccionado = null;
        }
        super.dispose();
    }
}
