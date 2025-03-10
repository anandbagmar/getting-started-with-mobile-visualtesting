package io.samples.appium.android;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.config.MobileOptions;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.StitchMode;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

class CalculatorTest {
    private static final String className = CalculatorTest.class.getSimpleName();
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static final boolean IS_FULL_RESET = true;
    private static final boolean IS_MULTI_DEVICE = false;
    private static BatchInfo batch;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;
    private static String APK_NAME = "sampleApps" + File.separator + "Calculator_8.4.1.apk";
    private static String APK_WITH_NML_NAME = "sampleApps" + File.separator + "dist" + File.separator + "Calculator_8.4.1.apk";
    private static boolean IS_EYES_ENABLED = true;
    private static boolean IS_NML = false;
    private final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private AppiumDriver driver;
    private Eyes eyes;

    private CalculatorTest() {

    }

    @BeforeSuite
    static void beforeAll() {
        startAppiumServer();
        String localBatchName = className + "-NML=" + IS_NML + "-MULTI_DEVICE=" + IS_MULTI_DEVICE + "-" + new File(APK_NAME).getName();
        String ciBatchName = System.getenv("APPLITOOLS_BATCH_NAME");
        String applitoolsBatchName = ciBatchName == null ? localBatchName : ciBatchName;
        batch = new BatchInfo(applitoolsBatchName);
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

    private void configureEyes(Method testInfo) {
        System.out.println("Setup Eyes configuration");
        eyes = new Eyes();
        eyes.setLogHandler(new StdoutLogHandler(true));
        Configuration configuration = eyes.getConfiguration();
        configuration.addProperty("username", userName);
        configuration.setApiKey(APPLITOOLS_API_KEY);
        configuration.setBatch(batch);
        configuration.setBranchName("main");
        configuration.setCaptureStatusBar(true);
        configuration.setDisableBrowserFetching(true);
        configuration.setEnablePatterns(true);
        configuration.setEnvironmentName("prod");
        configuration.setHideCaret(true);
        configuration.setIgnoreCaret(true);
        configuration.setIgnoreDisplacements(true);
        configuration.setIsDisabled(!IS_EYES_ENABLED);
        configuration.setMatchLevel(MatchLevel.STRICT);
        configuration.setSaveNewTests(false);
        configuration.setServerUrl("https://eyes.applitools.com");
        configuration.setStitchMode(StitchMode.CSS);
        eyes.setConfiguration(configuration);
        if (IS_NML && IS_MULTI_DEVICE) {
            //            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Galaxy_S10_Plus)));
            //            eyes.setConfiguration(eyes.getConfiguration().addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Galaxy_S21)));
        }
        eyes.setConfiguration(eyes.getConfiguration().setMobileOptions(MobileOptions.keepNavigationBar(false)));
        eyes.open(driver, className, testInfo.getName());
    }

    @Test
    void calculatorTest_id() {
        eyes.check("Calculator!-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("Calculator!");

        int p1 = 3;
        int p2 = 5;

        driver.findElement(By.id("digit_" + p1)).click();
        eyes.check("digit_" + p1 + "-byElement", Target.region(By.id("digit_" + p1)));
        eyes.check("digit_" + p1 + "-by", Target.window().layout(By.id("digit_" + p1)));

        driver.findElement(By.id("op_add")).click();
        eyes.check("op_add-byElement", Target.region(By.id("op_add")));

        driver.findElement(By.id("digit_" + p2)).click();
        eyes.check("digit_" + p2 + "-byElement", Target.region(By.id("digit_" + p2)));

        driver.findElement(By.id("eq")).click();
        eyes.check("eq-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("eq");
    }

    //    @Test
    void calculatorTest_full() {
        eyes.check("Calculator!-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("Calculator!");

        int p1 = 5;
        int p2 = 6;

        driver.findElement(By.id("digit_" + p1)).click();
        eyes.check("digit_" + p1 + "-byElement", Target.region(driver.findElement(By.id("digit_" + p1))));

        driver.findElement(By.id("op_add")).click();
        eyes.check("op_add-byElement", Target.region(driver.findElement(By.id("op_add"))));

        driver.findElement(By.id("digit_" + p2)).click();
        eyes.check("digit_" + p2 + "-byElement", Target.region(driver.findElement(By.id("digit_" + p2))));

        driver.findElement(By.id("eq")).click();
        eyes.check("eq-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("eq");
    }
}
