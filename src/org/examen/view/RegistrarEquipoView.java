package org.examen.view;

import org.examen.controller.EquipoController;
import org.examen.dto.EquipoDTO;
import org.examen.model.enums.TipoEquipo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Ventana Swing para el caso de uso UC2 - Registrar Equipo.
 * Valida los datos ingresados, arma el EquipoDTO e invoca a
 * EquipoController.getInstance().registrarEquipo(...).
 */
public class RegistrarEquipoView extends JFrame {

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JComboBox<TipoEquipo> cmbTipoEquipo = new JComboBox<>(TipoEquipo.values());
    private final JTextField txtValorDiario = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JCheckBox chkRequiereInstalacion = new JCheckBox("Requiere instalacion tecnica");
    private final JTextField txtUsuario = new JTextField("admin");

    public RegistrarEquipoView() {
        setTitle("Registrar Equipo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(440, 360);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelCampos = new JPanel(new GridLayout(8, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelCampos.add(new JLabel("Codigo:"));
        panelCampos.add(txtCodigo);
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Descripcion:"));
        panelCampos.add(txtDescripcion);
        panelCampos.add(new JLabel("Tipo de equipo:"));
        panelCampos.add(cmbTipoEquipo);
        panelCampos.add(new JLabel("Valor diario:"));
        panelCampos.add(txtValorDiario);
        panelCampos.add(new JLabel("Stock disponible:"));
        panelCampos.add(txtStock);
        panelCampos.add(new JLabel("Instalacion:"));
        panelCampos.add(chkRequiereInstalacion);
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
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String usuario = txtUsuario.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || usuario.isEmpty()) {
            mostrarError("Codigo, nombre, descripcion y usuario son obligatorios.");
            return;
        }

        double valorDiario;
        try {
            valorDiario = Double.parseDouble(txtValorDiario.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("El valor diario debe ser un numero valido.");
            return;
        }
        if (valorDiario <= 0) {
            mostrarError("El valor diario debe ser mayor a cero.");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("El stock debe ser un numero entero valido.");
            return;
        }
        if (stock < 0) {
            mostrarError("El stock no puede ser negativo.");
            return;
        }

        TipoEquipo tipoEquipo = (TipoEquipo) cmbTipoEquipo.getSelectedItem();

        EquipoDTO dto = new EquipoDTO();
        dto.setCodigo(codigo);
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setTipoEquipo(tipoEquipo != null ? tipoEquipo.name() : null);
        dto.setValorDiario(valorDiario);
        dto.setStockDisponible(stock);
        dto.setRequiereInstalacion(chkRequiereInstalacion.isSelected());

        try {
            EquipoDTO registrado = EquipoController.getInstance().registrarEquipo(dto, usuario);
            JOptionPane.showMessageDialog(this,
                    "Equipo registrado correctamente.\nCodigo: " + registrado.getCodigo()
                            + "\nEstado: " + registrado.getEstado(),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtValorDiario.setText("");
        txtStock.setText("");
        chkRequiereInstalacion.setSelected(false);
        cmbTipoEquipo.setSelectedIndex(0);
        txtCodigo.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos", JOptionPane.ERROR_MESSAGE);
    }
}
