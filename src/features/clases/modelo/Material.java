package features.clases.modelo;

import comun.modelo.Entidad;

//// Material necesario para llevar a cabo la clase, fibrones, computadoras, proyector, etc.

public class Material extends Entidad {
    private String nombre;
    private String descripcion;
    private boolean llevado;
    private Clase clase;

    public Material() {
    }

    public Material(int id, String nombre, String descripcion, boolean llevado) {
        super(id);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.llevado = llevado;
    }

    public Material(String nombre, String descripcion, boolean llevado, Clase clase) {
        this(0, nombre, descripcion, llevado, clase);
    }

    public Material(int id, String nombre, String descripcion, boolean llevado, Clase clase) {
        super(id);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.llevado = llevado;
        this.clase = clase;
    }

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

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
}
