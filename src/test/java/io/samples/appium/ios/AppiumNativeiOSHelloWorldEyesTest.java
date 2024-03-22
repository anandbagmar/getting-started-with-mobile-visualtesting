package io.samples.appium.ios;

import io.samples.Hooks;
import org.junit.jupiter.api.Test;

class AppiumNativeiOSHelloWorldEyesTest extends Hooks {
    AppiumNativeiOSHelloWorldEyesTest() {
        IS_EYES_ENABLED = true;
        PLATFORM_NAME = "ios";
        IS_NATIVE = true;
        IS_NML = false;
    }

    @Test
    void runIOSNativeAppTest() {
        new IOSTestHelper().runHelloWorldTestForiOS(driver, eyes);
    }
}
