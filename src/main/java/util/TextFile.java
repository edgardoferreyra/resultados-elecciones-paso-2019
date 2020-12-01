package util;

import excepciones.ServiceException;
import negocio.Agrupacion;
import negocio.Region;
import negocio.Regiones;
import negocio.Resultados;
import modelos.TSBHashtableDA;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextFile {
    private File file;


    public TextFile(String path) {
        file = new File(path);
    }

    public String leerEncabezado() {
        String linea = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                linea = scanner.nextLine();
                break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo");
        }
        return linea;
    }

    public TSBHashtableDA identificarAgrupaciones() throws ServiceException {
        String linea = "", campos[];
        TSBHashtableDA table = new TSBHashtableDA(10);
        Agrupacion agrupacion;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                linea = scanner.nextLine();
                campos = linea.split("\\|");
                //Filtramos votación para Presidente
                if (campos[0].compareTo("000100000000000") == 0) {
                    agrupacion = new Agrupacion((campos[2]), campos[3]);
                    table.put(agrupacion.getCodigo(), agrupacion);
                }

            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("No se pudo leer el archivo descripcion_postulaciones.dsv en el directorio seleccionado");
        }
        return table;
    }

    // METODO DE ABAJO CONTROLADO
    public void sumarVotosPorRegion(Resultados resultados,Regiones regiones) throws ServiceException {
        String linea = "", campos[], codAgrupacion;
        int votos;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                linea = scanner.nextLine();
                campos = linea.split("\\|");
                codAgrupacion = campos[5];
                //Filtramos votación para Presidente
                if (campos[4].compareTo("000100000000000") == 0) {
                    votos = Integer.parseInt(campos[6]);
                    //Acumulamos los votos del pais
                    resultados.sumarVotos("00", codAgrupacion, votos);
                    //Acumulamos los votos del distrito, seccion, circuito y de la mesa
                    for (int i = 0; i < 4; i++) {
                        resultados.sumarVotos(campos[i], codAgrupacion, votos);

                    }
                    Region Pais = regiones.getPais();
                    Region distrito = Pais.getSubregion(campos[0]);
                    Region seccion = distrito.getSubregion(campos[1]);
                    Region circuito = seccion.getSubregion(campos[2]);
                    circuito.getOrPutSubregion(campos[3]);

                }
            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("No se pudo leer el archivo mesas_totales_agrp_politica.dsv");
        }
    }

    public void sumarVotosPorAgrupacion(TSBHashtableDA table) {
        String linea = "", campos[];
        Agrupacion agrupacion;
        int votos;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                linea = scanner.nextLine();
                campos = linea.split("\\|");
                //Filtramos votación para Presidente
                if (campos[4].compareTo("000100000000000") == 0) {
                    agrupacion = (Agrupacion) table.get(campos[5]);
                    votos = Integer.parseInt(campos[6]);
                    agrupacion.sumarVotos(votos);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo");
        }
    }

    //METODO DE ABAJO CONTROLADO
    public Region identificarRegiones() throws ServiceException {
        String linea = "", campos[], codigo, nombre;
        Region pais = new Region("00", "Argentina");
        Region distrito, seccion;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                linea = scanner.nextLine();
                campos = linea.split("\\|");

                codigo = campos[0];
                //CONTROLADO HASTA ACA

                nombre = campos[1];
                switch (codigo.length()) {
                    case 2:
                        //Distrito
                        distrito = pais.getOrPutSubregion(codigo);
                        distrito.setNombre(nombre);
                        break;
                    case 5:
                        //Seccion
                        distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
                        seccion = distrito.getOrPutSubregion(codigo);
                        seccion.setNombre(nombre);
                        break;
                    case 11:
                        //Circuito
                        distrito = pais.getOrPutSubregion(codigo.substring(0, 2));
                        seccion = distrito.getOrPutSubregion(codigo.substring(0, 5));
                        Region circuito = new Region(codigo,nombre);
                        seccion.agregarSubregion(circuito);

                        break;


                }
            }
        } catch (FileNotFoundException e) {
            throw new ServiceException("No se pudo leer el archivo descripcion_regiones.dsv");
        }

        return pais;

    }

}



