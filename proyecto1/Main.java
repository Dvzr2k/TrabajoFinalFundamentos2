package proyecto1;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Crear la instancia de la clase CRUD (Almacen)
        CRUD<Producto> almacen = new Almacen();

        // Agregar productos iniciales al CRUD
        try {
            almacen.agregar(new Producto(1, "Laptop - Dell XPS 13", 1200.0, 5));
            almacen.agregar(new Producto(2, "Laptop Gamer - ASUS ROG Zephyrus G14", 1600.0, 3));
            almacen.agregar(new Producto(3, "Monitor - LG UltraGear 27\" QHD", 400.0, 8));
            almacen.agregar(new Producto(4, "Teclado Mecánico - Logitech G Pro X", 120.0, 12));
            almacen.agregar(new Producto(5, "Mouse Gamer - Razer DeathAdder V3 Pro", 150.0, 10));
            almacen.agregar(new Producto(6, "Disco Duro Externo - Seagate 2TB Portable", 70.0, 20));
            almacen.agregar(new Producto(7, "Unidad SSD - Samsung 970 EVO Plus 1TB", 110.0, 15));
            almacen.agregar(new Producto(8, "Impresora Multifuncional - HP DeskJet 4155e", 120.0, 7));
            almacen.agregar(new Producto(9, "Cámara Web - Logitech C920 HD Pro", 70.0, 10));
            almacen.agregar(new Producto(10, "Auriculares Gamer - HyperX Cloud II Wireless", 120.0, 6));

        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar productos iniciales: " + e.getMessage());
        }

        // Crear la interfaz gráfica con la instancia de CRUD
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CrudGUI(almacen);
            }
        });
    }
}
