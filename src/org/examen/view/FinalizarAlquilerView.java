package org.examen.view;

import org.examen.controller.AlquilerController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Ventana Swing para el caso de uso UC4 - Finalizar alquiler y calcular saldo.
 * Valida los datos ingresados e invoca a
 * AlquilerController.getInstance().finalizarAlquiler(...), mostrando el importe
 * pendiente calculado.
 */
public class FinalizarAlquilerView extends JFrame {

    private final JTextField txtIdAlquiler = new JTextField();
    private final JTextField txtUsuario = new JTextField("admin");

    public FinalizarAlquilerView() {
        setTitle("Finalizar Alquiler");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelCampos.add(new JLabel("Numero de alquiler:"));
        panelCampos.add(txtIdAlquiler);
        panelCampos.add(new JLabel("Usuario responsable:"));
        panelCampos.add(txtUsuario);

        JButton btnFinalizar = new JButton("Finalizar y calcular saldo");
        btnFinalizar.addActionListener(e -> finalizar());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnFinalizar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelCampos, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void finalizar() {
        String usuario = txtUsuario.getText().trim();
        if (txtIdAlquiler.getText().trim().isEmpty() || usuario.isEmpty()) {
            mostrarError("El numero de alquiler y el usuario son obligatorios.");
            return;
        }

        int idAlquiler;
        try {
            idAlquiler = Integer.parseInt(txtIdAlquiler.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("El numero de alquiler debe ser un numero entero valido.");
            return;
        }

        try {
            double importePendiente = AlquilerController.getInstance()
                    .finalizarAlquiler(idAlquiler, usuario);
            JOptionPane.showMessageDialog(this,
                    "Alquiler #" + idAlquiler + " finalizado correctamente.\n"
                            + "Importe pendiente: " + String.format("%.2f", importePendiente),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        txtIdAlquiler.setText("");
        txtIdAlquiler.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos", JOptionPane.ERROR_MESSAGE);
    }
}
