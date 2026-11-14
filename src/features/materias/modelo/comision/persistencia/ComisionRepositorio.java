package features.materias.modelo.comision.persistencia;

import comun.excepcion.EntidadNoEncontradaException;
import comun.excepcion.PersistenciaException;
import comun.persistencia.ConnectDB;
import comun.persistencia.Repositorio;
import features.alumnos.modelo.Alumno;
import features.ayudantes.modelo.Ayudante;
import features.establecimientos.modelo.Establecimiento;
import features.materias.modelo.comision.model.Comision;
import features.materias.modelo.materia.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComisionRepositorio implements Repositorio<Comision> {
    private static final String SELECT_BASE =
        "SELECT c.id, c.nombre, c.materia_id, c.establecimiento_id, " +
                "m.codigo AS materia_codigo, m.nombre AS materia_nombre, " +
                "e.nombre AS establecimiento_nombre, " +
                "e.direccion AS establecimiento_direccion, " +
                "e.localidad AS establecimiento_localidad " +
                "FROM comision c " +
                "JOIN materia m ON m.id = c.materia_id " +
                "LEFT JOIN establecimiento e ON e.id = c.establecimiento_id";

    private static final String SELECT_TODOS =
            SELECT_BASE + " ORDER BY c.nombre";

    private static final String SELECT_POR_ID =
            SELECT_BASE + " WHERE c.id = ?";

    private static final String INSERT =
            "INSERT INTO comision (nombre, materia_id, establecimiento_id) " +
                    "VALUES (?, ?, ?)";

    private static final String UPDATE =
            "UPDATE comision SET nombre = ?, materia_id = ?, establecimiento_id = ? " +
                    "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM comision WHERE id = ?";

    private static final String INSERT_ALUMNO =
            "INSERT INTO comision_alumno (comision_id, alumno_id) VALUES (?, ?)";

    private static final String DELETE_ALUMNO =
            "DELETE FROM comision_alumno WHERE comision_id = ? AND alumno_id = ?";

    private static final String SELECT_ALUMNOS =
            "SELECT a.id, a.nombre, a.apellido, a.dni, a.email, a.telefono, a.legajo " +
                    "FROM alumno a " +
                    "JOIN comision_alumno ca ON ca.alumno_id = a.id " +
                    "WHERE ca.comision_id = ? " +
                    "ORDER BY a.apellido, a.nombre";

    private static final String INSERT_AYUDANTE =
            "INSERT INTO comision_ayudante (comision_id, ayudante_id) VALUES (?, ?)";

    private static final String DELETE_AYUDANTE =
            "DELETE FROM comision_ayudante WHERE comision_id = ? AND ayudante_id = ?";

    private static final String SELECT_AYUDANTES =
            "SELECT a.id, a.nombre, a.apellido, a.dni, a.email, a.telefono, a.area, " +
                    "a.alumno_vinculado_id, " +
                    "al.id AS alumno_id, al.nombre AS alumno_nombre, " +
                    "al.apellido AS alumno_apellido, al.dni AS alumno_dni, " +
                    "al.email AS alumno_email, al.telefono AS alumno_telefono, " +
                    "al.legajo AS alumno_legajo " +
                    "FROM ayudante a " +
                    "JOIN comision_ayudante ca ON ca.ayudante_id = a.id " +
                    "LEFT JOIN alumno al ON al.id = a.alumno_vinculado_id " +
                    "WHERE ca.comision_id = ? " +
                    "ORDER BY a.apellido, a.nombre";

    private Comision mapear(ResultSet rs) throws SQLException {
        Materia materia = new Materia(
                rs.getInt("materia_id"),
                rs.getString("materia_codigo"),
                rs.getString("materia_nombre")
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
        return new Comision(
                rs.getInt("id"),
                rs.getString("nombre"),
                materia,
                establecimiento
        );
    }

    private Alumno mapearAlumno(ResultSet rs) throws SQLException {
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

    private Ayudante mapearAyudante(ResultSet rs) throws SQLException {
        Ayudante ayudante = new Ayudante(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("area")
        );

        int alumnoVinculadoId = rs.getInt("alumno_vinculado_id");
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

        return ayudante;
    }

    private void validarMateria(Comision entidad) throws PersistenciaException {
        if (entidad.getMateria() == null) {
            throw new PersistenciaException("La comisión debe tener una materia");
        }
        if (entidad.getMateria().esNueva()) {
            throw new PersistenciaException(
                    "La materia debe estar guardada antes de asociarla a la comisión");
        }
    }

    private void validarEstablecimiento(Comision entidad) throws PersistenciaException {
        if (entidad.getEstablecimiento() != null && entidad.getEstablecimiento().esNueva()) {
            throw new PersistenciaException(
                    "El establecimiento debe estar guardado antes de asociarlo a la comisión");
        }
    }
    
    private void validarInscripcion(int comisionId, Alumno alumno) throws PersistenciaException {
        if (comisionId <= 0) {
            throw new PersistenciaException("El id de la comisión debe ser válido");
        }
        if (alumno == null || alumno.esNueva()) {
            throw new PersistenciaException("El alumno debe estar guardado");
        }
    }

    private void validarInscripcionAyudante(int comisionId, Ayudante ayudante)
            throws PersistenciaException {

        if (comisionId <= 0) {
            throw new PersistenciaException("El id de la comisión debe ser válido");
        }
        if (ayudante == null || ayudante.esNueva()) {
            throw new PersistenciaException("El ayudante debe estar guardado");
        }
    }


    private void cargarEstablecimiento(PreparedStatement ps, int indice, Comision entidad)
            throws SQLException {

        if (entidad.getEstablecimiento() == null) {
            ps.setNull(indice, Types.INTEGER);
        } else {
            ps.setInt(indice, entidad.getEstablecimiento().getId());
        }
    }

    public void inscribirAlumno(int comisionId, Alumno alumno) throws PersistenciaException {
        validarInscripcion(comisionId, alumno);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ALUMNO)) {

            ps.setInt(1, comisionId);
            ps.setInt(2, alumno.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al inscribir el alumno en la comisión", e);
        }
    }

    public void quitarAlumno(int comisionId, Alumno alumno) throws PersistenciaException {
        validarInscripcion(comisionId, alumno);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ALUMNO)) {

            ps.setInt(1, comisionId);
            ps.setInt(2, alumno.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException(
                        "El alumno con id " + alumno.getId() + " no estaba inscripto en la comisión " + comisionId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al quitar el alumno de la comisión", e);
        }
    }

    public List<Alumno> listarAlumnos(int comisionId) throws PersistenciaException {
        List<Alumno> alumnos = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALUMNOS)) {

            ps.setInt(1, comisionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alumnos.add(mapearAlumno(rs));
                }
            }
            return alumnos;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar los alumnos de la comisión " + comisionId, e);
        }
    }

    public void inscribirAyudante(int comisionId, Ayudante ayudante) throws PersistenciaException {
        validarInscripcionAyudante(comisionId, ayudante);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_AYUDANTE)) {

            ps.setInt(1, comisionId);
            ps.setInt(2, ayudante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al inscribir el ayudante en la comisión", e);
        }
    }

    public void quitarAyudante(int comisionId, Ayudante ayudante) throws PersistenciaException {
        validarInscripcionAyudante(comisionId, ayudante);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_AYUDANTE)) {

            ps.setInt(1, comisionId);
            ps.setInt(2, ayudante.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException(
                        "El ayudante con id " + ayudante.getId() + " no estaba inscripto en la comisión " + comisionId);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al quitar el ayudante de la comisión", e);
        }
    }

    public List<Ayudante> listarAyudantes(int comisionId) throws PersistenciaException {
        List<Ayudante> ayudantes = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_AYUDANTES)) {

            ps.setInt(1, comisionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ayudantes.add(mapearAyudante(rs));
                }
            }
            return ayudantes;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar los ayudantes de la comisión " + comisionId, e);
        }
    }


    @Override
    public void guardar(Comision entidad) throws PersistenciaException {
        if(entidad == null) {
            throw new PersistenciaException("La comision no puede ser nula");
        }
        if (!entidad.esNueva()) {
            throw new PersistenciaException("La comision ya tiene id, use actualizar()");
        }

        validarMateria(entidad);
        validarEstablecimiento(entidad);

        try (Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,entidad.getNombre());
            ps.setInt(2,entidad.getMateria().getId());
            cargarEstablecimiento(ps,3,entidad);
            ps.executeUpdate();

            try(ResultSet claves = ps.getGeneratedKeys()) {
                if(claves.next()) {
                    entidad.setId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar la comision " , e);
        }

    }

    @Override
    public List<Comision> listar() throws PersistenciaException {
        List<Comision> comisiones = new ArrayList<>();

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                comisiones.add(mapear(rs));
            }
            return comisiones;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar las comisiones", e);
        }
    }



    @Override
    public Comision buscarPorId(int id)
            throws PersistenciaException, EntidadNoEncontradaException {

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new EntidadNoEncontradaException("No existe una comisión con id " + id);
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar la comisión con id " + id, e);
        }
    }




    @Override
    public void actualizar(Comision entidad) throws PersistenciaException {
        if (entidad == null || entidad.esNueva()) {
            throw new PersistenciaException("La comisión a actualizar debe tener un id válido");
        }
        validarMateria(entidad);
        validarEstablecimiento(entidad);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, entidad.getNombre());
            ps.setInt(2, entidad.getMateria().getId());
            cargarEstablecimiento(ps, 3, entidad);
            ps.setInt(4, entidad.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una comisión con id " + entidad.getId());
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al actualizar la comisión con id " + entidad.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No existe una comisión con id " + id);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar la comisión con id " + id, e);
        }
    }



}
