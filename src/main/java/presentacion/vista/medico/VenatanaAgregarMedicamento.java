package presentacion.vista.medico;

import logica.entidades.lista.CatalogoMedicamentos;
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
        list.getColumnModel().getColumn(3).setPreferredWidth(80);  // Stock

        // Configurar tabla para mejor visualización
        list.setRowHeight(25);
        list.getTableHeader().setReorderingAllowed(false);

        // Configurar spinners
        cantidadMedicamento.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        duracionMedicamento.setModel(new SpinnerNumberModel(7, 1, 365, 1));

        // Deshabilitar botones inicialmente
        guardar.setEnabled(false);
        ok.setEnabled(false);
    }

    private void configurarEventos() {
        // Botón Guardar (principal)
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDetalleMedicamento();
            }
        });

        // Botón OK (alternativo)
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDetalleMedicamento();
            }
        });

        // Botón Cancelar (principal)
        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        // Botón Cancel (alternativo)
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarOperacion();
            }
        });

        // Búsqueda al escribir
        atributoFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicamentos();
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

        // Doble click en tabla
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && medicamentoSeleccionado != null) {
                    guardarDetalleMedicamento();
                }
            }
        });
    }

    private void buscarMedicamentos() {
        String criterio = atributoFld.getText().trim();
        String atributoSeleccionado = (String) atributoMedicamento.getSelectedItem();

        try {
            if (criterio.isEmpty()) {
                cargarTodosMedicamentos();
            } else {
                CatalogoMedicamentos medicamentos = controlador.getModelo().obtenerMedicamentos();
                Lista<Object> medicamentosFiltrados = new Lista<>();

                for (Medicamento medicamento : medicamentos) {;
                    boolean coincide = false;
                    switch (atributoSeleccionado) {
                        case "Nombre":
                            coincide = medicamento.getNombre().toLowerCase().contains(criterio.toLowerCase());
                            break;
                        case "Código":
                            coincide = medicamento.getCodigo().toLowerCase().contains(criterio.toLowerCase());
                            break;
                        case "Presentación":
                            coincide = medicamento.getPresentacion().toLowerCase().contains(criterio.toLowerCase());
                            break;
                    }

                    if (coincide) {
                        medicamentosFiltrados.agregarFinal(medicamento);
                    }
                }

                tableModel.setDatos(medicamentosFiltrados);

                if (medicamentosFiltrados.getTam() == 0) {
                    JOptionPane.showMessageDialog(this,
                            "No se encontraron medicamentos con el criterio: " + criterio,
                            "Sin resultados",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarMedicamento() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                Object elemento = tableModel.getObjetoEnFila(filaSeleccionada);
                if (elemento instanceof Medicamento) {
                    medicamentoSeleccionado = (Medicamento) elemento;
                    guardar.setEnabled(true);
                    ok.setEnabled(true);

                    // Verificar stock bajo
                    if (medicamentoSeleccionado.getStock() <= 10) {
                        JOptionPane.showMessageDialog(this,
                                "⚠️ ADVERTENCIA: Este medicamento tiene stock bajo (" +
                                        medicamentoSeleccionado.getStock() + " unidades)",
                                "Stock Bajo",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (Exception e) {
                medicamentoSeleccionado = null;
                guardar.setEnabled(false);
                ok.setEnabled(false);
            }
        } else {
            medicamentoSeleccionado = null;
            guardar.setEnabled(false);
            ok.setEnabled(false);
        }
    }

    private void guardarDetalleMedicamento() {
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un medicamento de la lista",
                    "Medicamento no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Obtener valores de los campos
            int cantidad = (Integer) cantidadMedicamento.getValue();
            int duracion = (Integer) duracionMedicamento.getValue();
            String indicaciones = indicacionesMedicamentoFld.getText().trim();

            if (indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Las indicaciones son obligatorias",
                        "Indicaciones requeridas",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar stock suficiente
            if (cantidad > medicamentoSeleccionado.getStock()) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "La cantidad solicitada (" + cantidad + ") excede el stock disponible (" +
                                medicamentoSeleccionado.getStock() + ").\n¿Desea continuar de todos modos?",
                        "Stock Insuficiente",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirmacion != JOptionPane.YES_OPTION) {
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

            confirmado = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear detalle de medicamento: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarOperacion() {
        // Confirmar cancelación si hay cambios
        if (medicamentoSeleccionado != null || !indicacionesMedicamentoFld.getText().trim().isEmpty()) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
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
            CatalogoMedicamentos todosMedicamentos = controlador.getModelo().obtenerMedicamentos();
            Lista<Object> datos = new Lista<>();

            for (Medicamento medicamento : todosMedicamentos) {
                datos.agregarFinal(medicamento);
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

    private void verificarMedicamentosBajoStock(CatalogoMedicamentos medicamentos) {
        Lista<Medicamento> medicamentosBajoStock = new Lista<>();

        for (Medicamento med : medicamentos) {
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
                        "2. 👆 Seleccione el medicamento de la lista\n\n" +
                        "3. ⚙️ Configure los detalles:\n" +
                        "   • Cantidad: número de unidades\n" +
                        "   • Duración: días de tratamiento\n" +
                        "   • Indicaciones: instrucciones para el paciente\n\n" +
                        "4. ✅ Presione 'Guardar' para agregar a la receta\n\n" +
                        "⚠️ Atención: Los medicamentos con stock bajo se marcan\n" +
                        "con advertencias para su conocimiento.";

        JOptionPane.showMessageDialog(this, ayuda, "Ayuda - Agregar Medicamento",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
