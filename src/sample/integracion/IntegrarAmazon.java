package sample.integracion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IntegrarAmazon {
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static ArrayList<Mobile> getPhoneList(String search) {
        // Cargamos Google Chrome
        System.setProperty("webdriver.chrome.driver", "./lib/chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ArrayList<Mobile> movilesAmazon = new ArrayList<>();

        // Entramos a la seccion de moviles de Amazon.es y resolvemos el Captcha
        driver.get("https://www.amazon.es/s?rh=n%3A17425698031");
        System.out.println("Rellene el formulario de captcha y pulse enter");
        Scanner sc = new Scanner(System.in);
        String cadena = sc.nextLine();

        // Buscamos el string search en la barra de busqueda
        WebElement element = driver.findElement(By.name("field-keywords"));
        element.sendKeys(search);
        element.submit();

        try {
            // Esperamos a que cargue la web
            WebDriverWait waiting = new WebDriverWait(driver, 10);
            waiting.until(ExpectedConditions.presenceOfElementLocated(By.className("a-last")));

            // Aplicamos filtros a la busqueda de moviles
            WebElement priceMin = driver.findElement(By.name("low-price"));
            priceMin.sendKeys("40");
            priceMin.submit();
            waiting.until(ExpectedConditions.presenceOfElementLocated(By.className("a-last")));
        } catch(Exception e) {
            System.out.println("No se han encontrado moviles");
            driver.quit();
            return movilesAmazon;
        }
        int totalMoviles = 0;
        WebElement elementoActual, navegacion, precio;

        //Recorremos las 3 primeras paginas
        for(int j=0; j<3;j++) {
            int k=1;
            // Esperamos a que carguen los resultados de la web
            WebDriverWait wait = new WebDriverWait(driver , 30);
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(@data-cel-widget,'search_result')]")));

            //Almacenamos los moviles en una lista
            List<WebElement> listaElementos = driver.findElements(By.xpath("//*[contains(@data-cel-widget,'search_result')]"));
            //totalMoviles += listaElementos.size();
            //System.out.println("Total Moviles : "+ totalMoviles);

            //Recorremos todos los telefonos encontrados
            for (int i=0; i<listaElementos.size(); i++){
                elementoActual = listaElementos.get(i);
                List<WebElement> listaNombre = elementoActual.findElements(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div/span[4]/div[1]/div["+ k +"]/div/span/div/div/div[2]/div[2]/div/div[1]/div/div/div[1]/h2/a/span"));
                List<WebElement> listaPrecio = elementoActual.findElements(By.xpath("/html/body/div[1]/div[1]/div[1]/div[2]/div/span[4]/div[1]/div[" + k + "]/div/span/div/div/div[2]/div[2]/div/div[2]/div[1]/div/div[1]/div/div"));

                String precioMovil = "";
                String precioMovilOg = "";
                String precioDescuento = "No tiene descuento";

                //Miramos si el codigo tiene descuento
                if (listaPrecio.size()>0){
                    precio = listaPrecio.get(0);
                    int iend = precio.getText().indexOf("€");

                    //Si tiene dos signos de € significa que hay descuento
                    if (iend != -1 && precio.getText().chars().filter(num -> num == '€').count() > 1)
                    {
                        precioMovil = precio.getText().substring(0 , iend);
                        precioMovilOg = precio.getText().substring(iend+1 , precio.getText().length()-1);
                        precioMovil = precioMovil.replaceAll(",", ".");
                        precioMovil = precioMovil.replaceAll("€", "");
                        precioMovilOg = precioMovilOg.replaceAll(",", ".");
                        Double aux = (- ((Double.parseDouble(precioMovil) - Double.parseDouble(precioMovilOg))/Double.parseDouble(precioMovilOg)))*100;
                        precioDescuento = df2.format(aux) + "%";
                    } else {
                        precioMovil = precio.getText();
                        precioMovil = precioMovil.replaceAll("€", "");
                        precioMovil = precioMovil.replaceAll(",", ".");
                    }
                }

                if (listaNombre.size()>0){
                    navegacion = listaNombre.get(0);
                    Mobile aux = new Mobile(navegacion.getText(),precioMovil, precioDescuento,"");
                    //System.out.println("P: "+ j + " E: " + k + " D: " + precioDescuento + " P: " + precioMovil + " " + navegacion.getText());

                    if(precioMovil != "") {
                        movilesAmazon.add(aux);
                    }
                }
                k++;
            }
            WebElement nextPage = driver.findElement(By.className("a-last"));
            nextPage.click();
        }

        //Cerramos el navegador
        driver.quit();

        //Obviamente falta implementar
        return movilesAmazon;
    }
}