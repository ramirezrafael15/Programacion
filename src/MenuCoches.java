import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MenuCoches extends JFrame {

    private CocheDAO cocheDAO = new CocheDAO();

    public MenuCoches() {
        setTitle("Gestión de Coches");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar barraMenu = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        JMenuItem itemAgregar = new JMenuItem("Añadir Coche");
        JMenuItem itemMostrar = new JMenuItem("Mostrar Coches");

        menu.add(itemAgregar);
        menu.add(itemMostrar);
        barraMenu.add(menu);
        setJMenuBar(barraMenu);

        itemAgregar.addActionListener(e -> abrirFormularioInsertar());
        itemMostrar.addActionListener(e -> mostrarTablaCoches());

        add(new JLabel("Menú de Gestión de Coches", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    private void abrirFormularioInsertar() {
        JDialog dialogo = new JDialog(this, "Añadir Coche", true);
        dialogo.setSize(400, 220);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField txtMarca = new JTextField();
        JTextField txtAnio  = new JTextField();
        JTextField txtColor = new JTextField();
        JButton btnGuardar  = new JButton("Guardar");

        dialogo.add(new JLabel("Marca:")); dialogo.add(txtMarca);
        dialogo.add(new JLabel("Año:"));   dialogo.add(txtAnio);
        dialogo.add(new JLabel("Color:")); dialogo.add(txtColor);
        dialogo.add(new JLabel());         dialogo.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            String marca     = txtMarca.getText().trim();
            String anioTexto = txtAnio.getText().trim();
            String color     = txtColor.getText().trim();

            if (marca.isEmpty() || anioTexto.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "No se permiten campos vacíos.");
                return;
            }
            if (!anioTexto.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialogo, "El campo Año solo debe contener números.");
                return;
            }

            cocheDAO.insertarCoche(new Coche(marca, Integer.parseInt(anioTexto), color));
            JOptionPane.showMessageDialog(dialogo, "Coche añadido correctamente.");
            dialogo.dispose();
        });

        dialogo.setVisible(true);
    }

    private void mostrarTablaCoches() {
        JFrame ventanaTabla = new JFrame("Lista de Coches");
        ventanaTabla.setSize(600, 350);
        ventanaTabla.setLocationRelativeTo(this);

        String[] columnas = {"ID", "Marca", "Año", "Color"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Coche c : cocheDAO.obtenerCoches()) {
            modelo.addRow(new Object[]{c.getId(), c.getMarca(), c.getAnio(), c.getColor()});
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventanaTabla, "Selecciona un coche para editar.");
                return;
            }

            int    id    = (int)    modelo.getValueAt(fila, 0);
            String marca = (String) modelo.getValueAt(fila, 1);
            int    anio  = (int)    modelo.getValueAt(fila, 2);
            String color = (String) modelo.getValueAt(fila, 3);

            JTextField txtMarca = new JTextField(marca);
            JTextField txtAnio  = new JTextField(String.valueOf(anio));
            JTextField txtColor = new JTextField(color);

            Object[] campos = {"Marca:", txtMarca, "Año:", txtAnio, "Color:", txtColor};
            int opcion = JOptionPane.showConfirmDialog(ventanaTabla, campos, "Editar Coche", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                if (txtMarca.getText().trim().isEmpty() || txtAnio.getText().trim().isEmpty() || txtColor.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(ventanaTabla, "No se permiten campos vacíos.");
                    return;
                }
                if (!txtAnio.getText().trim().matches("\\d+")) {
                    JOptionPane.showMessageDialog(ventanaTabla, "El año debe ser numérico.");
                    return;
                }

                cocheDAO.actualizarCoche(new Coche(id, txtMarca.getText().trim(),
                        Integer.parseInt(txtAnio.getText().trim()), txtColor.getText().trim()));

                JOptionPane.showMessageDialog(ventanaTabla, "Coche actualizado correctamente.");
                ventanaTabla.dispose();
                mostrarTablaCoches();
            }
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventanaTabla, "Selecciona un coche para eliminar.");
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(ventanaTabla,
                    "¿Seguro que quieres eliminar este coche?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                cocheDAO.eliminarCoche((int) modelo.getValueAt(fila, 0));
                JOptionPane.showMessageDialog(ventanaTabla, "Coche eliminado correctamente.");
                ventanaTabla.dispose();
                mostrarTablaCoches();
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        ventanaTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        ventanaTabla.add(panelBotones, BorderLayout.SOUTH);
        ventanaTabla.setVisible(true);
    }

    public static void main(String[] args) {
        new MenuCoches().setVisible(true);
    }
}
