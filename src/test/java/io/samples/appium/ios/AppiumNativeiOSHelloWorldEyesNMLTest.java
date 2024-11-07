package io.samples.appium.ios;

import com.applitools.eyes.*;
import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.visualgrid.model.IosMultiDeviceTarget;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
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
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static io.samples.Wait.waitFor;

class AppiumNativeiOSHelloWorldEyesNMLTest {
    private static final String className = AppiumNativeiOSHelloWorldEyesNMLTest.class.getSimpleName();
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
    private static final String IPHONE_16_PRO_IOS_DEVICE_NAME = "iPhone 15 Pro Max";
    private static final String IPHONE_16_PRO_IOS_PLATFORM_VERSION = "17.5";
    private static final String IPHONE_16_PRO_IOS_UDID = "02D3F6C9-7F16-4139-ABD8-F227BE5AB47A";
    private static final String IOS_UDID = IPHONE_16_PRO_IOS_UDID;
    private static final String IOS_DEVICE_NAME = IPHONE_16_PRO_IOS_DEVICE_NAME;
    private static final String IOS_PLATFORM_VERSION = IPHONE_16_PRO_IOS_PLATFORM_VERSION;

    private static String APP_NAME = "sampleApps" + File.separator + "HelloWorldiOS.app";
    private static String APP_WITH_NML_NAME = "sampleApps" + File.separator + "dist" + File.separator + "HelloWorldiOS-instrumented.zip";

    private static boolean IS_EYES_ENABLED = true;
    private static boolean IS_NML = true;
    private static final boolean IS_MULTI_DEVICE = true;
    private static final boolean IS_FULL_RESET = false;

    AppiumNativeiOSHelloWorldEyesNMLTest() {
    }

    @BeforeAll
    static void beforeAll() {
        startAppiumServer();
        String batchName = className + "-NML=" + IS_NML + "-MULTI_DEVICE=" + IS_MULTI_DEVICE + "-" + new File(APP_NAME).getName();
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
        if (IS_NML & IS_EYES_ENABLED) {
            xcuiTestOptions.setCapability("app", new File(APP_WITH_NML_NAME).getAbsolutePath());
            System.out.printf("Add devices to NML configuration using capabilities: %%n%s%n", xcuiTestOptions);
            Eyes.setMobileCapabilities(xcuiTestOptions, APPLITOOLS_API_KEY);
        } else {
            xcuiTestOptions.setCapability("app", new File(APP_NAME).getAbsolutePath());
        }

        System.out.println("XCUITestOptions: " + xcuiTestOptions);
        try {
            driver = new IOSDriver(new URL(APPIUM_SERVER_URL), xcuiTestOptions);
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
        if (IS_NML & IS_MULTI_DEVICE) {
            Configuration config = eyes.getConfiguration();

            config.addMultiDeviceTarget(
                    //                    IosMultiDeviceTarget.iPhone_8(),
                    IosMultiDeviceTarget.iPhone_11(),
                    IosMultiDeviceTarget.iPhone_12_mini(),
                    IosMultiDeviceTarget.iPhone_13_Pro_Max(),
                    IosMultiDeviceTarget.iPhone_14());

            eyes.setConfiguration(config);
        }
        eyes.open(driver, className, testInfo.getTestMethod().get().getName());
    }

    @Test
    void runIOSNativeAppTest() {
        eyes.checkWindow("App launched - checkWindow");
        eyes.check("App launched - target.window", Target.window());
        driver.findElement(AppiumBy.accessibilityId("Make the number below random.")).click();
        waitFor(1);
        eyes.check("MakeRandomNumberCheckbox-window-strict", Target.window().strict());
        eyes.check("MakeRandomNumberCheckbox-window-layout", Target.window().layout());
        WebElement randomNumberElement = getRandomNumberElement(driver, eyes);
        generateRandomNumber(driver);
        waitFor(1);
        randomNumberElement = getRandomNumberElement(driver, eyes);
        eyes.check("MakeRandomNumberCheckbox-region-strict-randomNumber", Target.region(randomNumberElement).strict());
        eyes.check("MakeRandomNumberCheckbox-region-layout-randomNumber", Target.region(randomNumberElement).layout());

        driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox")).click();
        waitFor(1);

        eyes.check("SimulateDiffsCheckbox-layout", Target.window().layout());
        eyes.check("SimulateDiffsCheckbox-strict", Target.window().strict());
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Click me!\"]")).click();
        waitFor(1);
        eyes.checkWindow("Click me!");
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }

    private static void generateRandomNumber(AppiumDriver driver) {
        int numberOfClicks = new Random().nextInt(100) % 10;
        System.out.printf("Click on get random number %d times%n", numberOfClicks);
        for (int i = 0; i < numberOfClicks; i++) {
            driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")).click();
            waitFor(1);
        }
    }

    private static WebElement getRandomNumberElement(AppiumDriver driver, Eyes eyes) {
        List<WebElement> webElementList = driver.findElements(AppiumBy.xpath("//XCUIElementTypeStaticText[@name]"));
        for (WebElement element : webElementList) {
            String text = element.getText();
            System.out.println(text);
            try {
                long randomNumber = Long.parseLong(text);
                System.out.println("Random number: " + randomNumber);
                return element;
            } catch (NumberFormatException e) {
                System.out.println("Not the element we are looking for");
            }
        }
        throw new RuntimeException("Random number not available");
    }

}
