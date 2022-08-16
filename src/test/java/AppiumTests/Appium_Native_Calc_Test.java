package AppiumTests;

import Utilities.Wait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

class Appium_Native_Calc_Test {

    private static AppiumDriverLocalService localAppiumServer;
    private AppiumDriver driver;
    private static String APPIUM_SERVER_URL = "NOT_SET";

    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws
                                  MalformedURLException {
        System.out.println("Test - " + testInfo.getDisplayName());
        System.out.println(String.format("Create AppiumDriver for - %s",
                                         APPIUM_SERVER_URL));
        // Appium 1.x
        // DesiredCapabilities capabilities = new DesiredCapabilities();

        // Appium 2.x
        UiAutomator2Options capabilities = new UiAutomator2Options();

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
        driver = new AppiumDriver(new URL(APPIUM_SERVER_URL),
                                    capabilities);
        System.out.println(String.format("Created AppiumDriver for - %s",
                                         APPIUM_SERVER_URL));

        handleUpgradePopup();
    }

    private static void startAppiumServer() {
        System.out.println(String.format("Start local Appium server"));
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        // Use any port, in case the default 4723 is already taken (maybe by another Appium server)
        serviceBuilder.usingAnyFreePort();
        // Appium 1.x
        // localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);
        //                                            .withBasePath("/wd/hub/");

        // Appium 2.x
        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);

        localAppiumServer.start();
        APPIUM_SERVER_URL = localAppiumServer.getUrl()
                                             .toString();
        System.out.println(String.format("Appium server started on url: '%s'",
                                         localAppiumServer.getUrl()
                                                          .toString()));
    }

    private void handleUpgradePopup() {
        Wait.waitFor(1);
        WebElement upgradeAppNotificationElement = driver.findElement(By.id("android:id/button1"));
        if (null != upgradeAppNotificationElement) {
            upgradeAppNotificationElement.click();
            Wait.waitFor(1);
        }
        WebElement gotItElement = driver.findElement(By.id("com.android2.calculator3:id/cling_dismiss"));
        if (null != gotItElement) {
            gotItElement.click();
            Wait.waitFor(1);
        }
    }

    @Test
    public void calcTest() {
        int p1 = 3;
        int p2 = 5;
        driver.findElement(By.id("digit" + p1))
              .click();
        driver.findElement(By.id("plus"))
              .click();
        driver.findElement(By.id("digit" + p2))
              .click();
        driver.findElement(By.id("equal"))
              .click();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @AfterAll
    static void afterAll() {
        System.out.println(String.format("Stopping the local Appium server running on: '%s'",
                                         APPIUM_SERVER_URL));
        localAppiumServer.stop();
        System.out.println("Is Appium server running? " + localAppiumServer.isRunning());
    }
}
