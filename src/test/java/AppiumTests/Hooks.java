package AppiumTests;

import Utilities.Wait;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.selenium.ClassicRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Hooks {

    private static final String className = "AppiumTest";
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static BatchInfo batch;
    private static ClassicRunner classicRunner;
    AppiumDriver driver;
    Eyes eyes;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;

    @BeforeAll
    public static void beforeAll() {
        String batchName = userName + "-" + className;
        System.out.println("BeforeAll");
//        startAppiumServer();
        if(null == batch) {
            System.out.println("Batch is null. Instantiate");
            batch = new BatchInfo(batchName);
            batch.setId(String.valueOf(epochSecond));
        } else {
            System.out.println("Batch is already instantiated");
        }
        System.out.println("batch name: " + batch.getName());
        System.out.println("batch sequenceName: " + batch.getSequenceName());
        System.out.println("batch startedAt: " + batch.getStartedAt());
        System.out.println("batch BatchId: " + batch.getId());
        System.out.println("Is ClassicRunner null? " + (null == classicRunner));
        classicRunner = new ClassicRunner();
        classicRunner.setDontCloseBatches(true);
    }

    private static void startAppiumServer() {
        System.out.println(String.format("Start local Appium server"));
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        // Use any port, in case the default 4723 is already taken (maybe by another Appium server)
        serviceBuilder.usingAnyFreePort();
        serviceBuilder.withArgument(GeneralServerFlag.ALLOW_INSECURE, "adb_shell");
        serviceBuilder.withArgument(GeneralServerFlag.RELAXED_SECURITY);

        // Appium 1.x
        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder)
                                                    .withBasePath("/wd/hub/");

        // Appium 2.x
        //        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);

        localAppiumServer.start();
        APPIUM_SERVER_URL = localAppiumServer.getUrl().toString();
        System.out.println(String.format("Appium server started on url: '%s'",
                                         localAppiumServer.getUrl().toString()));
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws MalformedURLException {
        System.out.println("BeforeEach: Test - " + testInfo.getDisplayName());
        System.out.println(String.format("Create AppiumDriver for - %s", APPIUM_SERVER_URL));
        // Appium 1.x
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Appium 2.x
        //        UiAutomator2Options capabilities = new UiAutomator2Options();

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("app", new File("./sampleApps/AndroidCalculator.apk").getAbsolutePath());
        capabilities.setCapability("appPackage", "com.android2.calculator3");
        capabilities.setCapability("appActivity", "com.android2.calculator3.Calculator");
        driver = new AppiumDriver(new URL(APPIUM_SERVER_URL), capabilities);
        System.out.println(String.format("Created AppiumDriver for - %s", APPIUM_SERVER_URL));

        handleUpgradePopup();

        eyes = new Eyes(classicRunner);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setServerUrl("https://jioeyes.applitools.com");
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(batch);
        eyes.setBranchName("main");
        eyes.setIsDisabled(false);
        eyes.setIgnoreCaret(true);
        eyes.setIgnoreDisplacements(true);
        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.addProperty("username", userName);
        eyes.open(driver, className, testInfo.getDisplayName());
    }

    private void handleUpgradePopup() {
        Wait.waitFor(1);
        WebElement upgradeAppNotificationElement = (WebElement) driver.findElement(
                By.id("android:id/button1"));
        if(null != upgradeAppNotificationElement) {
            upgradeAppNotificationElement.click();
            Wait.waitFor(1);
        }
        WebElement gotItElement = (WebElement) driver.findElement(By.id("com.android2.calculator3:id/cling_dismiss"));
        if(null != gotItElement) {
            gotItElement.click();
            Wait.waitFor(1);
        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("AfterEach");
        driver.quit();
        eyes.closeAsync();
        TestResultsSummary allTestResults = classicRunner.getAllTestResults(false);
        System.out.println(allTestResults);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AfterAll");
        System.out.println(String.format("Stopping the local Appium server running on: '%s'",
                                         APPIUM_SERVER_URL));
        if(null != localAppiumServer) {
            localAppiumServer.stop();
            System.out.println("Is Appium server running? " + localAppiumServer.isRunning());
        }
        batch.setCompleted(true);
    }
}
