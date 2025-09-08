package presentacion.vista.sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaCambiarClave extends JDialog {
    private JPasswordField campoClaveActual;
    private JPasswordField campoClaveNueva;
    private JPasswordField campoConfirmarClave;
    private JButton botonAceptar;
    private JButton botonCancelar;
    private JRadioButton botonMostrarClave;

    public VentanaCambiarClave(Frame parent) {
        super(parent, "Cambiar Clave", true);
        initComponents();
        setupLayout();
        setupEventHandlers();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        campoClaveActual = new JPasswordField(15);
        campoClaveNueva = new JPasswordField(15);
        campoConfirmarClave = new JPasswordField(15);

        botonAceptar = new JButton("Aceptar");
        botonCancelar = new JButton("Cancelar");

        botonMostrarClave = new JRadioButton();
    }

    private void configurarEstilos() {

        campoClaveActual.setFont(new Font("Arial", Font.PLAIN, 12));
        campoClaveNueva.setFont(new Font("Arial", Font.PLAIN, 12));
        campoConfirmarClave.setFont(new Font("Arial", Font.PLAIN, 12));

        // Botones
        Font buttonFont = new Font("Arial", Font.PLAIN, 11);
        botonCancelar.setFont(buttonFont);
        botonMostrarClave.setFont(buttonFont);

        // Colores de botones
        botonAceptar.setBackground(new Color(0, 153, 76));
        botonAceptar.setForeground(Color.WHITE);
        botonCancelar.setBackground(new Color(204, 51, 51));
        botonCancelar.setForeground(Color.WHITE);

        // Radio button para mostrar clave
        configurarBotonMostrarClave();
    }

    private void configurarBotonMostrarClave() {
        try {
            // Intentar cargar iconos personalizados
            ImageIcon ojoCerrado = new ImageIcon(getClass().getResource("/Iconos/ojo_cerrado.png"));
            ImageIcon ojoAbierto = new ImageIcon(getClass().getResource("/Iconos/ojo_abierto.png"));

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

    private JRadioButton crearBotonMostrarPersistente(JPasswordField campo) {
        JRadioButton boton = new JRadioButton();

        // Guardamos el echoChar original
        char echoChar = '*'; // <-- asterisco en lugar de puntos
        campo.setEchoChar(echoChar);

        try {
            ImageIcon ojoCerrado = new ImageIcon(getClass().getResource("/Iconos/ojo_cerrado.png"));
            ImageIcon ojoAbierto = new ImageIcon(getClass().getResource("/Iconos/ojo_abierto.png"));

            Image imgCerrado = ojoCerrado.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            Image imgAbierto = ojoAbierto.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

            boton.setIcon(new ImageIcon(imgCerrado));
            boton.setSelectedIcon(new ImageIcon(imgAbierto));

        } catch (Exception e) {
            boton.setText("👁");
        }

        // Sin fondo, borde ni foco
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setToolTipText("Mostrar / ocultar contraseña");

        // Cambiar visibilidad al seleccionar/desmarcar
        boton.addActionListener(e -> {
            if (boton.isSelected()) {
                campo.setEchoChar((char) 0); // mostrar texto plano
            } else {
                campo.setEchoChar(echoChar); // volver a ocultar con asteriscos
            }
        });

        return boton;
    }

    private void setupLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Clave actual
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Clave actual:"), gbc);
        gbc.gridx = 1;
        panel.add(campoClaveActual, gbc);
        gbc.gridx = 2;
        panel.add(crearBotonMostrarPersistente(campoClaveActual), gbc);

        // Nueva clave
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nueva clave:"), gbc);
        gbc.gridx = 1;
        panel.add(campoClaveNueva, gbc);
        gbc.gridx = 2;
        panel.add(crearBotonMostrarPersistente(campoClaveNueva), gbc);

        // Confirmar nueva clave
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirmar nueva clave:"), gbc);
        gbc.gridx = 1;
        panel.add(campoConfirmarClave, gbc);
        gbc.gridx = 2;
        panel.add(crearBotonMostrarPersistente(campoConfirmarClave), gbc);

        // Panel inferior con botones aceptar/cancelar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private JRadioButton crearBotonMostrarConIcono(JPasswordField campo) {
        JRadioButton boton = new JRadioButton();

        // Guardamos el echoChar original
        char echoChar = campo.getEchoChar();

        try {
            // Cargar iconos desde resources
            ImageIcon ojoCerrado = new ImageIcon(getClass().getResource("/Iconos/ojo_cerrado.png"));
            ImageIcon ojoAbierto = new ImageIcon(getClass().getResource("/Iconos/ojo_abierto.png"));

            // Escalar iconos a 16x16
            Image imgCerrado = ojoCerrado.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            Image imgAbierto = ojoAbierto.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

            boton.setIcon(new ImageIcon(imgCerrado));
            boton.setSelectedIcon(new ImageIcon(imgAbierto));

        } catch (Exception e) {
            // Fallback: texto emoji
            boton.setText("👁");
        }

        // Configuración estética: sin fondo ni borde
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setToolTipText("Mostrar / ocultar contraseña");

        // Mostrar contraseña mientras se mantiene presionado
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                campo.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                campo.setEchoChar(echoChar);
            }
        });

        return boton;
    }

    private void setupEventHandlers() {
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] actual = campoClaveActual.getPassword();
                char[] nueva = campoClaveNueva.getPassword();
                char[] confirmar = campoConfirmarClave.getPassword();

                if (nueva.length == 0) {
                    JOptionPane.showMessageDialog(VentanaCambiarClave.this,
                            "La nueva clave no puede estar vacía.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!java.util.Arrays.equals(nueva, confirmar)) {
                    JOptionPane.showMessageDialog(VentanaCambiarClave.this,
                            "Las nuevas claves no coinciden.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Aquí puedes validar la clave actual contra tu modelo/datos
                // Ejemplo ficticio:
                String claveGuardada = "1234"; // reemplazar por la real
                if (!String.valueOf(actual).equals(claveGuardada)) {
                    JOptionPane.showMessageDialog(VentanaCambiarClave.this,
                            "La clave actual es incorrecta.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(VentanaCambiarClave.this,
                        "La clave fue cambiada con éxito.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                botonMostrarClave.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        campoConfirmarClave.setEchoChar(botonMostrarClave.isSelected() ? (char) 0 : '*');
                    }
                });
            }
        });


        botonCancelar.addActionListener(e -> dispose());
    }
}
