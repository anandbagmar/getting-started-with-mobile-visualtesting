package io.samples.appium.android;

import com.applitools.eyes.appium.Target;
import io.samples.Hooks;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class SecondEyesTest extends Hooks {
    private SecondEyesTest() {
        IS_EYES_ENABLED = true;
        IS_NATIVE = true;
        PLATFORM_NAME = "android";
    }

    @Test
    void appiumTest3() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 3))
                .click();
        eyes.check("digit-3-by", Target.region(By.id("digit" + 3)));
    }

    @Test
    void appiumTest4() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 4))
                .click();
        eyes.check("digit-4-by", Target.region(By.id("digit" + 4)));
    }
}