package una.ac.cr.proyectoprograiv.logic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @Size(max = 100)
    @NotNull
    @Column(name = "provincia", nullable = false, length = 100)
    private String provincia;

    @Size(max = 100)
    @NotNull
    @Column(name = "canton", nullable = false, length = 100)
    private String canton;

    @Size(max = 100)
    @NotNull
    @Column(name = "distrito", nullable = false, length = 100)
    private String distrito;

    @Size(max = 255)
    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Lob
    @Column(name = "tipo", nullable = false)
    private String tipo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia != null ? provincia.trim() : null;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}