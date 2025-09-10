package presentacion.vista.administrador;

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
 * Panel Histórico para todos los usuarios - Vista MVC
 * Permite visualizar recetas a MÉDICOS, FARMACEUTAS y ADMINISTRADORES
 */
public class PanelHistorico {
    // Componentes del formulario (declarados en el .form)
    private JPanel panelPrincipal;
    private JTextField numFld;
    private JButton buscar;
    private JTable list;
    private JTextArea detalles;

    // MVC Components
    private ControladorPrincipal controlador;
    private TableModelPrincipal tableModel;
    private Receta recetaSeleccionada = null;

    public PanelHistorico(ControladorPrincipal controlador) {
        this.controlador = controlador;
        inicializarComponentes();
        configurarEventos();
        cargarTodasLasRecetas();
    }

    private void inicializarComponentes() {
        // Configurar tabla de recetas
        tableModel = TableModelPrincipal.crearModeloRecetas();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configurar columnas
        list.getColumnModel().getColumn(0).setPreferredWidth(100); // Número
        list.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Paciente
        list.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID Médico
        list.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha Confección
        list.getColumnModel().getColumn(4).setPreferredWidth(80);  // Estado

        // Configurar área de detalles
        detalles.setEditable(false);
        detalles.setWrapStyleWord(true);
        detalles.setLineWrap(true);
    }

