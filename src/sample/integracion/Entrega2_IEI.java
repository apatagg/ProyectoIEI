package sample.integracion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Shaheer
 */
public class Entrega2_IEI {
    
    static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Mobile> listaAmazon = new ArrayList<>(),
                                     listaFnac = new ArrayList<>(),
                                     listaPCComponentes = new ArrayList<>();


    public static ArrayList<Mobile> main(String search, boolean amazon, boolean fnac, boolean pccom) throws InterruptedException {
        // TODO code application logic here
        ArrayList<Mobile> listaFinal = new ArrayList<Mobile>();
        
        if(search.equalsIgnoreCase("null")){
            System.out.println("No ha elegido ninguna marca.");
        } else {
            if(pccom) listaPCComponentes = IntegrarPCComponentes.getPhoneList(search);
            if(fnac) listaFnac = IntegrarFnac.getPhoneList(search);
            if(amazon)  listaAmazon = IntegrarAmazon.getPhoneList(search);

           System.out.println("==================================================");
          System.out.println("Amazon: " + listaAmazon.size() + " móviles");
           System.out.println("PCComponentes: " + listaPCComponentes.size() + " móviles");
          System.out.println("Fnac: " + listaFnac.size() + " móviles");
           System.out.println("==================================================");
        }


        for(int i=0; i<listaAmazon.size();i++){
            listaAmazon.get(i).setShop("Amazon");
            listaFinal.add(listaAmazon.get(i));
        }

        for(int i=0; i<listaFnac.size();i++){
            listaFnac.get(i).setShop("Fnac");
            listaFinal.add(listaFnac.get(i));
        }

        for(int i=0; i<listaPCComponentes.size();i++){
            listaPCComponentes.get(i).setShop("PcComponentes");
            listaFinal.add(listaPCComponentes.get(i));
        }

        return listaFinal;
    }
}
