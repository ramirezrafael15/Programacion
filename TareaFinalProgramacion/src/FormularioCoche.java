import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;

public class FormularioCoche extends JDialog{
    private static ArrayList<Coche> coches = new ArrayList<>();
    private static final String ARCHIVO = "coches.csv";

    private JTextField txtMarca;
    private JTextField txtAnio;
    private JTextField txtColor;

    public FormularioCoche(JFrame parent, boolean mostrarTabla) {
        super(parent, true);
        cargarCoches();

        if (mostrarTabla) {
            setTitle("Lista de Coches");
            mostrarTabla();
        }else {
            setTitle("Añadir Coche");
            mostrarFormulario();
        }

        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    private void mostrarFormulario(){
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(4,2, 10, 10));

        panel.add(new JLabel("Marca: "));
        txtMarca = new JTextField();
        panel.add(txtMarca);

        panel.add(new JLabel("Año: "));
        txtAnio = new JTextField();
        panel.add(txtAnio);

        panel.add(new JLabel("Color: "));
        txtColor = new JTextField();
        panel.add(txtColor);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarCoche());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarCoche(){
        String marca = txtMarca.getText().trim();
        String anioTexto = txtAnio.getText().trim();
        String color = txtColor.getText().trim();

        if (marca.isEmpty() || anioTexto.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se permiten campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            int anio = Integer.parseInt(anioTexto);

            if (anio < 1886 || anio > 2100) {
                JOptionPane.showMessageDialog(this, "Introduce un año válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            coches.add(new Coche(marca, anio, color));
            guardarEnArchivo();
            JOptionPane.showMessageDialog(this, "Coche añadido con éxito");

            txtMarca.setText("");
            txtAnio.setText("");
            txtColor.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El campo Año solo acepta números", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTabla(){
        setLayout(new BorderLayout());
        String[] columnas = {"Marca", "Año", "Color"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Coche coche : coches){
            modelo.addRow(new Object[]{
                    coche.getMarca(),
                    coche.getAnio(),
                    coche.getColor()
            });
        }

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        JButton btnEliminar = new JButton("Eliminar Coches");
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un coche para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            coches.remove(filaSeleccionada);
            modelo.removeRow(filaSeleccionada);
            guardarEnArchivo();
            JOptionPane.showMessageDialog(this, "Coche eliminado con éxito");
        });

        JPanel panelSur = new JPanel();
        panelSur.add(btnEliminar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        if (coches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay coches registrados todavía");
        }
    }
    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Coche coche : coches) {
                pw.println(coche.getMarca() + "," + coche.getAnio() + "," + coche.getColor());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCoches() {
        coches.clear();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    coches.add(new Coche(partes[0], Integer.parseInt(partes[1]), partes[2]));
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

