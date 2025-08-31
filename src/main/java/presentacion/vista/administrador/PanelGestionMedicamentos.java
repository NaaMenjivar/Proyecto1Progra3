package presentacion.vista.administrador;

import logica.*;
import logica.excepciones.CatalogoException;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;
import modelo.*;
import modelo.lista.Lista;
import presentacion.modelo.ModeloTablaMedicamentos;

public class PanelGestionMedicamentos extends JPanel {
    // Componentes
    private JTable tabla;
    private ModeloTablaMedicamentos modeloTabla;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JTextField campoFiltro;

    // Gestores
    private GestorCatalogos gestor;

    public PanelGestionMedicamentos() {
        initGestores();
        initComponents();
        initEvents();
        initData();
    }

    private void initGestores() {
        gestor = new GestorCatalogos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Medicamentos"));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campoFiltro = new JTextField(20);
        panelFiltro.add(new JLabel("Filtrar:"));
        panelFiltro.add(campoFiltro);
        add(panelFiltro, BorderLayout.NORTH);

        modeloTabla = new ModeloTablaMedicamentos();
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnAgregar.addActionListener(e -> agregarMedicamento());
        btnModificar.addActionListener(e -> modificarMedicamento());
        btnEliminar.addActionListener(e -> eliminarMedicamento());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotones();
            }
        });

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modificarMedicamento();
                }
            }
        });

        campoFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarMedicamentos();
            }
        });
    }

    private void initData() {
        cargarMedicamentos();
        actualizarBotones();
    }

    // Métodos de acción
    private void agregarMedicamento() {
        String codigo = JOptionPane.showInputDialog(this, "Código del medicamento:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        String nombre = JOptionPane.showInputDialog(this, "Nombre del medicamento:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String presentacion = JOptionPane.showInputDialog(this, "Presentación:");
        if (presentacion == null || presentacion.trim().isEmpty()) return;

        String stockStr = JOptionPane.showInputDialog(this, "Stock inicial:");
        if (stockStr == null || stockStr.trim().isEmpty()) return;
        int stock;
        try {
            stock = Integer.parseInt(stockStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock debe ser un número entero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Medicamento medicamento = new Medicamento(codigo, nombre, presentacion);
            medicamento.setStock(stock);
            boolean exito = gestor.agregarMedicamento(medicamento);
            if (exito) {
                cargarMedicamentos();
                JOptionPane.showMessageDialog(this, "Medicamento agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el medicamento", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMedicamento() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para modificar");
            return;
        }

        Medicamento medicamento = modeloTabla.getMedicamentoEnFila(fila);
        if (medicamento != null) {
            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", medicamento.getNombre());
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

            String nuevaPresentacion = JOptionPane.showInputDialog(this, "Nueva presentación:", medicamento.getPresentacion());
            if (nuevaPresentacion == null || nuevaPresentacion.trim().isEmpty()) return;

            String nuevoStockStr = JOptionPane.showInputDialog(this, "Nuevo stock:", medicamento.getStock());
            if (nuevoStockStr == null || nuevoStockStr.trim().isEmpty()) return;
            int nuevoStock;
            try {
                nuevoStock = Integer.parseInt(nuevoStockStr.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stock debe ser un número entero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            medicamento.setNombre(nuevoNombre);
            medicamento.setPresentacion(nuevaPresentacion);
            medicamento.setStock(nuevoStock);

            try {
                boolean exito = gestor.actualizarMedicamento(medicamento);
                if (exito) {
                    cargarMedicamentos();
                    JOptionPane.showMessageDialog(this, "Medicamento actualizado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el medicamento", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarMedicamento() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para eliminar");
            return;
        }

        Medicamento medicamento = modeloTabla.getMedicamentoEnFila(fila);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el medicamento " + medicamento.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = gestor.eliminarMedicamento(medicamento.getCodigo());
                if (exito) {
                    cargarMedicamentos();
                    JOptionPane.showMessageDialog(this, "Medicamento eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el medicamento", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarBotones() {
        boolean haySeleccion = tabla.getSelectedRow() >= 0;
        btnModificar.setEnabled(haySeleccion);
        btnEliminar.setEnabled(haySeleccion);
    }

    private void cargarMedicamentos() {
        Lista<Medicamento> medicamentos = gestor.obtenerTodosMedicamentos();
        modeloTabla.setMedicamentos(medicamentos);
    }

    private void filtrarMedicamentos() {
        String filtro = campoFiltro.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            cargarMedicamentos();
            return;
        }

        Lista<Medicamento> todosMedicamentos = gestor.obtenerTodosMedicamentos();
        Lista<Medicamento> medicamentosFiltrados = new Lista<>();

        for (int i = 0; i < todosMedicamentos.getTam(); i++) {
            Medicamento medicamento = todosMedicamentos.obtenerPorPos(i);
            String nombre = medicamento.getNombre().toLowerCase();
            String codigo = medicamento.getCodigo().toLowerCase();
            String presentacion = medicamento.getPresentacion().toLowerCase();

            if (nombre.contains(filtro) || codigo.contains(filtro) || presentacion.contains(filtro)) {
                medicamentosFiltrados.agregarFinal(medicamento);
            }
        }

        modeloTabla.setMedicamentos(medicamentosFiltrados);
    }

    public void refrescarDatos() {
        cargarMedicamentos();
    }

    public int getNumeroMedicamentos() {
        return modeloTabla.getRowCount();
    }
}
