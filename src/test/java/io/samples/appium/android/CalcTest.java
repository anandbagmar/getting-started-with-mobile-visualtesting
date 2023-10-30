package io.samples.appium.android;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class CalcTest extends AndroidHooks {

    @Test
    void calcTest() {
        int p1 = 3;
        int p2 = 5;
        driver.findElement(By.id("digit" + p1))
                .click();
        driver.findElement(By.id("plus"))
                .click();
        driver.findElement(By.id("digit" + p2))
                .click();
        driver.findElement(By.id("equal"))
                .click();
    }
}
