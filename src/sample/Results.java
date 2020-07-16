package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.integracion.Mobile;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Results implements Initializable {

    /*public TableView<Mobile> tablaResultados;
    public TableColumn<Mobile, String> modelo;
    public TableColumn<Mobile, String> precio;
    public TableColumn<Mobile, String> descuento;*/
    public TableView tablaResultados;
    public TableColumn modelo;
    public TableColumn precio;
    public TableColumn descuento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    //Receive message from scene 1
    public void mostrarResultados(ArrayList<Mobile> listaMoviles) {
        ObservableList<Mobile> data = FXCollections.<Mobile>observableArrayList();
        data.addAll(listaMoviles);

        tablaResultados.setItems(data);
    }
}
