package presentacion.vista.administrador;

import presentacion.controlador.ControladorCatalogos;
import presentacion.modelo.ModeloTablaUsuarios;
import modelo.*;
import modelo.lista.Lista;
import utilidades.GeneradorIds;
import logica.excepciones.CatalogoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * VISTA - MVC Simple con .form de IntelliJ
 * SOLUCION: Fuerza la inicialización correcta del .form
 */
public class PanelGestionMedicos extends JPanel {

    // ============================================
    // COMPONENTES EXACTOS DEL ARCHIVO .form
    // DEBEN coincidir EXACTAMENTE con el .form
    // ============================================

    // Paneles principales
    private JPanel Medico;
    private JPanel Busqueda;
    private JPanel Listado;

    // Campos de texto
    private JTextField idFld;
    private JTextField nombreFld;
    private JTextField especialidadFld;
    private JTextField nombreBusquedaFld;

    // Botones
    private JButton botonGuardarMedico;
    private JButton botonLimpiarCamposMedico;
    private JButton botonBorrarMedico;
    private JButton botonBuscar;
    private JButton botonGenerarReporte;

    // Labels
    private JLabel idLabel;
    private JLabel nombreLabel;
    private JLabel especialidadLabel;
    private JLabel tituloMedico;
    private JLabel nombreBusquedaLabel;

    // Paneles de botones
    private JPanel panelBotones1;
    private JPanel panelBotones2;

    // Tabla y scroll - ESTOS SON CRÍTICOS
    private JTable tablaListaMedicos;
    private JScrollPane scrollTablaMedicos;  // Nombre exacto del .form

    // ============================================
    // COMPONENTES MVC
    // ============================================
    private ControladorCatalogos controlador;
    private ModeloTablaUsuarios modelo;
    private boolean modoEdicion = false;

