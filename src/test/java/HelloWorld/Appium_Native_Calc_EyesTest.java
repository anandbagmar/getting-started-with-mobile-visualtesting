package HelloWorld;

import Utilities.Wait;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.selenium.ClassicRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

class Appium_Native_Calc_EyesTest {

    private static BatchInfo batch;
    private static final String className = Appium_Native_Calc_EyesTest.class.getSimpleName();
    private static ClassicRunner classicRunner;
    private AppiumDriver<WebElement> driver;
    private Eyes eyes;
    private final String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub";
    private static final String userName = System.getProperty("user.name");

    @BeforeAll
    public static void beforeAll() {
        batch = new BatchInfo(userName + "-" + className);
        classicRunner = new ClassicRunner();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws
                                  MalformedURLException {
        System.out.println("Test - " + testInfo.getDisplayName());
        System.out.println(String.format("Create AppiumDriver for - %s",
                                         APPIUM_SERVER_URL));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                                   "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
                                   "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,
                                   "Android");
        capabilities.setCapability("autoGrantPermissions",
                                   true);
        capabilities.setCapability("fullReset",
                                   true);
        capabilities.setCapability("app",
                                   new File("./sampleApps/AndroidCalculator.apk").getAbsolutePath());
        capabilities.setCapability("appPackage",
                                   "com.android2.calculator3");
        capabilities.setCapability("appActivity",
                                   "com.android2.calculator3.Calculator");
        driver = new AppiumDriver<>(new URL(APPIUM_SERVER_URL),
                                    capabilities);
        System.out.println(String.format("Created AppiumDriver for - %s",
                                         APPIUM_SERVER_URL));

        handleUpgradePopup();

        eyes = new Eyes(classicRunner);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(batch);
        eyes.setBranchName("main");
        eyes.setIsDisabled(false);
        eyes.setIgnoreCaret(true);
        eyes.setIgnoreDisplacements(true);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.addProperty("username",
                         userName);
        eyes.open(driver,
                  className,
                  testInfo.getDisplayName());
    }

    private void handleUpgradePopup() {
        Wait.waitFor(1);
        MobileElement upgradeAppNotificationElement = (MobileElement) driver.findElementById("android:id/button1");
        if (null != upgradeAppNotificationElement) {
            upgradeAppNotificationElement.click();
            Wait.waitFor(1);
        }
        MobileElement gotItElement = (MobileElement) driver.findElementById("com.android2.calculator3:id/cling_dismiss");
        if (null != gotItElement) {
            gotItElement.click();
            Wait.waitFor(1);
        }
    }

    @Test
    public void appiumTest() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 2))
              .click();
        eyes.checkWindow("digit" + 2);
        driver.findElement(By.id("plus"))
              .click();
        eyes.checkWindow("plus");
        driver.findElement(By.id("digit" + 3))
              .click();
        eyes.checkWindow("digit" + 3);
        driver.findElement(By.id("equal"))
              .click();
        eyes.checkWindow("Calc works!");
    }

    @AfterEach
    void tearDown() {
//        ResultUtils.checkAppiumResults(eyes);
        driver.quit();
        eyes.closeAsync();
        TestResultsSummary allTestResults = classicRunner.getAllTestResults(false);
        System.out.println(allTestResults);
    }
}