package una.ac.cr.proyectoprograiv.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.proyectoprograiv.data.ClienteRepository;
import una.ac.cr.proyectoprograiv.data.DireccionRepository;
import una.ac.cr.proyectoprograiv.data.UsuarioRepository;

import java.io.IOException;
import java.io.InputStream;
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
    private Map<String, Provincia> provincias;

    public Iterable<Cliente> clienteFindAll() { return clienteRepository.findAll(); }

    public Iterable<Usuario> usuarioFindAll() { return usuarioRepository.findAll(); }
    public Optional<Cliente> clienteFindById(String id) { return clienteRepository.findById(id); }
    public Optional<Usuario> usuarioFindById(String id) { return usuarioRepository.findById(id); }
    public void clienteSave(Cliente cliente) { clienteRepository.save(cliente); }
    public void usuarioSave(Usuario usuario) {usuarioRepository.save(usuario); }
    public void direccionSave(Direccion direccion) { direccionRepository.save(direccion); }

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

    public Map<String, Provincia> getProvincias() {
        return provincias;
    }
}

