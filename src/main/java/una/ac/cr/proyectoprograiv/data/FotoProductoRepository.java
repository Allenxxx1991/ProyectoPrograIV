package una.ac.cr.proyectoprograiv.data;

import org.springframework.data.repository.CrudRepository;
import una.ac.cr.proyectoprograiv.logic.FotoProducto;
import una.ac.cr.proyectoprograiv.logic.Producto;

import java.util.List;

public interface FotoProductoRepository extends CrudRepository<FotoProducto, Integer> {
    List<FotoProducto> findByIdProducto(Producto idProducto);
}
