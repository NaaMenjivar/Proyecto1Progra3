package presentacion.vista.principales;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCambiarClave extends JDialog {
    private JPasswordField campoClaveActual;
    private JPasswordField campoClaveNueva;
    private JPasswordField campoConfirmarClave;
    private JButton botonAceptar;
    private JButton botonCancelar;

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
    }

    private void setupLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Clave actual:"), gbc);
        gbc.gridx = 1;
        panel.add(campoClaveActual, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nueva clave:"), gbc);
        gbc.gridx = 1;
        panel.add(campoClaveNueva, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirmar nueva clave:"), gbc);
        gbc.gridx = 1;
        panel.add(campoConfirmarClave, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
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
            }
        });

        botonCancelar.addActionListener(e -> dispose());
    }
}
