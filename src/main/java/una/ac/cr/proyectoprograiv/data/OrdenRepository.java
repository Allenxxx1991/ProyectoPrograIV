package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Orden;
import java.util.Optional;

public interface OrdenRepository extends CrudRepository<Orden, String>  {
    Optional<Orden> findByEstado(String estado);
    Optional<Orden> findById(Integer id);

}
