package una.ac.cr.proyectoprograiv.presentation.login;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.ac.cr.proyectoprograiv.logic.Cliente;
import una.ac.cr.proyectoprograiv.logic.Service;
import una.ac.cr.proyectoprograiv.logic.Orden;

import java.util.Optional;

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
//
//        String rol = (String) session.getAttribute("rol");
//
//        if (rol == null || !rol.equals("dependiente")) {
//            return "redirect:/login"; // Redirige a la página de login si no es un dependiente
//        }

        Iterable<Orden> ordenesPendientes = ordenService.obtenerOrdenesPorEstado("pendiente");
        model.addAttribute("ordenes", ordenesPendientes);
        return "presentation/purchase/ViewOrdenesPendientes";
    }

    @GetMapping("/cliente/{cedula}")
    public String verOrdenesCliente(@PathVariable String cedula, Model model) {
        // Buscar el cliente por su cédula
        Optional<Cliente> clienteOptional = ordenService.clienteFindById(cedula);

        if (clienteOptional.isEmpty()) {
            return "redirect:/login";  // Si no se encuentra el cliente, redirigir al login
        }

        Cliente cliente = clienteOptional.get();

        // Obtener las órdenes del cliente
        Iterable<Orden> ordenesCliente = ordenService.obtenerOrdenesPorCliente(cliente.getIdCliente());
        model.addAttribute("ordenes", ordenesCliente);

        return "presentation/purchase/ViewVerOrdenesCliente";  // Renderiza la vista de las órdenes del cliente
    }


//    // Obtener las órdenes del cliente
//        Iterable<Orden> ordenesCliente = ordenService.obtenerOrdenesPorCliente(cliente);
//
//        // Pasar las órdenes y el ID del cliente al modelo
//        model.addAttribute("ordenes", ordenesCliente);
//        model.addAttribute("clienteId", cliente.getIdCliente());  // Pasar el ID del cliente a la vista
//
//        return "presentation/purchase/ViewVerOrdenesCliente";  // Renderizar la vista de las órdenes del cliente
//    }


//    // Método para obtener las órdenes de un cliente específico
//    @GetMapping("/cliente/{idCliente}")
//    public String verOrdenesCliente(@PathVariable String idCliente, Model model) {
//        Iterable<Orden> ordenesCliente = ordenService.obtenerOrdenesPorCliente(idCliente);
//        model.addAttribute("ordenes", ordenesCliente);
//        return "presentation/purchase/ViewVerOrdenesCliente"; // Vista Thymeleaf
//    }

    // Método para cambiar el estado de una orden (utilizado por dependientes)
    @PostMapping("/cambiar-estado")
    public String cambiarEstadoOrden(@RequestParam Integer idOrden, @RequestParam String nuevoEstado) {
        ordenService.cambiarEstadoOrden(idOrden, nuevoEstado);
        return "redirect:/ordenes/pendientes"; // Redirecciona a la lista de órdenes pendientes
    }
}
