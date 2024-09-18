package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.DetalleOrden;
import una.ac.cr.proyectoprograiv.logic.Orden;

import java.util.List;

public interface DetalleOrdenRepository extends CrudRepository<DetalleOrden, Integer> {
    List<DetalleOrden> findByIdOrden(Orden ordenId);
}
