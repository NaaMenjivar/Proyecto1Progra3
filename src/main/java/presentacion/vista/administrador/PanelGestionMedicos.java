package presentacion.vista.administrador;

import presentacion.controlador.ControladorCatalogos;
import presentacion.modelo.ModeloTablaUsuarios;
import modelo.*;
import utilidades.GeneradorIds;
import logica.excepciones.CatalogoException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * VIEW - Panel de gestión de médicos siguiendo patrón MVC tradicional
 * ✅ USA LOS COMPONENTES EXACTOS DEL ARCHIVO .form
 * ✅ Implementa PropertyChangeListener para recibir notificaciones del modelo
 */
public class PanelGestionMedicos extends JPanel implements PropertyChangeListener {

    // ============================================
    // ✅ COMPONENTES EXACTOS DEL ARCHIVO .form
    // ============================================
    private JPanel Medico;
    private JPanel Busqueda;
    private JPanel Listado;
    private JTextField idFld;
    private JTextField nombreFld;
    private JTextField especialidadFld;
    private JPanel panelBotones1;
    private JButton botonGuardarMedico;
    private JButton botonLimpiarCamposMedico;
    private JButton botonBorrarMedico;
    private JLabel idLabel;
    private JLabel tituloMedico;
    private JLabel especialidadLabel;
    private JLabel nombreLabel;
    private JLabel nombreBusquedaLabel;
    private JTextField nombreBusquedaFld;
    private JPanel panelBotones2;
    private JButton botonBuscar;
    private JButton botonGenerarReporte;
    private JTable tablaListaMedicos;

    // ============================================
    // COMPONENTES MVC
    // ============================================
    private ControladorCatalogos controlador;     // CONTROLLER
    private ModeloTablaUsuarios modelo;           // MODEL

    // ============================================
    // CONSTRUCTOR
    // ============================================
    public PanelGestionMedicos() {
        initMVC();
        configurarTabla();
        configurarEventos();
        configurarValidaciones();
    }

    // ============================================
    // INICIALIZACIÓN MVC
    // ============================================
    private void initMVC() {
        // Crear e inicializar componentes MVC
        this.controlador = new ControladorCatalogos();
        this.modelo = new ModeloTablaUsuarios();

        // Establecer relaciones MVC
        this.controlador.setModelo(this.modelo);
        this.modelo.addPropertyChangeListener(this);

        System.out.println("✅ PanelGestionMedicos: MVC inicializado correctamente");
    }

    // ============================================
    // CONFIGURACIÓN DE TABLA (USA COMPONENTES DEL .form)
    // ============================================
    private void configurarTabla() {
        // ✅ VERIFICAR que los componentes del .form existen
        if (tablaListaMedicos != null && modelo != null) {
            // Conectar el modelo con la tabla del .form
            tablaListaMedicos.setModel(modelo);

            // Configuraciones visuales
            tablaListaMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaListaMedicos.setRowHeight(25);
            tablaListaMedicos.getTableHeader().setReorderingAllowed(false);
            tablaListaMedicos.setFillsViewportHeight(true);

            // Configurar anchos de columnas
            javax.swing.table.TableColumnModel columnModel = tablaListaMedicos.getColumnModel();
            if (columnModel.getColumnCount() >= 4) {
                columnModel.getColumn(0).setPreferredWidth(100); // ID
                columnModel.getColumn(1).setPreferredWidth(200); // Nombre
                columnModel.getColumn(2).setPreferredWidth(150); // Especialidad
                columnModel.getColumn(3).setPreferredWidth(80);  // Estado
            }

            System.out.println("✅ Tabla configurada con modelo MVC");
        } else {
            System.err.println("❌ ERROR: tablaListaMedicos o modelo es null");
        }
    }

