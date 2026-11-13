package modelo.academicas;

import modelo.Entidad;

//// Material necesario para llevar a cabo la clase, fibrones, computadoras, proyector, etc.

public class Material extends Entidad {
    private String nombre;
    private String descripcion;
    private boolean llevado;



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isLlevado() {
        return llevado;
    }

    public void setLlevado(boolean llevado) {
        this.llevado = llevado;
    }
}
