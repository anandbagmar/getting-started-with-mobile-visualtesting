package io.samples.appium.android;

import com.applitools.eyes.appium.Target;
import io.samples.Hooks;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class CalculatorEyesNMLTest extends Hooks {
    private CalculatorEyesNMLTest() {
        IS_EYES_ENABLED = true;
        IS_NATIVE = true;
        IS_NML = true;
        PLATFORM_NAME = "android";
    }

    @Test
    void appiumTest3() {
        eyes.check(Target.window().fully(false).useSystemScreenshot(true).withName("Calculator-SystemScreenshot"));
        eyes.check(Target.window().fully(false).withName("Calculator"));
        int number = 3;
        driver.findElement(By.id("digit_" + number)).click();
        eyes.check("digit_" + number + "-by", Target.region(By.id("digit_" + number)));
    }

    @Test
    void appiumTest4() {
        eyes.checkWindow("Calculator!");
        int number = 4;
        driver.findElement(By.id("digit_" + number)).click();
        eyes.check("digit_" + number + "-by", Target.region(By.id("digit_" + number)));
    }
}
