package modelo.academicas;

import java.time.LocalDateTime;

public class Recordatorio extends EventoAgenda{
    private Clase clase; /// puede ser null
    private Material material; /// puede ser null
    private boolean realizado;

    public Recordatorio() {
    }

    public Recordatorio(int id, LocalDateTime fechaHora, String titulo, String descripcion, Comision comision, Clase clase, Material material, boolean realizado) {
        super(id, fechaHora, titulo, descripcion, comision);
        this.clase = clase;
        this.material = material;
        this.realizado = realizado;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    @Override
    public String toString() {
        return "Recordatorio{" +
                "clase=" + clase +
                ", material=" + material +
                ", realizado=" + realizado +
                '}';
    }
}
