package PMCJAVA;

public class orden {
    private static int idCounter = 0;  // Contador de IDs para generar un ID único
    private int id;
    private producto producto;
    private int cantidad;
    private float total;

    public orden(producto producto, int cantidad) {
        this.id = ++idCounter;  // Asigna un ID único a cada orden
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = producto.getCosto() * cantidad;  // Calcula el total en base al costo
    }

    public int getId() {
        return id;
    }

    public producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getTotal() {
        return total;
    }

    public float getIngreso() {
        return producto.getPrecio() * cantidad;  // Calcula el ingreso que generaría la orden
    }

    @Override
    public String toString() {
        return "Orden ID: " + id + " - Producto: " + producto.getNombre() + " - Cantidad: " + cantidad;
    }
}

