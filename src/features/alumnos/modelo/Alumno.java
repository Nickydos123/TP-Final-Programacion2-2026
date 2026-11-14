package features.alumnos.modelo;

import comun.modelo.Persona;
import features.materias.modelo.Comision;

import java.util.ArrayList;
import java.util.List;

public class Alumno extends Persona {
    private String legajo;
    private List<Comision> comisiones = new ArrayList<>();

    public Alumno() {
    }

    public Alumno(String nombre, String apellido, String dni, String email, String telefono, String legajo) {
        super(nombre, apellido, dni, email, telefono);
        comisiones = new ArrayList<>();
        this.legajo = legajo;
    }

    public Alumno(int id, String nombre, String apellido, String dni, String email, String telefono, String legajo) {
        super(id, nombre, apellido, dni, email, telefono);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public List<Comision> getComisiones() {
        return comisiones;
    }

    public void agregarComision(Comision comision) {
        if (comision != null && !comisiones.contains(comision)) {
            comisiones.add(comision);
        }
    }

    public void quitarComision(Comision comision) {
        /// Completar
        /// Completar
        /// Completar
        comisiones.remove(comision);
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + ' ' + getApellido() + '\'' +
                ", legajo=" + legajo +
                ", comisiones=" + comisiones +
                '}';
    }
}
