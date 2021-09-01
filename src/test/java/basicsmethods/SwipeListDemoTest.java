package basicsmethods;

import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.util.List;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;

public class SwipeListDemoTest {
    private AndroidDriver<AndroidElement> driver;

    @BeforeMethod
    public void beforeMethod() {
        //configuramos el driver
        String APPIUM_URL_SERVER = "http://localhost:4723/wd/hub";

        File fileAPK = new File("src/main/resources/apk/swipeListDemo.apk");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "android_emulator");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        capabilities.setCapability(MobileCapabilityType.APP, fileAPK.getAbsolutePath());

        try {
            driver = new AndroidDriver<>(new URL(APPIUM_URL_SERVER), capabilities);
        } catch (Exception e) {
            driver = null;
        }

        driver.findElement(By.id("android:id/button1")).click();
        driver.findElement(By.className("android.widget.CheckBox")).click();
        driver.findElement(By.id("android:id/button1")).click();
    }

    @Test
    public void longPressTest() {
        //hacemos long press en el primer elemento
        By elementsLocators = MobileBy.id("com.fortysevendeg.android.swipelistview:id/front");
        List<AndroidElement> listElements = driver.findElements(elementsLocators);
        AndroidElement firstElement = listElements.get(0);
        AndroidElement secondElement = listElements.get(1);

        /*
        long press
        apreciamos que hay que pasarle el elemento a presionar
        además de la duración, no olvidar de hacer release y perform
        */
        new TouchAction<>(driver)
                .longPress(longPressOptions()
                        .withElement(element(firstElement))
                        .withDuration(ofSeconds(3)))
                .release()
                .perform();

        Utilities.waitFor(3);
    }

    @Test
    public void scrollIntoTextTest() {
        /*
        scroll into text
        apreciamos que hay que pasarle el texto exacto a donde hay que hacer scroll
        a su vez nos devuelve el elemento y podemos manipularlo luego
        */

        String text = "Clock";
        driver.findElementByAndroidUIAutomator(
                "UiScrollable(scrollable(true)).scrollIntoView(text(\"" + text + "\"))");

        Utilities.waitFor(3);
    }

    @Test
    public void scrollIntoTextContainsTest() {
        /*
        scroll into text contains (hacer scroll hasta un view que tenga una parte del texto especificado)
        apreciamos que hay que pasarle parte del texto a donde hay que hacer scroll
        a su vez nos devuelve el elemento y podemos manipularlo luego
        */

        String subText = "Live Wallpaper";
        driver.findElementByAndroidUIAutomator(
                "UiScrollable(scrollable(true)).scrollIntoView(textContains(\"" + subText + "\"))");

        Utilities.waitFor(3);
    }

    @Test
    public void swipeToTopTest() {
        String text = "System UI";
        driver.findElementByAndroidUIAutomator(
                "UiScrollable(scrollable(true)).scrollIntoView(text(\"" + text + "\"))");

        Utilities.waitFor(1);

        driver.findElementByAndroidUIAutomator(
                "new UiScrollable(scrollable(true)).scrollToBeginning(10)");
    }

    @Test
    public void verticalSwipeTest() {
        /*
            vertical swipe
            de esta manera hacemos un swipe vertical de un punto a otro con base en porcentajes
            primero tenemos que sacar el tamaño de la pantalla
            posteriormente, se necesita 2 puntos: (x, y1) (x, y2)
            ojo que el x es el mismo, ya que si no no sería horizontal
            otra cosa a considerar depende de los valores si se hace un swipe hacia arriba o hacia abajo
        */
        Dimension size = driver.manage().window().getSize(); //sacamos la dimensiones del celular
        //a nivel horizontal dónde hacer el swipe, usualmente se hace al 50% de la pantalla o sea al medio

        double anchorPercentage = 0.5;
        double startPercentage = 0.8;
        double endPercentage = 0.2;

        int anchor = (int) (size.height * anchorPercentage);
        int startPoint = (int) (size.width * startPercentage);//inicio del swipe
        int endPoint = (int) (size.width * endPercentage);// fin del swipe

        new TouchAction<>(driver)
                .press(point(anchor, startPoint))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(anchor, endPoint))
                .release()
                .perform();
    }

    @Test
    public void horizontalSwipeTest() {
        /*
            horizontal swipe
            de esta manera hacemos un swipe horizontal de un punto a otro con base en porcentajes
            primero tenemos que sacar el tamaño de la pantalla
            posteriormente, se necesita 2 puntos: (x1, y) (x2, y)
            ojo que el y es el mismo para ambos, ya que si no no sería horizontal
            otra cosa a considerar depende de los valores si se hace un swipe hacia izquierda o hacia derecha
        */
        Dimension size = driver.manage().window().getSize(); //sacamos la dimensiones del celular

        double anchorPercentage = 0.2;
        double startPercentage = 0.2;
        double endPercentage = 0.8;

        int anchor = (int) (size.height * anchorPercentage);
        int startPoint = (int) (size.width * startPercentage);
        int endPoint = (int) (size.width * endPercentage);

        new TouchAction<>(driver)
                .press(point(startPoint, anchor))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(endPoint, anchor))
                .release()
                .perform();
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }
}
