package AutomationTest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;


public class WebDriverSetup {

    private static ThreadLocal<RemoteWebDriver> threadDriver = null;

    public WebDriver driver(){
        return threadDriver.get();
    }

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        threadDriver = new ThreadLocal<RemoteWebDriver>();

        DesiredCapabilities DC = new DesiredCapabilities().chrome();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        DC.setCapability(ChromeOptions.CAPABILITY, options);

        threadDriver.set(new RemoteWebDriver(new URL(ClassGlobals.hubUrl), DC));
    }

}
