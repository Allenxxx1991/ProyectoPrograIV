package una.ac.cr.proyectoprograiv.presentation.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.proyectoprograiv.logic.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
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
    public String showRegistro(Model model,
                               @RequestParam(required = false) String provinciaSeleccionada,
                               @RequestParam(required = false) String cantonSeleccionado,
                               @RequestParam(required = false) String distritoSeleccionado,
                               @RequestParam(required = false) String provinciaSecundariaSeleccionada,
                               @RequestParam(required = false) String cantonSecundarioSeleccionado,
                               @RequestParam(required = false) String distritoSecundarioSeleccionado) {

        // Cargar las provincias al modelo
        model.addAttribute("provincias", service.getProvincias());

        // Crear los objetos de cliente y direcciones
        Cliente cliente = new Cliente();
        Direccion direccionPrincipal = new Direccion();
        Direccion direccionSecundaria = new Direccion(); // Opcional

        model.addAttribute("cliente", cliente);
        model.addAttribute("direccionPrincipal", direccionPrincipal);
        model.addAttribute("direccionSecundaria", direccionSecundaria);

        // Manejar provincia seleccionada (dirección principal)
        if (provinciaSeleccionada != null) {
            Provincia provincia = service.getProvincias().get(provinciaSeleccionada);
            model.addAttribute("cantones", provincia.getCantones());
            model.addAttribute("provinciaSeleccionada", provinciaSeleccionada);

            // Manejar cantón seleccionado (dirección principal)
            if (cantonSeleccionado != null && provincia.getCantones().containsKey(cantonSeleccionado)) {
                Canton canton = provincia.getCantones().get(cantonSeleccionado);
                model.addAttribute("distritos", canton.getDistritos());
                model.addAttribute("cantonSeleccionado", cantonSeleccionado);

                // Manejar distrito seleccionado (dirección principal)
                if (distritoSeleccionado != null) {
                    model.addAttribute("distritoSeleccionado", distritoSeleccionado);
                }
            }
        }

        // Manejar provincia secundaria seleccionada (dirección secundaria)
        if (provinciaSecundariaSeleccionada != null) {
            Provincia provinciaSecundaria = service.getProvincias().get(provinciaSecundariaSeleccionada);
            model.addAttribute("cantonesSecundarios", provinciaSecundaria.getCantones());
            model.addAttribute("provinciaSecundariaSeleccionada", provinciaSecundariaSeleccionada);

            // Manejar cantón seleccionado (dirección secundaria)
            if (cantonSecundarioSeleccionado != null && provinciaSecundaria.getCantones().containsKey(cantonSecundarioSeleccionado)) {
                Canton cantonSecundario = provinciaSecundaria.getCantones().get(cantonSecundarioSeleccionado);
                model.addAttribute("distritosSecundarios", cantonSecundario.getDistritos());
                model.addAttribute("cantonSecundarioSeleccionado", cantonSecundarioSeleccionado);

                // Manejar distrito secundario seleccionado
                if (distritoSecundarioSeleccionado != null) {
                    model.addAttribute("distritoSecundarioSeleccionado", distritoSecundarioSeleccionado);
                }
            }
        }

        return "/presentation/login/ViewRegistro";
    }


    @PostMapping("/presentation/login/registro")
    public String register(
            @ModelAttribute Cliente cliente,
            @ModelAttribute("direccionPrincipal") Direccion direccionPrincipal,
            @ModelAttribute("direccionSecundaria") Direccion direccionSecundaria,
            @RequestParam(required = false) String provinciaSeleccionada,
            @RequestParam(required = false) String cantonSeleccionado,
            @RequestParam(required = false) String distritoSeleccionado,
            @RequestParam(required = false) String provinciaSecundariaSeleccionada,
            @RequestParam(required = false) String cantonSecundarioSeleccionado,
            @RequestParam(required = false) String distritoSecundarioSeleccionado,
            Model model) {

        // Siempre cargar las provincias
        Map<String, Provincia> provincias = service.getProvincias();
        model.addAttribute("provincias", provincias);


        if (provinciaSeleccionada != null || cantonSeleccionado != null || provinciaSecundariaSeleccionada != null || cantonSecundarioSeleccionado != null) {
            if (provinciaSeleccionada != null && provincias.containsKey(provinciaSeleccionada)) {
                Provincia provincia = provincias.get(provinciaSeleccionada);
                model.addAttribute("cantones", provincia.getCantones());
                model.addAttribute("provinciaSeleccionada", provinciaSeleccionada);
            }

            if (cantonSeleccionado != null && provincias.containsKey(provinciaSeleccionada)) {
                Provincia provincia = provincias.get(provinciaSeleccionada);
                if (provincia.getCantones().containsKey(cantonSeleccionado)) {
                    Canton canton = provincia.getCantones().get(cantonSeleccionado);
                    model.addAttribute("distritos", canton.getDistritos());
                    model.addAttribute("cantonSeleccionado", cantonSeleccionado);
                    model.addAttribute("distritoSeleccionado", distritoSeleccionado);
                }
            }

            // Cargar cantones y distritos para la dirección secundaria (opcional)
            if (provinciaSecundariaSeleccionada != null && provincias.containsKey(provinciaSecundariaSeleccionada)) {
                Provincia provinciaSecundaria = provincias.get(provinciaSecundariaSeleccionada);
                model.addAttribute("cantonesSecundarios", provinciaSecundaria.getCantones());
                model.addAttribute("provinciaSecundariaSeleccionada", provinciaSecundariaSeleccionada);
            }

                // Cargar distritos si se selecciona un cantón secundario
            if (cantonSecundarioSeleccionado != null && provincias.containsKey(provinciaSecundariaSeleccionada)) {
                Provincia provinciaSecundaria = provincias.get(provinciaSecundariaSeleccionada);
                if (provinciaSecundaria.getCantones().containsKey(cantonSecundarioSeleccionado)) {
                    Canton cantonSecundario = provinciaSecundaria.getCantones().get(cantonSecundarioSeleccionado);
                    model.addAttribute("distritosSecundarios", cantonSecundario.getDistritos());
                    model.addAttribute("cantonSecundarioSeleccionado", cantonSecundarioSeleccionado);
                    model.addAttribute("distritoSecundarioSeleccionado", distritoSecundarioSeleccionado);
                }
            }
        }

        // Si no es una solicitud para cambiar provincia/cantón, intentar guardar los datos
        try {
            // Guardar cliente
            service.clienteSave(cliente);

            // Guardar dirección principal
            direccionPrincipal.setIdCliente(cliente);
            direccionPrincipal.setTipo("principal");
            service.direccionSave(direccionPrincipal);

            // Guardar dirección secundaria si es válida
            if (isDireccionValida(direccionSecundaria)) {
                direccionSecundaria.setIdCliente(cliente);
                direccionSecundaria.setTipo("alternativa");
                service.direccionSave(direccionSecundaria);
            }

            // Redirigir a la página de login
            User user = new User();
            model.addAttribute("user", user);
            return "presentation/login/ViewLogin";
        } catch (Exception e) {
            // Manejo de errores
            model.addAttribute("error", "Ocurrió un error al guardar las direcciones.");
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
