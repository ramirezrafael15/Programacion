import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestión de coches");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuCoches = new JMenu("Coches");
        JMenuItem itemAnadir = new JMenuItem("Añadir Coche");
        JMenuItem itemMostrar = new JMenuItem("Mostrar Coche");

        itemAnadir.addActionListener(e -> {
            FormularioCoche formulario = new FormularioCoche(this, false);
            formulario.setVisible(true);
        });

        itemMostrar.addActionListener(e -> {
            FormularioCoche tabla = new FormularioCoche(this, true);
            tabla.setVisible(true);
        });

        menuCoches.add(itemAnadir);
        menuCoches.add(itemMostrar);
        menuBar.add(menuCoches);
        setJMenuBar(menuBar);

    }
}