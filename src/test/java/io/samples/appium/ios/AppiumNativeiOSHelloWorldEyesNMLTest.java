package io.samples.appium.ios;

import io.samples.Hooks;
import org.junit.jupiter.api.Test;

class AppiumNativeiOSHelloWorldEyesNMLTest extends Hooks {
    AppiumNativeiOSHelloWorldEyesNMLTest() {
        IS_EYES_ENABLED = true;
        PLATFORM_NAME = "ios";
        IS_NATIVE = true;
        IS_NML = true;
    }

    @Test
    void runIOSNativeAppTest() {
        new IOSTestHelper().runHelloWorldTestForiOS(driver, eyes);
    }
}
