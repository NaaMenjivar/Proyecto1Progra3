package presentacion.vista.principales;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.ModeloPrincipal;
import presentacion.vista.administrador.PanelGestionFarmaceutas;
import presentacion.vista.administrador.PanelGestionMedicamentos;
import presentacion.vista.administrador.PanelGestionPacientes;
import presentacion.vista.administrador.PanelGestionMedicos;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;

    // Paneles de gestión
    private PanelGestionMedicos panelMedicos;
    private PanelGestionFarmaceutas panelFarmaceutas;
    private PanelGestionPacientes panelPacientes;
    private PanelGestionMedicamentos panelMedicamentos;

    private ControladorPrincipal controllerPrincipal;
    private ModeloPrincipal modeloPrincipal;

    public VentanaPrincipal() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupWindow();
    }

    private void initializeComponents() {
        setTitle("Sistema Hospital - Ventana Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear JTabbedPane principal
        tabbedPane = new JTabbedPane();

        // Inicializar paneles de gestión - USAR LOS PANELES REALES
        panelMedicos = new PanelGestionMedicos();
        panelFarmaceutas = new PanelGestionFarmaceutas();
        panelPacientes = new PanelGestionPacientes();
        panelMedicamentos = new PanelGestionMedicamentos();

        // Cargar datos iniciales en los paneles
        cargarDatosIniciales();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel Dashboard (placeholder)
        JPanel dashboardPanel = crearPanelPlaceholder("Dashboard",
                "Aquí se mostrarán estadísticas y gráficos del sistema");

        // Panel Histórico (placeholder)
        JPanel historicoPanel = crearPanelPlaceholder("Histórico de Recetas",
                "Aquí se mostrará el histórico de todas las recetas del sistema");

        // Panel Acerca de (placeholder)
        JPanel acercaPanel = crearPanelAcercaDe();

        // Agregar pestañas con iconos - USAR LOS PANELES REALES
        tabbedPane.addTab("Médicos", createTabIcon(Color.RED), panelMedicos, "Gestión de médicos del hospital");
        tabbedPane.addTab("Farmaceutas", createTabIcon(Color.ORANGE), panelFarmaceutas, "Gestión de farmaceutas del hospital");
        tabbedPane.addTab("Pacientes", createTabIcon(Color.BLUE), panelPacientes, "Gestión de pacientes del hospital");
        tabbedPane.addTab("Medicamentos", createTabIcon(Color.GREEN), panelMedicamentos, "Gestión del catálogo de medicamentos");
        tabbedPane.addTab("Dashboard", createTabIcon(Color.MAGENTA), dashboardPanel, "Estadísticas y reportes del sistema");
        tabbedPane.addTab("Histórico", createTabIcon(Color.CYAN), historicoPanel, "Histórico de recetas del sistema");
        tabbedPane.addTab("Acerca de...", createTabIcon(Color.GRAY), acercaPanel, "Información sobre el sistema");

        add(tabbedPane, BorderLayout.CENTER);

        // Agregar barra de estado
        JPanel barraEstado = crearBarraEstado();
        add(barraEstado, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        // Listener para cambio de pestañas
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String tabTitle = tabbedPane.getTitleAt(selectedIndex);

            // Refrescar datos cuando se selecciona una pestaña de gestión
            switch (selectedIndex) {
                case 0: // Médicos
                    if (panelMedicos != null) {
                        panelMedicos.refrescarDatos();
                    }
                    break;
                case 1: // Farmaceutas
                    if (panelFarmaceutas != null) {
                        panelFarmaceutas.refrescarDatos();
                    }
                    break;
                case 2: // Pacientes
                    if (panelPacientes != null) {
                        panelPacientes.refrescarDatos();
                    }
                    break;
                case 3: // Medicamentos
                    if (panelMedicamentos != null) {
                        panelMedicamentos.refrescarDatos();
                    }
                    break;
            }

            // Actualizar título de la ventana
            setTitle("Sistema Hospital - " + tabTitle);
        });
    }

    private void setupWindow() {
        // Configurar ventana
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar por defecto

        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("No se pudo configurar Look and Feel: " + e.getMessage());
        }
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================

    /**
     * Carga los datos iniciales en todos los paneles de gestión
     */
    private void cargarDatosIniciales() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Cargar datos en cada panel
                if (panelMedicos != null) {
                    panelMedicos.refrescarDatos();
                }
                if (panelFarmaceutas != null) {
                    panelFarmaceutas.refrescarDatos();
                }
                if (panelPacientes != null) {
                    panelPacientes.refrescarDatos();
                }
                if (panelMedicamentos != null) {
                    panelMedicamentos.refrescarDatos();
                }

                System.out.println("✅ Datos iniciales cargados en todos los paneles");
            } catch (Exception e) {
                System.err.println("❌ Error al cargar datos iniciales: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private JPanel crearPanelPlaceholder(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setForeground(new Color(0, 102, 153));

        JLabel labelDescripcion = new JLabel("<html><center>" + descripcion + "</center></html>", SwingConstants.CENTER);
        labelDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        labelDescripcion.setForeground(Color.GRAY);

        JLabel labelEstado = new JLabel("⚠️ Funcionalidad en desarrollo", SwingConstants.CENTER);
        labelEstado.setFont(new Font("Arial", Font.ITALIC, 12));
        labelEstado.setForeground(new Color(255, 140, 0));

        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(labelDescripcion, BorderLayout.CENTER);
        panel.add(labelEstado, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelAcercaDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Panel superior con logo/título
        JPanel panelSuperior = new JPanel(new FlowLayout());
        JLabel labelTitulo = new JLabel("Sistema Hospital v1.0");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(0, 102, 153));
        panelSuperior.add(labelTitulo);

        // Panel central con información
        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setOpaque(false);
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        areaInfo.setText(
                "Sistema de Prescripción y Despacho de Recetas\n\n" +
                        "Desarrollado para hospitales estatales que requieren un sistema\n" +
                        "digital para la gestión de recetas médicas.\n\n" +
                        "Funcionalidades principales:\n" +
                        "• Gestión de usuarios (médicos, farmaceutas, administradores)\n" +
                        "• Catálogo de pacientes y medicamentos\n" +
                        "• Prescripción digital de recetas\n" +
                        "• Despacho controlado en farmacia\n" +
                        "• Reportes y estadísticas\n\n" +
                        "Arquitectura: Capas + MVC\n" +
                        "Persistencia: XML con JDOM2\n" +
                        "Interfaz: Java Swing\n\n" +
                        "© 2024 - Sistema Hospital\n" +
                        "Versión 1.0.0"
        );

        JScrollPane scrollInfo = new JScrollPane(areaInfo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Información del Sistema"));
        scrollInfo.setPreferredSize(new Dimension(500, 300));

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollInfo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBorder(BorderFactory.createLoweredBevelBorder());
        barraEstado.setPreferredSize(new Dimension(0, 25));

        JLabel labelEstado = new JLabel(" Sistema Hospital - Listo");
        labelEstado.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel labelFecha = new JLabel(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " ");
        labelFecha.setFont(new Font("Arial", Font.PLAIN, 11));
        labelFecha.setHorizontalAlignment(SwingConstants.RIGHT);

        barraEstado.add(labelEstado, BorderLayout.WEST);
        barraEstado.add(labelFecha, BorderLayout.EAST);

        return barraEstado;
    }

    // ================================
    // MÉTODOS PARA CREAR ICONOS
    // ================================

    private Icon createTabIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
                g2.setColor(color.darker());
                g2.drawOval(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    // ================================
    // MÉTODOS PÚBLICOS PARA CONTROLADOR
    // ================================

    /**
     * Muestra un mensaje de error al usuario
     */
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de información al usuario
     */
    public void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cambia a una pestaña específica
     */
    public void cambiarAPestana(int indice) {
        if (indice >= 0 && indice < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(indice);
        }
    }

    /**
     * Obtiene el panel de médicos para acceso directo
     */
    public PanelGestionMedicos getPanelMedicos() {
        return panelMedicos;
    }

    /**
     * Obtiene el panel de farmaceutas para acceso directo
     */
    public PanelGestionFarmaceutas getPanelFarmaceutas() {
        return panelFarmaceutas;
    }

    /**
     * Obtiene el panel de pacientes para acceso directo
     */
    public PanelGestionPacientes getPanelPacientes() {
        return panelPacientes;
    }

    /**
     * Obtiene el panel de medicamentos para acceso directo
     */
    public PanelGestionMedicamentos getPanelMedicamentos() {
        return panelMedicamentos;
    }

    /**
     * Actualiza todos los paneles de gestión
     */
    public void refrescarTodosLosPaneles() {
        System.out.println("🔄 Refrescando todos los paneles...");

        try {
            if (panelMedicos != null) {
                try {
                    panelMedicos.refrescarDatos();
                    System.out.println("✅ Panel Médicos actualizado");
                } catch (Exception e) {
                    System.err.println("❌ Error al actualizar Panel Médicos: " + e.getMessage());
                }
            }

            if (panelFarmaceutas != null) {
                try {
                    panelFarmaceutas.refrescarDatos();
                    System.out.println("✅ Panel Farmaceutas actualizado");
                } catch (Exception e) {
                    System.err.println("❌ Error al actualizar Panel Farmaceutas: " + e.getMessage());
                }
            }

            if (panelPacientes != null) {
                try {
                    panelPacientes.refrescarDatos();
                    System.out.println("✅ Panel Pacientes actualizado");
                } catch (Exception e) {
                    System.err.println("❌ Error al actualizar Panel Pacientes: " + e.getMessage());
                }
            }

            if (panelMedicamentos != null) {
                try {
                    panelMedicamentos.refrescarDatos();
                    System.out.println("✅ Panel Medicamentos actualizado");
                } catch (Exception e) {
                    System.err.println("❌ Error al actualizar Panel Medicamentos: " + e.getMessage());
                }
            }

            System.out.println("✅ Actualización de paneles completada");

        } catch (Exception e) {
            System.err.println("❌ Error general al actualizar paneles: " + e.getMessage());
            mostrarMensajeError("Error al actualizar datos: " + e.getMessage());
        }
    }

    /**
     * Obtiene estadísticas generales del sistema
     */
    public String obtenerEstadisticasGenerales() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS GENERALES ===\n");

        try {
            if (panelMedicos != null) {
                stats.append("Médicos: ").append(panelMedicos.getNumeroMedicos()).append("\n");
            }
            if (panelFarmaceutas != null) {
                stats.append("Farmaceutas: ").append(panelFarmaceutas.getNumeroFarmaceutas()).append("\n");
            }
            if (panelPacientes != null) {
                stats.append("Pacientes: ").append(panelPacientes.getNumeroPacientes()).append("\n");
            }
            if (panelMedicamentos != null) {
                stats.append("Medicamentos: ").append(panelMedicamentos.getNumeroMedicamentos()).append("\n");
            }
        } catch (Exception e) {
            stats.append("Error al obtener estadísticas: ").append(e.getMessage());
        }

        return stats.toString();
    }

    /**
     * Verifica si hay cambios sin guardar en algún panel
     */
    public boolean hayCambiosSinGuardar() {
        try {
            if (panelMedicos != null && panelMedicos.hayCambiosSinGuardar()) {
                return true;
            }
            // Agregar verificaciones para otros paneles cuando tengan el método
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar cambios: " + e.getMessage());
            return false;
        }
    }

    /**
     * Establece el controlador principal
     */
    public void setControlador(ControladorPrincipal controlador) {
        this.controllerPrincipal = controlador;
    }

    /**
     * Establece el modelo principal
     */
    public void setModelo(ModeloPrincipal modelo) {
        this.modeloPrincipal = modelo;
    }
}