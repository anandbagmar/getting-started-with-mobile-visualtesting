package io.samples.appium.android;

import io.samples.Hooks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class CalcTest extends Hooks {
    CalcTest() {
        IS_EYES_ENABLED = false;
        IS_NATIVE = true;
        PLATFORM_NAME = "android";
    }

    @Test
    void calcTest() {
        int p1 = 3;
        int p2 = 5;
        driver.findElement(By.id("digit" + p1)).click();
        driver.findElement(By.id("plus")).click();
        driver.findElement(By.id("digit" + p2)).click();
        driver.findElement(By.id("equal")).click();
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }
}
