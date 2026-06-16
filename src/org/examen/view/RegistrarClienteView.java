package org.examen.view;

import org.examen.controller.ClienteController;
import org.examen.dto.ClienteDTO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Ventana Swing para el caso de uso UC1 - Registrar Cliente.
 * Valida los datos ingresados, arma el ClienteDTO e invoca a
 * ClienteController.getInstance().registrarCliente(...).
 */
public class RegistrarClienteView extends JFrame {

    private final JTextField txtDniCuit = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JTextField txtUsuario = new JTextField("admin");

    public RegistrarClienteView() {
        setTitle("Registrar Cliente");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 8, 8));
        panelCampos.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelCampos.add(new JLabel("DNI/CUIT:"));
        panelCampos.add(txtDniCuit);
        panelCampos.add(new JLabel("Nombre / Razon social:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Telefono:"));
        panelCampos.add(txtTelefono);
        panelCampos.add(new JLabel("Email:"));
        panelCampos.add(txtEmail);
        panelCampos.add(new JLabel("Direccion:"));
        panelCampos.add(txtDireccion);
        panelCampos.add(new JLabel("Usuario responsable:"));
        panelCampos.add(txtUsuario);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelCampos, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void registrar() {
        String dniCuit = txtDniCuit.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String usuario = txtUsuario.getText().trim();

        if (dniCuit.isEmpty() || nombre.isEmpty() || telefono.isEmpty()
                || email.isEmpty() || direccion.isEmpty() || usuario.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            mostrarError("El email ingresado no tiene un formato valido.");
            return;
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setDniCuit(dniCuit);
        dto.setNombreRazonSocial(nombre);
        dto.setTelefono(telefono);
        dto.setEmail(email);
        dto.setDireccion(direccion);

        try {
            ClienteDTO registrado = ClienteController.getInstance().registrarCliente(dto, usuario);
            JOptionPane.showMessageDialog(this,
                    "Cliente registrado correctamente.\nDNI/CUIT: " + registrado.getDniCuit()
                            + "\nEstado: " + registrado.getEstado(),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        txtDniCuit.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtDniCuit.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos", JOptionPane.ERROR_MESSAGE);
    }
}
