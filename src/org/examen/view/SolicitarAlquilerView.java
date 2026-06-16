package org.examen.view;

import org.examen.controller.AlquilerController;
import org.examen.dto.AlquilerDTO;
import org.examen.dto.DetalleAlquilerDTO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ventana Swing para el caso de uso UC3 - Solicitar alquiler.
 * Permite cargar la cabecera del alquiler y una lista de items (equipo +
 * cantidad), valida los datos, arma el AlquilerDTO e invoca a
 * AlquilerController.getInstance().solicitarAlquiler(...).
 */
public class SolicitarAlquilerView extends JFrame {

    private static final String FORMATO_FECHA = "yyyy-MM-dd";

    private final JTextField txtDniCuit = new JTextField();
    private final JComboBox<String> cmbTipoAlquiler =
            new JComboBox<>(new String[]{"COMUN", "CORPORATIVO", "MASIVO"});
    private final JTextField txtFechaEvento = new JTextField();
    private final JTextField txtCantidadDias = new JTextField();
    private final JTextField txtUsuario = new JTextField("admin");

    private final JTextField txtCodigoEquipo = new JTextField();
    private final JTextField txtCantidadEquipo = new JTextField();

    private final DefaultTableModel modeloDetalles = new DefaultTableModel(
            new Object[]{"Codigo equipo", "Cantidad"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaDetalles = new JTable(modeloDetalles);

    public SolicitarAlquilerView() {
        setTitle("Solicitar Alquiler");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 520);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelCabecera = new JPanel(new GridLayout(5, 2, 8, 8));
        panelCabecera.setBorder(BorderFactory.createTitledBorder("Datos del alquiler"));

        panelCabecera.add(new JLabel("DNI/CUIT cliente:"));
        panelCabecera.add(txtDniCuit);
        panelCabecera.add(new JLabel("Tipo de alquiler:"));
        panelCabecera.add(cmbTipoAlquiler);
        panelCabecera.add(new JLabel("Fecha evento (" + FORMATO_FECHA + "):"));
        panelCabecera.add(txtFechaEvento);
        panelCabecera.add(new JLabel("Cantidad de dias:"));
        panelCabecera.add(txtCantidadDias);
        panelCabecera.add(new JLabel("Usuario responsable:"));
        panelCabecera.add(txtUsuario);

        JPanel panelItem = new JPanel(new GridLayout(2, 2, 8, 8));
        panelItem.setBorder(BorderFactory.createTitledBorder("Agregar equipo"));
        panelItem.add(new JLabel("Codigo equipo:"));
        panelItem.add(txtCodigoEquipo);
        panelItem.add(new JLabel("Cantidad:"));
        panelItem.add(txtCantidadEquipo);

        JButton btnAgregar = new JButton("Agregar item");
        btnAgregar.addActionListener(e -> agregarItem());
        JButton btnQuitar = new JButton("Quitar item seleccionado");
        btnQuitar.addActionListener(e -> quitarItem());

        JPanel panelBotonesItem = new JPanel();
        panelBotonesItem.add(btnAgregar);
        panelBotonesItem.add(btnQuitar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelCabecera, BorderLayout.NORTH);
        panelSuperior.add(panelItem, BorderLayout.CENTER);
        panelSuperior.add(panelBotonesItem, BorderLayout.SOUTH);

        JButton btnSolicitar = new JButton("Solicitar alquiler");
        btnSolicitar.addActionListener(e -> solicitar());
        JPanel panelInferior = new JPanel();
        panelInferior.add(btnSolicitar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);
        getContentPane().add(panelInferior, BorderLayout.SOUTH);
    }

    private void agregarItem() {
        String codigo = txtCodigoEquipo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarError("El codigo del equipo es obligatorio.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadEquipo.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("La cantidad debe ser un numero entero valido.");
            return;
        }
        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor a cero.");
            return;
        }

        modeloDetalles.addRow(new Object[]{codigo, cantidad});
        txtCodigoEquipo.setText("");
        txtCantidadEquipo.setText("");
        txtCodigoEquipo.requestFocus();
    }

    private void quitarItem() {
        int fila = tablaDetalles.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un item de la tabla para quitarlo.");
            return;
        }
        modeloDetalles.removeRow(fila);
    }

    private void solicitar() {
        String dniCuit = txtDniCuit.getText().trim();
        String usuario = txtUsuario.getText().trim();

        if (dniCuit.isEmpty() || usuario.isEmpty()) {
            mostrarError("El DNI/CUIT del cliente y el usuario son obligatorios.");
            return;
        }

        Date fechaEvento = parsearFecha(txtFechaEvento.getText().trim());
        if (fechaEvento == null) {
            mostrarError("La fecha del evento debe tener el formato " + FORMATO_FECHA + ".");
            return;
        }

        int cantidadDias;
        try {
            cantidadDias = Integer.parseInt(txtCantidadDias.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarError("La cantidad de dias debe ser un numero entero valido.");
            return;
        }
        if (cantidadDias <= 0) {
            mostrarError("La cantidad de dias debe ser mayor a cero.");
            return;
        }

        if (modeloDetalles.getRowCount() == 0) {
            mostrarError("Debe agregar al menos un equipo al alquiler.");
            return;
        }

        List<DetalleAlquilerDTO> detalles = new ArrayList<>();
        for (int i = 0; i < modeloDetalles.getRowCount(); i++) {
            String codigo = (String) modeloDetalles.getValueAt(i, 0);
            int cantidad = (int) modeloDetalles.getValueAt(i, 1);
            detalles.add(new DetalleAlquilerDTO(codigo, cantidad, 0));
        }

        AlquilerDTO dto = new AlquilerDTO();
        dto.setDniCuitCliente(dniCuit);
        dto.setTipoAlquiler((String) cmbTipoAlquiler.getSelectedItem());
        dto.setFechaEvento(fechaEvento);
        dto.setCantidadDias(cantidadDias);
        dto.setDetalles(detalles);

        try {
            AlquilerDTO registrado = AlquilerController.getInstance().solicitarAlquiler(dto, usuario);
            JOptionPane.showMessageDialog(this,
                    "Alquiler registrado correctamente.\nNumero: " + registrado.getId()
                            + "\nEstado: " + registrado.getEstado(),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void limpiar() {
        txtDniCuit.setText("");
        txtFechaEvento.setText("");
        txtCantidadDias.setText("");
        cmbTipoAlquiler.setSelectedIndex(0);
        modeloDetalles.setRowCount(0);
        txtDniCuit.requestFocus();
    }

    private Date parsearFecha(String texto) {
        if (texto.isEmpty()) {
            return null;
        }
        SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
        formato.setLenient(false);
        try {
            return formato.parse(texto);
        } catch (ParseException ex) {
            return null;
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos", JOptionPane.ERROR_MESSAGE);
    }
}
