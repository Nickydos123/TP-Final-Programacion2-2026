package features.establecimientos.modelo.persistencia;

import comun.enums.DiaSemana;
import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.excepcion.ValidacionException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.establecimientos.modelo.Establecimiento;
import features.establecimientos.modelo.Horario;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioRepositorio implements Repositorio<Horario> {
    /// trae todo con joins: horario -> comision -> materia y LEFT JOIN establecimiento
    ///  porque el establecimiento del horario es opcional. Así evitamos llamar a otros repositorios dentro de una conexión abierta.
    private static final String SELECT_BASE =
            "SELECT h.id, h.dia, h.hora_inicio, h.hora_fin, " +
                    "h.establecimiento_id, h.comision_id, " +
                    "e.nombre AS establecimiento_nombre, " +
                    "e.direccion AS establecimiento_direccion, " +
                    "e.localidad AS establecimiento_localidad, " +
                    "c.nombre AS comision_nombre, " +
                    "m.id AS materia_id, m.codigo AS materia_codigo, " +
                    "m.nombre AS materia_nombre " +
                    "FROM horario h " +
                    "JOIN comision c ON c.id = h.comision_id " +
                    "JOIN materia m ON m.id = c.materia_id " +
                    "LEFT JOIN establecimiento e ON e.id = h.establecimiento_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY h.dia, h.hora_inicio";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE h.id = ?";

    private static final String INSERT =
            "INSERT INTO horario (dia, hora_inicio, hora_fin, establecimiento_id, comision_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE horario SET dia = ?, hora_inicio = ?, hora_fin = ?, " +
                    "establecimiento_id = ?, comision_id = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM horario WHERE id = ?";

    private Horario mapear(ResultSet rs) throws SQLException {
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
                materia
        );

        try {
            return new Horario(
                    rs.getInt("id"),
                    DiaSemana.valueOf(rs.getString("dia")),
                    rs.getTime("hora_inicio").toLocalTime(),
                    rs.getTime("hora_fin").toLocalTime(),
                    establecimiento,
                    comision
            );
        } catch (ValidacionException e) {
            throw new SQLException("Horario inválido en la base de datos", e);
        }
    }

    private void validarHorario(Horario entidad) throws PersistenciaException {
        if (entidad.getDia() == null) {
            throw new PersistenciaException("El día del horario es obligatorio");
        }

        try {
            entidad.validarHorario();
        } catch (ValidacionException e) {
            throw new PersistenciaException("Horario inválido: " + e.getMessage(), e);
        }

        if (entidad.getComision().esNueva()) {
            throw new PersistenciaException(
                    "La comisión debe estar guardada antes de asociarla al horario");
        }
        if (entidad.getEstablecimiento() != null && entidad.getEstablecimiento().esNueva()) {
            throw new PersistenciaException(
                    "El establecimiento debe estar guardado antes de asociarlo al horario");
        }
    }

    private void cargarEstablecimiento(PreparedStatement ps, int indice, Horario entidad)
            throws SQLException {

        if (entidad.getEstablecimiento() == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, entidad.getEstablecimiento().getId());
        }
    }

    @Override
    public void guardar(Horario entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("El horario no puede ser nulo");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("El horario ya tiene id; use actualizar()");
        }
        validarHorario(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getDia().name());
            ps.setTime(2, Time.valueOf(entidad.getHoraInicio()));
            ps.setTime(3, Time.valueOf(entidad.getHoraFin()));
            cargarEstablecimiento(ps, 4, entidad);
            ps.setInt(5, entidad.getComision().getId());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el horario", e);
        }
    }


    @Override
    public List<Horario> listar() throws PersistenciaException {
        List<Horario> horarios = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                horarios.add(mapear(rs));
            }
            return horarios;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los horarios", e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un horario con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el horario con id " + id, e);
        }
    }

    @Override
    public Horario buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un horario con id " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el horario con id " + id, e);
        }
    }

    @Override
    public void actualizar(Horario entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("El horario a actualizar debe tener un id válido");
        }
        validarHorario(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, entidad.getDia().name());
            ps.setTime(2, Time.valueOf(entidad.getHoraInicio()));
            ps.setTime(3, Time.valueOf(entidad.getHoraFin()));
            cargarEstablecimiento(ps, 4, entidad);
            ps.setInt(5, entidad.getComision().getId());
            ps.setInt(6, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un horario con id " + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar el horario con id " + entidad.getId(), e);
        }
    }


}
