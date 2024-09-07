package una.ac.cr.proyectoprograiv.logic;

import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.proyectoprograiv.data.ClienteRepository;
import una.ac.cr.proyectoprograiv.data.DireccionRepository;
import una.ac.cr.proyectoprograiv.data.UsuarioRepository;

import java.util.Optional;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DireccionRepository direccionRepository;

    public Iterable<Cliente> clienteFindAll() { return clienteRepository.findAll(); }

    public Iterable<Usuario> usuarioFindAll() { return usuarioRepository.findAll(); }
    public Optional<Cliente> clienteFindById(String id) { return clienteRepository.findById(id); }
    public Optional<Usuario> usuarioFindById(String id) { return usuarioRepository.findById(id); }
    public void clienteSave(Cliente cliente) { clienteRepository.save(cliente); }
    public void usuarioSave(Usuario usuario) {usuarioRepository.save(usuario); }
    public void direccionSave(Direccion direccion) { direccionRepository.save(direccion); }
}
