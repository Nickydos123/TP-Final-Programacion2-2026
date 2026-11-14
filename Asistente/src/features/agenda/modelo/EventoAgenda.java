package features.agenda.modelo;

import comun.modelo.Entidad;
import features.materias.modelo.Comision;

import java.time.LocalDateTime;

public abstract class EventoAgenda extends Entidad {
    private LocalDateTime fechaHora;
    private String titulo;
    private String descripcion;
    private Comision comision;

    public EventoAgenda() {
    }

    /// Constructor para enviar a clases hijas

    public EventoAgenda(int id, LocalDateTime fechaHora, String titulo, String descripcion, Comision comision) {
        super(id);
        this.fechaHora = fechaHora;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comision = comision;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }
}
