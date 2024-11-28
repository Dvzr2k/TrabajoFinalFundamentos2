package proyecto1;

import java.util.Optional;

public class Almacen extends Basealmacen<Producto> {

    @Override
    public Producto obtenerPorId(int id) throws Exception {
        return items.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new Exception("Producto no encontrado"));
    }

    @Override
    public boolean verificarId(int id) throws Exception {
        return items.stream().anyMatch(p -> p.getId() == id);
    }

    @Override
    public void actualizar(int id, Producto nuevoProducto) throws Exception {
        Producto producto = obtenerPorId(id);
        producto.setNombre(nuevoProducto.getNombre());
        producto.setPrecio(nuevoProducto.getPrecio());
        producto.setStock(nuevoProducto.getStock());
    }
}
