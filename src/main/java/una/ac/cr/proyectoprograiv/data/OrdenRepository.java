package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Cliente;
import una.ac.cr.proyectoprograiv.logic.Orden;

import java.util.List;

public interface OrdenRepository extends CrudRepository<Orden, Integer> {
    List<Orden> findOrdenByIdCliente(Cliente cliente);
}
