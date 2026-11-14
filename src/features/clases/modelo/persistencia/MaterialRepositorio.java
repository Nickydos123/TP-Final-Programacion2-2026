package features.clases.modelo.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.clases.modelo.Clase;
import features.clases.modelo.Material;
import features.establecimientos.modelo.Establecimiento;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialRepositorio implements Repositorio<Material> {
    private static final String SELECT_BASE =
            "SELECT ma.id, ma.nombre, ma.descripcion, ma.llevado, ma.clase_id, " +
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
                    "FROM material ma " +
                    "LEFT JOIN clase cl ON cl.id = ma.clase_id " +
                    "LEFT JOIN comision c ON c.id = cl.comision_id " +
                    "LEFT JOIN materia m ON m.id = c.materia_id " +
                    "LEFT JOIN establecimiento e ON e.id = c.establecimiento_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY ma.nombre";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE ma.id = ?";

    private static final String SELECT_POR_CLASE =
            SELECT_BASE + " WHERE ma.clase_id = ? ORDER BY ma.nombre";

    private static final String INSERT =
            "INSERT INTO material (nombre, descripcion, llevado, clase_id) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE material SET nombre = ?, descripcion = ?, llevado = ?, " +
                    "clase_id = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM material WHERE id = ?";

    public List<Material> listarPorClase(int claseId) throws PersistenciaException {
        List<Material> materiales = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CLASE)) {

            ps.setInt(1, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materiales.add(mapear(rs));
                }
            }
            return materiales;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar los materiales de la clase " + claseId, e);
        }
    }

    private void validarMaterial(Material entidad) throws PersistenciaException {
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new PersistenciaException("El nombre del material es obligatorio");
        }
        if (entidad.getClase() != null && entidad.getClase().esNueva()) {
            throw new PersistenciaException(
                    "La clase debe estar guardada antes de asociarla al material");
        }
    }

    private void cargarClase(PreparedStatement ps, int indice, Material entidad)
            throws SQLException {

        if (entidad.getClase() == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, entidad.getClase().getId());
        }
    }
    private Material mapear(ResultSet rs) throws SQLException {
        Clase clase = null;

        int claseId = rs.getInt("clase_id");
        if (!rs.wasNull()) {
            int comisionId = rs.getInt("clase_comision_id");
            Comision comision = null;

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

            clase = new Clase(
                    claseId,
                    rs.getTimestamp("clase_fecha_hora").toLocalDateTime(),
                    rs.getString("clase_titulo"),
                    rs.getString("clase_descripcion"),
                    comision,
                    rs.getString("clase_tema"),
                    null,
                    null
            );
        }

        return new Material(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("llevado"),
                clase
        );
    }

    @Override
    public void guardar(Material entidad) throws PersistenciaException {
        if (entidad == null) {
            throw new PersistenciaException("El material no puede ser nulo");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("El material ya tiene id; use actualizar()");
        }
        validarMaterial(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setBoolean(3, entidad.isLlevado());
            cargarClase(ps, 4, entidad);

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el material", e);
        }
    }

    @Override
    public List<Material> listar() throws PersistenciaException {
        List<Material> materiales = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                materiales.add(mapear(rs));
            }
            return materiales;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar los materiales", e);
        }
    }


    @Override
    public Material buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe un material con id " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el material con id " + id, e);
        }
    }

    @Override
    public void actualizar(Material entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("El material a actualizar debe tener un id válido");
        }
        validarMaterial(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setBoolean(3, entidad.isLlevado());
            cargarClase(ps, 4, entidad);
            ps.setInt(5, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un material con id " + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar el material con id " + entidad.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe un material con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el material con id " + id, e);
        }
    }


}

