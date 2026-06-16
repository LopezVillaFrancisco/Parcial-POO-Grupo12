package org.examen.view;

import org.examen.controller.ClienteController;
import org.examen.controller.EquipoController;
import org.examen.dto.ClienteDTO;
import org.examen.dto.EquipoDTO;
import org.examen.model.enums.TipoEquipo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Ventana principal del Sistema de Gestion de Alquiler de Equipamiento para
 * Eventos. Funciona como menu de acceso a las distintas funcionalidades Swing.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Gestion de Alquiler de Equipamiento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 360);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Menu principal");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(15));

        panel.add(crearBoton("Registrar Cliente (UC1)",
                e -> new RegistrarClienteView().setVisible(true)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBoton("Registrar Equipo (UC2)",
                e -> new RegistrarEquipoView().setVisible(true)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBoton("Solicitar Alquiler (UC3)",
                e -> new SolicitarAlquilerView().setVisible(true)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBoton("Finalizar Alquiler (UC4)",
                e -> new FinalizarAlquilerView().setVisible(true)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBoton("Consultar Equipos Disponibles (UC5)",
                e -> new ConsultarEquiposDisponiblesView().setVisible(true)));
        panel.add(Box.createVerticalStrut(20));
        panel.add(crearBoton("Cargar datos de ejemplo",
                e -> cargarDatosDeEjemplo()));

        getContentPane().add(panel);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.addActionListener(accion);
        return boton;
    }

    /**
     * Carga un cliente y algunos equipos de ejemplo para poder probar de
     * inmediato los casos de uso de solicitud y consulta.
     */
    private void cargarDatosDeEjemplo() {
        try {
            ClienteController.getInstance().registrarCliente(
                    new ClienteDTO("20304050", "Eventos del Sur SRL", "1145678900",
                            "contacto@eventossur.com", "Av. Siempreviva 123", null, 0),
                    "admin");

            EquipoController.getInstance().registrarEquipo(
                    new EquipoDTO("EQ-001", "Parlante 1000W", "Sonido profesional",
                            TipoEquipo.SONIDO.name(), null, 5000, 10, true),
                    "admin");
            EquipoController.getInstance().registrarEquipo(
                    new EquipoDTO("EQ-002", "Reflector LED", "Iluminacion para escenario",
                            TipoEquipo.ILUMINACION.name(), null, 3000, 20, false),
                    "admin");
            EquipoController.getInstance().registrarEquipo(
                    new EquipoDTO("EQ-003", "Carpa 5x5", "Carpa estructural",
                            TipoEquipo.CARPA.name(), null, 8000, 4, true),
                    "admin");

            JOptionPane.showMessageDialog(this,
                    "Datos de ejemplo cargados:\n- Cliente 20304050\n- Equipos EQ-001, EQ-002, EQ-003",
                    "Datos de ejemplo", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Los datos de ejemplo ya estaban cargados o hubo un error: " + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si no se puede aplicar el look and feel del sistema, se usa el por defecto.
        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
