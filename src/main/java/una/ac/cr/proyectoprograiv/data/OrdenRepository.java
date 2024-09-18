package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Orden;

public interface OrdenRepository extends CrudRepository<Orden, Integer> {
}
