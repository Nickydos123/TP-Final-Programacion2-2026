package comun.persistencia;

import java.util.List;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.modelo.Entidad;

public interface Repositorio<T extends Entidad> {

    List<T> listar() throws PersistenciaException;

    T buscarPorId(int id) throws PersistenciaException, EntidadNoEncontradaException;

    void guardar(T entidad) throws PersistenciaException;

    void actualizar(T entidad) throws PersistenciaException;

    void eliminar(int id) throws PersistenciaException;
}
