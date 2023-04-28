package AppiumTests;

import com.applitools.eyes.appium.Target;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class FirstEyesTest extends Hooks {
    @Test
    public void appiumTest1() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 1))
              .click();
        eyes.check("digit-1-by", Target.region(By.id("digit" + 1)));
//        eyes.checkWindow("digit" + 2);
//        driver.findElement(By.id("minus"))
//              .click();
//        eyes.checkWindow("plus");
//        driver.findElement(By.id("digit" + 3))
//              .click();
//        eyes.check("digit-3-byElement", Target.region(driver.findElement(By.id("digit" + 3))));
//        eyes.checkWindow("digit" + 3);
//        driver.findElement(By.id("equal"))
//              .click();
//        eyes.checkWindow("Calc works!");
    }

    @Test
    public void appiumTest2() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 2))
              .click();
        eyes.check("digit-2-by", Target.region(By.id("digit" + 2)));
//        eyes.checkWindow("digit" + 2);
//        driver.findElement(By.id("minus"))
//              .click();
//        eyes.checkWindow("plus");
//        driver.findElement(By.id("digit" + 4))
//              .click();
//        eyes.check("digit-4-byElement", Target.region(driver.findElement(By.id("digit" + 4))));
//        eyes.checkWindow("digit" + 4);
//        driver.findElement(By.id("equal"))
//              .click();
//        eyes.checkWindow("Calc works!");
    }
}