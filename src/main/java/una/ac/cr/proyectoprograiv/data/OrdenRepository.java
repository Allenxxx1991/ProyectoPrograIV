package una.ac.cr.proyectoprograiv.data;
import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Cliente;
import una.ac.cr.proyectoprograiv.logic.Orden;

import java.util.List;

public interface OrdenRepository extends CrudRepository<Orden, Integer> {
    // Método personalizado para encontrar órdenes por estado
    Iterable<Orden> findByEstado(String estado);

    // Método personalizado para encontrar órdenes por el ID del cliente
    Iterable<Orden> findByIdCliente(Cliente idCliente);

    List<Orden> findOrdenByIdCliente(Cliente cliente);
}
