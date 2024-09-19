package una.ac.cr.proyectoprograiv.presentation.purchase;

import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private HttpSession httpSession;

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

            PurchaseModel purchaseModel = (PurchaseModel) model.getAttribute("purchaseModel");
            assert purchaseModel != null;

            boolean productExists = purchaseModel.getLineas().stream()
                    .anyMatch(detalle -> detalle.getIdProducto().getId().equals(producto.getId()));

            if (!productExists) {
                DetalleOrden detalleOrden = new DetalleOrden();
                detalleOrden.setIdProducto(producto);
                detalleOrden.setCantidad(1);
                detalleOrden.setSubtotal(producto.getPrecio().multiply(new BigDecimal(detalleOrden.getCantidad())));
                purchaseModel.addLinea(detalleOrden);
                purchaseModel.calculateTotal();
            }else {
                model.addAttribute("message", "Product is already in the order.");
            }
        }

        return "presentation/purchase/ViewProductos";
    }


    @PostMapping("/purchase/createOrder")
    public String createOrder(@RequestParam String medioPago, Model model) {
        PurchaseModel purchaseModel = (PurchaseModel) model.getAttribute("purchaseModel");

        Orden orden = new Orden();
        Optional<Cliente> cliente = (Optional<Cliente>) httpSession.getAttribute("cliente");
        orden.setIdCliente(cliente.get());
        orden.setEstado("pendiente");
        orden.setMedioPago(medioPago);
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

    @PostMapping("/purchase/updateProductQuantity")
    public String updateProductQuantity(@RequestParam("productId") int productId,
                                        @RequestParam("cantidad") int cantidad,
                                        Model model) {
        if (cantidad < 1 || cantidad > 100) {
            model.addAttribute("error", "Quantity must be between 1 and 100.");
            return "presentation/purchase/ViewProductos";
        }

        PurchaseModel purchaseModel = (PurchaseModel) model.getAttribute("purchaseModel");
        assert purchaseModel != null;

        purchaseModel.getLineas().stream()
                .filter(detalle -> detalle.getIdProducto().getId().equals(productId))
                .findFirst()
                .ifPresent(detalle -> {
                    detalle.setCantidad(cantidad);
                    detalle.setSubtotal(detalle.getIdProducto().getPrecio().multiply(new BigDecimal(detalle.getCantidad())));
                });
        purchaseModel.calculateTotal();

        return "presentation/purchase/ViewProductos";
    }



}

