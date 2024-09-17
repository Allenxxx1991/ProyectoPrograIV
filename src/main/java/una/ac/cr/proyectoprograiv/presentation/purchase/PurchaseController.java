package una.ac.cr.proyectoprograiv.presentation.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import una.ac.cr.proyectoprograiv.logic.FotoProducto;
import una.ac.cr.proyectoprograiv.logic.Producto;
import una.ac.cr.proyectoprograiv.logic.Service;
import java.util.LinkedHashSet;

import java.util.List;

@org.springframework.stereotype.Controller("purchase")
public class PurchaseController {
    @Autowired
    Service service;

    @GetMapping("/purchase/products")
    public String listProducts(Model model) {
        List<Producto> productos = (List<Producto>) service.productoFindAll();
        for (Producto producto : productos) {
            List<FotoProducto> fotos = service.fotoProductoFindByIdProducto(producto);
            producto.setFotoproductos(new LinkedHashSet<>(fotos));
        }
        model.addAttribute("productos", productos);
        return "presentation/purchase/ViewProductos";
    }
}

