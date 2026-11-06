package persistencia;

import java.util.List;

import excepcion.EntidadNoEncontradaException;
import excepcion.PersistenciaException;
import modelo.Entidad;

public interface Repositorio<T extends Entidad> {

    List<T> listar() throws PersistenciaException;

    T buscarPorId(int id) throws PersistenciaException, EntidadNoEncontradaException;

    void guardar(T entidad) throws PersistenciaException;

    void actualizar(T entidad) throws PersistenciaException;

    void eliminar(int id) throws PersistenciaException;
}
