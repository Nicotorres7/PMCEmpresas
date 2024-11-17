package PMCJAVA;

import java.util.HashMap;

public class producto {

    private String nombre;
    private float precio;
    private HashMap<materiaPrima, Integer> materiasPrimas;
    private float costo;

    public producto(String nombre, float precio, HashMap<materiaPrima, Integer> materiasPrimas) {
        this.nombre = nombre;
        this.precio = precio;
        this.materiasPrimas = materiasPrimas;
        this.costo = 0;
        for (int i = 0; i < materiasPrimas.size(); i++) {
            materiaPrima materiaPrima = (materiaPrima) materiasPrimas.keySet().toArray()[i];
            this.costo += materiaPrima.getPrecioGramo() * materiasPrimas.get(materiaPrima);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public HashMap<materiaPrima, Integer> getMateriasPrimas() {
        return materiasPrimas;
    }

    public float getCosto() {
        return costo;
    }

    public void addMateriaPrima(materiaPrima materiaPrima, int cantidad) {
        this.materiasPrimas.put(materiaPrima, cantidad);
        this.costo += materiaPrima.getPrecioGramo();
    }

    @Override
    public String toString() {
        return nombre;  // Mostrar el nombre en los ComboBox
    }
}