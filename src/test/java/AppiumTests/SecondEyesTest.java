package AppiumTests;

import com.applitools.eyes.appium.Target;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

class SecondEyesTest
        extends Hooks {

    @Test
    public void appiumTest3() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 3))
              .click();
        eyes.check("digit-3-by", Target.region(By.id("digit" + 3)));
    }

    @Test
    public void appiumTest4() {
        eyes.checkWindow("Calculator!");
        driver.findElement(By.id("digit" + 4))
              .click();
        eyes.check("digit-4-by", Target.region(By.id("digit" + 4)));
    }
}