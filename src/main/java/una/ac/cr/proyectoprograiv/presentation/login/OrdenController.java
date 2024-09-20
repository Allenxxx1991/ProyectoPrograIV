package una.ac.cr.proyectoprograiv.presentation.login;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.proyectoprograiv.logic.Service;
import una.ac.cr.proyectoprograiv.logic.Orden;

@Controller
@RequestMapping("/ordenes") // Esto mapea todas las rutas bajo "/ordenes"
public class OrdenController {

    @Autowired
    private Service ordenService;

    @Autowired
    private HttpSession session;

    // Método para listar todas las órdenes pendientes (para dependientes)
    @GetMapping("/pendientes")
    public String verOrdenesPendientes(Model model) {
//        System.out.println("Accediendo a /ordenes/pendientes"); // Mensaje de depuración
//        String rol = (String) session.getAttribute("rol");
//
//        if (rol == null || !rol.equals("dependiente")) {
//            return "redirect:/login"; // Redirige a la página de login si no es un dependiente
//        }

        Iterable<Orden> ordenesPendientes = ordenService.obtenerOrdenesPorEstado("pendiente");
        model.addAttribute("ordenes", ordenesPendientes);
        return "presentation/purchase/ViewOrdenesPendientes";
    }

//    // Método para obtener las órdenes de un cliente específico
//    @GetMapping("/cliente/{idCliente}")
//    public String verOrdenesCliente(@PathVariable String idCliente, Model model) {
//        Iterable<Orden> ordenesCliente = ordenService.obtenerOrdenesPorCliente(idCliente);
//        model.addAttribute("ordenes", ordenesCliente);
//        return "verOrdenesCliente"; // Vista Thymeleaf
//    }

    // Método para cambiar el estado de una orden (utilizado por dependientes)
    @PostMapping("/cambiar-estado")
    public String cambiarEstadoOrden(@RequestParam Integer idOrden, @RequestParam String nuevoEstado) {
        ordenService.cambiarEstadoOrden(idOrden, nuevoEstado);
        return "redirect:/ordenes/pendientes"; // Redirecciona a la lista de órdenes pendientes
    }
//    // Método para ver una orden específica por su ID
//    @GetMapping("/{idOrden}")
//    public String verOrden(@PathVariable Integer idOrden, Model model) {
//        Optional<Orden> ordenOpt = ordenService.ordenFindById(idOrden);
//        if (ordenOpt.isPresent()) {
//            model.addAttribute("orden", ordenOpt.get());
//            return "detalleOrden"; // Vista Thymeleaf
//        } else {
//            model.addAttribute("error", "Orden no encontrada");
//            return "error"; // Página de error
//        }
//    }
//
//    // Método para listar todas las órdenes (opcional: puede ser para administradores)
//    @GetMapping("/todas")
//    public String listarTodasLasOrdenes(Model model) {
//        Iterable<Orden> todasLasOrdenes = ordenService.ordenFindAll();
//        model.addAttribute("ordenes", todasLasOrdenes);
//        return "listaOrdenes"; // Vista Thymeleaf
//    }
}
