package modelo.academicas;

import modelo.Enums.EstadoAsistencia;

public class Asistencia {
    private EstadoAsistencia estado;
    private String observacion;

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsistencia estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
