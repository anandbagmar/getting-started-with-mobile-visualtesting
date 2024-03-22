package io.samples.appium.ios;

import io.appium.java_client.AppiumBy;
import io.samples.Hooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class AppiumNativeiOSHelloWorldTest extends Hooks {
    AppiumNativeiOSHelloWorldTest() {
        IS_EYES_ENABLED = false;
        PLATFORM_NAME = "ios";
        IS_NATIVE = true;
    }

    @Test
    void runIOSNativeAppTest() {
        driver.findElement(AppiumBy.accessibilityId("Make the number below random.")).click();
        driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")).click();
        driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox")).click();
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Click me!\"]")).click();
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
