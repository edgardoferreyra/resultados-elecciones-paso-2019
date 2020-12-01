package interfaz;

import excepciones.ServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import negocio.Agrupaciones;
import negocio.Region;
import negocio.Regiones;
import negocio.Resultados;

import java.io.File;

public class Controller {
    public Label lblUbicacion;
    public ListView lvwResultados;
    public ComboBox cboDistritos;
    public ComboBox cboSecciones;
    public ComboBox cboCircuitos;
    public Resultados resultados;
    public Label lblCargaDatosInfo;
    public Button btnCambiar;
    public Button btnCargar;
    public ComboBox cboMesa;

    public void cambiarUbicacion(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Seleccione ubicación de los datos");
        dc.setInitialDirectory(new File("C:\\Users"));
        //dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = dc.showDialog(null);
        if (file != null)
            lblUbicacion.setText(file.getPath());
    }

// *********hasta acá controlado*********

    public void cargarDatos(ActionEvent actionEvent) {
        lblUbicacion.getScene().setCursor(Cursor.WAIT);
        final ObservableList[] ol = new ObservableList[2];

        btnCambiar.setDisable(true);
        btnCargar.setDisable(true);

        lblCargaDatosInfo.setText("Cargando Datos al sistema. Por favor espere.");
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                File directorio = new File(lblUbicacion.getText());

                try {
                    // Validamos que se haya seleccionada
                    // un directorio y no un archivo
                    if (!directorio.isDirectory()) {
                        throw new ServiceException("El directorio seleccionado no es válido");
                    }


                    //Generamos lista de agrupaciones
                    Agrupaciones.leerAgrupaciones(lblUbicacion.getText());

                    //Generamos lista de distritos del país
                    Regiones regiones = new Regiones(lblUbicacion.getText());
                    ol[0] = FXCollections.observableArrayList(regiones.getDistritos());

                    //Procesamos los totales por región
                    resultados = new Resultados(lblUbicacion.getText(), regiones);

                    ol[1] = FXCollections.observableArrayList(resultados.getResultadosRegion("00"));

                } catch (ServiceException se) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(se.getMessage());
                        alert.showAndWait();
                    });
                    this.cancel(true);
                }

                return null;
            }
        };
        task.setOnSucceeded(e -> {
            cboDistritos.setDisable(false);
            lvwResultados.setDisable(false);
            cboDistritos.setItems(ol[0]);
            lvwResultados.setItems(ol[1]);
            lblCargaDatosInfo.setText("Datos Cargados en el sistema.");
            lblUbicacion.getScene().setCursor(Cursor.DEFAULT);
        });
        task.setOnCancelled(e -> {
            lblCargaDatosInfo.setText("");
            btnCambiar.setDisable(false);
            btnCargar.setDisable(false);
            lblUbicacion.getScene().setCursor(Cursor.DEFAULT);
        });
        new Thread(task).start();


    }

    public void elegirDistrito(ActionEvent actionEvent) {
        //Generamos lista de secciones del distrito elegido
        ObservableList ol;

        Region distrito = (Region) cboDistritos.getValue();
        ol = FXCollections.observableArrayList(distrito.getSubregiones());
        cboSecciones.setItems(ol);
        //Mostramos resultados del Distrito
        ol = FXCollections.observableArrayList(resultados.getResultadosRegion(distrito.getCodigo()));
        lvwResultados.setItems(ol);
        cboSecciones.setDisable(false);

    }

    public void elegirSeccion(ActionEvent actionEvent) {
        ObservableList ol;
        //Genera una lista de circuitos de la sección elegida
        if (cboSecciones.getValue() != null) {
            Region seccion = (Region) cboSecciones.getValue();
            ol = FXCollections.observableArrayList(seccion.getSubregiones());
            cboCircuitos.setItems(ol);
            //intento que vuelva a cargar
            if (cboCircuitos.getItems() == null)
                cboCircuitos.setItems(ol);
            //Mostramos resultados de la sección
            ol = FXCollections.observableArrayList(resultados.getResultadosRegion(seccion.getCodigo()));
            if (ol.size() == 0) {
                cboCircuitos.setDisable(true);
                cboMesa.setDisable(true);
                lvwResultados.setItems(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No hay votos contabilizados para esta sección");
                alert.showAndWait();
            } else {
                cboCircuitos.setDisable(false);
                lvwResultados.setVisible(true);
                lvwResultados.setItems(ol);
            }

        } else {
            cboCircuitos.setItems(null);
            cboMesa.setItems(null);
            cboCircuitos.setDisable(true);
            cboMesa.setDisable(true);

        }
    }

    public void elegirCircuito(ActionEvent actionEvent) {
        ObservableList ol;
        //Genera una lista de circuitos de la sección elegida
        if (cboCircuitos.getValue() != null) {

            Region circuito = (Region) cboCircuitos.getValue();
            ol = FXCollections.observableArrayList(circuito.getSubregiones());
            cboMesa.setItems(ol);
            if (cboMesa.getItems() == null)
                cboMesa.setItems(ol);
            //Mostramos resultados del circuito
            ol = FXCollections.observableArrayList(resultados.getResultadosRegion(circuito.getCodigo()));
            if (ol.size() == 0) {
                cboMesa.setDisable(true);
                lvwResultados.setItems(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No hay votos contabilizados para este circuito");
                alert.showAndWait();
            } else {
                cboMesa.setDisable(false);
                lvwResultados.setVisible(true);
                lvwResultados.setItems(ol);
            }

        } else {
            cboCircuitos.setItems(null);
            cboMesa.setItems(null);
            cboMesa.setDisable(true);
        }
    }


    public void elegirMesa(ActionEvent actionEvent) {
        ObservableList ol;
        //Genera una lista de circuitos de la sección elegida
        if (cboMesa.getValue() != null) {
            Region mesa = (Region) cboMesa.getValue();
            //Mostramos resultados del circuito
            ol = FXCollections.observableArrayList(resultados.getResultadosRegion(mesa.getCodigo()));
            if (ol.size() == 0) {
                lvwResultados.setItems(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No hay votos contabilizados para esta mesa");
                alert.showAndWait();
            } else {
                lvwResultados.setVisible(true);
                lvwResultados.setItems(ol);
            }

        } else {
            cboMesa.setItems(null);
        }

    }
}
