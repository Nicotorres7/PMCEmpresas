package PMCJAVA;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import PMCJAVA.Optimizer;

import javax.swing.table.TableRowSorter;

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

        // Cargar datos
        loadInformation();

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

        // Actualizar datos
        actualizarMateriasPrimas();
        actualizarProductos();
        actualizarOrdenes();
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
        for (materiaPrima m : materiasPrimas) {
            materiasComboBoxModel.addElement(m);
        }
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
        for (producto p : productos) {
            productosComboBoxModel.addElement(p);
        }
        JComboBox<producto> productosComboBox = new JComboBox<>(productosComboBoxModel);
        
        JComboBox<materiaPrima> materiasComboBox = new JComboBox<>(materiasComboBoxModel);
        JTextField cantidadMateriaField = new JTextField(5);
        JButton agregarMateriaProductoButton = new JButton("Añadir Materia Prima");

        HashMap<materiaPrima, Integer> materiasPrimasSeleccionadas = new HashMap<>();
        productosArea = new JTextArea(10, 30);
        productosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productosArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        agregarMateriaProductoButton.addActionListener(e -> {
            materiaPrima materia = (materiaPrima) materiasComboBox.getSelectedItem();
            int cantidad = Integer.parseInt(cantidadMateriaField.getText());
            materiasPrimasSeleccionadas.put(materia, cantidad);
            cantidadMateriaField.setText("");
        });

        agregarProductoButton.addActionListener(e -> {
            String nombre = nombreProductoField.getText();
            float precio = Float.parseFloat(precioProductoField.getText());
            producto nuevoProducto = new producto(nombre, precio, new HashMap<>(materiasPrimasSeleccionadas));
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

    private JTable ordenesTable;
    private DefaultTableModel tableModel;

    private JPanel crearPanelOrdenes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Márgenes

        // Panel de entrada de órdenes
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Alineación y espaciado
        inputPanel.setBackground(Color.white);

        JComboBox<producto> productosComboBox = new JComboBox<>(productosComboBoxModel);
        JTextField cantidadOrdenField = new JTextField(5);
        JButton agregarOrdenButton = new JButton("Agregar Orden");

        // Definir el modelo de la tabla
        String[] columnNames = {"Producto", "Cantidad", "Costo Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };

        // Crear la tabla y configurarla
        ordenesTable = new JTable(tableModel);
        ordenesTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ordenesTable.setRowHeight(25);

        // Configurar encabezados
        JTableHeader header = ordenesTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Ajustar el ancho de las columnas
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = ordenesTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
        }

        // Alternar colores de filas
        ordenesTable.setFillsViewportHeight(true);
        ordenesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                        boolean isSelected, boolean hasFocus,
                                                        int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 240, 240));
                    } else {
                        c.setBackground(Color.white);
                    }
                }
                return c;
            }
        });

        // Crear el JScrollPane para la tabla
        JScrollPane tableScrollPane = new JScrollPane(ordenesTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 400));

        // Acción del botón para agregar una orden
        agregarOrdenButton.addActionListener(e -> {
            producto prod = (producto) productosComboBox.getSelectedItem();
            try {
                int cantidad = Integer.parseInt(cantidadOrdenField.getText());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(panel, "La cantidad debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                orden nuevaOrden = new orden(prod, cantidad);
                ordenes.add(nuevaOrden);
                actualizarOrdenes();
                cantidadOrdenField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Por favor, ingresa una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Agregar componentes al panel de entrada
        inputPanel.add(new JLabel("Producto:"));
        inputPanel.add(productosComboBox);
        inputPanel.add(new JLabel("Cantidad:"));
        inputPanel.add(cantidadOrdenField);
        inputPanel.add(agregarOrdenButton);

        // Agregar el panel de entrada y la tabla al panel principal
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JLabel("Órdenes Registradas:"), BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        ordenesTable.setRowSorter(sorter);

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
        File materiasFile = new File("datos/materiasPrimas.txt");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(materiasFile, false))) {
            for (materiaPrima m : materiasPrimas) {
                pw.println(m.getNombre() + "," + m.getPrecioGramo());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar materias primas.");
        }
    }

    private void actualizarProductos() {
        productosArea.setText("");
        for (producto p : productos) {
            productosArea.append("Nombre: " + p.getNombre() + ", Precio: " + p.getPrecio() + "\n");
        }
        File productosFile = new File("datos/productos.txt");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(productosFile, false))) {
            for (producto p : productos) {
                pw.print(p.getNombre() + "," + p.getPrecio());
                for (materiaPrima m : p.getMateriasPrimas().keySet()) {
                    pw.print("," + m.getNombre() + ":" + Integer.toString(p.getMateriasPrimas().get(m)));
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar productos.");
        }
    }

    private void actualizarOrdenes() {
        // Limpiar el modelo de la tabla
        tableModel.setRowCount(0);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        for (orden o : ordenes) {
            // Formatear el costo total
            String costoFormateado = "$" + decimalFormat.format(o.getTotal());

            // Añadir una nueva fila al modelo
            tableModel.addRow(new Object[]{
                o.getProducto().getNombre(),
                o.getCantidad(),
                costoFormateado
            });
        }

        // Guardar en archivo (mantén esta parte si lo necesitas)
        File ordenesFile = new File("datos/ordenes.txt");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(ordenesFile, false))) {
            for (orden o : ordenes) {
                pw.println(o.getProducto().getNombre() + "," + o.getCantidad());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar órdenes.");
        }
    }

    

    private void optimizarOrdenes() {
        float presupuesto = Float.parseFloat(presupuestoField.getText());
        DecimalFormat decimalFormat = new DecimalFormat("#, ###"); 
        resultadoArea.setText("Optimizando con presupuesto: $" + decimalFormat.format(presupuesto) + "\n");

               
        List<orden> ordenesSeleccionadas = Optimizer.optimizerOrdenes(ordenes, presupuesto);
        if (!ordenesSeleccionadas.isEmpty()) {
            for (orden o : ordenesSeleccionadas) {
                resultadoArea.append("Orden: Producto: " + o.getProducto().getNombre() + 
                                     ", Cantidad: " + o.getCantidad() + 
                                     ", Ingreso Total: $" + decimalFormat.format(o.getIngreso()) + "\n");
            }
        } else {
            resultadoArea.append("No se pueden realizar órdenes dentro del presupuesto.\n");
        }
    }


    private void loadInformation() {
        // Materias primas
        File materiasFile = new File("datos/materiasPrimas.txt");
        if (materiasFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(materiasFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    String nombre = parts[0];
                    float precioGramo = Float.parseFloat(parts[1]);
                    materiasPrimas.add(new materiaPrima(nombre, precioGramo));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar materias primas.");
            }
        }
        // Productos
        File productosFile = new File("datos/productos.txt");
        if (productosFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(productosFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    String nombre = parts[0];
                    float precio = Float.parseFloat(parts[1]);
                    HashMap<materiaPrima, Integer> materias = new HashMap<>();
                    for (int i = 2; i < parts.length; i++) {
                        String[] materiaParts = parts[i].split(":");
                        String nombreMateria = materiaParts[0];
                        int cantidad = Integer.parseInt(materiaParts[1]);
                        for (materiaPrima m : materiasPrimas) {
                            if (m.getNombre().equals(nombreMateria)) {
                                materias.put(m, cantidad);
                                break;
                            }
                        }
                    }
                    productos.add(new producto(nombre, precio, materias));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar productos.");
            }
        }
        // Órdenes
        File ordenesFile = new File("datos/ordenes.txt");
        if (ordenesFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(ordenesFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    String nombreProducto = parts[0];
                    int cantidad = Integer.parseInt(parts[1]);
                    producto prod = null;
                    for (producto p : productos) {
                        if (p.getNombre().equals(nombreProducto)) {
                            prod = p;
                            break;
                        }
                    }
                    if (prod != null) {
                        ordenes.add(new orden(prod, cantidad));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar órdenes.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazUsuario interfaz = new InterfazUsuario();
            interfaz.setVisible(true);
        });
    }
}