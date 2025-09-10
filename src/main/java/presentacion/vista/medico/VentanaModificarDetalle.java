package presentacion.vista.medico;

import presentacion.controlador.ControladorPrincipal;
import logica.entidades.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana para modificar detalles de medicamentos en la receta - Vista Modal
 */
public class VentanaModificarDetalle extends JDialog {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JSpinner cantidadMedicamento;
    private JSpinner duracionMedicamento;  // CAMPO CORREGIDO - este ya estaba
    private JTextField indicacionesMedicamento;  // CAMPO CORREGIDO - era indicacionesMedicamentoFld
    private JButton guardar;
    private JButton cancelar;

    // MVC Components
    private ControladorPrincipal controlador;
    private DetalleReceta detalleOriginal;
    private DetalleReceta detalleModificado = null;
    private Medicamento medicamentoInfo = null;
    private boolean confirmado = false;

    public VentanaModificarDetalle(ControladorPrincipal controlador, DetalleReceta detalle) {
        super((JFrame) null, "Modificar Detalle de Medicamento", true);
        this.controlador = controlador;
        this.detalleOriginal = detalle;

        inicializarVentana();
        inicializarComponentes();
        configurarEventos();
        cargarDatosDetalle();
    }

    private void inicializarVentana() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        // Configurar spinners
        configurarSpinners();

        // Cargar información del medicamento
        cargarInformacionMedicamento();
    }

    private void configurarSpinners() {
        // Spinner de cantidad
        SpinnerNumberModel cantidadModel = new SpinnerNumberModel(1, 1, 999, 1);
        cantidadMedicamento.setModel(cantidadModel);

        // Spinner de duración
        SpinnerNumberModel duracionModel = new SpinnerNumberModel(1, 1, 365, 1);
        duracionMedicamento.setModel(duracionModel);

        // Configurar editores para mostrar mejor
        JSpinner.NumberEditor cantidadEditor = new JSpinner.NumberEditor(cantidadMedicamento, "0");
        cantidadMedicamento.setEditor(cantidadEditor);

        JSpinner.NumberEditor duracionEditor = new JSpinner.NumberEditor(duracionMedicamento, "0");
        duracionMedicamento.setEditor(duracionEditor);
    }

    private void configurarEventos() {
        // Botón Guardar
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });

        // Botón Cancelar
        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarModificacion();
            }
        });

        // Validación en tiempo real
        cantidadMedicamento.addChangeListener(e -> validarFormulario());
        duracionMedicamento.addChangeListener(e -> validarFormulario());

        indicacionesMedicamento.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validarFormulario(); }
        });
    }

    private void cargarInformacionMedicamento() {
        if (detalleOriginal == null) return;

        try {
            medicamentoInfo = controlador.getModelo().getGestorCatalogos()
                    .obtenerTodosMedicamentos().buscarPorId(detalleOriginal.getCodigoMedicamento());
        } catch (Exception e) {
            // Manejar error silenciosamente
        }
    }

    private void cargarDatosDetalle() {
        if (detalleOriginal != null) {
            // Cargar valores actuales en los componentes
            cantidadMedicamento.setValue(detalleOriginal.getCantidad());
            duracionMedicamento.setValue(detalleOriginal.getDuracionDias());
            indicacionesMedicamento.setText(detalleOriginal.getIndicaciones());

            // Validar formulario inicial
            validarFormulario();

            // Enfocar en el primer campo
            SwingUtilities.invokeLater(() -> cantidadMedicamento.requestFocus());
        }
    }

    private void validarFormulario() {
        boolean formularioValido = true;

        // Validar cantidad
        int cantidad = (Integer) cantidadMedicamento.getValue();
        if (cantidad <= 0) {
            formularioValido = false;
        }

        // Validar duración
        int duracion = (Integer) duracionMedicamento.getValue();
        if (duracion <= 0) {
            formularioValido = false;
        }

        // Validar indicaciones
        String indicaciones = indicacionesMedicamento.getText().trim();
        if (indicaciones.isEmpty()) {
            formularioValido = false;
        }

        // Verificar si hay cambios
        boolean hayCambios = false;
        if (detalleOriginal != null) {
            hayCambios = cantidad != detalleOriginal.getCantidad() ||
                    duracion != detalleOriginal.getDuracionDias() ||
                    !indicaciones.equals(detalleOriginal.getIndicaciones());
        }

        // Habilitar guardar solo si es válido y hay cambios
        guardar.setEnabled(formularioValido && hayCambios);

        // Actualizar texto del botón
        if (!hayCambios && formularioValido) {
            guardar.setText("Sin cambios");
        } else if (formularioValido && hayCambios) {
            guardar.setText("Guardar cambios");
        } else {
            guardar.setText("Guardar");
        }
    }

    private void guardarCambios() {
        // Obtener valores del formulario
        int cantidad = (Integer) cantidadMedicamento.getValue();
        int duracion = (Integer) duracionMedicamento.getValue();
        String indicaciones = indicacionesMedicamento.getText().trim();

        // Validaciones finales
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a cero",
                    "Cantidad inválida",
                    JOptionPane.ERROR_MESSAGE);
            cantidadMedicamento.requestFocus();
            return;
        }

        if (duracion <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La duración debe ser mayor a cero",
                    "Duración inválida",
                    JOptionPane.ERROR_MESSAGE);
            duracionMedicamento.requestFocus();
            return;
        }

        if (indicaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Las indicaciones son obligatorias",
                    "Indicaciones requeridas",
                    JOptionPane.WARNING_MESSAGE);
            indicacionesMedicamento.requestFocus();
            return;
        }

        // Crear detalle modificado
        detalleModificado = new DetalleReceta(
                detalleOriginal.getCodigoMedicamento(),
                cantidad,
                indicaciones,
                duracion
        );

        // Validar que el detalle modificado es válido
        if (!detalleModificado.esValidoPrescripcion()) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar los cambios. Verifique los datos ingresados.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmar cambios
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Confirmar los cambios realizados?",
                "Confirmar modificación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            confirmado = true;

            JOptionPane.showMessageDialog(this,
                    "✅ Detalle del medicamento modificado exitosamente",
                    "Cambios guardados",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    private void cancelarModificacion() {
        // Verificar si hay cambios sin guardar
        boolean hayCambios = false;

        if (detalleOriginal != null) {
            int cantidad = (Integer) cantidadMedicamento.getValue();
            int duracion = (Integer) duracionMedicamento.getValue();
            String indicaciones = indicacionesMedicamento.getText().trim();

            hayCambios = cantidad != detalleOriginal.getCantidad() ||
                    duracion != detalleOriginal.getDuracionDias() ||
                    !indicaciones.equals(detalleOriginal.getIndicaciones());
        }

        if (hayCambios) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "Hay cambios sin guardar. ¿Está seguro de cancelar?",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        detalleModificado = null;
        confirmado = false;
        dispose();
    }

    // Método público para obtener el detalle modificado
    public DetalleReceta getDetalleModificado() {
        return confirmado ? detalleModificado : null;
    }

    // Método para verificar si se confirmó la operación
    public boolean isConfirmado() {
        return confirmado;
    }
}
