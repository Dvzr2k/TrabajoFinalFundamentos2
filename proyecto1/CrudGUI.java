package proyecto1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class CrudGUI {
    private JFrame frame;
    private JTextField txtId, txtNombre, txtPrecio, txtBuscarId, txtStock;
    private JTable table;
    private DefaultTableModel model;
    private CRUD<Producto> almacen;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar;
    private JPanel actionPanel;
    private TableRowSorter<DefaultTableModel> sorter;

    public CrudGUI(CRUD<Producto> almacen) {
        this.almacen = almacen;
        initializeGUI();
    }

    private void initializeGUI() {
        // Configuración de la ventana principal
        frame = new JFrame("Gestión de Productos");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de entrada de datos con estilo mejorado
        JPanel inputPanel = createInputPanel();
        frame.add(inputPanel, BorderLayout.NORTH);

        // Tabla con capacidades mejoradas
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones con diseño más limpio
        createActionPanel();
        frame.add(actionPanel, BorderLayout.SOUTH);

        // Styling
        applyModernLookAndFeel();

        frame.setLocationRelativeTo(null);  // Centrar en pantalla
        frame.setVisible(true);

        // Cargar productos al inicio
        cargarProductos();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(10);
        txtId.setToolTipText("Ingrese el ID del producto");
        panel.add(txtId, gbc);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(10);
        txtNombre.setToolTipText("Ingrese el nombre del producto");
        panel.add(txtNombre, gbc);

        // Precio
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(10);
        txtPrecio.setToolTipText("Ingrese el precio del producto");
        panel.add(txtPrecio, gbc);

        // Stock
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(10);
        txtStock.setToolTipText("Ingrese el stock del producto");
        panel.add(txtStock, gbc);

        // Buscar por ID
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Buscar por ID:"), gbc);
        gbc.gridx = 1;
        txtBuscarId = new JTextField(10);
        txtBuscarId.setToolTipText("Ingrese ID para buscar");
        panel.add(txtBuscarId, gbc);

        return panel;
    }

    private void createTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Hacer tabla no editable
            }
        };
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Precio");
        model.addColumn("Stock");

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Añadir ordenamiento
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Listener para selección de fila
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
                txtId.setText(model.getValueAt(modelRow, 0).toString());
                txtNombre.setText(model.getValueAt(modelRow, 1).toString());
                txtPrecio.setText(model.getValueAt(modelRow, 2).toString());
                txtStock.setText(model.getValueAt(modelRow, 3).toString());
            }
        });
    }

    private void createActionPanel() {
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Crear botones con iconos
        btnAgregar = createStyledButton("Agregar", "add");
        btnActualizar = createStyledButton("Actualizar", "update");
        btnEliminar = createStyledButton("Eliminar", "delete");
        btnBuscar = createStyledButton("Buscar", "search");
        btnLimpiar = createStyledButton("Limpiar", "clear");

        // Agregar listeners
        btnAgregar.addActionListener(e -> agregarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBuscar.addActionListener(e -> buscarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        actionPanel.add(btnAgregar);
        actionPanel.add(btnActualizar);
        actionPanel.add(btnEliminar);
        actionPanel.add(btnBuscar);
        actionPanel.add(btnLimpiar);
    }

    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }

    private void applyModernLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void buscarProducto() {
        try {
            int id = Integer.parseInt(txtBuscarId.getText());
            Producto producto = almacen.obtenerPorId(id);

            // Limpiar filtro anterior
            sorter.setRowFilter(null);

            // Resaltar fila encontrada
            for (int i = 0; i < model.getRowCount(); i++) {
                if (Integer.parseInt(model.getValueAt(i, 0).toString()) == id) {
                    table.setRowSelectionInterval(table.convertRowIndexToView(i),
                            table.convertRowIndexToView(i));
                    table.scrollRectToVisible(table.getCellRect(table.convertRowIndexToView(i), 0, true));
                    break;
                }
            }

            // Llenar campos
            txtId.setText(String.valueOf(producto.getId()));
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtStock.setText(String.valueOf(producto.getStock()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Producto no encontrado: " + e.getMessage(),
                    "Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void agregarProducto() {
        try {
            validarEntradas();
            int id = Integer.parseInt(txtId.getText());

            if(almacen.verificarId(id)){
                throw new InvalidParameterException("El id ya esta registrado");
            }

            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());

            Producto producto = new Producto(id, nombre, precio, stock);
            almacen.agregar(producto);

            cargarProductos();
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizarProducto() {
        try {
            validarEntradas();
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());

            Producto producto = new Producto(id, nombre, precio, stock);
            almacen.actualizar(id, producto);

            cargarProductos();
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingrese valores numéricos válidos.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "¿Está seguro que desea eliminar este producto?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                almacen.eliminar(id);
                cargarProductos();
                limpiarCampos();
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = almacen.obtenerTodos();
            model.setRowCount(0);

            for (Producto producto : productos) {
                model.addRow(new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getStock()
                });
            }
        } catch (Exception e) {
            mostrarError("Error al cargar los productos: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtBuscarId.setText("");
        txtStock.setText("");
        table.clearSelection();
        sorter.setRowFilter(null);
    }

    private void validarEntradas() throws Exception {
        if (txtId.getText().trim().isEmpty()) {
            throw new Exception("El ID no puede estar vacío");
        }
        if (txtNombre.getText().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío");
        }
        if (txtPrecio.getText().trim().isEmpty()) {
            throw new Exception("El precio no puede estar vacío");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                frame,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