    // ============================================
    // CONSTRUCTOR CON FORZADO DE INICIALIZACION
    // ============================================
    public PanelGestionMedicos() {
        System.out.println("🚀 INICIANDO PanelGestionMedicos...");

        // CRÍTICO: Forzar la inicialización del .form PRIMERO
        try {
            // Llamar al método generado por IntelliJ (si existe)
            $$$setupUI$$$();
        } catch (Exception e) {
            System.out.println("⚠️ Método $$$setupUI$$$() no encontrado, IntelliJ lo generará automáticamente");
        }

        // Pequeña pausa para asegurar inicialización
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(50); // 50ms para asegurar que el .form esté listo
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            inicializarComponentes();
        });
    }

    /**
     * MÉTODO CRÍTICO: Aquí se inicializa todo después del .form
     */
    private void inicializarComponentes() {
        System.out.println("🔧 Inicializando componentes...");

        // Verificar componentes críticos
        verificarComponentesDelForm();

        // Inicializar MVC
        initMVC();

        // Configurar todo
        configurarTabla();
        configurarEventos();
        configurarEstilos();

        // Cargar datos
        cargarDatos();

        System.out.println("✅ PanelGestionMedicos completamente inicializado!");
    }

    /**
     * Verifica que los componentes del .form existan
     */
    private void verificarComponentesDelForm() {
        System.out.println("🔍 Verificando componentes del .form...");

        // Verificaciones críticas
        if (tablaListaMedicos == null) {
            System.err.println("❌ CRÍTICO: tablaListaMedicos es NULL");
            System.err.println("   Verifique el field name en el .form");
        } else {
            System.out.println("✅ tablaListaMedicos inicializada");
        }

        if (botonGuardarMedico == null) {
            System.err.println("❌ CRÍTICO: botonGuardarMedico es NULL");
        } else {
            System.out.println("✅ botonGuardarMedico inicializado");
        }

        if (nombreFld == null) {
            System.err.println("❌ CRÍTICO: nombreFld es NULL");
        } else {
            System.out.println("✅ nombreFld inicializado");
        }

        if (especialidadFld == null) {
            System.err.println("❌ CRÍTICO: especialidadFld es NULL");
        } else {
            System.out.println("✅ especialidadFld inicializado");
        }

        if (scrollTablaMedicos == null) {
            System.err.println("❌ ADVERTENCIA: scrollTablaMedicos (JScrollPane) es NULL");
        } else {
            System.out.println("✅ scrollTablaMedicos (JScrollPane) inicializado");
        }

        // Contar componentes inicializados
        int componentesOK = 0;
        int componentesTotal = 5; // Los críticos

        if (tablaListaMedicos != null) componentesOK++;
        if (botonGuardarMedico != null) componentesOK++;
        if (nombreFld != null) componentesOK++;
        if (especialidadFld != null) componentesOK++;
        if (scrollTablaMedicos != null) componentesOK++;

        System.out.println("📊 Componentes inicializados: " + componentesOK + "/" + componentesTotal);

        if (componentesOK < componentesTotal) {
            System.err.println("⚠️ ADVERTENCIA: Algunos componentes no se inicializaron");
            System.err.println("   Revise los field names en el archivo .form");
        }
    }

    private void initMVC() {
        try {
            this.controlador = new ControladorCatalogos();
            this.modelo = new ModeloTablaUsuarios();
            System.out.println("✅ MVC inicializado");
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar MVC: " + e.getMessage());
        }
    }

    private void configurarTabla() {
        if (tablaListaMedicos == null || modelo == null) {
            System.err.println("❌ No se puede configurar tabla: componentes null");
            return;
        }

        try {
            tablaListaMedicos.setModel(modelo);
            tablaListaMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaListaMedicos.setRowHeight(25);
            tablaListaMedicos.getTableHeader().setReorderingAllowed(false);
            tablaListaMedicos.setFillsViewportHeight(true);

            // Configurar anchos de columnas
            if (tablaListaMedicos.getColumnModel().getColumnCount() >= 4) {
                tablaListaMedicos.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
                tablaListaMedicos.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
                tablaListaMedicos.getColumnModel().getColumn(2).setPreferredWidth(150); // Especialidad
                tablaListaMedicos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Estado
            }

            System.out.println("✅ Tabla configurada correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al configurar tabla: " + e.getMessage());
        }
    }

    private void configurarEventos() {
        System.out.println("🔧 Configurando eventos...");

        // Botón Guardar
        if (botonGuardarMedico != null) {
            botonGuardarMedico.addActionListener(e -> {
                System.out.println("👆 Click en Guardar");
                guardarMedico();
            });
        }

        // Botón Limpiar
        if (botonLimpiarCamposMedico != null) {
            botonLimpiarCamposMedico.addActionListener(e -> {
                System.out.println("👆 Click en Limpiar");
                limpiarFormulario();
            });
        }

        // Botón Borrar
        if (botonBorrarMedico != null) {
            botonBorrarMedico.addActionListener(e -> {
                System.out.println("👆 Click en Borrar");
                borrarMedico();
            });
        }

        // Botón Buscar
        if (botonBuscar != null) {
            botonBuscar.addActionListener(e -> {
                System.out.println("👆 Click en Buscar");
                buscarMedicos();
            });
        }

        // Botón Generar Reporte
        if (botonGenerarReporte != null) {
            botonGenerarReporte.addActionListener(e -> {
                System.out.println("👆 Click en Generar Reporte");
                generarReporte();
            });
        }

        // Eventos de tabla
        if (tablaListaMedicos != null) {
            tablaListaMedicos.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    seleccionarMedicoEnTabla();
                }
            });

            // Doble clic para editar
            tablaListaMedicos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        System.out.println("👆 Doble click en tabla");
                        activarModoEdicion();
                    }
                }
            });
        }

        // Búsqueda en tiempo real
        if (nombreBusquedaFld != null) {
            nombreBusquedaFld.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    buscarMedicos();
                }
            });
        }

        // Enter para guardar
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    guardarMedico();
                }
            }
        };

        if (idFld != null) idFld.addKeyListener(enterListener);
        if (nombreFld != null) nombreFld.addKeyListener(enterListener);
        if (especialidadFld != null) especialidadFld.addKeyListener(enterListener);

        System.out.println("✅ Eventos configurados");
    }

    private void configurarEstilos() {
        try {
            // Configurar colores de botones
            if (botonGuardarMedico != null) {
                botonGuardarMedico.setBackground(new Color(0, 153, 76));
                botonGuardarMedico.setForeground(Color.WHITE);
                botonGuardarMedico.setOpaque(true);
            }

            if (botonBorrarMedico != null) {
                botonBorrarMedico.setBackground(new Color(204, 51, 51));
                botonBorrarMedico.setForeground(Color.WHITE);
                botonBorrarMedico.setOpaque(true);
                botonBorrarMedico.setEnabled(false);
            }

            System.out.println("✅ Estilos configurados");

        } catch (Exception e) {
            System.err.println("❌ Error al configurar estilos: " + e.getMessage());
        }
    }

    // ============================================
    // MÉTODOS DE ACCIÓN
    // ============================================

    private void cargarDatos() {
        System.out.println("📊 Cargando datos...");

        try {
            Lista<Usuario> medicos = controlador.buscarMedicos();
            modelo.setUsuarios(medicos);
            actualizarEstadoBotones();
            System.out.println("✅ Datos cargados: " + medicos.getTam() + " médicos");

            // Verificar que la tabla se actualizó
            if (tablaListaMedicos != null) {
                System.out.println("📋 Filas en tabla: " + tablaListaMedicos.getRowCount());
            }

        } catch (Exception e) {
            System.err.println("❌ Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarMedico() {
        System.out.println("💾 Guardando médico...");

        if (!validarFormulario()) {
            return;
        }

        try {
            Medico medico = extraerDatosFormulario();
            boolean exito;

            if (modoEdicion) {
                exito = controlador.actualizarUsuario(medico);
                if (exito) {
                    mostrarMensaje("Médico actualizado exitosamente");
                }
            } else {
                exito = controlador.agregarMedico(medico);
                if (exito) {
                    mostrarMensaje("Médico agregado exitosamente");
                }
            }

            if (exito) {
                limpiarFormulario();
                cargarDatos();
            }

        } catch (CatalogoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void borrarMedico() {
        if (tablaListaMedicos == null) {
            mostrarError("Tabla no disponible");
            return;
        }

        int fila = tablaListaMedicos.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un médico para eliminar");
            return;
        }

        Usuario usuario = modelo.getUsuarioEnFila(fila);
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar al médico " + usuario.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = controlador.eliminarUsuario(usuario.getId());
                if (exito) {
                    mostrarMensaje("Médico eliminado exitosamente");
                    limpiarFormulario();
                    cargarDatos();
                }
            } catch (CatalogoException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private void buscarMedicos() {
        String filtro = "";
        if (nombreBusquedaFld != null) {
            filtro = nombreBusquedaFld.getText().trim();
        }

        Lista<Usuario> medicosFiltrados = controlador.filtrarMedicos(filtro);
        modelo.setUsuarios(medicosFiltrados);

        System.out.println("🔍 Búsqueda: '" + filtro + "' -> " + medicosFiltrados.getTam() + " resultados");
    }

    private void seleccionarMedicoEnTabla() {
        if (tablaListaMedicos == null) return;

        int fila = tablaListaMedicos.getSelectedRow();
        if (fila >= 0) {
            Usuario usuario = modelo.getUsuarioEnFila(fila);
            if (usuario instanceof Medico) {
                cargarMedicoEnFormulario((Medico) usuario);
                actualizarEstadoBotones();
            }
        }
    }

    private void activarModoEdicion() {
        modoEdicion = true;
        if (idFld != null) {
            idFld.setEnabled(false);
        }
        if (botonGuardarMedico != null) {
            botonGuardarMedico.setText("Actualizar");
        }
        mostrarMensaje("Modo edición activado");
    }

    private void generarReporte() {
        String reporte = controlador.obtenerResumenEstadisticas();

        JTextArea textArea = new JTextArea(reporte);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Reporte del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    // ============================================
    // MÉTODOS DE FORMULARIO
    // ============================================

    private boolean validarFormulario() {
        if (nombreFld == null || especialidadFld == null) {
            mostrarError("Formulario no está disponible");
            return false;
        }

        limpiarValidacionesVisuales();
        boolean valido = true;

        if (nombreFld.getText().trim().isEmpty()) {
            nombreFld.setBackground(Color.PINK);
            valido = false;
        }

        if (especialidadFld.getText().trim().isEmpty()) {
            especialidadFld.setBackground(Color.PINK);
            valido = false;
        }

        if (!modoEdicion && idFld != null) {
            String id = idFld.getText().trim();
            if (!id.isEmpty() && controlador.existeUsuario(id)) {
                idFld.setBackground(Color.PINK);
                valido = false;
            }
        }

        return valido;
    }

    private void limpiarValidacionesVisuales() {
        if (idFld != null) idFld.setBackground(null);
        if (nombreFld != null) nombreFld.setBackground(null);
        if (especialidadFld != null) especialidadFld.setBackground(null);
    }

    private Medico extraerDatosFormulario() {
        String id = idFld != null ? idFld.getText().trim() : "";
        String nombre = nombreFld != null ? nombreFld.getText().trim() : "";
        String especialidad = especialidadFld != null ? especialidadFld.getText().trim() : "";

        if (id.isEmpty()) {
            id = GeneradorIds.generarIdMedico();
        }

        return new Medico(id, nombre, id, especialidad);
    }

    private void cargarMedicoEnFormulario(Medico medico) {
        if (idFld != null) idFld.setText(medico.getId());
        if (nombreFld != null) nombreFld.setText(medico.getNombre());
        if (especialidadFld != null) especialidadFld.setText(medico.getEspecialidad());
        limpiarValidacionesVisuales();
    }

    private void limpiarFormulario() {
        if (idFld != null) {
            idFld.setText("");
            idFld.setEnabled(true);
        }
        if (nombreFld != null) nombreFld.setText("");
        if (especialidadFld != null) especialidadFld.setText("");

        modoEdicion = false;

        if (botonGuardarMedico != null) {
            botonGuardarMedico.setText("Guardar");
        }
        if (tablaListaMedicos != null) {
            tablaListaMedicos.clearSelection();
        }

        limpiarValidacionesVisuales();
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = tablaListaMedicos != null && tablaListaMedicos.getSelectedRow() >= 0;
        if (botonBorrarMedico != null) {
            botonBorrarMedico.setEnabled(haySeleccion);
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ============================================
    // MÉTODOS PÚBLICOS
    // ============================================

    public void refrescarDatos() {
        System.out.println("🔄 Refrescando datos del panel...");
        cargarDatos();
    }

    public int getNumeroMedicos() {
        return modelo != null ? modelo.getRowCount() : 0;
    }

    public boolean hayCambiosSinGuardar() {
        boolean hayTextoNombre = nombreFld != null && !nombreFld.getText().trim().isEmpty();
        boolean hayTextoEspecialidad = especialidadFld != null && !especialidadFld.getText().trim().isEmpty();
        return hayTextoNombre || hayTextoEspecialidad;
    }

    // ============================================
    // MÉTODO PLACEHOLDER PARA INTELLIJ
    // IntelliJ puede generar este método automáticamente
    // ============================================

    /**
     * Método generado por IntelliJ para inicializar componentes del .form
     * Si no existe, IntelliJ lo creará automáticamente al compilar
     */
    private void $$$setupUI$$$() {
        // Este método será generado automáticamente por IntelliJ
        // si no existe. No modificar manualmente.
    }
}