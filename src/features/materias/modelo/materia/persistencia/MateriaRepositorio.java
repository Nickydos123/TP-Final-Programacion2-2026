package features.materias.modelo.materia.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;

import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.materias.modelo.materia.model.Materia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaRepositorio implements Repositorio<Materia> {
    private static final String COLUMNAS =
            "id,codigo,nombre ";

    private static final String SELECT_TODOS =
            "SELECT " + COLUMNAS + "FROM materia ORDER BY nombre";

    private static final String SELECT_POR_ID =
            "SELECT " + COLUMNAS + "FROM materia WHERE ID = ?";

    private static final String INSERT =
            "INSERT INTO materia (codigo,nombre) VALUES (? , ?)";

    private static final String UPDATE =
            "UPDATE materia SET codigo = ?, nombre = ? WHERE id = ?";


    private static final String DELETE =
            "DELETE FROM materia WHERE id = ? ";

    @Override
    public void guardar(Materia entidad) throws PersistenciaException {
        if (entidad == null)
        {
            throw new PersistenciaException("La materia no puede ser nula");
        }
        if(!entidad.esNueva())
        {
            throw new PersistenciaException("La materia ya tiene id, use actualizar()");
        }

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,entidad.getCodigo());
            ps.setString(2,entidad.getNombre());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if(claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        }catch (SQLException e)
            {
                throw new PersistenciaException("Error al guardar materia",e);
            }

    }

    @Override
    public List<Materia> listar() throws PersistenciaException {
        List<Materia> materias = new ArrayList<>();

        try(Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                materias.add(mapear(rs));
            }
            return materias;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar materias", e);
        }
    }

    @Override
    public Materia buscarPorId(int id) throws PersistenciaException, EntidadNoEncontradaException {
        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }
            throw new EntidadNoEncontradaException("No existe la materia con el id" + id);
        } catch (SQLException e)
        {
            throw new PersistenciaException("Error al buscar la materia con id " + id , e);
        }
    }


    @Override
    public void actualizar(Materia entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("La materia a actualizar debe tener un id valido");
        }

        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1,entidad.getCodigo());
            ps.setString(2,entidad.getNombre());
            ps.setInt(3,entidad.getId());

            if(ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una materia con id " + entidad.getId());
            }
        } catch (SQLException e)
        {
            throw new PersistenciaException("Error al actualizar la materia con id " + entidad.getId(),e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1,id);
            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una materia con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar la materia con id " + id, e);
        }
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        return new Materia(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre")
        );
    }


}
