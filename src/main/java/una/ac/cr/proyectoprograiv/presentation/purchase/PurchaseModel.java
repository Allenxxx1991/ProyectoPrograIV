package una.ac.cr.proyectoprograiv.presentation.purchase;

import una.ac.cr.proyectoprograiv.logic.DetalleOrden;
import una.ac.cr.proyectoprograiv.logic.Producto;

import java.util.ArrayList;
import java.util.List;

public class PurchaseModel {
    List<Producto> productos;
    List<DetalleOrden> lineas;

    public PurchaseModel() {
        productos = new ArrayList<Producto>();
        lineas = new ArrayList<DetalleOrden>();
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
}
