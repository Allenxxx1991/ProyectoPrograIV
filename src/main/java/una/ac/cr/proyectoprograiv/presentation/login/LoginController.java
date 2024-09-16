package una.ac.cr.proyectoprograiv.presentation.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String show(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/presentation/login/ViewLogin";
    }

    @PostMapping("/presentation/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        try {
            Optional<Cliente> cliente = service.clienteFindById(user.getUsuario());
            if (cliente.isPresent() && cliente.get().getContrasena().equals(user.getPassword())) {
                httpSession.setAttribute("cliente", cliente);
                return "presentation/home/ViewHome";
            }

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
                    httpSession.setAttribute("administrador", usuario);
                    return "presentation/home/ViewHome";
                }
            }

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

        model.addAttribute("provincias", service.getProvincias());

        Cliente cliente = new Cliente();
        Direccion direccionPrincipal = new Direccion();
        Direccion direccionSecundaria = new Direccion(); // Opcional

        model.addAttribute("cliente", cliente);
        model.addAttribute("direccionPrincipal", direccionPrincipal);
        model.addAttribute("direccionSecundaria", direccionSecundaria);

        if (provinciaSeleccionada != null) {
            Provincia provincia = service.getProvincias().get(provinciaSeleccionada);
            model.addAttribute("cantones", provincia.getCantones());
            model.addAttribute("provinciaSeleccionada", provinciaSeleccionada);

            if (cantonSeleccionado != null && provincia.getCantones().containsKey(cantonSeleccionado)) {
                Canton canton = provincia.getCantones().get(cantonSeleccionado);
                model.addAttribute("distritos", canton.getDistritos());
                model.addAttribute("cantonSeleccionado", cantonSeleccionado);

                if (distritoSeleccionado != null) {
                    model.addAttribute("distritoSeleccionado", distritoSeleccionado);
                }
            }
        }

        if (provinciaSecundariaSeleccionada != null) {
            Provincia provinciaSecundaria = service.getProvincias().get(provinciaSecundariaSeleccionada);
            model.addAttribute("cantonesSecundarios", provinciaSecundaria.getCantones());
            model.addAttribute("provinciaSecundariaSeleccionada", provinciaSecundariaSeleccionada);

            if (cantonSecundarioSeleccionado != null && provinciaSecundaria.getCantones().containsKey(cantonSecundarioSeleccionado)) {
                Canton cantonSecundario = provinciaSecundaria.getCantones().get(cantonSecundarioSeleccionado);
                model.addAttribute("distritosSecundarios", cantonSecundario.getDistritos());
                model.addAttribute("cantonSecundarioSeleccionado", cantonSecundarioSeleccionado);

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
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String descripcionSecundaria,
            Model model) {

        Map<String, Provincia> provincias = service.getProvincias();
        model.addAttribute("provincias", provincias);

        if (provinciaSeleccionada != null && !provinciaSeleccionada.isEmpty()) {
            Provincia provincia = provincias.get(provinciaSeleccionada);
            direccionPrincipal.setProvincia(provincia.getNombre());
            model.addAttribute("cantones", provincia.getCantones());
        }

        if (cantonSeleccionado != null) {
            Provincia provincia = provincias.get(provinciaSeleccionada);
            Canton canton = provincia.getCantones().get(cantonSeleccionado);
            direccionPrincipal.setCanton(canton.getNombre());
            model.addAttribute("distritos", canton.getDistritos());
        }

        if (distritoSeleccionado != null) {
            Provincia provincia = provincias.get(provinciaSeleccionada);
            Canton canton = provincia.getCantones().get(cantonSeleccionado);
            String distritoNombre = canton.getDistritos().get(distritoSeleccionado);
            direccionPrincipal.setDistrito(distritoNombre);
        }

        if (!provinciaSecundariaSeleccionada.isEmpty()) {
            Provincia provinciaSecundaria = provincias.get(provinciaSecundariaSeleccionada);
            direccionSecundaria.setProvincia(provinciaSecundaria.getNombre());
            model.addAttribute("cantonesSecundarios", provinciaSecundaria.getCantones());
        }

        if (cantonSecundarioSeleccionado != null && !cantonSecundarioSeleccionado.isEmpty()) {
            Provincia provinciaSecundaria = provincias.get(provinciaSecundariaSeleccionada);
            Canton cantonSecundario = provinciaSecundaria.getCantones().get(cantonSecundarioSeleccionado);
            direccionSecundaria.setCanton(cantonSecundario.getNombre());
            model.addAttribute("distritosSecundarios", cantonSecundario.getDistritos());
        }

        if (distritoSecundarioSeleccionado != null) {
            Provincia provinciaSecundaria = provincias.get(provinciaSecundariaSeleccionada);
            Canton canton = provinciaSecundaria.getCantones().get(cantonSecundarioSeleccionado);
            String distritoNombre = canton.getDistritos().get(distritoSecundarioSeleccionado);
            direccionSecundaria.setDistrito(distritoNombre);
        }

        if ("save".equals(action)) {
            try {
                service.clienteSave(cliente);

                direccionPrincipal.setIdCliente(cliente);
                direccionPrincipal.setTipo("principal");
                direccionPrincipal.setDescripcion(descripcion);
                service.direccionSave(direccionPrincipal);

                if (isDireccionValida(direccionSecundaria)) {
                    direccionSecundaria.setIdCliente(cliente);
                    direccionSecundaria.setTipo("alternativa");
                    direccionSecundaria.setDescripcion(descripcionSecundaria);
                    service.direccionSave(direccionSecundaria);
                }

                User user = new User();
                model.addAttribute("user", user);
                return "presentation/login/ViewLogin";
            } catch (Exception e) {
                model.addAttribute("error", "Ocurrió un error al guardar las direcciones.");
                return "presentation/login/ViewRegistro";
            }
        }
        model.addAttribute("provinciaSeleccionada", provinciaSeleccionada);
        model.addAttribute("cantonSeleccionado", cantonSeleccionado);
        model.addAttribute("distritoSeleccionado", distritoSeleccionado);
        model.addAttribute("provinciaSecundariaSeleccionada", provinciaSecundariaSeleccionada);
        model.addAttribute("cantonSecundarioSeleccionado", cantonSecundarioSeleccionado);
        model.addAttribute("distritoSecundarioSeleccionado", distritoSecundarioSeleccionado);
        return "presentation/login/ViewRegistro";
    }

    private boolean isDireccionValida(Direccion direccion) {
        return (direccion.getProvincia() != null && !direccion.getProvincia().isEmpty()) &&
                (direccion.getCanton() != null && !direccion.getCanton().isEmpty()) &&
                (direccion.getDistrito() != null && !direccion.getDistrito().isEmpty()) &&
                (direccion.getDescripcion() != null && !direccion.getDescripcion().isEmpty());
    }

    @GetMapping("/registroUsuario")
    public String showRegistro(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "presentation/login/ViewRegistroUsuario";
    }

    @PostMapping("/presentation/login/registroUsuario")
    public String userRegister(
            @ModelAttribute Usuario usuario,
            Model model){
        try {
            usuario.setEstado("inactivo");
            usuario.setRol("dependiente");
            service.usuarioSave(usuario);
            User user = new User();
            model.addAttribute("user", user);
            return "presentation/login/ViewLogin";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar las direcciones.");
            return "presentation/login/ViewRegistroUsuario";
        }
    }
}
