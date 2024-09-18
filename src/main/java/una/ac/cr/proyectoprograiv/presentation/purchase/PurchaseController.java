package una.ac.cr.proyectoprograiv.presentation.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.proyectoprograiv.logic.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller("purchase")
@SessionAttributes("purchaseModel")
public class PurchaseController {
    @Autowired
    Service service;
    @ModelAttribute("purchaseModel")
    public PurchaseModel createPurchaseModel() {
        return new PurchaseModel();
    }

    @GetMapping("/purchase/products")
    public String listProducts(Model model) {
        List<Producto> productos = (List<Producto>) service.productoFindAll();
        for (Producto producto : productos) {
            List<FotoProducto> fotos = service.fotoProductoFindByIdProducto(producto);
            producto.setFotoproductos(new LinkedHashSet<>(fotos));
        }
        PurchaseModel purchaseModel = new PurchaseModel();
        purchaseModel.setProductos(productos);
        model.addAttribute("purchaseModel", purchaseModel);
        return "presentation/purchase/ViewProductos";
    }

    @PostMapping("/purchase/addProduct")
    public String addProductToOrder(@RequestParam("productId") int productId, Model model) {
        Optional<Producto> optionalProducto = service.productoFindById(productId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();

            DetalleOrden detalleOrden = new DetalleOrden();
            detalleOrden.setIdProducto(producto);
            detalleOrden.setCantidad(1);
            detalleOrden.setSubtotal(producto.getPrecio().multiply(new BigDecimal(detalleOrden.getCantidad())));

            PurchaseModel purchaseModel = (PurchaseModel) model.getAttribute("purchaseModel");
            assert purchaseModel != null;
            purchaseModel.addLinea(detalleOrden);
        }

        return "presentation/purchase/ViewProductos";
    }

    @PostMapping("/purchase/createOrder")
    public String createOrder(Model model) {
        PurchaseModel purchaseModel = (PurchaseModel) model.getAttribute("purchaseModel");

        Orden orden = new Orden();
        List<Cliente> clientes = (List<Cliente>) service.clienteFindAll();
        orden.setIdCliente(clientes.getFirst());  //I set the first client for now, I'll fix it later
        orden.setEstado("pendiente");
        orden.setMedioPago("efectivo");
        orden.setFechaCreacion(Instant.now());

        BigDecimal total = BigDecimal.ZERO;
        assert purchaseModel != null;
        for (DetalleOrden detalle : purchaseModel.getLineas()) {

            total = total.add(detalle.getSubtotal());
            orden.getDetalleordens().add(detalle);
        }
        orden.setTotal(total);

        service.ordenSave(orden);

        for (DetalleOrden detalle : purchaseModel.getLineas()) {
            detalle.setIdOrden(orden);
            service.detalleOrdenSave(detalle);
        }
        purchaseModel.getLineas().clear();

        return "redirect:/purchase/products";
    }



}

