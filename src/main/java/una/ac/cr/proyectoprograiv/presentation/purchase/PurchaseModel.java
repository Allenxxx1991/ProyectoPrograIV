package una.ac.cr.proyectoprograiv.presentation.purchase;

import una.ac.cr.proyectoprograiv.logic.DetalleOrden;
import una.ac.cr.proyectoprograiv.logic.Producto;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PurchaseModel {
    List<Producto> productos;
    List<DetalleOrden> lineas;
    private BigDecimal total;

    public PurchaseModel() {
        productos = new ArrayList<Producto>();
        lineas = new ArrayList<DetalleOrden>();
        total = BigDecimal.ZERO;
    }

    public void addLinea(DetalleOrden detalleOrden) {
        lineas.add(detalleOrden);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<DetalleOrden> getLineas() {
        return lineas;
    }

    public void setLineas(List<DetalleOrden> lineas) {
        this.lineas = lineas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void calculateTotal() {
        total = lineas.stream()
                .map(detalle -> detalle.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteDetalleOrden(Integer detalleId) {
        lineas.removeIf(detalle -> detalle.getIdProducto().getId().equals(detalleId)); // Assuming DetalleOrden has a getId() method
    }
}
