package io.samples.appium.ios;

import com.applitools.eyes.appium.Target;
import io.appium.java_client.AppiumBy;
import io.samples.Hooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class AppiumNativeiOSHelloWorldEyesTest extends Hooks {
    AppiumNativeiOSHelloWorldEyesTest() {
        IS_EYES_ENABLED = true;
        PLATFORM_NAME = "ios";
        IS_NATIVE = true;
    }

    @Test
    void runIOSNativeAppTest() {
        eyes.checkWindow("App launched");
        eyes.check("App launched", Target.window());
        driver.findElement(AppiumBy.accessibilityId("Make the number below random.")).click();
        driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")).click();
        eyes.check("MakeRandomNumberCheckbox", Target.region(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")));
        driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox")).click();
        eyes.check("", Target.window().layout(AppiumBy.accessibilityId("SimulateDiffsCheckbox")));
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Click me!\"]")).click();
        eyes.checkWindow("Click me!");
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
