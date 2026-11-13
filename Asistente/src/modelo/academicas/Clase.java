package modelo.academicas;

import modelo.Alumno;
import modelo.Entidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clase extends EventoAgenda {
    private String tema; /// Tema de la clase, ejemplo : En la clase de dio JDBC, repaso parcial 2.
    private Map<Alumno, Asistencia> asistencias; /// hacer asistencia
    private List<Material> materiales; /// materiales necesarios para la clase ¿?
    /** Hereda de eventoAgenda
     private LocalDateTime fechaHora;
     private String titulo;
     private String descripcion;
     private Comision comision;
     */

    public Clase() {
        this.asistencias = new HashMap<>();
        this.materiales = new ArrayList<>();
    }

    public Clase(int id, LocalDateTime fechaHora, String titulo, String descripcion, Comision comision, String tema, Map<Alumno, Asistencia> asistencias, List<Material> materiales) {
        super(id, fechaHora, titulo, descripcion, comision);
        this.tema = tema;
        this.asistencias = new HashMap<>();
        this.materiales = new ArrayList<>();
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Map<Alumno, Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Map<Alumno, Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    public void agregarMaterial()
    {
        /// COMPLETAR
        /// COMPLETAR
        /// COMPLETAR
    }

    @Override
    public String toString() {
        return "Clase{" +
                "tema='" + tema + '\'' +
                ", asistencias=" + asistencias +
                ", materiales=" + materiales +
                '}';
    }
}
