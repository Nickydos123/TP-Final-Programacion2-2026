package features.ayudantes.persistencia;

import features.alumnos.modelo.Alumno;
import features.ayudantes.modelo.Ayudante;
import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AyudanteRepositorio implements Repositorio<Ayudante> {
    private static final String SELECT_BASE =
            "SELECT a.id, a.nombre, a.apellido, a.dni, a.email, a.telefono, a.area, " +
                    "a.alumno_vinculado_id, " +
                    "al.id AS alumno_id, al.nombre AS alumno_nombre, " +
                    "al.apellido AS alumno_apellido, al.dni AS alumno_dni, " +
                    "al.email AS alumno_email, al.telefono AS alumno_telefono, " +
                    "al.legajo AS alumno_legajo " +
                    "FROM ayudante a " +
                    "LEFT JOIN alumno al ON al.id = a.alumno_vinculado_id"; /// LEFT JOIN porque el alumno vinculado puede ser null

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY a.apellido, a.nombre";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE a.id = ?";

    private static final String INSERT =
            "INSERT INTO ayudante " +
                    "(nombre, apellido, dni, email, telefono, area, alumno_vinculado_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE ayudante SET nombre = ?, apellido = ?, email = ?, telefono = ?, " +
                    "area = ?, alumno_vinculado_id = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM ayudante WHERE id = ?";

    @Override
    public List<Ayudante> listar() throws PersistenciaException {
        List<Ayudante> ayudantes = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ayudantes.add(mapear(rs));
            }
            return ayudantes;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los ayudantes", e);
        }
    }

    private Ayudante mapear(ResultSet rs) throws SQLException {
        Ayudante ayudante = new Ayudante(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("area")
        );

        if (!rs.wasNull()) {
            int alumnoId = rs.getInt("alumno_vinculado_id");
            if (!rs.wasNull()) {
                Alumno alumno = new Alumno(
                        rs.getInt("alumno_id"),
                        rs.getString("alumno_nombre"),
                        rs.getString("alumno_apellido"),
                        rs.getString("alumno_dni"),
                        rs.getString("alumno_email"),
                        rs.getString("alumno_telefono"),
                        rs.getString("alumno_legajo")
                );
                ayudante.setAlumnoVinculado(alumno);
            }
        }
        return ayudante;
    }


    @Override
    public Ayudante buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un ayudante con id " + id);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el ayudante con id " + id, e);
        }
    }


    @Override
    public void guardar(Ayudante entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("El ayudante no puede ser nulo");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("El ayudante ya tiene id; use actualizar()");
        }
        validarAlumnoVinculado(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getDni());
            ps.setString(4, entidad.getEmail());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getArea());
            cargarAlumnoVinculado(ps, 7, entidad);

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el ayudante", e);
        }
    }

    private void validarAlumnoVinculado(Ayudante entidad) throws PersistenciaException {
        Alumno alumno = entidad.getAlumnoVinculado();
        if (alumno != null && alumno.esNueva()) {
            throw new PersistenciaException(
                    "El alumno vinculado debe estar guardado antes de asociarlo al ayudante");
        }
    }

    private void cargarAlumnoVinculado(PreparedStatement ps, int indice, Ayudante entidad)
            throws SQLException {

        Alumno alumno = entidad.getAlumnoVinculado();
        if (alumno == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, alumno.getId());
        }
    }


    @Override
    public void actualizar(Ayudante entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("El ayudante a actualizar debe tener un id válido");
        }
        validarAlumnoVinculado(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellido());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getTelefono());
            ps.setString(5, entidad.getArea());
            cargarAlumnoVinculado(ps, 6, entidad);
            ps.setInt(7, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un ayudante con id " + entidad.getId());
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar el ayudante con id " + entidad.getId(), e);
        }
    }


    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un ayudante con id " + id);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el ayudante con id " + id, e);
        }
    }

}
