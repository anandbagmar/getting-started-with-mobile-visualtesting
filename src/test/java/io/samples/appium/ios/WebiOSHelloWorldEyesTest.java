package io.samples.appium.ios;

import io.samples.Hooks;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.Date;

import static io.samples.Wait.waitFor;

class WebiOSHelloWorldTest extends IOSHooks {
    private WebiOSHelloWorldTest() {
        IS_NATIVE = false;
    }
    @Test
    void runIOSWebTest() throws InterruptedException {
        System.out.println("Start time: " + new Date());
        waitFor(3);
        driver.get("https://applitools.com/helloworld");
        for (int stepNumber = 0; stepNumber < 5; stepNumber++) {
            driver.findElement(By.linkText("?diff1")).click();
            waitFor(1);
        }

        driver.findElement(By.tagName("button")).click();
        driver.quit();

        System.out.println("End time: " + new Date());
    }


//    private AppiumDriver createAppiumDriver() {
//        // Appium 1.x
//        // DesiredCapabilities dc = new DesiredCapabilities();
//
//        // Appium 2.x
//        XCUITestOptions dc = new XCUITestOptions();
//
//        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
//        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
//        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, PLATFORM_VERSION);
//        dc.setCapability(MobileCapabilityType.DEVICE_NAME, DEVICE_NAME);
//        dc.setCapability(MobileCapabilityType.UDID, UDID);
//        dc.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
//        dc.setCapability(MobileCapabilityType.APP, "io.appium.SafariLauncher");
//
//        return new AppiumDriver(getAppiumServerUrl(), dc);
//    }
}
