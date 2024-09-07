package una.ac.cr.proyectoprograiv.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @OneToMany(mappedBy = "idProducto")
    private Set<DetalleOrden> detalleordens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProducto")
    private Set<FotoProducto> fotoproductos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Set<DetalleOrden> getDetalleordens() {
        return detalleordens;
    }

    public void setDetalleordens(Set<DetalleOrden> detalleordens) {
        this.detalleordens = detalleordens;
    }

    public Set<FotoProducto> getFotoproductos() {
        return fotoproductos;
    }

    public void setFotoproductos(Set<FotoProducto> fotoproductos) {
        this.fotoproductos = fotoproductos;
    }

}