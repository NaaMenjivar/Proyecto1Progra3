package presentacion.vista.principales;

import presentacion.controlador.ControladorPrincipal;
import presentacion.modelo.ModeloPrincipal;
import presentacion.vista.administrador.PanelGestionFarmaceutas;
import presentacion.vista.administrador.PanelGestionMedicamentos;
import presentacion.vista.administrador.PanelGestionPacientes;
import presentacion.vista.administrador.PanelGestionMedicos;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana Principal del Sistema Hospital
 * ✅ ACTUALIZADA para usar PanelGestionMedicos con patrón MVC
 */
public class VentanaPrincipal extends JFrame {
    private JTabbedPane tabbedPane;

    // ============================================
    // PANELES DE GESTIÓN CON MVC
    // ============================================
    private PanelGestionMedicos panelMedicos;              // ✅ Usa MVC completo
    private PanelGestionFarmaceutas panelFarmaceutas;      // ✅ Mantiene patrón existente
    private PanelGestionPacientes panelPacientes;          // ✅ Mantiene patrón existente
    private PanelGestionMedicamentos panelMedicamentos;    // ✅ Mantiene patrón existente

    // ============================================
    // CONTROLADORES Y MODELOS MVC
    // ============================================
    private ControladorPrincipal controllerPrincipal;
    private ModeloPrincipal modeloPrincipal;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public VentanaPrincipal() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupWindow();
    }

    // ============================================
    // INICIALIZACIÓN DE COMPONENTES
    // ============================================
    private void initializeComponents() {
        setTitle("Sistema Hospital - Ventana Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear JTabbedPane principal
        tabbedPane = new JTabbedPane();

        // ✅ INICIALIZAR PANELES - el MVC se configura automáticamente
        panelMedicos = new PanelGestionMedicos();              // ✅ MVC ya configurado en constructor
        panelFarmaceutas = new PanelGestionFarmaceutas();      // ✅ Patrón existente
        panelPacientes = new PanelGestionPacientes();          // ✅ Patrón existente
        panelMedicamentos = new PanelGestionMedicamentos();    // ✅ Patrón existente
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // ============================================
        // PANELES PLACEHOLDER PARA FUNCIONALIDADES FALTANTES
        // ============================================

        // Panel Dashboard (placeholder)
        JPanel dashboardPanel = crearPanelPlaceholder("Dashboard",
                "Aquí se mostrarán estadísticas y gráficos del sistema");

        // Panel Histórico (placeholder)
        JPanel historicoPanel = crearPanelPlaceholder("Histórico de Recetas",
                "Aquí se mostrará el histórico de todas las recetas del sistema");

        // Panel Prescripción (placeholder para médicos)
        JPanel prescripcionPanel = crearPanelPlaceholder("Prescripción de Recetas",
                "Aquí los médicos podrán confeccionar recetas para pacientes");

        // Panel Despacho (placeholder para farmaceutas)
        JPanel despachoPanel = crearPanelPlaceholder("Despacho de Medicamentos",
                "Aquí los farmaceutas podrán despachar medicamentos");

        // Panel Acerca de
        JPanel acercaPanel = crearPanelAcercaDe();

        // ============================================
        // AGREGAR PESTAÑAS CON ICONOS
        // ============================================

        // Pestañas de gestión (Administrador)
        tabbedPane.addTab("Médicos", createTabIcon(Color.RED), panelMedicos,
                "Gestión de médicos del hospital - ✅ MVC Completo");
        tabbedPane.addTab("Farmaceutas", createTabIcon(Color.ORANGE), panelFarmaceutas,
                "Gestión de farmaceutas del hospital");
        tabbedPane.addTab("Pacientes", createTabIcon(Color.BLUE), panelPacientes,
                "Gestión de pacientes del hospital");
        tabbedPane.addTab("Medicamentos", createTabIcon(Color.GREEN), panelMedicamentos,
                "Gestión del catálogo de medicamentos");

        // Pestañas funcionales (en desarrollo)
        tabbedPane.addTab("Prescripción", createTabIcon(Color.MAGENTA), prescripcionPanel,
                "Prescripción de recetas - En desarrollo");
        tabbedPane.addTab("Despacho", createTabIcon(Color.CYAN), despachoPanel,
                "Despacho de medicamentos - En desarrollo");

        // Pestañas de información
        tabbedPane.addTab("Dashboard", createTabIcon(Color.PINK), dashboardPanel,
                "Estadísticas y reportes del sistema - En desarrollo");
        tabbedPane.addTab("Histórico", createTabIcon(Color.LIGHT_GRAY), historicoPanel,
                "Histórico de recetas del sistema - En desarrollo");
        tabbedPane.addTab("Acerca de...", createTabIcon(Color.GRAY), acercaPanel,
                "Información sobre el sistema");

        add(tabbedPane, BorderLayout.CENTER);

        // Agregar barra de estado
        JPanel barraEstado = crearBarraEstado();
        add(barraEstado, BorderLayout.SOUTH);
    }

    // ============================================
    // CONFIGURACIÓN DE EVENTOS
    // ============================================
    private void setupEventListeners() {
        // Listener para cambio de pestañas
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String tabTitle = tabbedPane.getTitleAt(selectedIndex);

            // ✅ REFRESCAR DATOS cuando se selecciona una pestaña de gestión
            switch (selectedIndex) {
                case 0: // Médicos - ✅ MVC automático, no necesita refresh manual
                    // El MVC se encarga automáticamente de mantener datos actualizados
                    System.out.println("✅ Pestaña Médicos seleccionada - MVC activo");
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

                default:
                    // Otras pestañas no necesitan refresh
                    break;
            }

            // Actualizar título de la ventana
            setTitle("Sistema Hospital - " + tabTitle);
        });
    }

    // ============================================
    // CONFIGURACIÓN DE VENTANA
    // ============================================
    private void setupWindow() {
        // Configurar ventana
        setSize(1200, 800);
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

    // ============================================
    // MÉTODOS AUXILIARES PARA CREAR PANELES
    // ============================================

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
                        "• Prescripción digital de recetas (en desarrollo)\n" +
                        "• Despacho controlado en farmacia (en desarrollo)\n" +
                        "• Reportes y estadísticas\n\n" +
                        "Arquitectura: Capas + MVC\n" +
                        "Persistencia: XML con JDOM2\n" +
                        "Interfaz: Java Swing\n\n" +
                        "Estado actual:\n" +
                        "✅ Gestión de Médicos - MVC Completo\n" +
                        "✅ Gestión de Farmaceutas - Funcional\n" +
                        "✅ Gestión de Pacientes - Funcional\n" +
                        "✅ Gestión de Medicamentos - Funcional\n" +
                        "⚠️ Prescripción - En desarrollo\n" +
                        "⚠️ Despacho - En desarrollo\n" +
                        "⚠️ Dashboard - En desarrollo\n\n" +
                        "© 2024 - Sistema Hospital\n" +
                        "Versión 1.0.0"
        );

        JScrollPane scrollInfo = new JScrollPane(areaInfo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Información del Sistema"));
        scrollInfo.setPreferredSize(new Dimension(500, 400));

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollInfo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBorder(BorderFactory.createLoweredBevelBorder());
        barraEstado.setPreferredSize(new Dimension(0, 25));

        JLabel labelEstado = new JLabel(" Sistema Hospital - Listo | Médicos: MVC Activo");
        labelEstado.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel labelFecha = new JLabel(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " ");
        labelFecha.setFont(new Font("Arial", Font.PLAIN, 11));
        labelFecha.setHorizontalAlignment(SwingConstants.RIGHT);

        barraEstado.add(labelEstado, BorderLayout.WEST);
        barraEstado.add(labelFecha, BorderLayout.EAST);

        return barraEstado;
    }

    // ============================================
    // MÉTODOS PARA CREAR ICONOS
    // ============================================
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

    // ============================================
    // MÉTODOS PÚBLICOS PARA CONTROLADOR
    // ============================================

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
     * ✅ Obtiene el panel de médicos para acceso directo (MVC)
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
     * ✅ Actualiza todos los paneles de gestión
     */
    public void refrescarTodosLosPaneles() {
        // ✅ Panel de médicos - MVC se actualiza automáticamente
        if (panelMedicos != null) {
            panelMedicos.refrescarDatos();
        }

        // Otros paneles - refresh manual
        if (panelFarmaceutas != null) {
            panelFarmaceutas.refrescarDatos();
        }
        if (panelPacientes != null) {
            panelPacientes.refrescarDatos();
        }
        if (panelMedicamentos != null) {
            panelMedicamentos.refrescarDatos();
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

    /**
     * ✅ Obtiene estadísticas de todos los paneles
     */
    public String obtenerResumenSistema() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DEL SISTEMA ===\n");

        // Estadísticas del panel de médicos (MVC)
        if (panelMedicos != null) {
            resumen.append("Médicos registrados: ").append(panelMedicos.getNumeroMedicos()).append("\n");
        }

        // Estadísticas de otros paneles
        if (panelFarmaceutas != null) {
            resumen.append("Farmaceutas registrados: ").append(panelFarmaceutas.getNumeroFarmaceutas()).append("\n");
        }
        if (panelPacientes != null) {
            resumen.append("Pacientes registrados: ").append(panelPacientes.getNumeroPacientes()).append("\n");
        }
        if (panelMedicamentos != null) {
            resumen.append("Medicamentos en catálogo: ").append(panelMedicamentos.getNumeroMedicamentos()).append("\n");
        }

        resumen.append("\n✅ Panel de Médicos: MVC Completo");
        resumen.append("\n✅ Otros paneles: Funcionales");

        return resumen.toString();
    }
}