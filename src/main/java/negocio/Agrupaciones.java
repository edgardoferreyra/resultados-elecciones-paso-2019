package negocio;

import excepciones.ServiceException;
import util.TextFile;
import modelos.TSBHashtableDA;

import java.util.Collection;

public class Agrupaciones {
    private static TSBHashtableDA inicial;
    private TSBHashtableDA conteo;

    public Agrupaciones() {
        conteo = new TSBHashtableDA();
        for(Object o:inicial.values()){
            Agrupacion a = (Agrupacion) o;
            conteo.put(a.getCodigo(),new Agrupacion(a.getCodigo(),a.getNombre()));

        }
    }

    public static void leerAgrupaciones(String path) throws ServiceException {
        TextFile fileAgrupaciones = new TextFile(path + "/descripcion_postulaciones.dsv");
        inicial = fileAgrupaciones.identificarAgrupaciones();

    }
    public Agrupacion getAgrupacion(String codAgrupacion){
        return (Agrupacion) conteo.get(codAgrupacion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : conteo.values()
        )
            sb.append("\n" + o);
        return sb.toString();
    }

    public Collection getResultados() {
        return conteo.values();
    }
}