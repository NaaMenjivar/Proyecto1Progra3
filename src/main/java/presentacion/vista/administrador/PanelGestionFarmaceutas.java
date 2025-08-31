package presentacion.vista.administrador;

import logica.*;
import logica.excepciones.CatalogoException;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import modelo.lista.Lista;
import presentacion.modelo.ModeloTablaUsuarios;

public class PanelGestionFarmaceutas extends JPanel {
    // Componentes
    private JTable tabla;
    private ModeloTablaUsuarios modeloTabla;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JTextField campoFiltro;

    // Gestores
    private GestorCatalogos gestor;

    public PanelGestionFarmaceutas() {
        initGestores();      // 1. Inicializar gestores
        initComponents();    // 2. Crear componentes
        initEvents();        // 3. Configurar eventos
        initData();          // 4. Cargar datos iniciales
    }

    private void initGestores() {
        gestor = new GestorCatalogos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Farmaceutas"));

        // Panel superior - Filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campoFiltro = new JTextField(20);
        panelFiltro.add(new JLabel("Filtrar:"));
        panelFiltro.add(campoFiltro);
        add(panelFiltro, BorderLayout.NORTH);

        // Panel central - Tabla
        modeloTabla = new ModeloTablaUsuarios();
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior - Botones
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
        // Eventos de botones
        btnAgregar.addActionListener(e -> agregarFarmaceuta());
        btnModificar.addActionListener(e -> modificarFarmaceuta());
        btnEliminar.addActionListener(e -> eliminarFarmaceuta());

        // Eventos de tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotones();
            }
        });

        // Eventos de mouse - doble clic para modificar
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    modificarFarmaceuta();
                }
            }
        });

        // Evento de filtro
        campoFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarFarmaceutas();
            }
        });
    }

    private void initData() {
        cargarFarmaceutas();
        actualizarBotones();
    }

    // Métodos de acción
    private void agregarFarmaceuta() {
        String id = JOptionPane.showInputDialog(this, "ID del farmaceuta:");
        if (id == null || id.trim().isEmpty()) return;

        String nombre = JOptionPane.showInputDialog(this, "Nombre del farmaceuta:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            Farmaceuta farmaceuta = new Farmaceuta(id, nombre, id); // clave = id por defecto
            boolean exito = gestor.agregarFarmaceuta(farmaceuta);
            if (exito) {
                cargarFarmaceutas();
                JOptionPane.showMessageDialog(this, "Farmaceuta agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el farmaceuta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarFarmaceuta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un farmaceuta para modificar");
            return;
        }

        Usuario usuario = modeloTabla.getUsuarioEnFila(fila);
        if (usuario instanceof Farmaceuta) {
            Farmaceuta farmaceuta = (Farmaceuta) usuario;

            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", farmaceuta.getNombre());
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                farmaceuta.setNombre(nuevoNombre);

                try {
                    boolean exito = gestor.actualizarUsuario(farmaceuta);
                    if (exito) {
                        cargarFarmaceutas();
                        JOptionPane.showMessageDialog(this, "Farmaceuta actualizado exitosamente");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo actualizar el farmaceuta", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void eliminarFarmaceuta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un farmaceuta para eliminar");
            return;
        }

        Usuario usuario = modeloTabla.getUsuarioEnFila(fila);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al farmaceuta " + usuario.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = gestor.eliminarUsuario(usuario.getId());
                if (exito) {
                    cargarFarmaceutas();
                    JOptionPane.showMessageDialog(this, "Farmaceuta eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el farmaceuta", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void cargarFarmaceutas() {
        Lista<Usuario> farmaceutas = gestor.buscarFarmaceutas();
        modeloTabla.setUsuarios(farmaceutas);
    }

    private void filtrarFarmaceutas() {
        String filtro = campoFiltro.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            cargarFarmaceutas(); // Mostrar todos si no hay filtro
            return;
        }

        // Filtrar farmaceutas por nombre o ID
        Lista<Usuario> todosFarmaceutas = gestor.buscarFarmaceutas();
        Lista<Usuario> farmaceutasFiltrados = new Lista<>();

        for (int i = 0; i < todosFarmaceutas.getTam(); i++) {
            Usuario farmaceuta = todosFarmaceutas.obtenerPorPos(i);
            String nombre = farmaceuta.getNombre().toLowerCase();
            String id = farmaceuta.getId().toLowerCase();

            if (nombre.contains(filtro) || id.contains(filtro)) {
                farmaceutasFiltrados.agregarFinal(farmaceuta);
            }
        }

        modeloTabla.setUsuarios(farmaceutasFiltrados);
    }

    // Método público para refrescar datos (útil para cuando se llame desde VentanaPrincipal)
    public void refrescarDatos() {
        cargarFarmaceutas();
    }

    // Método para obtener el número de farmaceutas
    public int getNumeroFarmaceutas() {
        return modeloTabla.getRowCount();
    }
}
