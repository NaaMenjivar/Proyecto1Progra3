package presentacion.vista.medico;

import logica.entidades.lista.Lista;
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
    private JSpinner cantidadSpinner;
    private JSpinner duracionSpinner;
    private JTextField indicacionesFld;
    private JButton guardar;
    private JButton cancelar;
    private JLabel medicamentoLabel;

    // MVC Components
    private ControladorPrincipal controlador;
    private DetalleReceta detalleOriginal;
    private DetalleReceta detalleModificado = null;
    private boolean confirmado = false;

    public VentanaModificarDetalle(ControladorPrincipal controlador, DetalleReceta detalle) {
        super((JFrame) null, "Modificar Detalle", true);
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
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        // Configurar spinners
        cantidadSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        duracionSpinner.setModel(new SpinnerNumberModel(7, 1, 365, 1));

        // Configurar campo de indicaciones
        indicacionesFld.setColumns(30);
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

        // Enter en campo de indicaciones
        indicacionesFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
    }

    private void cargarDatosDetalle() {
        if (detalleOriginal == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No se recibió información del detalle a modificar",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        try {
            // Buscar información del medicamento para mostrar
            Lista<Medicamento> medicamentos = controlador.getModelo().obtenerMedicamentos();
            Medicamento medicamento = null;

            for (int i = 0; i < medicamentos.getTam(); i++) {
                Medicamento med = medicamentos.obtenerPorPos(i);
                if (med.getCodigo().equals(detalleOriginal.getCodigoMedicamento())) {
                    medicamento = med;
                    break;
                }
            }

            // Mostrar información del medicamento
            if (medicamento != null) {
                medicamentoLabel.setText("Medicamento: " + medicamento.getNombre() +
                        " (" + medicamento.getCodigo() + ") - " +
                        medicamento.getPresentacion());
            } else {
                medicamentoLabel.setText("Medicamento: " + detalleOriginal.getCodigoMedicamento());
            }

            // Cargar valores actuales
            cantidadSpinner.setValue(detalleOriginal.getCantidad());
            duracionSpinner.setValue(detalleOriginal.getDuracionDias());
            indicacionesFld.setText(detalleOriginal.getIndicaciones());

            // Actualizar título
            setTitle("Modificar Detalle - " +
                    (medicamento != null ? medicamento.getNombre() : detalleOriginal.getCodigoMedicamento()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos del detalle: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarCambios() {
        try {
            // Obtener nuevos valores
            int nuevaCantidad = (Integer) cantidadSpinner.getValue();
            int nuevaDuracion = (Integer) duracionSpinner.getValue();
            String nuevasIndicaciones = indicacionesFld.getText().trim();

            // Validar indicaciones
            if (nuevasIndicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Las indicaciones son obligatorias",
                        "Indicaciones requeridas",
                        JOptionPane.WARNING_MESSAGE);
                indicacionesFld.requestFocus();
                return;
            }

            // Verificar si hay cambios
            boolean hayCambios = nuevaCantidad != detalleOriginal.getCantidad() ||
                    nuevaDuracion != detalleOriginal.getDuracionDias() ||
                    !nuevasIndicaciones.equals(detalleOriginal.getIndicaciones());

            if (!hayCambios) {
                JOptionPane.showMessageDialog(this,
                        "No se detectaron cambios en el detalle",
                        "Sin cambios",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }

            // Verificar stock si aumentó la cantidad
            if (nuevaCantidad > detalleOriginal.getCantidad()) {
                Lista<Medicamento> medicamentos = controlador.getModelo().obtenerMedicamentos();
                Medicamento medicamento = null;

                for (int i = 0; i < medicamentos.getTam(); i++) {
                    Medicamento med = medicamentos.obtenerPorPos(i);
                    if (med.getCodigo().equals(detalleOriginal.getCodigoMedicamento())) {
                        medicamento = med;
                        break;
                    }
                }

                if (medicamento != null && nuevaCantidad > medicamento.getStock()) {
                    int confirmacion = JOptionPane.showConfirmDialog(this,
                            "La cantidad solicitada (" + nuevaCantidad + ") excede el stock disponible (" +
                                    medicamento.getStock() + ").\n¿Desea continuar de todos modos?",
                            "Stock Insuficiente",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirmacion != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // Crear detalle modificado
            detalleModificado = new DetalleReceta(
                    detalleOriginal.getCodigoMedicamento(),
                    nuevaCantidad,
                    nuevasIndicaciones,
                    nuevaDuracion
            );

            // Confirmar cambios
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de guardar los cambios realizados?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                confirmado = true;
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar cambios: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarModificacion() {
        // Verificar si hay cambios sin guardar
        boolean hayCambios = false;

        try {
            int nuevaCantidad = (Integer) cantidadSpinner.getValue();
            int nuevaDuracion = (Integer) duracionSpinner.getValue();
            String nuevasIndicaciones = indicacionesFld.getText().trim();

            hayCambios = nuevaCantidad != detalleOriginal.getCantidad() ||
                    nuevaDuracion != detalleOriginal.getDuracionDias() ||
                    !nuevasIndicaciones.equals(detalleOriginal.getIndicaciones());
        } catch (Exception e) {
            // Ignorar errores en la verificación
        }

        if (hayCambios) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de cancelar? Se perderán los cambios realizados.",
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

    // Método para obtener el detalle original
    public DetalleReceta getDetalleOriginal() {
        return detalleOriginal;
    }

    // Método para centrar en pantalla
    public void centrarEnPantalla() {
        setLocationRelativeTo(null);
    }

    // Método para mostrar ayuda
    public void mostrarAyuda() {
        String ayuda =
                "CÓMO MODIFICAR UN DETALLE:\n\n" +
                        "1. ⚙️ Modifique los valores deseados:\n" +
                        "   • Cantidad: número de unidades\n" +
                        "   • Duración: días de tratamiento\n" +
                        "   • Indicaciones: instrucciones para el paciente\n\n" +
                        "2. ✅ Presione 'Guardar' para confirmar cambios\n\n" +
                        "3. ❌ Presione 'Cancelar' para descartar cambios\n\n" +
                        "⚠️ Nota: Si aumenta la cantidad, se verificará\n" +
                        "el stock disponible del medicamento.";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Modificar Detalle",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
