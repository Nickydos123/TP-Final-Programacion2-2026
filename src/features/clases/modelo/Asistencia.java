package features.clases.modelo;

import comun.enums.EstadoAsistencia;
import features.alumnos.modelo.Alumno;

public class Asistencia {
    private Alumno alumno;
    private Clase clase;
    private EstadoAsistencia estado;
    private String observacion;

    public Asistencia() {
        this.estado = EstadoAsistencia.SIN_REGISTRAR;
    }

    public Asistencia(Alumno alumno, Clase clase) {
        this.alumno = alumno;
        this.clase = clase;
        this.estado = EstadoAsistencia.SIN_REGISTRAR;
        this.observacion = null;
    }

    public Asistencia(Alumno alumno, Clase clase, EstadoAsistencia estado, String observacion) {
        this.alumno = alumno;
        this.clase = clase;
        setEstado(estado);
        this.observacion = observacion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsistencia estado) {
        this.estado = estado != null ? estado : EstadoAsistencia.SIN_REGISTRAR;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
