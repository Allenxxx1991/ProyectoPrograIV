package una.ac.cr.proyectoprograiv.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import una.ac.cr.proyectoprograiv.data.ProductoRepository;
import una.ac.cr.proyectoprograiv.logic.Producto;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Optional<Producto> findById(int id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void updateProduct(int id, String nombre, String descripcion, Double precio) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(BigDecimal.valueOf(precio));
            productoRepository.save(producto);
        }
    }
}