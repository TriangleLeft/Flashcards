import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

public class AndroidSetup {
    private AndroidDriver<AndroidElement> driver;
    private FlashcardsApp app;

    @Before
    public void setUp() throws Exception {
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "../app/build/outputs/apk/");
        File appFile = new File(appDir, "app-debug.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability("app", appFile.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "com.triangleleft.flashcards");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, ".ui.login.LoginActivity");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        app = new FlashcardsApp(driver);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void apiDemo() throws InterruptedException {
        final String email = "test@email.com";
        final String password = "password1";
        LoginPage loginPage = app.loginPage();
        loginPage.email.sendKeys(email);
        loginPage.password.sendKeys(password);
        loginPage.checkbox.click();
    }
}
