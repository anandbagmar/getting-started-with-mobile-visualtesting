package io.samples.appium.ios;

import io.samples.Hooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static io.samples.Wait.waitFor;

class WebiOSHelloWorldTest extends Hooks {
    private WebiOSHelloWorldTest() {
        IS_EYES_ENABLED = false;
        PLATFORM_NAME = "ios";
        IS_NATIVE = false;
    }

    @Test
    void runIOSWebTest() {
        driver.get("https://applitools.com/helloworld");
        waitFor(2);
        for (int stepNumber = 0; stepNumber < 2; stepNumber++) {
            By linkText = By.linkText("?diff1");
            driver.findElement(linkText).click();
            waitFor(1);
        }
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
