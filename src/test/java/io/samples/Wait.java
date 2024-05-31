package io.samples;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Wait {

    public static void waitFor(int durationInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec", durationInSec));
            Thread.sleep(durationInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static WebElement waitTillElementIsPresent(WebDriver driver, By elementId) {
        return waitTillElementIsPresent(driver, elementId, 10);
    }

    public static WebElement waitTillElementIsPresent(WebDriver driver, By elementId, int numberOfSecondsToWait) {
        return (new WebDriverWait(driver, Duration.ofSeconds(numberOfSecondsToWait)).until(ExpectedConditions.presenceOfElementLocated(elementId)));
    }

}
