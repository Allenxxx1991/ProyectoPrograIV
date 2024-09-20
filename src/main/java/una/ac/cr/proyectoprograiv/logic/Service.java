package una.ac.cr.proyectoprograiv.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.proyectoprograiv.data.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FotoProductoRepository fotoProductoRepository;
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;
    private Map<String, Provincia> provincias;

    public Iterable<Cliente> clienteFindAll() { return clienteRepository.findAll(); }

    public Iterable<Usuario> usuarioFindAll() { return usuarioRepository.findAll(); }
    public Optional<Cliente> clienteFindById(String id) { return clienteRepository.findById(id); }
    public Optional<Usuario> usuarioFindById(String id) { return usuarioRepository.findById(id); }
    public Iterable<Producto> productoFindAll() { return productoRepository.findAll(); }
    public Optional<Producto> productoFindById(int id) { return productoRepository.findById(id); }
    public void clienteSave(Cliente cliente) { clienteRepository.save(cliente); }
    public void usuarioSave(Usuario usuario) {usuarioRepository.save(usuario); }
    public void ordenSave(Orden orden) {ordenRepository.save(orden); }
    public void detalleOrdenSave(DetalleOrden detalleOrden) {detalleOrdenRepository.save(detalleOrden); }
    public void direccionSave(Direccion direccion) { direccionRepository.save(direccion); }
    public void productoSave(Producto producto) {productoRepository.save(producto); }
    public void fotoProductoSave(FotoProducto foto) {fotoProductoRepository.save(foto); }
    public List<FotoProducto> fotoProductoFindByIdProducto(Producto producto) { return fotoProductoRepository.findByIdProducto(producto); }
    public List<DetalleOrden> getDetalleOrdenByOrden(Orden orden) { return detalleOrdenRepository.findByIdOrden(orden); }

    @PostConstruct
    public void cargarDatos() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream jsonStream = getClass().getResourceAsStream("/static/json/provincias_cantones_distritos_costa_rica.json");
        if (jsonStream == null) {
            throw new IOException("Archivo JSON no encontrado");
        }
        // Leer el archivo JSON y mapear a la estructura de datos
        provincias = mapper.readValue(jsonStream, mapper.getTypeFactory().constructMapType(Map.class, String.class, Provincia.class));
    }
    @PostConstruct
    public void initData() {
        // Agregar dependientes quemados para prueba
        if (!usuarioRepository.existsById("222")) {
            Usuario dependiente1 = new Usuario();
            dependiente1.setIdUsuario("222");
            dependiente1.setNombre("John");
            dependiente1.setApellido("Doe");
            dependiente1.setEmail("john.doe@cookie-shop.com");
            dependiente1.setContrasena("222"); // Asegúrate de usar una contraseña cifrada en producción
            dependiente1.setRol("dependiente");
            dependiente1.setEstado("activo");
            usuarioRepository.save(dependiente1);
        }
    }

    public Map<String, Provincia> getProvincias() {
        return provincias;
    }

    // Métodos para Órdenes
    public Iterable<Orden> ordenFindAll() { return ordenRepository.findAll(); }
    public Optional<Orden> ordenFindById(Integer id) { return ordenRepository.findById(id); }

    public void cambiarEstadoOrden(Integer idOrden, String nuevoEstado) {
        Optional<Orden> ordenOpt = ordenRepository.findById(idOrden);
        if (ordenOpt.isPresent()) {
            Orden orden = ordenOpt.get();
            if ("cancelado".equals(orden.getEstado()) || "entregado".equals(orden.getEstado())) {
                throw new IllegalStateException("No se puede cambiar el estado de una orden ya entregada o cancelada.");
            }
            orden.setEstado(nuevoEstado);
            ordenRepository.save(orden);
        }
    }
    // Método para obtener todas las órdenes pendientes
    public Iterable<Orden> obtenerOrdenesPorEstado(String estado) {
        return ordenRepository.findByEstado(estado);
    }

    // Método para obtener todas las órdenes de un cliente específico
    public Iterable<Orden> obtenerOrdenesPorCliente(String idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if (cliente.isPresent()) {
            return ordenRepository.findByIdCliente(cliente.get());
        } else {
            return new ArrayList<>(); // Devuelve una lista vacía si el cliente no existe
        }
    }
}

