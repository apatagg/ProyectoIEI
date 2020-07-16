package sample.integracion;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IntegrarFnac {

    private enum SEARCHBY {ID, CLASSNAME}
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    //Path: C:/Program Files/Selenium WebDriver/geckodriver-v0.26.0-win64/geckodriver.exe
    public static ArrayList<Mobile> getPhoneList(String search) {
        FirefoxDriver driver = getFirefoxDriver("./lib/geckodriver-v0.26.0-win64/geckodriver.exe");
        ArrayList<Mobile> result = new ArrayList<>();

        try {
            // 0. Cerramos las cookies si las hubiera
            closeCookies(driver);

            // 1. Introducimos nuestra busqueda
            searchDevice(driver, search);
            Thread.sleep(1000);

            // 2. Seleccionamos el filtro de tecnologías de la barra superior
            selectTechFilter(driver);
            Thread.sleep(1000);

            // 3. Marcamos el filtro lateral de smartphones
            selectSmartphoneFilter(driver);
            Thread.sleep(1000);

            // 4. Obtenemos el listado de teléfonos
            result = getPhoneResults(driver);
        } catch (Exception e) {
            System.err.println("¡Búsqueda errónea!: \n" + e);
        }
        driver.quit();
        return result;
    }

    private static ArrayList<Mobile> getPhoneResults(FirefoxDriver driver) throws InterruptedException {
        ArrayList<Mobile> result = new ArrayList<>();
        boolean showMoreResults = true;

        while(showMoreResults) {
            //waitTo(driver,"Article-list", SEARCHBY.CLASSNAME);
            Thread.sleep(1000);

            WebElement phoneList = driver.findElement(By.className("Article-list"));
            List<WebElement> phones = phoneList.findElements(By.className("Article-item"));

            phones.forEach(phone -> {
                try {
                    String price = phone.findElement(By.className("userPrice")).getText().replace("€", "").replaceAll(",", ".");;
                    String oldPrice = "0";
                    try { oldPrice = phone.findElement(By.className("oldPrice")).getText().replace("€", "").replaceAll(",", ".");;
                    } catch (Exception e)  {System.out.println(" Error al buscar precio antiguo " + e);}
                    if(oldPrice == "0") oldPrice = price;
                    String name = phone.findElement(By.className("Article-title")).getText();
                    Double aux = (- ((Double.parseDouble(price) - Double.parseDouble(oldPrice))/Double.parseDouble(oldPrice)))*100;
                    String f = df2.format(aux) + "%";

                    if(f.equalsIgnoreCase("-0%")) f = "No tiene descuento";

                    result.add(new Mobile(name, price, f, "No calculado"));
                } catch (Exception e)  {System.out.println(" Error al buscar precio - nombre " + e);}
            });

            if(!nextButtonVisible(driver)) { showMoreResults = false; }
            else { driver.findElement(By.className("actionNext")).click(); }
        }

        return result;
    }

    private static boolean nextButtonVisible(FirefoxDriver driver) {
        WebElement nextButton = driver.findElement(By.className("actionNext"));
        return nextButton.isDisplayed();
    }

    private static void selectSmartphoneFilter(FirefoxDriver driver) throws TimeoutException {
        // Seleccionamos la opción de "Smartphones y teléfonos móviles"
        if(!waitTo(driver, "noir", SEARCHBY.CLASSNAME)) { throw new TimeoutException(); }

        List<WebElement> elements = driver.findElementsByClassName("noir");
        for(int i = 0; i < elements.size(); i++) {
            WebElement option = elements.get(i);
            String txt = option.getText();

            if(txt.startsWith("Smartphones y teléfonos móviles")) {
                option.click();
                break;
            }
        }
    }

    private static void closeCookies(FirefoxDriver driver) {
        WebElement cookiesPopup = driver.findElement(By.xpath("/html/body/aside/div/button"));
        if(cookiesPopup != null) {
            cookiesPopup.click();
        }
    }

    private static void searchDevice(FirefoxDriver driver, String search) throws TimeoutException {
        String id = "Fnac_Search";

        if(!waitTo(driver, id, SEARCHBY.ID)) { throw new TimeoutException(); }

        WebElement searchBar = driver.findElementById(id);

        searchBar.sendKeys(search);
        searchBar.submit();
    }

    private static void selectTechFilter(FirefoxDriver driver) throws TimeoutException {
        String selectCategory = "select-target", selectOption = "select-option";

        if(!waitTo(driver, selectCategory, SEARCHBY.CLASSNAME)) { throw new TimeoutException(); }

        driver.findElementByClassName(selectCategory).click();

        if(!waitTo(driver, selectOption, SEARCHBY.CLASSNAME)) { throw new TimeoutException(); }

        List<WebElement> elements = driver.findElementsByClassName(selectOption);
        for(int i = 0; i < elements.size(); i++) {
            WebElement option = elements.get(i);

            if(option.getText().equals("Tecnología")) {
                option.click();
                break;
            }
        }
    }


    private static boolean waitTo(FirefoxDriver driver, String search, SEARCHBY searchType) {
        WebDriverWait waiting = new WebDriverWait(driver, 10);
        boolean completed = true;
        try {
            switch (searchType) {
                case ID:
                    waiting.until(ExpectedConditions.presenceOfElementLocated(By.id(search)));
                    break;
                case CLASSNAME:
                    waiting.until(ExpectedConditions.presenceOfElementLocated(By.className(search)));
                    break;
                default:
                    break;
            }
        } catch (TimeoutException e) {
            completed = false;
        }
        return completed;
    }

    private static FirefoxDriver getFirefoxDriver(String WebdriverPath) {
        System.setProperty("webdriver.gecko.driver", WebdriverPath);

        FirefoxDriver driver = new FirefoxDriver();
        driver.get("https://www.fnac.es/");
        //driver.manage().window().maximize();
        return driver;
    }
}
