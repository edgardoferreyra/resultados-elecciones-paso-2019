package negocio;


public class Agrupacion {
    String codAgrupacion;
    String nombreAgrupacion;
    private int votos;


    public Agrupacion() {
    }

    public Agrupacion(String codAgrupacion, String nombreAgrupacion) {
        this.codAgrupacion = codAgrupacion;
        this.nombreAgrupacion = nombreAgrupacion;
        this.votos=0;
    }
    public void sumarVotos(int cantidad)
    {
        votos += cantidad;
    }
    public String getCodigo() {
        return codAgrupacion;
    }

    public void setCodAgrupacion(String codAgrupacion) {
        this.codAgrupacion = codAgrupacion;
    }

    public String getNombre() {
        return nombreAgrupacion;
    }

    public void setNombreAgrupacion(String nombreAgrupacion) {
        this.nombreAgrupacion = nombreAgrupacion;
    }

    @Override
    public String toString() {
        return "Agrupacion{" +
                "codigo='" + codAgrupacion + '\'' +
                ", nombre='" + nombreAgrupacion + '\'' +
                ", votos=" + votos +
                '}';
    }
}
