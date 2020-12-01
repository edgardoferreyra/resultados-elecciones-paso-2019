package negocio;

import excepciones.ServiceException;
import util.TextFile;
import modelos.TSBHashtableDA;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class Resultados {
    private TSBHashtableDA tabla;


    public Resultados(String path, Regiones regiones) throws ServiceException {
        tabla = new TSBHashtableDA();
        TextFile fileMesas = new TextFile(path + "/mesas_totales_agrp_politica.dsv");
        fileMesas.sumarVotosPorRegion(this,regiones);

    }

    public void sumarVotos(String codRegion, String codAgrupacion, int votos) {
        int actual;
        // Buscamos la región en la tabla y la creamos si no existe
        if (tabla.get(codRegion) == null)
            tabla.put(codRegion, new Agrupaciones());

        Agrupaciones a = (Agrupaciones) tabla.get(codRegion);
        a.getAgrupacion(codAgrupacion).sumarVotos(votos);
    }
    public Collection getResultadosRegion(String codRegion){
        Agrupaciones a = (Agrupaciones) tabla.get(codRegion);
        if (a == null) {
            Collection col = Collections.EMPTY_LIST;
            return col;
        }
        return a.getResultados();
    }


 
}

