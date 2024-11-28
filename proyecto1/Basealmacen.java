package proyecto1;

import java.util.ArrayList;
import java.util.List;


public abstract class Basealmacen<T> implements CRUD<T> {
    protected List<T> items = new ArrayList<>();

    @Override
    public void agregar(T item) throws IllegalArgumentException {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo.");
        }
        items.add(item);
    }

    @Override
    public List<T> obtenerTodos() {
        return new ArrayList<>(items);
    }

    @Override
    public abstract T obtenerPorId(int id) throws Exception;

    @Override
    public abstract void actualizar(int id, T item) throws Exception;

    @Override
    public void eliminar(int id) throws Exception {
        T item = obtenerPorId(id);
        items.remove(item);

    }
}