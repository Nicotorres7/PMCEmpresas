package PMCJAVA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class InterfazUsuario extends JFrame {

    private List<materiaPrima> materiasPrimas;
    private List<producto> productos;
    private List<orden> ordenes;
    private JTextField presupuestoField;
    private JTextArea resultadoArea;
    private JTextArea materiasArea;
    private JTextArea productosArea;
    private JTextArea ordenesArea;
    private DefaultComboBoxModel<materiaPrima> materiasComboBoxModel;
    private DefaultComboBoxModel<producto> productosComboBoxModel;

    public InterfazUsuario() {
        materiasPrimas = new ArrayList<>();
        productos = new ArrayList<>();
        ordenes = new ArrayList<>();

        setTitle("Gestión de Producción");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#f0f8ff"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.decode("#e0f7fa"));

        // Pestaña Materias Primas
        tabbedPane.add("Materias Primas", crearPanelMateriasPrimas());

        // Pestaña Productos
        tabbedPane.add("Productos", crearPanelProductos());

        // Pestaña Órdenes
        tabbedPane.add("Órdenes", crearPanelOrdenes());

        // Pestaña Optimización
        tabbedPane.add("Optimización", crearPanelOptimizacion());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelMateriasPrimas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JTextField nombreMateriaField = new JTextField(10);
        JTextField precioGramoField = new JTextField(10);
        JButton agregarMateriaButton = new JButton("Agregar Materia Prima");

        materiasComboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<materiaPrima> materiasComboBox = new JComboBox<>(materiasComboBoxModel);
        materiasArea = new JTextArea(10, 30);
        materiasArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(materiasArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        agregarMateriaButton.addActionListener(e -> {
            String nombre = nombreMateriaField.getText();
            float precioGramo = Float.parseFloat(precioGramoField.getText());
            materiaPrima nuevaMateria = new materiaPrima(nombre, precioGramo);
            materiasPrimas.add(nuevaMateria);
            materiasComboBoxModel.addElement(nuevaMateria);
            nombreMateriaField.setText("");
            precioGramoField.setText("");
            actualizarMateriasPrimas();
        });

        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreMateriaField);
        inputPanel.add(new JLabel("Precio por gramo:"));
        inputPanel.add(precioGramoField);
        inputPanel.add(agregarMateriaButton);

        panel.add(inputPanel);
        panel.add(new JLabel("Materias Primas:"));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JTextField nombreProductoField = new JTextField(10);
        JTextField precioProductoField = new JTextField(10);
        JButton agregarProductoButton = new JButton("Agregar Producto");

        productosComboBoxModel = new DefaultComboBoxModel<>();
        JComboBox<producto> productosComboBox = new JComboBox<>(productosComboBoxModel);
        
        JComboBox<materiaPrima> materiasComboBox = new JComboBox<>(materiasComboBoxModel);
        JTextField cantidadMateriaField = new JTextField(5);
        JButton agregarMateriaProductoButton = new JButton("Añadir Materia Prima");

        List<materiaPrima> materiasPrimasSeleccionadas = new ArrayList<>();
        productosArea = new JTextArea(10, 30);
        productosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productosArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        agregarMateriaProductoButton.addActionListener(e -> {
            materiaPrima materia = (materiaPrima) materiasComboBox.getSelectedItem();
            int cantidad = Integer.parseInt(cantidadMateriaField.getText());
            for (int i = 0; i < cantidad; i++) {
                materiasPrimasSeleccionadas.add(materia);
            }
            cantidadMateriaField.setText("");
        });

        agregarProductoButton.addActionListener(e -> {
            String nombre = nombreProductoField.getText();
            float precio = Float.parseFloat(precioProductoField.getText());
            producto nuevoProducto = new producto(nombre, precio, new ArrayList<>(materiasPrimasSeleccionadas));
            productos.add(nuevoProducto);
            productosComboBoxModel.addElement(nuevoProducto);
            nombreProductoField.setText("");
            precioProductoField.setText("");
            materiasPrimasSeleccionadas.clear();
            actualizarProductos();
        });

        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nombreProductoField);
        inputPanel.add(new JLabel("Precio:"));
        inputPanel.add(precioProductoField);
        inputPanel.add(new JLabel("Materia Prima:"));
        inputPanel.add(materiasComboBox);
        inputPanel.add(new JLabel("Cantidad:"));
        inputPanel.add(cantidadMateriaField);
        inputPanel.add(agregarMateriaProductoButton);
        inputPanel.add(agregarProductoButton);

        panel.add(inputPanel);
        panel.add(new JLabel("Productos:"));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel crearPanelOrdenes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JComboBox<producto> productosComboBox = new JComboBox<>(productosComboBoxModel);
        JTextField cantidadOrdenField = new JTextField(5);
        JButton agregarOrdenButton = new JButton("Agregar Orden");

        ordenesArea = new JTextArea(10, 30);
        ordenesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ordenesArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        agregarOrdenButton.addActionListener(e -> {
            producto prod = (producto) productosComboBox.getSelectedItem();
            int cantidad = Integer.parseInt(cantidadOrdenField.getText());
            orden nuevaOrden = new orden(prod, cantidad);
            ordenes.add(nuevaOrden);
            actualizarOrdenes();
            cantidadOrdenField.setText("");
        });

        inputPanel.add(new JLabel("Producto:"));
        inputPanel.add(productosComboBox);
        inputPanel.add(new JLabel("Cantidad:"));
        inputPanel.add(cantidadOrdenField);
        inputPanel.add(agregarOrdenButton);

        panel.add(inputPanel);
        panel.add(new JLabel("Órdenes:"));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel crearPanelOptimizacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel presupuestoPanel = new JPanel();
        presupuestoPanel.setLayout(new FlowLayout());
        presupuestoPanel.setPreferredSize(new Dimension(500, 80));

        presupuestoField = new JTextField(10);
        resultadoArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        JButton optimizarButton = new JButton("Optimizar Órdenes");
        optimizarButton.addActionListener(e -> optimizarOrdenes());

        presupuestoPanel.add(new JLabel("Presupuesto:"));
        presupuestoPanel.add(presupuestoField);
        presupuestoPanel.add(optimizarButton);

        panel.add(presupuestoPanel);
        panel.add(new JLabel("Resultado de Optimización:"));
        panel.add(scrollPane);

        return panel;
    }

    private void actualizarMateriasPrimas() {
        materiasArea.setText("");
        for (materiaPrima m : materiasPrimas) {
            materiasArea.append("Nombre: " + m.getNombre() + ", Precio: " + m.getPrecioGramo() + "\n");
        }
    }

    private void actualizarProductos() {
        productosArea.setText("");
        for (producto p : productos) {
            productosArea.append("Nombre: " + p.getNombre() + ", Precio: " + p.getPrecio() + "\n");
        }
    }

    private void actualizarOrdenes() {
        ordenesArea.setText("");
        for (orden o : ordenes) {
            ordenesArea.append("Producto: " + o.getProducto().getNombre() + ", Cantidad: " + o.getCantidad() +
                               ", Total: " + o.getTotal() + "\n");
        }
    }

    private void optimizarOrdenes() {
        float presupuesto = Float.parseFloat(presupuestoField.getText());
        resultadoArea.setText("Optimizando con presupuesto: " + presupuesto + "\n");

        List<orden> ordenesSeleccionadas = maximizarIngresosConPresupuesto(ordenes, presupuesto);
        if (!ordenesSeleccionadas.isEmpty()) {
            for (orden o : ordenesSeleccionadas) {
                resultadoArea.append("Orden: Producto: " + o.getProducto().getNombre() + 
                                     ", Cantidad: " + o.getCantidad() + ", Total: " + o.getTotal() + "\n");
            }
        } else {
            resultadoArea.append("No se pueden realizar órdenes dentro del presupuesto.\n");
        }
    }

    private List<orden> maximizarIngresosConPresupuesto(List<orden> ordenes, float presupuesto) {
        List<orden> seleccionadas = new ArrayList<>();
        float total = 0;

        for (orden o : ordenes) {
            if (total + o.getTotal() <= presupuesto) {
                seleccionadas.add(o);
                total += o.getTotal();
            }
        }

        return seleccionadas;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazUsuario interfaz = new InterfazUsuario();
            interfaz.setVisible(true);
        });
    }
}






