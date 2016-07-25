import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class LoginPage {
    private final AppiumDriver driver;

    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_email")
    public WebElement email;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_password")
    public WebElement password;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_checkbox")
    public WebElement checkbox;
    @AndroidFindBy(id = "com.triangleleft.flashcards:id/login_button")
    public WebElement buttonSignIn;

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS), this);
    }
}
