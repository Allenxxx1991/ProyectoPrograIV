package una.ac.cr.proyectoprograiv.presentation.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.ac.cr.proyectoprograiv.logic.Service;
import una.ac.cr.proyectoprograiv.logic.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller("admin")
public class AdminController {
    @Autowired
    private Service service;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/solicitudes")
    String showSolicitudes(Model model) {
        List<Usuario> usuarios = service.usuarioFindByEstado("inactivo");
        model.addAttribute("usuarios", usuarios);
        return "/presentation/admin/ViewSolicitudes";
    }

    @PostMapping("/admin/aceptarSolicitud")
    String aceptarSolicitud(Model model, @RequestParam String idUsuario) {
        try {
            Optional<Usuario> usuario = service.usuarioFindById(idUsuario);
            if (usuario.isPresent()) {
                Usuario usuarioAux = usuario.get();
                usuarioAux.setEstado("activo");
                service.usuarioSave(usuarioAux);
                model.addAttribute("message", "El usuario con id: "+ idUsuario + " ha sido aceptado.");
            }
        }catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        List<Usuario> usuarios = service.usuarioFindByEstado("inactivo");
        model.addAttribute("usuarios", usuarios);
        return "/presentation/admin/ViewSolicitudes";
    }

    @GetMapping("/admin/usuarios")
    String showUsuarios(Model model) {
        List<Usuario> usuarios = (List<Usuario>) service.usuarioFindAll();
        List<Usuario> filteredUsuarios = usuarios.stream()
                .filter(usuario -> !"datos".equals(usuario.getEstado()) && "dependiente".equals(usuario.getRol()))
                .collect(Collectors.toList());
        model.addAttribute("usuarios", filteredUsuarios);
        return "/presentation/admin/ViewUsuarios";
    }

    @PostMapping("/cambiarRol")
    String cambioRol(Model model, @RequestParam String idUsuario, @RequestParam String nuevoEstado) {
        try {
            Usuario usuario = service.usuarioFindById(idUsuario).get();
            usuario.setEstado(nuevoEstado);
            service.usuarioSave(usuario);
        }catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return showUsuarios(model);
    }
}
