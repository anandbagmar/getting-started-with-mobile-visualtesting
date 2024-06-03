package io.samples.appium.ios;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

import static io.samples.Wait.waitFor;

class WebiOSHelloWorldTest {
    private static final String className = WebiOSHelloWorldTest.class.getSimpleName();
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static BatchInfo batch;
    private final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private AppiumDriver driver;
    private Eyes eyes;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;

    private static final String IPHONE_6S_IOS_DEVICE_NAME = "iPhone";
    private static final String IPHONE_6S_IOS_PLATFORM_VERSION = "17.0";
    private static final String IPHONE_6S_IOS_UDID = "auto";
    private static final String IPHONE_15_PRO_MAX_IOS_DEVICE_NAME = "iPhone 15 Pro Max";
    private static final String IPHONE_15_PRO_MAX_IOS_PLATFORM_VERSION = "17.5";
    private static final String IPHONE_15_PRO_MAX_IOS_UDID = "BA898BDF-2EEF-436D-AFA9-799855B2F9BE";
    private static final String IOS_UDID = IPHONE_15_PRO_MAX_IOS_UDID;
    private static final String IOS_DEVICE_NAME = IPHONE_15_PRO_MAX_IOS_DEVICE_NAME;
    private static final String IOS_PLATFORM_VERSION = IPHONE_15_PRO_MAX_IOS_PLATFORM_VERSION;

    private static final boolean IS_FULL_RESET = false;
    private static boolean IS_EYES_ENABLED = true;

    private WebiOSHelloWorldTest() {
    }

    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
        String batchName = className;
        batch = new BatchInfo(batchName);
        batch.setId(String.valueOf(epochSecond));
        batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
        System.out.println("Create AppiumRunner");
        System.out.printf("Batch name: %s%n", batch.getName());
        System.out.printf("Batch startedAt: %s%n", batch.getStartedAt().getTime());
        System.out.printf("Batch BatchId: %s%n", batch.getId());
    }

    @AfterAll
    static void afterAll() {
        System.out.printf("AfterAll: Stopping the local Appium server running on: '%s'%n", APPIUM_SERVER_URL);
        if (null != batch) {
            batch.setCompleted(true);
        }
        if (null != localAppiumServer) {
            localAppiumServer.stop();
            System.out.printf("Is Appium server running? %s%n", localAppiumServer.isRunning());
        }
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.printf("Test: %s - BeforeEach%n", testInfo.getTestMethod().get().getName());
        setUpiOS(testInfo);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("AfterEach: Test - " + testInfo.getTestMethod().get().getName());
        boolean isPass = true;
        if (IS_EYES_ENABLED) {
            TestResults testResults = eyes.close(false);
            System.out.printf("Test: %s\n%s%n", testResults.getName(), testResults);
            if (testResults.getStatus().equals(TestResultsStatus.Failed) || testResults.getStatus().equals(TestResultsStatus.Unresolved)) {
                isPass = false;
            }
        }
        if (null != driver) {
            driver.quit();
        }
        Assertions.assertTrue(isPass, "Visual differences found.");
    }

    private static void startAppiumServer() {
        System.out.println("Start local Appium server");
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        // Use any port, in case the default 4723 is already taken (maybe by another Appium server)
        serviceBuilder.usingAnyFreePort();
        serviceBuilder.withAppiumJS(new File("./node_modules/appium/build/lib/main.js"));
        serviceBuilder.withLogFile(new File(System.getenv("LOG_DIR") + "/appium_logs.txt"));
        serviceBuilder.withArgument(GeneralServerFlag.ALLOW_INSECURE, "adb_shell");
        serviceBuilder.withArgument(GeneralServerFlag.RELAXED_SECURITY);

        // Appium 2.x
        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);

        localAppiumServer.start();
        APPIUM_SERVER_URL = localAppiumServer.getUrl().toString();
        System.out.printf("Appium server started on url: '%s'%n", localAppiumServer.getUrl().toString());
    }

    void setUpiOS(TestInfo testInfo) {
        System.out.println("BeforeEach: Test - " + testInfo.getTestMethod().get().getName());
        System.out.printf("Create AppiumDriver for iOS test - %s%n", APPIUM_SERVER_URL);

        // Appium 2.x
        //        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        DesiredCapabilities xcuiTestOptions = new DesiredCapabilities();
        xcuiTestOptions.setCapability("platformName", "iOS");
        xcuiTestOptions.setCapability("appium:automationName", "XCUITest");
        xcuiTestOptions.setCapability(XCUITestOptions.PLATFORM_VERSION_OPTION, IOS_PLATFORM_VERSION);
        xcuiTestOptions.setCapability(XCUITestOptions.DEVICE_NAME_OPTION, IOS_DEVICE_NAME);
        xcuiTestOptions.setCapability(XCUITestOptions.UDID_OPTION, IOS_UDID);
        xcuiTestOptions.setCapability(XCUITestOptions.FULL_RESET_OPTION, IS_FULL_RESET);
        xcuiTestOptions.setCapability(XCUITestOptions.SHOW_XCODE_LOG_OPTION, false);
        xcuiTestOptions.setCapability(XCUITestOptions.SHOW_IOS_LOG_OPTION, false);
        xcuiTestOptions.setCapability(XCUITestOptions.PRINT_PAGE_SOURCE_ON_FIND_FAILURE_OPTION, true);
        xcuiTestOptions.setCapability(XCUITestOptions.AUTO_ACCEPT_ALERTS_OPTION, true);
        xcuiTestOptions.setCapability(XCUITestOptions.BROWSER_NAME_OPTION, "safari");
        xcuiTestOptions.setCapability(XCUITestOptions.SAFARI_INITIAL_URL_OPTION, "https://google.com");

        System.out.println("XCUITestOptions: " + xcuiTestOptions);
        try {
            driver = new AppiumDriver(new URL(APPIUM_SERVER_URL), xcuiTestOptions);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1L));
        } catch (MalformedURLException e) {
            System.err.println("Error creating Appium driver for iOS device with capabilities: " + xcuiTestOptions);
            throw new RuntimeException(e);
        }

        configureEyes(testInfo);
    }

    private void configureEyes(TestInfo testInfo) {
        System.out.println("Setup Eyes configuration");
        eyes = new Eyes();

        eyes.setLogHandler(new StdoutLogHandler(true));
        eyes.setBatch(batch);
        eyes.setBranchName("main");
        eyes.setEnvName("prod");
        eyes.addProperty("username", userName);
        eyes.setApiKey(APPLITOOLS_API_KEY);
        eyes.setServerUrl("https://eyes.applitools.com");
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setIsDisabled(!IS_EYES_ENABLED);
        eyes.setIgnoreCaret(true);
        eyes.setIgnoreDisplacements(true);
        eyes.setSaveNewTests(false);
        eyes.open(driver, className, testInfo.getTestMethod().get().getName());
    }

    @Test
    void runIOSWebTest() {
        driver.get("https://applitools.com/helloworld");
        waitFor(2);
        eyes.checkWindow("App launched");
        for (int stepNumber = 0; stepNumber < 2; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            waitFor(1);
            eyes.check("step-" + stepNumber, Target.region(linkText).layout());
            waitFor(1);
        }
        driver.findElement(By.tagName("button")).click();
        eyes.check("Click Me", Target.window().layout());
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
