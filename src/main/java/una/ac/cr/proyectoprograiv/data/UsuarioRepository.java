package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {
    Optional<Usuario> findByIdUsuario(String id);
}
