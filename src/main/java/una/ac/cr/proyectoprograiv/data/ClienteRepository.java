package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Cliente;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Cliente, String> {
    Optional<Cliente> findByIdCliente(String idCliente);
    Optional<Cliente> findByEmail(String email);
}
