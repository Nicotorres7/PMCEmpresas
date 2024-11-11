package PMCJAVA;

import java.util.List;

public class producto {

    private String nombre;
    private float precio;
    private List<materiaPrima> materiasPrimas;
    private float costo;

    public producto(String nombre, float precio, List<materiaPrima> materiasPrimas) {
        this.nombre = nombre;
        this.precio = precio;
        this.materiasPrimas = materiasPrimas;
        this.costo = 0;
        for (materiaPrima materiaPrima : materiasPrimas) {
            this.costo += materiaPrima.getPrecioGramo();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public List<materiaPrima> getMateriasPrimas() {
        return materiasPrimas;
    }

    public float getCosto() {
        return costo;
    }

    public void addMateriaPrima(materiaPrima materiaPrima) {
        this.materiasPrimas.add(materiaPrima);
        this.costo += materiaPrima.getPrecioGramo();
    }

    @Override
    public String toString() {
        return nombre;  // Mostrar el nombre en los ComboBox
    }
}

