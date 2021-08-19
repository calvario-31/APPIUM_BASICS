package basicsmethods;

import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.Utilities;

import java.io.File;
import java.net.URL;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class SauceLabsTest {
    private AndroidDriver<AndroidElement> driver;

    @BeforeMethod
    public void beforeMethod(){
        //configuramos el driver
        String APPIUM_URL_SERVER = "http://localhost:4723/wd/hub";

        File fileAPK = new File("src/main/resources/apk/sauceLabs.apk");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("autoGrantPermissions",true);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android_emulator");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        capabilities.setCapability("appWaitActivity","com.swaglabsmobileapp.MainActivity");
        capabilities.setCapability(MobileCapabilityType.APP, fileAPK.getAbsolutePath());

        try{
            driver = new AndroidDriver<>(new URL(APPIUM_URL_SERVER), capabilities);
        }catch (Exception e){
            e.printStackTrace();
            driver = null;
        }
    }

    @Test
    public void singleTapTest(){
        /*
        single tap
        Especificamos el elemento de tipo AndroidElement
         */
        By buttonLoginLocator = MobileBy.AccessibilityId("test-LOGIN");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        AndroidElement buttonLogin =
                (AndroidElement) wait.until(ExpectedConditions.visibilityOfElementLocated(buttonLoginLocator));

        new TouchAction<>(driver)
                .tap(tapOptions()
                .withElement(element(buttonLogin)))
                .perform();

        Utilities.waitFor(5);
    }

    @Test
    public void sendKeysClickTest(){
        /*
        sendKeys: igual que selenium, sirve para escribir texto
        click: hace click en el botón en el medio de este
         */
        By inputUsername = MobileBy.AccessibilityId("test-Username");
        By inputPassword = MobileBy.AccessibilityId("test-Password");
        By buttonLogin = MobileBy.AccessibilityId("test-LOGIN");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOfElementLocated(inputUsername)).sendKeys("standard_user");
        driver.findElement(inputPassword).sendKeys("secret_sauce");
        driver.findElement(buttonLogin).click();

        Utilities.waitFor(3);
    }

    @Test
    public void generalSwipe(){
        //vamos a la pantalla de dibujo
        By inputUsername = MobileBy.AccessibilityId("test-Username");
        By inputPassword = MobileBy.AccessibilityId("test-Password");
        By buttonLogin = MobileBy.AccessibilityId("test-LOGIN");
        By burgerMenu = MobileBy.AccessibilityId("test-Menu");
        By drawPage = MobileBy.AccessibilityId("test-DRAWING");

        WebDriverWait wait = new WebDriverWait(driver, 5);

        wait.until(ExpectedConditions.visibilityOfElementLocated(inputUsername)).sendKeys("standard_user");
        driver.findElement(inputPassword).sendKeys("secret_sauce");
        driver.findElement(buttonLogin).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(burgerMenu)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(drawPage)).click();

         /*
            general swipe
            de esta manera hacemos un swipe general de un punto a otro con base en porcentajes
            primero tenemos que sacar el tamaño de la pantalla
            posteriormente, se necesita 2 puntos (x1, y1) y (x2, y2)
        */
        Dimension size = driver.manage().window().getSize(); //sacamos la dimensiones del celular

        System.out.println(size.width);
        System.out.println(size.height);

        By clearButtonLocator = MobileBy.AccessibilityId("test-CLEAR");
        wait.until(ExpectedConditions.visibilityOfElementLocated(clearButtonLocator));

        double firstXPercentage = 0.25;
        double firstYPercentage = 0.5;
        double secondXPercentage = 0.75;
        double secondYPercentage = 0.7;

        int xFirstPoint = (int) (size.width * firstXPercentage);
        int yFirstPoint = (int) (size.height * firstYPercentage);
        int xSecondPoint = (int) (size.width * secondXPercentage);
        int ySecondPoint = (int) (size.height * secondYPercentage);
        new TouchAction<>(driver)
                .press(point(xFirstPoint, yFirstPoint))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(xSecondPoint, ySecondPoint))
                .release()
                .perform();

        Utilities.waitFor(3);
    }

    @AfterMethod
    public void afterMethod(){
        if(driver != null){
            driver.quit();
        }
    }
}
