package PMCJAVA;

import java.util.ArrayList;
import java.util.List;

public class Optimizer {

    public List<orden> optimizarOrdenes(List<orden> ordenes, float presupuesto) {
        List<orden> seleccionadas = new ArrayList<>();
        float totalSeleccionado = 0;

        // Ordenar órdenes por total de menor a mayor
        ordenes.sort((o1, o2) -> Float.compare(o1.getTotal(), o2.getTotal()));

        for (orden ord : ordenes) {
            // Si la suma de los totales no supera el presupuesto, la añadimos
            if (totalSeleccionado + ord.getTotal() <= presupuesto) {
                seleccionadas.add(ord);
                totalSeleccionado += ord.getTotal();
            }
        }
        return seleccionadas;
    }
}

