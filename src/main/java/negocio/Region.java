package negocio;

import java.util.Collection;
import java.util.Map;

import modelos.TSBHashtableDA;

public class Region {
    private String codigo;
    private String nombre;
    private TSBHashtableDA subregiones;

    public Region(String codigo, String nombre, TSBHashtableDA subregiones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.subregiones = new TSBHashtableDA();
    }

    public Region(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.subregiones = new TSBHashtableDA();
    }


    public void agregarSubregion(Region region){
        subregiones.put(region.codigo, region);
    }

    public Collection getSubregiones() {
        return subregiones.values();
    }
    public Region getSubregion(String codigo){
        return (Region)subregiones.get(codigo);
    }

    @Override
    public String toString() {
        return "(" + codigo + ")" + nombre;
    }
// en el argumento cambié substring por codigo
    public Region getOrPutSubregion(String codigo) {
        Region sub = (Region) subregiones.get(codigo);
        if (sub == null) {
            subregiones.put(codigo, new Region(codigo, ""));
        }
        return (Region) subregiones.get(codigo);

    }

    public String getCodigo() {
        return codigo;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}


