package PMCJAVA;

import java.util.ArrayList;
import java.util.List;

public class Optimizer {

    public static List<orden> optimizerOrdenes(List<orden> ordenes, float presupuesto) {
        int n = ordenes.size();
        int W = (int) presupuesto;

        // Tabla DP para almacenar los ingresos máximos para un presupuesto dado
        float[][] dp = new float[n + 1][W + 1];

        // Llenar la tabla DP
        for (int i = 1; i <= n; i++) {
            orden ord = ordenes.get(i - 1);
            int costo = (int) ord.getTotal();

            for (int w = 0; w <= W; w++) {
                if (costo <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - costo] + ord.getIngreso());
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Recuperar las órdenes seleccionadas
        List<orden> seleccionadas = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                orden ord = ordenes.get(i - 1);
                seleccionadas.add(0, ord);  // Añadir al inicio para mantener el orden original
                w -= ord.getTotal();
            }
        }

        return seleccionadas;
    }
}


