package features.clases.modelo.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.clases.modelo.Clase;
import features.establecimientos.modelo.Establecimiento;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClaseRepositorio implements Repositorio<Clase> {
    private static final String SELECT_BASE =
            "SELECT cl.id, cl.fecha_hora, cl.titulo, cl.descripcion, " +
                    "cl.comision_id, cl.tema, " +
                    "c.nombre AS comision_nombre, " +
                    "m.id AS materia_id, m.codigo AS materia_codigo, " +
                    "m.nombre AS materia_nombre, " +
                    "e.id AS establecimiento_id, " +
                    "e.nombre AS establecimiento_nombre, " +
                    "e.direccion AS establecimiento_direccion, " +
                    "e.localidad AS establecimiento_localidad " +
                    "FROM clase cl " +
                    "JOIN comision c ON c.id = cl.comision_id " +
                    "JOIN materia m ON m.id = c.materia_id " +
                    "LEFT JOIN establecimiento e ON e.id = c.establecimiento_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY cl.fecha_hora";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE cl.id = ?";

    private static final String INSERT =
            "INSERT INTO clase (fecha_hora, titulo, descripcion, comision_id, tema) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE clase SET fecha_hora = ?, titulo = ?, descripcion = ?, " +
                    "comision_id = ?, tema = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM clase WHERE id = ?";


    private Clase mapear(ResultSet rs) throws SQLException {
        int establecimientoId = rs.getInt("establecimiento_id");
        Establecimiento establecimiento = null;
        if (!rs.wasNull()) {
            establecimiento = new Establecimiento(
                    establecimientoId,
                    rs.getString("establecimiento_nombre"),
                    rs.getString("establecimiento_direccion"),
                    rs.getString("establecimiento_localidad")
            );
        }

        Materia materia = new Materia(
                rs.getInt("materia_id"),
                rs.getString("materia_codigo"),
                rs.getString("materia_nombre")
        );

        Comision comision = new Comision(
                rs.getInt("comision_id"),
                rs.getString("comision_nombre"),
                materia,
                establecimiento
        );

        return new Clase(
                rs.getInt("id"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                comision,
                rs.getString("tema"),
                null,
                null
        );
    }

    private void validarClase(Clase entidad) throws PersistenciaException {
        if (entidad.getComision() == null) {
            throw new PersistenciaException("La clase debe tener una comisión");
        }
        if (entidad.getComision().esNueva()) {
            throw new PersistenciaException(
                    "La comisión debe estar guardada antes de asociarla a la clase");
        }
        if (entidad.getFechaHora() == null) {
            throw new PersistenciaException("La fecha y hora de la clase son obligatorias");
        }
        if (entidad.getTitulo() == null || entidad.getTitulo().isBlank()) {
            throw new PersistenciaException("El título de la clase es obligatorio");
        }
    }

    /// ------------------------ METODOS OVERRIDE -----------------------------///
    /// ------------------------ METODOS OVERRIDE -----------------------------///
    /// ------------------------ METODOS OVERRIDE -----------------------------///

    @Override
    public void guardar(Clase entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("La clase no puede ser nula");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("La clase ya tiene id; use actualizar()");
        }
        validarClase(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(2, entidad.getTitulo());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getComision().getId());
            ps.setString(5, entidad.getTema());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar la clase", e);
        }
    }

    @Override
    public List<Clase> listar() throws PersistenciaException {
        List<Clase> clases = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clases.add(mapear(rs));
            }
            return clases;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar las clases", e);
        }
    }

    @Override
    public Clase buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe una clase con id " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar la clase con id " + id, e);
        }
    }

    @Override
    public void actualizar(Clase entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("La clase a actualizar debe tener un id válido");
        }
        validarClase(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setTimestamp(1, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(2, entidad.getTitulo());
            ps.setString(3, entidad.getDescripcion());
            ps.setInt(4, entidad.getComision().getId());
            ps.setString(5, entidad.getTema());
            ps.setInt(6, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una clase con id " + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar la clase con id " + entidad.getId(), e);
        }
    }


    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una clase con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar la clase con id " + id, e);
        }
    }
}
