package io.samples.appium.android;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import static io.samples.Wait.waitTillElementIsPresent;

class VodqaTest {
    private static final String className = VodqaTest.class.getSimpleName();
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static final boolean IS_MULTI_DEVICE = false;
    private static final boolean IS_FULL_RESET = true;
    private static BatchInfo batch;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;
    private static String APK_NAME = "sampleApps" + File.separator + "VodQA.apk";
    private static String APK_WITH_NML_NAME = "sampleApps" + File.separator + "dist" + File.separator + "VodQA.apk";
    private static boolean IS_EYES_ENABLED = true;
    private static boolean IS_NML = true;
    private final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private AppiumDriver driver;
    private Eyes eyes;

    private VodqaTest() {
    }

    @BeforeSuite
    static void beforeAll() {
        startAppiumServer();
        String localBatchName = className + "-NML=" + IS_NML + "-MULTI_DEVICE=" + IS_MULTI_DEVICE + "-" + new File(APK_NAME).getName();
        String ciBatchName = System.getenv("APPLITOOLS_BATCH_NAME") == null ? localBatchName : System.getenv("APPLITOOLS_BATCH_NAME");
        batch = new BatchInfo(ciBatchName);
        // If the test runs via Jenkins, set the batch ID accordingly.
        String batchId = System.getenv("APPLITOOLS_BATCH_ID");
        batch.setId(Objects.requireNonNullElseGet(batchId, () -> String.valueOf(epochSecond)));
        batch.addProperty("REPOSITORY_NAME", new File(System.getProperty("user.dir")).getName());
        System.out.println("Create AppiumRunner");
        System.out.printf("Batch name: %s%n", batch.getName());
        System.out.printf("Batch startedAt: %s%n", batch.getStartedAt().getTime());
        System.out.printf("Batch BatchId: %s%n", batch.getId());
    }

    @AfterSuite
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

    @BeforeMethod
    public void beforeEach(Method testInfo) {
        System.out.printf("Test: %s - BeforeEach%n", testInfo.getName());
        setUpAndroid(testInfo);
    }

    @AfterMethod
    void tearDown(Method testInfo) {
        System.out.println("AfterEach: Test - " + testInfo.getName());
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
        Assert.assertTrue(isPass, "Visual differences found.");
    }

    void setUpAndroid(Method testInfo) {
        System.out.println("BeforeEach: Test - " + testInfo.getName());
        System.out.printf("Create AppiumDriver for android test - %s%n", APPIUM_SERVER_URL);
        // Appium 2.x
        DesiredCapabilities uiAutomator2Options = new DesiredCapabilities();
        uiAutomator2Options.setCapability("platformName", "Android");
        uiAutomator2Options.setCapability(UiAutomator2Options.DEVICE_NAME_OPTION, "Android");
        uiAutomator2Options.setCapability(UiAutomator2Options.AUTOMATION_NAME_OPTION, "UiAutomator2");
        uiAutomator2Options.setCapability(UiAutomator2Options.PRINT_PAGE_SOURCE_ON_FIND_FAILURE_OPTION, true);
        uiAutomator2Options.setCapability(UiAutomator2Options.AUTO_GRANT_PERMISSIONS_OPTION, true);
        uiAutomator2Options.setCapability(UiAutomator2Options.FULL_RESET_OPTION, IS_FULL_RESET);
        //        uiAutomator2Options.setCapability("nativeWebScreenshot", true);
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
        } catch (MalformedURLException e) {
            System.err.println("Error creating Appium driver for android device with capabilities: " + uiAutomator2Options);
            throw new RuntimeException(e);
        }
        System.out.printf("Created AppiumDriver for - %s%n", APPIUM_SERVER_URL);
        configureEyes(testInfo);
    }

    private void configureEyes(Method testInfo) {
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
        if (IS_NML && IS_MULTI_DEVICE) {
            //            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_6)));
            //            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_4_XL)));
        }
        eyes.open(driver, className, testInfo.getName());
    }

    @Test
    void vodQATest() {
        WebElement loginElement = waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@text=\"LOG IN\"]"));
        eyes.checkWindow("App Launched");

        loginElement.click();
        WebElement photoViewElement = waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@content-desc=\"photoView\"]"));
        eyes.checkWindow("Logged in");

        photoViewElement.click();
        WebElement backElement = waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@text=\"Back\"]"));
        eyes.checkWindow("Photo View");

        backElement.click();
        WebElement chainedViewElement = waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@content-desc=\"chainedView\"]"));
        chainedViewElement.click();
        backElement = waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@text=\"Back\"]"));
        eyes.checkWindow("Chained View");

        backElement.click();
        waitTillElementIsPresent(driver, AppiumBy.xpath("//android.widget.TextView[@content-desc=\"chainedView\"]"));
        eyes.checkWindow("Back to main screen");
    }
}
