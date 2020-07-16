package sample.integracion;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class IntegrarPCComponentes {
    private static ChromeDriver driver;

    public static ArrayList<Mobile> getPhoneList(String marca) throws InterruptedException{
        ArrayList<Mobile> moviles = new ArrayList<Mobile>();
        try {
            String exePath = "./lib/chromedriver/chromedriver.exe";
            //String exePath = "E:\\Escritorio\\IEI\\webdrivers\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", exePath);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
            driver.get("https://www.pccomponentes.com/");

            WebElement cookies = driver.findElement(By.xpath("//button[contains(text(),'ACEPTAR')]"));
            cookies.click();
            Thread.sleep(1000);
            WebElement cajaBusqueda = driver.findElement(By.name("query"));
            cajaBusqueda.sendKeys(marca);
            cajaBusqueda.sendKeys(Keys.RETURN);
            //cajaBusqueda.submit();
            Thread.sleep(1000);

            WebElement filters = driver.findElement(By.xpath("//b[contains(text(),'Smartphones/Gps')]"));
            WebElement parent = filters.findElement(By.xpath("./.."));
            String area = parent.getAttribute("aria-expanded");
            Thread.sleep(1000);

            if (area.equalsIgnoreCase("false")) {
                parent.click();
            }

            Thread.sleep(1000);

            List<WebElement> filtroSmartphones = driver.findElements(By.cssSelector("[data-id='1116']"));

            if (filtroSmartphones.size() > 0) {
                filtroSmartphones.get(0).click();
            } else {
                driver.quit();
                return moviles;
            }

            Thread.sleep(2000);
            List<WebElement> listaElementos = driver.findElements(By.tagName("article"));

            if (listaElementos.isEmpty()) {
                driver.quit();
                return moviles;
            }

            List<WebElement> vermas = driver.findElements(By.id("btnMore"));
            System.out.println("Ver mas: " + vermas.size());
            Thread.sleep(3000);
            int i = 0;
            while (!vermas.isEmpty() && i < vermas.size()) {
                int listSizeOld = listaElementos.size();
                WebDriverWait waiting;
                waiting = new WebDriverWait(driver, 10);
                waiting.until(ExpectedConditions.presenceOfElementLocated(By.id("btnMore")));
                vermas.get(i).click();
                //driver.findElement(By.id("btnMore")).click();
                System.out.println("Click " + (i + 1));
                Thread.sleep(5000);
                //waiting.wait(5000);
                listaElementos = driver.findElements(By.tagName("article"));
                System.out.println("Número de elementos de la lista: " + listaElementos.size());
                //vermas = driver.findElement(By.xpath("//button[contains(text(),'Ver más')]"));
                vermas = driver.findElements(By.id("btnMore"));
                if (vermas.isEmpty() || listSizeOld == listaElementos.size()) {
                    i++;
                }
            }

            Thread.sleep(2000);

            if (!listaElementos.isEmpty()) {
                for (int x = 0; x < listaElementos.size(); x++) {
                    String nombre = listaElementos.get(x).getAttribute("data-name");
                    String precio = listaElementos.get(x).getAttribute("data-price");
                    String descuento = "No tiene descuento";
                    List<WebElement> listaDescuentos = listaElementos.get(x).findElements(By.xpath(".//div[@class='tarjeta-articulo__descuento']"));

                    if (!listaDescuentos.isEmpty()) {
                        descuento = listaDescuentos.get(0).getText();
                    }
                    moviles.add(new Mobile(nombre, precio, descuento, ""));
                }
            }

            driver.quit();

            System.out.println("Móviles encontrados: " + moviles.size());

            return moviles;
        }catch(Exception e){
            return moviles;
        }
    }
}
