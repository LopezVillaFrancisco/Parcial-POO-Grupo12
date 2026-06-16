package org.examen.view;

import org.examen.controller.EquipoController;
import org.examen.dto.EquipoDTO;
import org.examen.model.enums.TipoEquipo;

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
import java.util.Date;
import java.util.List;

/**
 * Ventana Swing para el caso de uso UC5 - Consultar equipos disponibles.
 * Valida los parametros ingresados e invoca a
 * EquipoController.getInstance().consultarEquiposDisponibles(...), mostrando los
 * resultados en una tabla.
 */
public class ConsultarEquiposDisponiblesView extends JFrame {

    private static final String FORMATO_FECHA = "yyyy-MM-dd";

    private final JTextField txtFechaEvento = new JTextField();
    private final JTextField txtCantidadDias = new JTextField();
    private final JComboBox<TipoEquipo> cmbTipoEvento = new JComboBox<>(TipoEquipo.values());

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Codigo", "Nombre", "Tipo", "Valor diario", "Stock", "Instalacion"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaResultados = new JTable(modeloTabla);

    public ConsultarEquiposDisponiblesView() {
        setTitle("Consultar Equipos Disponibles");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        panelCampos.add(new JLabel("Fecha evento (" + FORMATO_FECHA + "):"));
        panelCampos.add(txtFechaEvento);
        panelCampos.add(new JLabel("Cantidad de dias:"));
        panelCampos.add(txtCantidadDias);
        panelCampos.add(new JLabel("Tipo de equipo:"));
        panelCampos.add(cmbTipoEvento);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> consultar());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnConsultar);
        panelSuperior.add(panelBoton, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
    }

    private void consultar() {
        String textoFecha = txtFechaEvento.getText().trim();
        if (textoFecha.isEmpty() || txtCantidadDias.getText().trim().isEmpty()) {
            mostrarError("La fecha y la cantidad de dias son obligatorias.");
            return;
        }

        Date fechaEvento = parsearFecha(textoFecha);
        if (fechaEvento == null) {
            mostrarError("La fecha debe tener el formato " + FORMATO_FECHA + ".");
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

        TipoEquipo tipoEvento = (TipoEquipo) cmbTipoEvento.getSelectedItem();

        try {
            List<EquipoDTO> disponibles = EquipoController.getInstance()
                    .consultarEquiposDisponibles(fechaEvento, cantidadDias,
                            tipoEvento != null ? tipoEvento.name() : null);
            cargarResultados(disponibles);
            if (disponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay equipos disponibles para los parametros indicados.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cargarResultados(List<EquipoDTO> equipos) {
        modeloTabla.setRowCount(0);
        for (EquipoDTO equipo : equipos) {
            modeloTabla.addRow(new Object[]{
                    equipo.getCodigo(),
                    equipo.getNombre(),
                    equipo.getTipoEquipo(),
                    equipo.getValorDiario(),
                    equipo.getStockDisponible(),
                    equipo.isRequiereInstalacion() ? "Si" : "No"
            });
        }
    }

    private Date parsearFecha(String texto) {
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
