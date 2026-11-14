package features.clases.modelo.persistencia;

import comun.enums.EstadoAsistencia;
import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import features.alumnos.modelo.Alumno;
import features.clases.modelo.Asistencia;
import features.clases.modelo.Clase;
import features.establecimientos.modelo.Establecimiento;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaRepositorio {

    private static final String SELECT_BASE =
            "SELECT a.clase_id, a.alumno_id, a.estado, a.observacion, " +
                    "al.nombre AS alumno_nombre, al.apellido AS alumno_apellido, " +
                    "al.dni AS alumno_dni, al.email AS alumno_email, " +
                    "al.telefono AS alumno_telefono, al.legajo AS alumno_legajo, " +
                    "cl.fecha_hora AS clase_fecha_hora, cl.titulo AS clase_titulo, " +
                    "cl.descripcion AS clase_descripcion, cl.tema AS clase_tema, " +
                    "cl.comision_id AS clase_comision_id, " +
                    "c.nombre AS comision_nombre, " +
                    "m.id AS materia_id, m.codigo AS materia_codigo, " +
                    "m.nombre AS materia_nombre, " +
                    "e.id AS establecimiento_id, " +
                    "e.nombre AS establecimiento_nombre, " +
                    "e.direccion AS establecimiento_direccion, " +
                    "e.localidad AS establecimiento_localidad " +
                    "FROM asistencia a " +
                    "JOIN alumno al ON al.id = a.alumno_id " +
                    "JOIN clase cl ON cl.id = a.clase_id " +
                    "JOIN comision c ON c.id = cl.comision_id " +
                    "JOIN materia m ON m.id = c.materia_id " +
                    "LEFT JOIN establecimiento e ON e.id = c.establecimiento_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY cl.fecha_hora, al.apellido, al.nombre";

    private static final String SELECT_POR_CLAVES =
            SELECT_BASE + " WHERE a.clase_id = ? AND a.alumno_id = ?";

    private static final String SELECT_POR_CLASE =
            SELECT_BASE + " WHERE a.clase_id = ? ORDER BY al.apellido, al.nombre";

    private static final String SELECT_POR_ALUMNO =
            SELECT_BASE + " WHERE a.alumno_id = ? ORDER BY cl.fecha_hora";

    private static final String INSERT =
            "INSERT INTO asistencia (clase_id, alumno_id, estado, observacion) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE asistencia SET estado = ?, observacion = ? " +
                    "WHERE clase_id = ? AND alumno_id = ?";

    private static final String DELETE =
            "DELETE FROM asistencia WHERE clase_id = ? AND alumno_id = ?";

    public void guardar(Asistencia asistencia) throws PersistenciaException {
        validarAsistencia(asistencia);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setInt(1, asistencia.getClase().getId());
            ps.setInt(2, asistencia.getAlumno().getId());
            ps.setString(3, asistencia.getEstado().name());
            ps.setString(4, asistencia.getObservacion());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al guardar la asistencia del alumno "
                            + asistencia.getAlumno().getId()
                            + " en la clase " + asistencia.getClase().getId(), e);
        }
    }

    public List<Asistencia> listar() throws PersistenciaException {
        List<Asistencia> asistencias = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                asistencias.add(mapear(rs));
            }
            return asistencias;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar las asistencias", e);
        }
    }

    public List<Asistencia> listarPorClase(int claseId) throws PersistenciaException {
        List<Asistencia> asistencias = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CLASE)) {

            ps.setInt(1, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asistencias.add(mapear(rs));
                }
            }
            return asistencias;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar las asistencias de la clase " + claseId, e);
        }
    }

    public List<Asistencia> listarPorAlumno(int alumnoId) throws PersistenciaException {
        List<Asistencia> asistencias = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ALUMNO)) {

            ps.setInt(1, alumnoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    asistencias.add(mapear(rs));
                }
            }
            return asistencias;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar las asistencias del alumno " + alumnoId, e);
        }
    }

    public Asistencia buscarPorClaves(int claseId, int alumnoId)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CLAVES)) {

            ps.setInt(1, claseId);
            ps.setInt(2, alumnoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException(
                    "No existe asistencia para el alumno " + alumnoId
                            + " en la clase " + claseId);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al buscar la asistencia del alumno " + alumnoId
                            + " en la clase " + claseId, e);
        }
    }

    public void actualizar(Asistencia asistencia) throws PersistenciaException {
        validarAsistencia(asistencia);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, asistencia.getEstado().name());
            ps.setString(2, asistencia.getObservacion());
            ps.setInt(3, asistencia.getClase().getId());
            ps.setInt(4, asistencia.getAlumno().getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException(
                        "No existe asistencia para el alumno " + asistencia.getAlumno().getId()
                                + " en la clase " + asistencia.getClase().getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar la asistencia del alumno "
                            + asistencia.getAlumno().getId()
                            + " en la clase " + asistencia.getClase().getId(), e);
        }
    }

    public void eliminar(int claseId, int alumnoId) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, claseId);
            ps.setInt(2, alumnoId);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException(
                        "No existe asistencia para el alumno " + alumnoId
                                + " en la clase " + claseId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al eliminar la asistencia del alumno " + alumnoId
                            + " en la clase " + claseId, e);
        }
    }

    private Asistencia mapear(ResultSet rs) throws SQLException {
        Alumno alumno = new Alumno(
                rs.getInt("alumno_id"),
                rs.getString("alumno_nombre"),
                rs.getString("alumno_apellido"),
                rs.getString("alumno_dni"),
                rs.getString("alumno_email"),
                rs.getString("alumno_telefono"),
                rs.getString("alumno_legajo")
        );

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
                rs.getInt("clase_comision_id"),
                rs.getString("comision_nombre"),
                materia,
                establecimiento
        );

        Clase clase = new Clase(
                rs.getInt("clase_id"),
                rs.getTimestamp("clase_fecha_hora").toLocalDateTime(),
                rs.getString("clase_titulo"),
                rs.getString("clase_descripcion"),
                comision,
                rs.getString("clase_tema"),
                null,
                null
        );

        return new Asistencia(
                alumno,
                clase,
                EstadoAsistencia.valueOf(rs.getString("estado")),
                rs.getString("observacion")
        );
    }

    private void validarAsistencia(Asistencia asistencia) throws PersistenciaException {
        if (asistencia == null) {
            throw new PersistenciaException("La asistencia no puede ser nula");
        }
        if (asistencia.getClase() == null || asistencia.getClase().esNueva()) {
            throw new PersistenciaException("La clase debe estar guardada");
        }
        if (asistencia.getAlumno() == null || asistencia.getAlumno().esNueva()) {
            throw new PersistenciaException("El alumno debe estar guardado");
        }
        if (asistencia.getEstado() == null) {
            throw new PersistenciaException("El estado de la asistencia es obligatorio");
        }
    }
}
