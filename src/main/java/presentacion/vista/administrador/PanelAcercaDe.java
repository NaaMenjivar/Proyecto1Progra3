package presentacion.vista.administrador;

import javax.swing.*;
import java.awt.*;

public class PanelAcercaDe {
    private JPanel panelPrincipal;

    public PanelAcercaDe() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTitulo = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("Prescripción y despacho de recetas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Eras Medium ITC", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(94, 218, 224)); // Color gris oscuro

        JLabel lblContacto = new JLabel("Hospital.Inc     @hospital.inc     +506 89765432", SwingConstants.CENTER);
        lblContacto.setFont(new Font("Courier New", Font.PLAIN, 14));

        panelTitulo.add(lblTitulo, BorderLayout.NORTH);
        panelTitulo.add(lblContacto, BorderLayout.SOUTH);

        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));

        try {
            ImageIcon iconoHospital = new ImageIcon(getClass().getResource("/Iconos/hospital.jpg"));
            if (iconoHospital.getIconWidth() > 0) {
                JLabel lblImagen = new JLabel(iconoHospital);
                panelImagen.add(lblImagen);
            } else {
                JLabel lblImagenAlt = new JLabel("🏥", SwingConstants.CENTER);
                lblImagenAlt.setFont(new Font("Arial Unicode MS", Font.PLAIN, 120));
                panelImagen.add(lblImagenAlt);
            }
        } catch (Exception e) {
            JLabel lblImagenAlt = new JLabel("🏥", SwingConstants.CENTER);
            lblImagenAlt.setFont(new Font("Arial Unicode MS", Font.PLAIN, 120));
            panelImagen.add(lblImagenAlt);
        }

        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Sistema"));

        JLabel lblVersion = new JLabel("Versión: 1.0.0", SwingConstants.CENTER);
        JLabel lblFecha = new JLabel("© 2025 - Sistema de Gestión Hospitalaria", SwingConstants.CENTER);

        panelInfo.add(lblVersion);
        panelInfo.add(lblFecha);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelImagen, BorderLayout.CENTER);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}
