package io.samples.appium.ios;

import com.applitools.eyes.appium.Target;
import io.appium.java_client.AppiumBy;
import io.samples.Hooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static io.samples.Wait.waitFor;

class AppiumNativeiOSHelloWorldEyesTest extends Hooks {
    AppiumNativeiOSHelloWorldEyesTest() {
        IS_EYES_ENABLED = true;
        PLATFORM_NAME = "ios";
        IS_NATIVE = true;
        IOS_APP=HELLO_WORLD;
    }

    @Test
    void runIOSNativeAppTest() {
        eyes.checkWindow("App 1 launched");
        eyes.check("App launched", Target.window());
        driver.findElement(AppiumBy.accessibilityId("Make the number below random.")).click();
        waitFor(1);
        eyes.check("MakeRandomNumberCheckbox-1", Target.region(driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox"))));
        driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")).click();
        waitFor(1);
        eyes.check("MakeRandomNumberCheckbox-2", Target.region(driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox"))));
        driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox")).click();
        waitFor(1);
        eyes.check("SimulateDiffsCheckbox-layout", Target.window().layout());
        eyes.check("SimulateDiffsCheckbox-strict", Target.window().strict());
        eyes.check("SimulateDiffsCheckbox-region", Target.window().layout(driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox"))));
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Click me!\"]")).click();
        waitFor(1);
        eyes.checkWindow("Click me!");
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
