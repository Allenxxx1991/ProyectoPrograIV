package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.Producto;

import java.util.Optional;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    Optional<Producto> findById(int id);
}
