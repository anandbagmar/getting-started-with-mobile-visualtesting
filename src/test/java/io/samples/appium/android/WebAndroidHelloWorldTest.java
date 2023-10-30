package io.samples.appium.android;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static io.samples.Wait.waitFor;

class WebAndroidHelloWorldTest extends AndroidHooks {
    private WebAndroidHelloWorldTest() {
        IS_NATIVE = false;
    }

    @Test
    void runAndroidWebTest() {
        driver.get("https://applitools.com/helloworld");
        for (int stepNumber = 0; stepNumber < 2; stepNumber++) {
            driver.findElement(By.linkText("?diff1")).click();
            waitFor(1);
        }
        driver.findElement(By.tagName("button")).click();
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
