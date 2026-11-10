package modelo;

import java.util.ArrayList;
import java.util.List;

public class Alumno extends Persona {
    private Integer legajo;
    private List<Comision> comisiones = new ArrayList<>();

    public Alumno() {
    }

    public Alumno(String nombre, String apellido, String dni, String email, String telefono, Integer legajo) {
        super(nombre, apellido, dni, email, telefono);
        comisiones = new ArrayList<>();
        this.legajo = legajo;
    }

    public Alumno(int id, String nombre, String apellido, String dni, String email, String telefono, Integer legajo) {
        super(id, nombre, apellido, dni, email, telefono);
        this.legajo = legajo;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
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
