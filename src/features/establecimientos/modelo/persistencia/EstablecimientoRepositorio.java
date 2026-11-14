package features.establecimientos.modelo.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.establecimientos.modelo.Establecimiento;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstablecimientoRepositorio implements Repositorio<Establecimiento> {
    private static final String COLUMNAS =
            "id, nombre, direccion, localidad";

    private static final String SELECT_TODOS =
            "SELECT " + COLUMNAS + " FROM establecimiento ORDER BY nombre";

    private static final String SELECT_POR_ID =
            "SELECT " + COLUMNAS + " FROM establecimiento WHERE id = ?";

    private static final String INSERT =
            "INSERT INTO establecimiento (nombre, direccion, localidad) VALUES (? , ?, ?) ";

    private static final String UPDATE =
            "UPDATE establecimiento SET nombre = ?, direccion = ? , localidad = ?" +
                    " WHERE id= ?" ;

    private static final String DELETE =
            "DELETE FROM establecimiento WHERE id = ?" ;


    @Override
    public void guardar(Establecimiento entidad) throws PersistenciaException {
        if(entidad == null) {
            throw new PersistenciaException("El establecimiento no puede ser nulo");
        }
        if(!entidad.esNueva()) {
            throw new PersistenciaException("El establecimiento ya tiene id; use actualizar()");
        }

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDireccion());
            ps.setString(3, entidad.getLocalidad());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el establecimiento", e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1,id); /// WHERE ID = ? <<<

            if (ps.executeUpdate() == 0 ) {
                throw new PersistenciaException("No existe un establecimiento con id " + id);
            }
        } catch (SQLException e)
        {
            throw new PersistenciaException("Error al eliminar el establecimiento con id " + id,e);
        }
    }

    @Override
    public void actualizar(Establecimiento entidad) throws PersistenciaException {
        if(entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("El establecimiento a actualizar debe tener id valido");
        }

        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1,entidad.getNombre());
            ps.setString(2,entidad.getDireccion());
            ps.setString(3, entidad.getLocalidad());
            ps.setInt(4, entidad.getId());

            if(ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un establecimiento con id" + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el establecimiento con id " + entidad.getId(),e);
        }

    }

    @Override
    public List<Establecimiento> listar() throws PersistenciaException {
        List<Establecimiento> establecimientos = new ArrayList<>();

        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                establecimientos.add(mapear(rs));
            }
            return establecimientos;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los establecimientos " ,e);
        }
    }

    @Override
    public Establecimiento buscarPorId(int id) throws PersistenciaException, EntidadNoEncontradaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1,id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un establecimiento con id " + id);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el establecimiento con id " + id, e);
        }
    }

    private Establecimiento mapear(ResultSet rs) throws SQLException {
        return new Establecimiento(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("localidad")
        );
    }





}
