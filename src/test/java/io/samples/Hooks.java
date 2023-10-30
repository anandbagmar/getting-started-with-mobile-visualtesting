package io.samples;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.appium.AppiumRunner;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.selenium.ClassicRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Hooks {

    private static final String className = "AppiumTest";
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static BatchInfo batch;
    private static AppiumRunner appiumRunner;
    protected AppiumDriver driver;
    protected Eyes eyes;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;
    protected static boolean IS_EYES_ENABLED = false;
    protected static boolean IS_NATIVE = true;
    protected static String PLATFORM_NAME = "android";

    private static final String IOS_UDID = "218E1E36-2A38-4FE5-9F83-B0D0247D2F90";
    private static final String IOS_DEVICE_NAME = "iPhone 15";
    private static final String IOS_PLATFORM_VERSION = "17.0";


    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
        String batchName = userName + "-" + className;
        batch = new BatchInfo(batchName);
        batch.setId(String.valueOf(epochSecond));
        System.out.println("Create AppiumRunner");
        appiumRunner = new AppiumRunner();
        appiumRunner.setDontCloseBatches(true);
        System.out.printf("batch name: %s%n", batch.getName());
        System.out.printf("batch sequenceName: %s%n", batch.getSequenceName());
        System.out.printf("batch startedAt: %s%n", batch.getStartedAt());
        System.out.printf("batch BatchId: %s%n", batch.getId());
    }

    @AfterAll
    static void afterAll() {
        System.out.printf("AfterAll: Stopping the local Appium server running on: '%s'%n",
                APPIUM_SERVER_URL);
        if (null != localAppiumServer) {
            localAppiumServer.stop();
            System.out.printf("Is Appium server running? %s%n", localAppiumServer.isRunning());
        }
        batch.setCompleted(true);
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.out.printf("Test: %s - BeforeEach%n", testInfo.getDisplayName());
        if (PLATFORM_NAME.equalsIgnoreCase("android")) {
            setUpAndroid(testInfo);
        } else {
            setUpiOS(testInfo);
        }
    }

    private static void startAppiumServer() {
        System.out.println(String.format("Start local Appium server"));
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        // Use any port, in case the default 4723 is already taken (maybe by another Appium server)
        serviceBuilder.usingAnyFreePort();
        serviceBuilder.withAppiumJS(new File("./node_modules/appium/build/lib/main.js"));
        serviceBuilder.withLogFile(new File("./target/appium_logs.txt"));
        serviceBuilder.withArgument(GeneralServerFlag.ALLOW_INSECURE, "adb_shell");
        serviceBuilder.withArgument(GeneralServerFlag.RELAXED_SECURITY);

        // Appium 1.x
        // localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder)
        //     .withBasePath("/wd/hub/");

        // Appium 2.x
        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);

        localAppiumServer.start();
        APPIUM_SERVER_URL = localAppiumServer.getUrl().toString();
        System.out.println(String.format("Appium server started on url: '%s'",
                localAppiumServer.getUrl().toString()));
    }

    void setUpiOS(TestInfo testInfo) {
        System.out.println("BeforeEach: Test - " + testInfo.getDisplayName());
        System.out.printf("Create AppiumDriver for iOS test - %s%n", APPIUM_SERVER_URL);

        // Appium 1.x
        // DesiredCapabilities xcuiTestOptions = new DesiredCapabilities();

        // Appium 2.x
        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        xcuiTestOptions.setCapability(MobileCapabilityType.PLATFORM_VERSION, IOS_PLATFORM_VERSION);
        xcuiTestOptions.setCapability(MobileCapabilityType.DEVICE_NAME, IOS_DEVICE_NAME);
        xcuiTestOptions.setCapability(MobileCapabilityType.UDID, IOS_UDID);
        xcuiTestOptions.setCapability(MobileCapabilityType.FULL_RESET, false);
        // xcuiTestOptions.setCapability(MobileCapabilityType.NO_RESET, false);

        xcuiTestOptions.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, true);
        // xcuiTestOptions.setCapability(IOSMobileCapabilityType.SHOW_IOS_LOG, true);
        xcuiTestOptions.setCapability(MobileCapabilityType.PRINT_PAGE_SOURCE_ON_FIND_FAILURE, true);
        xcuiTestOptions.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, true);
        if (IS_NATIVE) {
        xcuiTestOptions.setCapability("app", System.getProperty("user.dir") + "/sampleApps/eyes-ios-hello-world.zip");
        } else {
            xcuiTestOptions.setCapability(IOSMobileCapabilityType.BROWSER_NAME, "safari");
            xcuiTestOptions.setCapability(IOSMobileCapabilityType.SAFARI_INITIAL_URL, "https://google.com");

            // xcuiTestOptions.setCapability(MobileCapabilityType.APP, "io.appium.SafariLauncher");
        }

        try {
            driver = new AppiumDriver(new URL(APPIUM_SERVER_URL), xcuiTestOptions);
        } catch (MalformedURLException e) {
            System.err.println("Error creating Appium driver for iOS device with capabilities: " + xcuiTestOptions);
            throw new RuntimeException(e);
        }

        configureEyes(testInfo);
    }

    void setUpAndroid(TestInfo testInfo) {
        System.out.println("BeforeEach: Test - " + testInfo.getDisplayName());
        System.out.println(String.format("Create AppiumDriver for android test - %s", APPIUM_SERVER_URL));
        // Appium 1.x
        // DesiredCapabilities uiAutomator2Options = new DesiredCapabilities();

        // Appium 2.x
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();

        uiAutomator2Options.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        uiAutomator2Options.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        uiAutomator2Options.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        if (IS_NATIVE) {
            uiAutomator2Options.setCapability("autoGrantPermissions", true);
            uiAutomator2Options.setCapability("fullReset", true);
            uiAutomator2Options.setCapability("app", new File("./sampleApps/AndroidCalculator.apk").getAbsolutePath());
            uiAutomator2Options.setCapability("appPackage", "com.android2.calculator3");
            uiAutomator2Options.setCapability("appActivity", "com.android2.calculator3.Calculator");
        } else {
            uiAutomator2Options.setCapability(AndroidMobileCapabilityType.BROWSER_NAME, "chrome");
        }
        System.out.println("UiAutomator2Options: " + uiAutomator2Options);
        try {
            driver = new AppiumDriver(new URL(APPIUM_SERVER_URL), uiAutomator2Options);
        } catch (MalformedURLException e) {
            System.err.println("Error creating Appium driver for android device with capabilities: " + uiAutomator2Options);
            throw new RuntimeException(e);
        }
        System.out.printf("Created AppiumDriver for - %s%n", APPIUM_SERVER_URL);
        if (IS_NATIVE) {
            handleUpgradePopup();
        }

        configureEyes(testInfo);
    }

    private void configureEyes(TestInfo testInfo) {
        System.out.println("Setup Eyes configuration");
        eyes = new Eyes(appiumRunner);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setServerUrl("https://eyes.applitools.com");
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setBatch(batch);
        eyes.setBranchName("main");
        eyes.setIsDisabled(!IS_EYES_ENABLED);
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
        if (null != upgradeAppNotificationElement) {
            upgradeAppNotificationElement.click();
            Wait.waitFor(1);
        }
        WebElement gotItElement = (WebElement) driver.findElement(By.id("com.android2.calculator3:id/cling_dismiss"));
        if (null != gotItElement) {
            gotItElement.click();
            Wait.waitFor(1);
        }
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("AfterEach: Test - " + testInfo.getDisplayName());
        if (IS_EYES_ENABLED) {
            eyes.closeAsync();
            TestResultsSummary allTestResults = appiumRunner.getAllTestResults(false);
            System.out.println(allTestResults);
        }
        if (null != driver) {
            driver.close();
            driver.quit();
        }
    }
}
