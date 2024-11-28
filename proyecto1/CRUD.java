package proyecto1;

import java.util.List;

public interface CRUD<T> {
    void agregar(T t) throws IllegalArgumentException;
    void actualizar(int id, T t) throws Exception;
    void eliminar(int id) throws Exception;
    T obtenerPorId(int id) throws Exception;
    boolean verificarId(int id) throws Exception;
    List<T> obtenerTodos();
}