    private void configurarEventos() {
        // Evento de selección en tabla
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarReceta();
                }
            }
        });

        // Evento del botón buscar
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });

        // Buscar al presionar Enter en el campo
        numFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });
    }

    private void cargarTodasLasRecetas() {
        try {
            // Verificar que el usuario tiene permisos
            Usuario usuarioActual = controlador.getModelo().getUsuarioActual();

            if (usuarioActual == null) {
                mostrarError("Debe autenticarse para acceder al histórico");
                return;
            }

            // Verificar permisos - MÉDICOS, FARMACEUTAS y ADMINISTRADORES pueden ver histórico
            TipoUsuario tipoUsuario = usuarioActual.getTipo();
            if (tipoUsuario != TipoUsuario.MEDICO &&
                    tipoUsuario != TipoUsuario.FARMACEUTA &&
                    tipoUsuario != TipoUsuario.ADMINISTRADOR) {
                mostrarError("No tiene permisos para acceder al histórico de recetas");
                return;
            }

            // Obtener todas las recetas según el tipo de usuario
            Lista<Receta> recetas = obtenerRecetasSegunPermisos(usuarioActual);

            if (recetas == null || recetas.getTam() == 0) {
                // No mostrar error si no hay recetas, solo limpiar la tabla
                Lista<Object> datosVacios = new Lista<>();
                tableModel.setDatos(datosVacios);
                detalles.setText("No hay recetas para mostrar.");
                return;
            }

            // Convertir a lista de Object para el modelo de tabla
            Lista<Object> datosRecetas = new Lista<>();
            for (int i = 0; i < recetas.getTam(); i++) {
                datosRecetas.agregarFinal(recetas.obtenerPorPos(i));
            }

            tableModel.setDatos(datosRecetas);
            detalles.setText("Total de recetas: " + recetas.getTam());

        } catch (Exception e) {
            mostrarError("Error al cargar recetas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Lista<Receta> obtenerRecetasSegunPermisos(Usuario usuario) {
        try {
            switch (usuario.getTipo()) {
                case ADMINISTRADOR:
                case FARMACEUTA:
                    // Administradores y farmaceutas pueden ver TODAS las recetas
                    return controlador.getModelo().obtenerTodasLasRecetas();

                case MEDICO:
                    // Médicos solo pueden ver sus propias recetas
                    return controlador.getModelo().obtenerRecetasPorMedico(usuario.getId());

                default:
                    throw new SecurityException("Tipo de usuario no autorizado para acceder al histórico");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener recetas según permisos: " + e.getMessage());
            return new Lista<>(); // Retornar lista vacía en caso de error
        }
    }

    private void seleccionarReceta() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
                // Obtener la receta seleccionada del modelo
                recetaSeleccionada = (Receta) tableModel.getDatos().obtenerPorPos(filaSeleccionada);
                mostrarDetallesReceta(recetaSeleccionada);
            } catch (Exception e) {
                mostrarError("Error al seleccionar receta: " + e.getMessage());
            }
        } else {
            recetaSeleccionada = null;
            detalles.setText("");
        }
    }

    private void mostrarDetallesReceta(Receta receta) {
        if (receta == null) {
            detalles.setText("");
            return;
        }

        StringBuilder detallesTexto = new StringBuilder();
        detallesTexto.append("=== DETALLES DE LA RECETA ===\n\n");
        detallesTexto.append("Número: ").append(receta.getNumeroReceta()).append("\n");
        detallesTexto.append("Paciente: ").append(receta.getIdPaciente()).append("\n");
        detallesTexto.append("Médico: ").append(receta.getIdMedico()).append("\n");
        detallesTexto.append("Fecha de Confección: ").append(receta.getFechaConfeccion()).append("\n");
        detallesTexto.append("Fecha de Retiro: ").append(receta.getFechaRetiro()).append("\n");
        detallesTexto.append("Estado: ").append(receta.getEstado()).append("\n\n");

        // Mostrar medicamentos de la receta
        if (receta.getDetalles() != null && receta.getDetalles().getTam() > 0) {
            detallesTexto.append("=== MEDICAMENTOS PRESCRITOS ===\n\n");

            for (int i = 0; i < receta.getDetalles().getTam(); i++) {
                DetalleReceta detalle = receta.getDetalles().obtenerPorPos(i);
                detallesTexto.append("• Medicamento: ").append(detalle.getCodigoMedicamento()).append("\n");
                detallesTexto.append("  Cantidad: ").append(detalle.getCantidad()).append("\n");
                detallesTexto.append("  Duración: ").append(detalle.getDuracionTexto()).append(" días\n");
                detallesTexto.append("  Indicaciones: ").append(detalle.getIndicaciones()).append("\n\n");
            }
        } else {
            detallesTexto.append("No hay medicamentos registrados en esta receta.\n");
        }

        detalles.setText(detallesTexto.toString());
        detalles.setCaretPosition(0); // Scroll al inicio
    }

    private void buscarRecetas() {
        String numeroReceta = numFld.getText().trim();

        if (numeroReceta.isEmpty()) {
            // Si el campo está vacío, mostrar todas las recetas
            cargarTodasLasRecetas();
            return;
        }

        try {
            Usuario usuarioActual = controlador.getModelo().getUsuarioActual();
            if (usuarioActual == null) {
                mostrarError("Debe autenticarse para buscar recetas");
                return;
            }

            // Buscar receta específica
            Lista<Receta> todasLasRecetas = obtenerRecetasSegunPermisos(usuarioActual);
            Lista<Object> recetasFiltradas = new Lista<>();

            for (int i = 0; i < todasLasRecetas.getTam(); i++) {
                Receta receta = todasLasRecetas.obtenerPorPos(i);
                if (receta.getNumeroReceta().toLowerCase().contains(numeroReceta.toLowerCase())) {
                    recetasFiltradas.agregarFinal(receta);
                }
            }

            tableModel.setDatos(recetasFiltradas);

            if (recetasFiltradas.getTam() == 0) {
                detalles.setText("No se encontraron recetas con el criterio: " + numeroReceta);
            } else {
                detalles.setText("Se encontraron " + recetasFiltradas.getTam() + " receta(s)");
            }

        } catch (Exception e) {
            mostrarError("Error al buscar recetas: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panelPrincipal, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ================================
    // MÉTODOS REQUERIDOS PARA LA INTEGRACIÓN
    // ================================

    /**
     * Retorna el panel principal para ser integrado en VentanaPrincipal
     */
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    /**
     * Refresca los datos del panel cuando se selecciona la pestaña
     */
    public void refrescarDatos() {
        // Limpiar selección actual
        list.clearSelection();
        recetaSeleccionada = null;
        numFld.setText("");
        detalles.setText("");

        // Recargar todas las recetas
        cargarTodasLasRecetas();
    }

    // Método para limpiar campos (útil para el controlador)
    public void limpiarCampos() {
        numFld.setText("");
        detalles.setText("");
        list.clearSelection();
        recetaSeleccionada = null;
    }

    // Método para obtener la receta seleccionada (útil para el controlador)
    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }
}
