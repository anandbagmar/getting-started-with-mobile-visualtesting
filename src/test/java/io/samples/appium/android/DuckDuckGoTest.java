package io.samples.appium.android;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.visualgrid.model.AndroidDeviceInfo;
import com.applitools.eyes.visualgrid.model.AndroidDeviceName;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.samples.Wait;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

class DuckDuckGoTest {
    private static final String className = DuckDuckGoTest.class.getSimpleName();
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static BatchInfo batch;
    private final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private AppiumDriver driver;
    private Eyes eyes;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;
    private static String APK_NAME = "sampleApps" + File.separator + "duckduckgo-5.202.0.apk";
    private static String APK_WITH_NML_NAME = "sampleApps" + File.separator + "dist" + File.separator + "duckduckgo-5.202.0.apk";

    private static boolean IS_EYES_ENABLED = true;
    private static final boolean IS_FULL_RESET = true;
    private static boolean IS_NML = true;
    private static final boolean IS_MULTI_DEVICE = false;

    private DuckDuckGoTest() {
    }

    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
        String batchName = className + "-NML=" + IS_NML + "-MULTI_DEVICE=" + IS_MULTI_DEVICE + "-" + new File(APK_NAME).getName();
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
        setUpAndroid(testInfo);
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

    void setUpAndroid(TestInfo testInfo) {
        System.out.println("BeforeEach: Test - " + testInfo.getTestMethod().get().getName());
        System.out.printf("Create AppiumDriver for android test - %s%n", APPIUM_SERVER_URL);
        // Appium 2.x
        DesiredCapabilities uiAutomator2Options = new DesiredCapabilities();
        //        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setCapability("platformName", "Android");

        uiAutomator2Options.setCapability(UiAutomator2Options.AUTOMATION_NAME_OPTION, "UiAutomator2");
        uiAutomator2Options.setCapability(UiAutomator2Options.DEVICE_NAME_OPTION, "Android");
        uiAutomator2Options.setCapability(UiAutomator2Options.PRINT_PAGE_SOURCE_ON_FIND_FAILURE_OPTION, true);
        uiAutomator2Options.setCapability(UiAutomator2Options.AUTO_GRANT_PERMISSIONS_OPTION, true);
        uiAutomator2Options.setCapability(UiAutomator2Options.FULL_RESET_OPTION, IS_FULL_RESET);
        if (IS_NML && IS_EYES_ENABLED) {
            uiAutomator2Options.setCapability(UiAutomator2Options.APP_OPTION, new File(APK_WITH_NML_NAME).getAbsolutePath());
            System.out.printf("Add devices to NML configuration using capabilities: %%n%s%n", uiAutomator2Options);
            Eyes.setMobileCapabilities(uiAutomator2Options, APPLITOOLS_API_KEY);
        } else {
            uiAutomator2Options.setCapability(UiAutomator2Options.APP_OPTION, new File(APK_NAME).getAbsolutePath());
        }
        System.out.println("UiAutomator2Options:");
        for (String capabilityName : uiAutomator2Options.getCapabilityNames()) {
            System.out.println("\t" + capabilityName + ": " + uiAutomator2Options.getCapability(capabilityName));
        }

        try {
            driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), uiAutomator2Options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1L));
        } catch (MalformedURLException e) {
            System.err.println("Error creating Appium driver for android device with capabilities: " + uiAutomator2Options);
            throw new RuntimeException(e);
        }
        System.out.printf("Created AppiumDriver for - %s%n", APPIUM_SERVER_URL);
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
        eyes.setForceFullPageScreenshot(true);
        eyes.setSaveNewTests(false);
        if (IS_NML && IS_MULTI_DEVICE) {
            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Galaxy_S10_Plus)));
            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Galaxy_S21)));
        }
        eyes.open(driver, className, testInfo.getTestMethod().get().getName());
    }

    @Test
    void duckduckGoSettingsTest() {
        Wait.waitFor(5);
        WebElement primaryCTAElement = Wait.waitTillElementIsPresent(driver, AppiumBy.id("com.duckduckgo.mobile.android:id/primaryCta"));
        eyes.checkWindow("Primary CTA");
        primaryCTAElement.click();

        WebElement cancelDefaultBrowserUpdateElement = Wait.waitTillElementIsPresent(driver, AppiumBy.id("android:id/button2"));
        eyes.checkWindow("Cancel Default Browser Update");
        cancelDefaultBrowserUpdateElement.click();

        Wait.waitFor(5);
        WebElement menuElement = Wait.waitTillElementIsPresent(driver, AppiumBy.id("com.duckduckgo.mobile.android:id/browserMenuImageView"));
        eyes.checkWindow("Menu");
        menuElement.click();

        WebElement settingsElement = Wait.waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.duckduckgo.mobile.android:id/label\" and @text=\"Settings\"]"));
        eyes.checkWindow("Settings");
        settingsElement.click();

        WebElement fireButtonElement = Wait.waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.duckduckgo.mobile.android:id/primaryText\" and @text=\"Fire Button\"]"));
        eyes.checkWindow("Fire Button");
        fireButtonElement.click();

        WebElement fireButtonAnnimation = Wait.waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.duckduckgo.mobile.android:id/primaryText\" and @text=\"Fire Button Animation\"]"));
        eyes.checkWindow("Fire Button Animation");
    }

}
