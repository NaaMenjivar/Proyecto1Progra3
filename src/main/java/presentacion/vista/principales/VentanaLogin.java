package presentacion.vista.principales;

import javax.swing.*;

import presentacion.controlador.ControladorLogin;
import modelo.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin extends JDialog {
    // Componentes de la interfaz
    private JTextField campoId;
    private JPasswordField campoClave;
    private JComboBox<TipoUsuario> comboTipoUsuario;
    private JButton botonIngresar;
    private JButton botonCancelar;
    private JButton botonCambiarClave;
    private JRadioButton botonMostrarClave;
    private JLabel labelTitulo;
    private JLabel labelId;
    private JLabel labelClave;
    private JLabel labelTipo;
    private JPanel panelPrincipal;

    // Controlador
    private ControladorLogin controlador;

    // Variable para el resultado
    private boolean loginExitoso = false;

    public VentanaLogin(Frame parent) {
        super(parent, "Ingreso al Sistema Hospital", true);
        initComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
    }

    private void initComponents() {
        // Inicializar componentes
        labelTitulo = new JLabel("SISTEMA HOSPITAL", SwingConstants.CENTER);
        labelId = new JLabel("ID Usuario:");
        labelClave = new JLabel("Clave:");
        labelTipo = new JLabel("Tipo Usuario:");

        campoId = new JTextField(15);
        campoClave = new JPasswordField(15);
        comboTipoUsuario = new JComboBox<>(TipoUsuario.values());

        botonIngresar = new JButton("Ingresar");
        botonCancelar = new JButton("Cancelar");
        botonCambiarClave = new JButton("Cambiar Clave");
        botonMostrarClave = new JRadioButton();

        panelPrincipal = new JPanel();

        // Configurar estilos
        configurarEstilos();

        // Configurar iconos si están disponibles
        configurarIconos();
    }

    private void configurarEstilos() {
        // Título
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setForeground(new Color(0, 102, 153));

        // Labels
        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        labelId.setFont(labelFont);
        labelClave.setFont(labelFont);
        labelTipo.setFont(labelFont);

        // Campos de texto
        campoId.setFont(new Font("Arial", Font.PLAIN, 12));
        campoClave.setFont(new Font("Arial", Font.PLAIN, 12));

        // Botones
        Font buttonFont = new Font("Arial", Font.PLAIN, 11);
        botonIngresar.setFont(buttonFont);
        botonCancelar.setFont(buttonFont);
        botonCambiarClave.setFont(buttonFont);

        // Colores de botones
        botonIngresar.setBackground(new Color(0, 153, 76));
        botonIngresar.setForeground(Color.WHITE);
        botonCancelar.setBackground(new Color(204, 51, 51));
        botonCancelar.setForeground(Color.WHITE);
        botonCambiarClave.setBackground(new Color(255, 153, 51));

        // Radio button para mostrar clave
        configurarBotonMostrarClave();
    }

    private void configurarBotonMostrarClave() {
        try {
            // Intentar cargar iconos personalizados
            ImageIcon ojoCerrado = new ImageIcon(getClass().getResource("/imagenes/iconos/ojo_cerrado.png"));
            ImageIcon ojoAbierto = new ImageIcon(getClass().getResource("/imagenes/iconos/ojo_abierto.png"));

            if (ojoCerrado.getIconWidth() > 0) {
                Image ojoCerradoEscalado = ojoCerrado.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                Image ojoAbiertoEscalado = ojoAbierto.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

                botonMostrarClave.setIcon(new ImageIcon(ojoCerradoEscalado));
                botonMostrarClave.setSelectedIcon(new ImageIcon(ojoAbiertoEscalado));
            } else {
                // Fallback a texto si no hay iconos
                botonMostrarClave.setText("👁");
            }
        } catch (Exception e) {
            // Fallback a texto si no se pueden cargar los iconos
            botonMostrarClave.setText("👁");
            botonMostrarClave.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        }

        botonMostrarClave.setToolTipText("Mostrar/Ocultar clave");
        botonMostrarClave.setContentAreaFilled(false);
        botonMostrarClave.setBorderPainted(false);
        botonMostrarClave.setFocusPainted(false);
    }

    private void configurarIconos() {
        try {
            // Configurar iconos de botones
            ImageIcon iconoIngresar = new ImageIcon(getClass().getResource("/Iconos/login.jpg"));
            ImageIcon iconoCancelar = new ImageIcon(getClass().getResource("/Iconos/exit.jpg"));

            if (iconoIngresar.getIconWidth() > 0) {
                Image imgIngresar = iconoIngresar.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                botonIngresar.setIcon(new ImageIcon(imgIngresar));
            }

            if (iconoCancelar.getIconWidth() > 0) {
                Image imgCancelar = iconoCancelar.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                botonCancelar.setIcon(new ImageIcon(imgCancelar));
            }

        } catch (Exception e) {
            // Si no se pueden cargar los iconos, continuar sin ellos
            System.err.println("No se pudieron cargar los iconos: " + e.getMessage());
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Panel principal con GridBagLayout para mejor control
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(labelTitulo, gbc);

        // Espacio
        gbc.gridy = 1;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);

        // ID Usuario
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelPrincipal.add(labelId, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(campoId, gbc);

        // Clave
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(labelClave, gbc);

        gbc.gridx = 1; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(campoClave, gbc);

        gbc.gridx = 2; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(botonMostrarClave, gbc);

        // Tipo de Usuario
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelPrincipal.add(labelTipo, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(comboTipoUsuario, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonIngresar);
        panelBotones.add(botonCancelar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(panelBotones, gbc);

        // Botón cambiar clave (separado)
        JPanel panelCambiarClave = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCambiarClave.add(botonCambiarClave);

        gbc.gridy = 6;
        panelPrincipal.add(panelCambiarClave, gbc);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {

        // Eventos de botones
        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarLogin();
            }
        });

        botonCambiarClave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaCambiarClave();
            }
        });

        // Evento para mostrar/ocultar clave
        botonMostrarClave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campoClave.setEchoChar(botonMostrarClave.isSelected() ? (char) 0 : '*');
            }
        });

        // Enter para hacer login
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarLogin();
                }
            }
        };

        campoId.addKeyListener(enterKeyListener);
        campoClave.addKeyListener(enterKeyListener);
        comboTipoUsuario.addKeyListener(enterKeyListener);

        // Focus inicial
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                campoId.requestFocusInWindow();
            }
        });
    }

    private void setupWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());

        // Tamaño mínimo
        setMinimumSize(new Dimension(350, 300));
    }

    // Métodos de acción
    private void intentarLogin() {
        String id = campoId.getText().trim();
        String clave = new String(campoClave.getPassword());
        TipoUsuario tipo = (TipoUsuario) comboTipoUsuario.getSelectedItem();

        // Validaciones básicas
        if (id.isEmpty()) {
            mostrarError("Debe ingresar el ID de usuario");
            campoId.requestFocusInWindow();
            return;
        }

        if (clave.isEmpty()) {
            mostrarError("Debe ingresar la clave");
            campoClave.requestFocusInWindow();
            return;
        }

        if (controlador != null) {
            controlador.procesarLogin(id, clave, tipo);
        } else {
            mostrarError("Controlador Invalido o NULL");
        }
    }

    private void simularLogin(String id, String clave, TipoUsuario tipo) {
        // Simulación básica - en la implementación real esto sería manejado por el controlador
        if ("admin".equals(id) && "admin".equals(clave)) {
            loginExitoso = true;
            mostrarMensaje("Login exitoso como " + tipo.getDescripcion());
            limpiarCampos();
            dispose();
        } else {
            mostrarError("Credenciales inválidas");
            campoClave.setText("");
            campoId.requestFocusInWindow();
        }
    }

    private void cancelarLogin() {
        limpiarCampos();
        loginExitoso = false;
        dispose();
    }

    private void mostrarVentanaCambiarClave() {
        this.controlador.cambiarClave();
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoClave.setText("");
        comboTipoUsuario.setSelectedIndex(0);
        botonMostrarClave.setSelected(false);
        campoClave.setEchoChar('*');
    }

    // Métodos de utilidad para mensajes
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // Getters para el controlador
    public String getId() {
        return campoId.getText().trim();
    }

    public String getClave() {
        return new String(campoClave.getPassword());
    }

    public TipoUsuario getTipoUsuario() {
        return (TipoUsuario) comboTipoUsuario.getSelectedItem();
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }

    public void setControlador(ControladorLogin controlador) {
        this.controlador = controlador;
    }

    // Método para mostrar errores desde el controlador
    public void mostrarErrorLogin(String mensaje) {
        mostrarError(mensaje);
        campoClave.setText("");
        campoId.requestFocusInWindow();
    }

    public void mostrarLoginExitoso(String mensaje) {
        loginExitoso = true;
        mostrarMensaje(mensaje);
        limpiarCampos();
        dispose();
    }


}