package PMCJAVA;

public class materiaPrima {

    private String nombre;
    private float precioGramo;

    public materiaPrima(String nombre, float precioGramo) {
        this.nombre = nombre;
        this.precioGramo = precioGramo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecioGramo() {
        return precioGramo;
    }

    @Override
    public String toString() {
        return nombre;  // Mostrar el nombre en los ComboBox
    }
}

