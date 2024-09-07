package una.ac.cr.proyectoprograiv.presentation.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.proyectoprograiv.logic.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Controller("login")
public class LoginController {
    @Autowired
    private Service service;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/")
    public String show  (Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "/presentation/login/ViewLogin";
    }

    @PostMapping("/presentation/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        try {
            // Primero validamos si es un cliente
            Optional<Cliente> cliente = service.clienteFindById(user.getUsuario());
            if (cliente.isPresent() && cliente.get().getContrasena().equals(user.getPassword())) {
                // Usuario es cliente, almacenamos en la sesión y redirigimos
                httpSession.setAttribute("cliente", cliente);
                return "presentation/home/ViewHome";
            }

            // Si no es cliente, verificamos si es un usuario dependiente o administrador
            Optional<Usuario> usuario = service.usuarioFindById(user.getUsuario());
            if (usuario.isPresent() && usuario.get().getContrasena().equals(user.getPassword())) {
                if ("dependiente".equals(usuario.get().getRol())) {
                    if (!usuario.get().getEstado().equals("activo")) {
                        model.addAttribute("error", "Su cuenta está inactiva.");
                        return "presentation/login/ViewLogin";
                    }
                    httpSession.setAttribute("dependiente", usuario);
                    return "presentation/home/ViewHome";
                } else if ("administrador".equals(usuario.get().getRol())) {
                    // Usuario es administrador, almacenamos en la sesión y redirigimos
                    httpSession.setAttribute("administrador", usuario);
                    return "presentation/home/ViewHome";
                }
            }

            // Si las credenciales no son válidas para ningún tipo de usuario
            model.addAttribute("error", "Credenciales incorrectas.");
            return "presentation/login/ViewLogin";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error durante el inicio de sesión.");
            return "presentation/login/ViewLogin";
        }
    }

    @GetMapping("/registro")
    public String showRegistro(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);

        // Inicializamos las direcciones
        Direccion direccionPrincipal = new Direccion();
        Direccion direccionSecundaria = new Direccion(); // opcional

        model.addAttribute("direccionPrincipal", direccionPrincipal);
        model.addAttribute("direccionSecundaria", direccionSecundaria);

        return "/presentation/login/ViewRegistro";
    }

    @PostMapping("/presentation/login/registro")
    public String register(@ModelAttribute Cliente cli,
                           @ModelAttribute Direccion direccionPrincipal,
                           @RequestParam(value = "direccionSecundaria", required = false) Direccion direccionSecundaria,
                           Model model) {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(cli.getIdCliente());
            cliente.setNombre(cli.getNombre());
            cliente.setApellido(cli.getApellido());
            cliente.setEmail(cli.getEmail());
            cliente.setContrasena(cli.getContrasena());
            cliente.setTelefono(cli.getTelefono());

            // Guardar cliente
            service.clienteSave(cliente);

            // Direccion Principal
            direccionPrincipal.setIdCliente(cliente);
            direccionPrincipal.setTipo("PRINCIPAL");
            service.direccionSave(direccionPrincipal);

            // Direccion Secundaria (solo si está presente)
            if (direccionSecundaria != null &&
                    direccionSecundaria.getProvincia() != null &&
                    !direccionSecundaria.getProvincia().isEmpty()) {
                direccionSecundaria.setIdCliente(cliente);
                direccionSecundaria.setTipo("SECUNDARIA");
                service.direccionSave(direccionSecundaria);
            }

            User user = new User();
            model.addAttribute("user", user);
            return "presentation/login/ViewLogin";
        } catch (Exception e) {
            model.addAttribute("cliente", new Cliente());
            model.addAttribute("error", e.getMessage());
            return "presentation/login/ViewRegistro";
        }
    }

    private boolean isDireccionValida(Direccion direccion) {
        return (direccion.getProvincia() != null && !direccion.getProvincia().isEmpty()) ||
                (direccion.getCanton() != null && !direccion.getCanton().isEmpty()) ||
                (direccion.getDistrito() != null && !direccion.getDistrito().isEmpty()) ||
                (direccion.getDescripcion() != null && !direccion.getDescripcion().isEmpty());
    }

}
