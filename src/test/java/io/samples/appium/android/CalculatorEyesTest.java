package io.samples.appium.android;

import com.applitools.eyes.appium.Target;
import io.samples.Hooks;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class CalculatorEyesTest extends Hooks {
    private CalculatorEyesTest() {
        IS_EYES_ENABLED = true;
        IS_NATIVE = true;
        PLATFORM_NAME = "android";
    }

    @Test
    void calculatorTest_id() {
        eyes.check("Calculator!-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("Calculator!");

        int p1 = 3;
        int p2 = 5;

        driver.findElement(By.id("digit_" + p1)).click();
        eyes.check("digit_" + p1 + "-byElement", Target.region(By.id("digit_" + p1)));

        driver.findElement(By.id("op_add")).click();
        eyes.check("op_add-byElement", Target.region(By.id("op_add")));

        driver.findElement(By.id("digit_" + p2)).click();
        eyes.check("digit_" + p2 + "-byElement", Target.region(By.id("digit_" + p2)));

        driver.findElement(By.id("eq")).click();
        eyes.check("eq-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("eq");
    }

    @Test
    void calculatorTest_full() {
        eyes.check("Calculator!-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("Calculator!");

        int p1 = 5;
        int p2 = 6;

        driver.findElement(By.id("digit_" + p1)).click();
        eyes.check("digit_" + p1 + "-byElement", Target.region(driver.findElement(By.id("digit_" + p1))));

        driver.findElement(By.id("op_add")).click();
        eyes.check("op_add-byElement", Target.region(driver.findElement(By.id("op_add"))));

        driver.findElement(By.id("digit_" + p2)).click();
        eyes.check("digit_" + p2 + "-byElement", Target.region(driver.findElement(By.id("digit_" + p2))));

        driver.findElement(By.id("eq")).click();
        eyes.check("eq-ignoreCaret", Target.window().ignoreCaret(true));
        eyes.checkWindow("eq");
    }
}
