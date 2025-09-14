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

public class PanelHistorico {
    private JPanel panelPrincipal;
    private JTextField numFld;
    private JButton buscar;
    private JTable list;
    private JTextArea detalles;

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
        tableModel = TableModelPrincipal.crearModeloRecetas();
        list.setModel(tableModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.getColumnModel().getColumn(0).setPreferredWidth(100); // Número
        list.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Paciente
        list.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID Médico
        list.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha Confección
        list.getColumnModel().getColumn(4).setPreferredWidth(80);  // Estado

        detalles.setEditable(false);
        detalles.setWrapStyleWord(true);
        detalles.setLineWrap(true);
    }

    private void configurarEventos() {
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    seleccionarReceta();
                }
            }
        });

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });

        numFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });
    }

    private void cargarTodasLasRecetas() {
        try {
            Usuario usuarioActual = controlador.getModelo().getUsuarioActual();

            if (usuarioActual == null) {
                mostrarError("Debe autenticarse para acceder al histórico");
                return;
            }

            TipoUsuario tipoUsuario = usuarioActual.getTipo();
            if (tipoUsuario != TipoUsuario.MEDICO &&
                    tipoUsuario != TipoUsuario.FARMACEUTA &&
                    tipoUsuario != TipoUsuario.ADMINISTRADOR) {
                mostrarError("No tiene permisos para acceder al histórico de recetas");
                return;
            }

            Lista<Receta> recetas = obtenerRecetasSegunPermisos(usuarioActual);

            if (recetas == null || recetas.getTam() == 0) {
                Lista<Object> datosVacios = new Lista<>();
                tableModel.setDatos(datosVacios);
                detalles.setText("No hay recetas para mostrar.");
                return;
            }

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
                    return controlador.getModelo().obtenerTodasLasRecetas();

                case MEDICO:
                    return controlador.getModelo().obtenerRecetasPorMedico(usuario.getId());

                default:
                    throw new SecurityException("Tipo de usuario no autorizado para acceder al histórico");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener recetas según permisos: " + e.getMessage());
            return new Lista<>();
        }
    }

    private void seleccionarReceta() {
        int filaSeleccionada = list.getSelectedRow();
        if (filaSeleccionada >= 0) {
            try {
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
        detalles.setCaretPosition(0);
    }

    private void buscarRecetas() {
        String numeroReceta = numFld.getText().trim();

        if (numeroReceta.isEmpty()) {
            cargarTodasLasRecetas();
            return;
        }

        try {
            Usuario usuarioActual = controlador.getModelo().getUsuarioActual();
            if (usuarioActual == null) {
                mostrarError("Debe autenticarse para buscar recetas");
                return;
            }

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

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
    public void refrescarDatos() {
        list.clearSelection();
        recetaSeleccionada = null;
        numFld.setText("");
        detalles.setText("");

        cargarTodasLasRecetas();
    }
    public void limpiarCampos() {
        numFld.setText("");
        detalles.setText("");
        list.clearSelection();
        recetaSeleccionada = null;
    }
    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }
}
