package persistencia;

import excepcion.EntidadNoEncontradaException;
import excepcion.PersistenciaException;
import modelo.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoRepositorio implements Repositorio<Alumno>{
    private static final String COLUMNAS =
            "id, nombre, apellido, dni, email, telefono, legajo";

    private static final String SELECT_TODOS =
            "SELECT " + COLUMNAS + " FROM alumno ORDER BY apellido, nombre";

    private static final String SELECT_POR_ID =
            "SELECT " + COLUMNAS + " FROM alumno WHERE id = ?";

    private static final String INSERT =
            "INSERT INTO alumno (nombre, apellido, dni, email, telefono, legajo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE alumno SET nombre = ?, apellido = ?, email = ?, " +
                    "telefono = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM alumno WHERE id = ?";

    @Override
    public void guardar(Alumno entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("El alumno no puede ser nulo");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("El alumno ya tiene id; use actualizar()");
        }



        try (   Connection conn = ConnectDB.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getDni());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getLegajo());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el alumno", e);
        }

    }

    @Override
    public List<Alumno> listar() throws PersistenciaException {
        List<Alumno> alumnos = new ArrayList<>();

        try (
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                alumnos.add(mapear(rs));
            }
            return alumnos;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los alumnos", e);
        }
    }


    @Override
    public Alumno buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {


        try (
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un alumno con id " + id);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el alumno con id " + id, e);
        }
    }

    @Override
    public void actualizar(Alumno entidad) throws PersistenciaException {

            /// DNI Y LEGAJO NO SE PUEDEN ACTUALIZAR -------------------------------------------

        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("El alumno a actualizar debe tener un id válido");
        }



        try (
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getTelefono());
            ps.setInt(5, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un alumno con id " + entidad.getId());
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar el alumno con id " + entidad.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {


        try (
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un alumno con id " + id);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el alumno con id " + id, e);
        }
    }

    private Alumno mapear(ResultSet rs) throws SQLException {
        return new Alumno(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("legajo")
        );
    }
}

