package io.samples.appium.ios;

import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static io.samples.Wait.waitFor;

public class IOSTestHelper {
    void runHelloWorldTestForiOS(AppiumDriver driver, Eyes eyes) {
        eyes.checkWindow("App launched - checkWindow");
        eyes.check("App launched - target.window", Target.window());
        driver.findElement(AppiumBy.accessibilityId("Make the number below random.")).click();
        waitFor(1);
        eyes.check("MakeRandomNumberCheckbox-region-element", Target.region(driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox"))));
        eyes.check("MakeRandomNumberCheckbox-region-locator", Target.region(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")));
//        eyes.check("MakeRandomNumberCheckbox-window-combo-locator", Target.window().layout(AppiumBy.name("MakeRandomNumberCheckbox")));
        WebElement randomNumberElement = getRandomNumberElement(driver, eyes);
        generateRandomNumber(driver);
        waitFor(1);
        randomNumberElement = getRandomNumberElement(driver, eyes);
//        eyes.check("MakeRandomNumberCheckbox-window-combo-randomNumber", Target.window().strict(randomNumberElement));
//        eyes.check("MakeRandomNumberCheckbox-window-combo-randomNumber", Target.window().layout(randomNumberElement));
        eyes.check("MakeRandomNumberCheckbox-region-strict-randomNumber", Target.region(randomNumberElement).strict());
        eyes.check("MakeRandomNumberCheckbox-region-layout-randomNumber", Target.region(randomNumberElement).layout());

        driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox")).click();
        waitFor(1);

        eyes.check("SimulateDiffsCheckbox-layout", Target.window().layout());
        eyes.check("SimulateDiffsCheckbox-strict", Target.window().strict());
//        eyes.check("SimulateDiffsCheckbox-window-combo-element", Target.window().layout(driver.findElement(AppiumBy.accessibilityId("SimulateDiffsCheckbox"))));
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Click me!\"]")).click();
        waitFor(1);
        eyes.checkWindow("Click me!");
        Assertions.assertTrue(true, "Test completed. Assertions will be done by Applitools");
    }

    private static void generateRandomNumber(AppiumDriver driver) {
        int numberOfClicks = new Random().nextInt(100) % 10;
        System.out.printf("Click on get random number %d times%n", numberOfClicks);
        for (int i = 0; i <numberOfClicks; i++) {
            driver.findElement(AppiumBy.accessibilityId("MakeRandomNumberCheckbox")).click();
            waitFor(1);
        }
    }

    private static WebElement getRandomNumberElement(AppiumDriver driver, Eyes eyes) {
        List<WebElement> webElementList = driver.findElements(AppiumBy.xpath("//XCUIElementTypeStaticText[@name]"));
        for (WebElement element : webElementList) {
            String text = element.getText();
            System.out.println(text);
            try {
                long randomNumber = Long.parseLong(text);
                System.out.println("Random number: " + randomNumber);
                return element;
            } catch (NumberFormatException e) {
                System.out.println("Not the element we are looking for");
            }
        }
        throw new RuntimeException("Random number not available");
    }
}
