package una.ac.cr.proyectoprograiv.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @Size(max = 15)
    @Column(name = "id_cliente", nullable = false, length = 15)
    private String idCliente;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @OneToMany(mappedBy = "idCliente")
    private Set<Direccion> direccions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCliente")
    private Set<Orden> ordens = new LinkedHashSet<>();

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Direccion> getDireccions() {
        return direccions;
    }

    public void setDireccions(Set<Direccion> direccions) {
        this.direccions = direccions;
    }

    public Set<Orden> getOrdens() {
        return ordens;
    }

    public void setOrdens(Set<Orden> ordens) {
        this.ordens = ordens;
    }

}