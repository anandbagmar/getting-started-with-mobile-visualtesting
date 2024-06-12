package io.samples.appium.android;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.visualgrid.model.AndroidDeviceInfo;
import com.applitools.eyes.visualgrid.model.AndroidDeviceName;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.samples.appium.EyesResults;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

class FigmaCalculatorTest {
    private static final String className = CalculatorTest.class.getSimpleName();
    private static final long epochSecond = new Date().toInstant().getEpochSecond();
    private static final String userName = System.getProperty("user.name");
    private static BatchInfo batch;
    private final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private AppiumDriver driver;
    private Eyes eyes;
    private static String APPIUM_SERVER_URL = "http://localhost:4723/wd/hub/";
    private static AppiumDriverLocalService localAppiumServer;
    private static String APK_NAME = "sampleApps" + File.separator + "Calculator_8.4.1.apk";

    private static boolean IS_EYES_ENABLED = true;
    private static final boolean IS_FULL_RESET = true;

    private FigmaCalculatorTest() {
    }

    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
        String batchName = className + "-" + new File(APK_NAME).getName();
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
        if (null != driver) {
            driver.quit();
        }
    }

    private void getResults() {
        if (IS_EYES_ENABLED) {
            TestResults testResults = eyes.close(false);
            EyesResults.displayVisualValidationResults(testResults);
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
        uiAutomator2Options.setCapability(UiAutomator2Options.APP_OPTION, new File(APK_NAME).getAbsolutePath());
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
        eyes.setSaveNewTests(false);
    }

    @Test
    void calculatorFigmaTest() {
        String appName = "1. PNB Retail Onboarding";
        String testName = "Android Small - 2962";

        int p1 = 3;
        int p2 = 5;
        int p3 = 7;
        int p4 = 9;

        eyes.setBaselineEnvName("Android Small - 2962_460");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p3)).click();
        eyes.checkWindow("Android Small - 2962_460");
        getResults();

        eyes.setBaselineEnvName("Android Small - 2962_360");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p1)).click();
        eyes.checkWindow("Android Small - 2962_360");
        getResults();

        eyes.setBaselineEnvName("Android Small - 2962_400");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p1)).click();
        eyes.checkWindow("Android Small - 2962_400");
        getResults();

        eyes.setBaselineEnvName("Android Small - 2962_411");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p2)).click();
        eyes.checkWindow("Android Small - 2962_411");
        getResults();

        eyes.setBaselineEnvName("Android Small - 2962_450");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p3)).click();
        eyes.checkWindow("Android Small - 2962_450");
        getResults();



        eyes.setBaselineEnvName("Android Small - 2962_475");
        eyes.open(driver, appName, testName);
        driver.findElement(By.id("digit_" + p4)).click();
        eyes.checkWindow("Android Small - 2962_475");
        getResults();
    }
}