    // ============================================
    // CONFIGURACIÓN DE EVENTOS (USA COMPONENTES DEL .form)
    // ============================================
    private void configurarEventos() {

        // ============= EVENTOS DE BOTONES DEL .form =============
        if (botonGuardarMedico != null) {
            botonGuardarMedico.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (validar()) { // ✅ CORREGIDO: Usar nuevo nombre del método
                        Medico medico = take();
                        try {
                            if (modelo.isModoEdicion()) {
                                controlador.update(medico);
                                mostrarMensajeExito("MÉDICO ACTUALIZADO");
                            } else {
                                controlador.create(medico);
                                mostrarMensajeExito("MÉDICO REGISTRADO");
                            }
                        } catch (Exception ex) {
                            mostrarMensajeError(ex.getMessage());
                        }
                    }
                }
            });
        }

        if (botonLimpiarCamposMedico != null) {
            botonLimpiarCamposMedico.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controlador.clear();
                }
            });
        }

        if (botonBorrarMedico != null) {
            botonBorrarMedico.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    borrarMedicoSeleccionado();
                }
            });
        }

        if (botonBuscar != null) {
            botonBuscar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    aplicarFiltro();
                }
            });
        }

        if (botonGenerarReporte != null) {
            botonGenerarReporte.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generarReporte();
                }
            });
        }

        // ============= EVENTOS DE TABLA DEL .form =============
        if (tablaListaMedicos != null) {
            tablaListaMedicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        int filaSeleccionada = tablaListaMedicos.getSelectedRow();
                        if (filaSeleccionada >= 0) {
                            controlador.seleccionarMedico(filaSeleccionada);
                        }
                    }
                }
            });

            // Doble clic para editar
            tablaListaMedicos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int filaSeleccionada = tablaListaMedicos.getSelectedRow();
                        if (filaSeleccionada >= 0) {
                            controlador.seleccionarMedico(filaSeleccionada);
                            modelo.setModoEdicion(true);
                        }
                    }
                }
            });
        }

        // ============= BÚSQUEDA EN TIEMPO REAL =============
        if (nombreBusquedaFld != null) {
            nombreBusquedaFld.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String filtro = nombreBusquedaFld.getText().trim();
                    controlador.filtrar(filtro);
                }
            });
        }

        // ============= ENTER PARA GUARDAR =============
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (botonGuardarMedico != null) {
                        botonGuardarMedico.doClick();
                    }
                }
            }
        };

        if (nombreFld != null) {
            nombreFld.addKeyListener(enterKeyListener);
        }
        if (especialidadFld != null) {
            especialidadFld.addKeyListener(enterKeyListener);
        }
    }

    // ============================================
    // CONFIGURACIONES ADICIONALES
    // ============================================
    private void configurarValidaciones() {
        // Estado inicial de botones
        if (botonBorrarMedico != null) {
            botonBorrarMedico.setEnabled(false);
        }

        // Colores de botones (si se desea)
        if (botonGuardarMedico != null) {
            botonGuardarMedico.setBackground(new Color(0, 153, 76));
            botonGuardarMedico.setForeground(Color.WHITE);
        }

        if (botonBorrarMedico != null) {
            botonBorrarMedico.setBackground(new Color(204, 51, 51));
            botonBorrarMedico.setForeground(Color.WHITE);
        }
    }

    // ============================================
    // IMPLEMENTACIÓN DE PropertyChangeListener
    // ============================================
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ModeloTablaUsuarios.LIST:
                // La tabla se actualiza automáticamente por AbstractTableModel
                System.out.println("✅ Lista actualizada: " + modelo.getRowCount() + " médicos");
                break;

            case ModeloTablaUsuarios.CURRENT:
                // Actualizar formulario con médico actual
                Medico medico = modelo.getMedicoActual();
                if (medico != null) {
                    // ✅ USAR COMPONENTES DEL .form
                    if (idFld != null) {
                        idFld.setText(medico.getId() != null ? medico.getId() : "");
                    }
                    if (nombreFld != null) {
                        nombreFld.setText(medico.getNombre() != null ? medico.getNombre() : "");
                    }
                    if (especialidadFld != null) {
                        especialidadFld.setText(medico.getEspecialidad() != null ? medico.getEspecialidad() : "");
                    }

                    // Configurar modo edición
                    if (modelo.isModoEdicion()) {
                        if (idFld != null) idFld.setEditable(false);
                        if (botonGuardarMedico != null) botonGuardarMedico.setText("Actualizar");
                    } else {
                        if (idFld != null) idFld.setEditable(true);
                        if (botonGuardarMedico != null) botonGuardarMedico.setText("Guardar");
                    }

                    // Limpiar validaciones visuales
                    limpiarValidacionesVisuales();
                }

                // Actualizar estado de botones
                actualizarEstadoBotones();
                break;

            case ModeloTablaUsuarios.FILTER:
                String filtro = modelo.getFiltroActual();
                if (nombreBusquedaFld != null && !nombreBusquedaFld.getText().equals(filtro)) {
                    nombreBusquedaFld.setText(filtro);
                }
                System.out.println("✅ Filtro aplicado: '" + filtro + "' - " + modelo.getRowCount() + " resultados");
                break;
        }

        // Revalidar panel
        this.revalidate();
        this.repaint();
    }

    // ============================================
    // MÉTODOS DE ACCIÓN
    // ============================================

    /**
     * Toma los datos del formulario y crea un objeto Medico
     */
    private Medico take() {
        Medico medico = new Medico();

        // ✅ USAR COMPONENTES DEL .form
        String id = idFld != null ? idFld.getText().trim() : "";
        if (id.isEmpty()) {
            id = GeneradorIds.generarIdMedico();
        }

        medico.setId(id);
        medico.setNombre(nombreFld != null ? nombreFld.getText().trim() : "");
        medico.setEspecialidad(especialidadFld != null ? especialidadFld.getText().trim() : "");
        medico.setClave(id); // Clave igual al ID por defecto

        return medico;
    }

    /**
     * Valida los datos del formulario
     */
    private boolean validar() {
        boolean valid = true;

        // Limpiar validaciones anteriores
        limpiarValidacionesVisuales();

        // ✅ VALIDAR USANDO COMPONENTES DEL .form
        if (nombreFld != null && nombreFld.getText().trim().isEmpty()) {
            valid = false;
            nombreFld.setBackground(Color.PINK);
            nombreFld.setToolTipText("Nombre requerido");
        }

        if (especialidadFld != null && especialidadFld.getText().trim().isEmpty()) {
            valid = false;
            especialidadFld.setBackground(Color.PINK);
            especialidadFld.setToolTipText("Especialidad requerida");
        }

        return valid;
    }

    /**
     * Limpia las validaciones visuales
     */
    private void limpiarValidacionesVisuales() {
        if (idFld != null) {
            idFld.setBackground(null);
            idFld.setToolTipText(null);
        }
        if (nombreFld != null) {
            nombreFld.setBackground(null);
            nombreFld.setToolTipText(null);
        }
        if (especialidadFld != null) {
            especialidadFld.setBackground(null);
            especialidadFld.setToolTipText(null);
        }
    }

    /**
     * Actualiza el estado de los botones según el contexto
     */
    private void actualizarEstadoBotones() {
        Medico medicoActual = modelo.getMedicoActual();
        boolean hayMedicoSeleccionado = medicoActual != null &&
                medicoActual.getId() != null &&
                !medicoActual.getId().isEmpty();

        if (botonBorrarMedico != null) {
            botonBorrarMedico.setEnabled(hayMedicoSeleccionado);
        }

        if (botonGuardarMedico != null) {
            if (modelo.isModoEdicion()) {
                botonGuardarMedico.setText("Actualizar");
            } else {
                botonGuardarMedico.setText("Guardar");
            }
        }
    }

    /**
     * Borra el médico seleccionado
     */
    private void borrarMedicoSeleccionado() {
        Medico medicoActual = modelo.getMedicoActual();
        if (medicoActual == null || medicoActual.getId() == null || medicoActual.getId().isEmpty()) {
            mostrarMensajeError("Seleccione un médico para borrar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar al médico " + medicoActual.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.delete(medicoActual.getId());
                mostrarMensajeExito("MÉDICO ELIMINADO");
            } catch (Exception ex) {
                mostrarMensajeError(ex.getMessage());
            }
        }
    }

    /**
     * Aplica filtro de búsqueda
     */
    private void aplicarFiltro() {
        if (nombreBusquedaFld != null) {
            String filtro = nombreBusquedaFld.getText().trim();
            controlador.filtrar(filtro);
        }
    }

    /**
     * Genera reporte de estadísticas
     */
    private void generarReporte() {
        try {
            String resumen = controlador.obtenerResumenEstadisticas();

            JTextArea areaTexto = new JTextArea(15, 50);
            areaTexto.setText(resumen);
            areaTexto.setEditable(false);
            areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scroll = new JScrollPane(areaTexto);

            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "Reporte del Sistema Hospital",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            mostrarMensajeError("Error al generar reporte: " + e.getMessage());
        }
    }

    // ============================================
    // MÉTODOS DE UTILIDAD
    // ============================================

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ============================================
    // MÉTODOS PÚBLICOS PARA INTEGRACIÓN
    // ============================================

    /**
     * Establece el controlador (para integración externa)
     */
    public void setControlador(ControladorCatalogos controlador) {
        this.controlador = controlador;
        if (this.modelo != null) {
            this.controlador.setModelo(this.modelo);
        }
    }

    /**
     * Establece el modelo (para integración externa)
     */
    public void setModelo(ModeloTablaUsuarios modelo) {
        if (this.modelo != null) {
            this.modelo.removePropertyChangeListener(this);
        }

        this.modelo = modelo;
        this.modelo.addPropertyChangeListener(this);

        if (this.controlador != null) {
            this.controlador.setModelo(this.modelo);
        }

        // ✅ SOLO configurar la tabla - el JScrollPane lo maneja el .form automáticamente
        if (tablaListaMedicos != null) {
            tablaListaMedicos.setModel(this.modelo);
        }
    }

    /**
     * Refresca los datos desde el exterior
     */
    public void refrescarDatos() {
        if (controlador != null) {
            controlador.cargarTodosMedicos();
        }
    }

    /**
     * Obtiene el número de médicos mostrados
     */
    public int getNumeroMedicos() {
        return modelo != null ? modelo.getRowCount() : 0;
    }

    /**
     * Verifica si hay cambios sin guardar
     */
    public boolean hayCambiosSinGuardar() {
        boolean hayTextoNombre = nombreFld != null && !nombreFld.getText().trim().isEmpty();
        boolean hayTextoEspecialidad = especialidadFld != null && !especialidadFld.getText().trim().isEmpty();
        return hayTextoNombre || hayTextoEspecialidad;
    }
}