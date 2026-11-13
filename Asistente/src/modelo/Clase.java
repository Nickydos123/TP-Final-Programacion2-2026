package modelo;

import modelo.Enums.EstadoAsistencia;
import modelo.academicas.Comision;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Clase extends Entidad {
    private Comision comision;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private final Map<Integer, Asistencia> asistenciasPorAlumnoId =
            new LinkedHashMap<>();

    public Clase(Comision comision, LocalDate fecha,
                 LocalTime horaInicio, LocalTime horaFin) {
        if (comision == null || fecha == null || horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("Faltan datos obligatorios de la clase");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la de fin");
        }

        this.comision = comision;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public void cargarListaDeAlumnos() {
        for (Alumno alumno : comision.getAlumnos()) {
            if (alumno.getId() <= 0) {
                throw new IllegalStateException(
                        "Hay que guardar los alumnos antes de cargarlos en la clase");
            }
            asistenciasPorAlumnoId.putIfAbsent(
                    alumno.getId(), new Asistencia(alumno));
        }
    }

    public void registrarAsistencia(Alumno alumno, EstadoAsistencia estado) {
        if (alumno == null || estado == null) {
            throw new IllegalArgumentException("El alumno y el estado son obligatorios");
        }

        Asistencia asistencia = asistenciasPorAlumnoId.get(alumno.getId());
        if (asistencia == null) {
            throw new IllegalArgumentException("El alumno no pertenece a la lista de esta clase");
        }

        asistencia.cambiarEstado(estado);
    }

    public Map<Integer, Asistencia> getAsistenciasPorAlumnoId() {
        return Collections.unmodifiableMap(asistenciasPorAlumnoId);
    }
}