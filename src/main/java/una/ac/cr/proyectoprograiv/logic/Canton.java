package una.ac.cr.proyectoprograiv.logic;

import java.util.Map;

public class Canton {
    private String nombre;
    private Map<String, String> distritos;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, String> getDistritos() {
        return distritos;
    }

    public void setDistritos(Map<String, String> distritos) {
        this.distritos = distritos;
    }
}


