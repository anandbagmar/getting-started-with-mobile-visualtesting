package io.samples.appium.android;

import com.applitools.eyes.appium.Target;
import io.samples.Hooks;
import io.samples.Wait;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class CalculatorEyesNMLTest extends Hooks {
    private CalculatorEyesNMLTest() {
        IS_EYES_ENABLED = true;
        IS_NATIVE = true;
        IS_NML = true;
        PLATFORM_NAME = "android";
        APK_NAME = "Calculator_8.4.1.apk";
        PACKAGE_NAME = "com.google.android.calculator";
        ACTIVITY_NAME = "com.android.calculator2.Calculator";
    }

    @Test
    void calcNMLTest() {
        System.out.println("Launched");
        eyes.checkWindow("Launched");
        int number = 3;
        driver.findElement(By.id("digit_" + number)).click();
        System.out.println("Clicked 3");
        eyes.check("digit_" + number + "-by", Target.window().layout(By.id("digit_" + number)));
        Wait.waitFor(5);
        System.out.println("Done clicked 3");
    }
}
