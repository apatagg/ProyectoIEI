package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.integracion.Entrega2_IEI;
import sample.integracion.Mobile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ComboBox<String> marca;
    @FXML
    private TextField modelo;
    @FXML
    private CheckBox check_amazon, check_fnac, check_pccomponentes;

    private String marcaTxt;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marca.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldMarca, String newMarca) {
                if (newMarca != null) {
                    if(newMarca.equals("Apple")){
                        marcaTxt = "Iphone";
                    }else{
                        marcaTxt = newMarca;
                    }
                }


            }
        });



    }

    public void search() {
        try {
            System.out.println("Modelo: " + marcaTxt +" " +modelo.getText());
            ArrayList<Mobile> listaMoviles = Entrega2_IEI.main(marcaTxt + " " + modelo.getText(), check_amazon.isSelected(), check_fnac.isSelected(), check_pccomponentes.isSelected());

            try {
                //Load second scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("results.fxml"));
                Parent root = loader.load();

                //Get controller of scene2
                Results resultados = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                resultados.mostrarResultados(listaMoviles);

                //Show scene 2 in new window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Resultados");
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        } catch (Exception e) {
            System.out.println("eeee " + e);
        }
    }
}
