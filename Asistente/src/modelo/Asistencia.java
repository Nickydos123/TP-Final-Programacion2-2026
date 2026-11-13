package modelo;

import modelo.Enums.EstadoAsistencia;

public class Asistencia extends Entidad {
    private Alumno alumno;
    private EstadoAsistencia estado;
    private String observacion;

    public Asistencia(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno es obligatorio");
        }

        this.alumno = alumno;
        this.estado = EstadoAsistencia.SIN_REGISTRAR;
        this.observacion = "";
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void cambiarEstado(EstadoAsistencia estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion == null ? "" : observacion.trim();
    }
}