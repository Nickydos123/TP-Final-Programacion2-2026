package features.recordatorio.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.modelo.Entidad;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;import features.clases.modelo.Clase;
import features.clases.modelo.Material;
import features.establecimientos.modelo.Establecimiento;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;
import features.recordatorio.Recordatorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioRepositorio implements Repositorio<Recordatorio> {

    private static final String SELECT_BASE =
            "SELECT r.id, r.fecha_hora, r.titulo, r.descripcion, r.realizado, " +
                    "r.comision_id, r.clase_id, r.material_id, " +
                    "c.nombre AS comision_nombre, " +
                    "m.id AS materia_id, m.codigo AS materia_codigo, " +
                    "m.nombre AS materia_nombre, " +
                    "e.id AS establecimiento_id, " +
                    "e.nombre AS establecimiento_nombre, " +
                    "e.direccion AS establecimiento_direccion, " +
                    "e.localidad AS establecimiento_localidad, " +
                    "cl.fecha_hora AS clase_fecha_hora, " +
                    "cl.titulo AS clase_titulo, " +
                    "cl.descripcion AS clase_descripcion, " +
                    "cl.tema AS clase_tema, " +
                    "cl.comision_id AS clase_comision_id, " +
                    "cc.nombre AS clase_comision_nombre, " +
                    "cm.id AS clase_materia_id, " +
                    "cm.codigo AS clase_materia_codigo, " +
                    "cm.nombre AS clase_materia_nombre, " +
                    "ce.id AS clase_establecimiento_id, " +
                    "ce.nombre AS clase_establecimiento_nombre, " +
                    "ce.direccion AS clase_establecimiento_direccion, " +
                    "ce.localidad AS clase_establecimiento_localidad, " +
                    "mat.nombre AS material_nombre, " +
                    "mat.descripcion AS material_descripcion, " +
                    "mat.llevado AS material_llevado " +
                    "FROM recordatorio r " +
                    "LEFT JOIN comision c ON c.id = r.comision_id " +
                    "LEFT JOIN materia m ON m.id = c.materia_id " +
                    "LEFT JOIN establecimiento e ON e.id = c.establecimiento_id " +
                    "LEFT JOIN clase cl ON cl.id = r.clase_id " +
                    "LEFT JOIN comision cc ON cc.id = cl.comision_id " +
                    "LEFT JOIN materia cm ON cm.id = cc.materia_id " +
                    "LEFT JOIN establecimiento ce ON ce.id = cc.establecimiento_id " +
                    "LEFT JOIN material mat ON mat.id = r.material_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY r.fecha_hora";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE r.id = ?";

    private static final String INSERT =
            "INSERT INTO recordatorio " +
                    "(fecha_hora, titulo, descripcion, comision_id, clase_id, material_id, realizado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE recordatorio SET fecha_hora = ?, titulo = ?, descripcion = ?, " +
                    "comision_id = ?, clase_id = ?, material_id = ?, realizado = ? " +
                    "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM recordatorio WHERE id = ?";

    @Override
    public void guardar(Recordatorio entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("El recordatorio no puede ser nulo");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("El recordatorio ya tiene id; use actualizar()");
        }
        validarRecordatorio(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(2, entidad.getTitulo());
            ps.setString(3, entidad.getDescripcion());
            cargarOpcional(ps, 4, entidad.getComision());
            cargarOpcional(ps, 5, entidad.getClase());
            cargarOpcional(ps, 6, entidad.getMaterial());
            ps.setBoolean(7, entidad.isRealizado());

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el recordatorio", e);
        }
    }

    @Override
    public List<Recordatorio> listar() throws PersistenciaException {
        List<Recordatorio> recordatorios = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                recordatorios.add(mapear(rs));
            }
            return recordatorios;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los recordatorios", e);
        }
    }

    @Override
    public Recordatorio buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un recordatorio con id " + id);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al buscar el recordatorio con id " + id, e);
        }
    }

    @Override
    public void actualizar(Recordatorio entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException(
                    "El recordatorio a actualizar debe tener un id válido");
        }
        validarRecordatorio(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setTimestamp(1, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(2, entidad.getTitulo());
            ps.setString(3, entidad.getDescripcion());
            cargarOpcional(ps, 4, entidad.getComision());
            cargarOpcional(ps, 5, entidad.getClase());
            cargarOpcional(ps, 6, entidad.getMaterial());
            ps.setBoolean(7, entidad.isRealizado());
            ps.setInt(8, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException(
                        "No existe un recordatorio con id " + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar el recordatorio con id " + entidad.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un recordatorio con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al eliminar el recordatorio con id " + id, e);
        }
    }

    private Recordatorio mapear(ResultSet rs) throws SQLException {
        Comision comision = null;
        int comisionId = rs.getInt("comision_id");
        if (!rs.wasNull()) {
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

            comision = new Comision(
                    comisionId,
                    rs.getString("comision_nombre"),
                    materia,
                    establecimiento
            );
        }

        Clase clase = null;
        int claseId = rs.getInt("clase_id");
        if (!rs.wasNull()) {
            Comision claseComision = null;
            int claseComisionId = rs.getInt("clase_comision_id");

            if (!rs.wasNull()) {
                int claseEstablecimientoId = rs.getInt("clase_establecimiento_id");
                Establecimiento claseEstablecimiento = null;

                if (!rs.wasNull()) {
                    claseEstablecimiento = new Establecimiento(
                            claseEstablecimientoId,
                            rs.getString("clase_establecimiento_nombre"),
                            rs.getString("clase_establecimiento_direccion"),
                            rs.getString("clase_establecimiento_localidad")
                    );
                }

                Materia claseMateria = new Materia(
                        rs.getInt("clase_materia_id"),
                        rs.getString("clase_materia_codigo"),
                        rs.getString("clase_materia_nombre")
                );

                claseComision = new Comision(
                        claseComisionId,
                        rs.getString("clase_comision_nombre"),
                        claseMateria,
                        claseEstablecimiento
                );
            }

            clase = new Clase(
                    claseId,
                    rs.getTimestamp("clase_fecha_hora").toLocalDateTime(),
                    rs.getString("clase_titulo"),
                    rs.getString("clase_descripcion"),
                    claseComision,
                    rs.getString("clase_tema"),
                    null,
                    null
            );
        }

        Material material = null;
        int materialId = rs.getInt("material_id");
        if (!rs.wasNull()) {
            material = new Material(
                    materialId,
                    rs.getString("material_nombre"),
                    rs.getString("material_descripcion"),
                    rs.getBoolean("material_llevado")
            );
        }

        return new Recordatorio(
                rs.getInt("id"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                comision,
                clase,
                material,
                rs.getBoolean("realizado")
        );
    }

    private void validarRecordatorio(Recordatorio entidad) throws PersistenciaException {
        if (entidad.getFechaHora() == null) {
            throw new PersistenciaException(
                    "La fecha y hora del recordatorio son obligatorias");
        }
        if (entidad.getTitulo() == null || entidad.getTitulo().isBlank()) {
            throw new PersistenciaException("El título del recordatorio es obligatorio");
        }
        if (entidad.getComision() != null && entidad.getComision().esNueva()) {
            throw new PersistenciaException(
                    "La comisión debe estar guardada antes de asociarla al recordatorio");
        }
        if (entidad.getClase() != null && entidad.getClase().esNueva()) {
            throw new PersistenciaException(
                    "La clase debe estar guardada antes de asociarla al recordatorio");
        }
        if (entidad.getMaterial() != null && entidad.getMaterial().esNueva()) {
            throw new PersistenciaException(
                    "El material debe estar guardado antes de asociarlo al recordatorio");
        }
    }

    private void cargarOpcional(PreparedStatement ps, int indice, Entidad entidad)
            throws SQLException {

        if (entidad == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, entidad.getId());
        }
    }
}
