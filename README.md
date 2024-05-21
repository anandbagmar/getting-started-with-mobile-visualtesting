# getting-started-with-mobile-visualtesting

This project contains example Appium Java tests that use the 
[Applitools Eyes Visual AI](https://applitools.com/platform/eyes/) service and the
[Eyes Appium Java SDK](https://applitools.com/docs/api-ref/category/appium-java) to
perform visual UI tests on an example native mobile Android application.

If you want example visual UI tests for a browser-based web application, try
[Anand Bagmar's getting-started-with-visualtesting repo](https://github.com/anandbagmar/getting-started-with-visualtesting)
instead.

### Install Appium and related drivers & plugins

The NPM package manager can install the Appium service package and all of its related 
dependencies and utilities for you.

Run this command from a terminal prompt in the project root directory where the 
`package.json` file is stored.
```bash
npm install
```

## Usage

You will need to configure the Applitools Eyes Appium SDK, and launch an Android device
emulator on your local system with the example application installed before you run the
example tests in this project.

If you're testing your application using a mobile device grid, you should already know how
to connect an Appium test to your test device.  The instructions in this document describe
how to connect the tests to an emulator running on your computer.

**Note:** whether you plan to test the example application using a local emulator or a
remote device, you will still need to configure the Eyes Appium SDK as described below.

### Set your APPLITOOLS_API_KEY

First, 
[retrieve your API key](https://applitools.com/docs/topics/overview/obtain-api-key.html) 
for your user account from the Eyes Dashboard UI.  The Eyes Appium SDK will use this value 
as an authentication token to upload visual UI checks to your Eyes test results dashboard.

There are two ways to 
[provide your Applitools API key to the Eyes SDK](https://applitools.com/tutorials/quickstart/web/selenium/java/basic#setting-applitools-variables).

1. Store it the `APPLITOOLS_API_KEY` environment variable or `eyes.api_key` property.
   The Eyes SDK will read the value from there automatically, and you do not need to set 
   it explicitly in your test code.
  
2. You can hard-code your API key in the test source code by replacing 
   [line 105 of the io.samples.Hooks.java](src/test/java/android/io.samples.Hooks.java#L105) source file

>> `eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));`

> with 

>> `eyes.setApiKey("<YOUR_API_KEY_GOES_HERE>");`

***Note:*** Your Applitools API key is a secret value.  The tests you run will
use your API key as an authentication token to connect to the Eyes Service, so 
**treat it like a password** and do not share it with anyone.

**If you have hard-coded your API key in the example test code, do not to push the source code file that includes your secret API key to a public GitHub repo!**


### Start an Android device emulator

[Create an Android Virtual Device in Android Studio.](https://developer.android.com/studio/run/managing-avds)

[Run an emulator in Andriod Studio.](https://developer.android.com/studio/run/managing-avds#emulator)


### Install the example application

This project's sampleApps directory contains an APK file for the example Android 
Calculator native mobile application.

To 
[install the app on your emulated Android device](https://developer.android.com/studio/run/emulator-install-add-files), 
select the `AndroidCalculator.apk` file and drag it onto the emulated device's main 
screen.  Once you drop that file onto the emulator, you should see a new icon named 
Calculator appear among the other Android application icons installed on the device.

You don't need to start, nor launch the example app within the emulator before running your first
test.  The tests will automatically install, start and stop the example app as needed.

### Start the Appium service

The tests will automatically start an Appium server before running the first test method,
and stop the Appium server after the last test method in each example test class.

You do not need to start the Appium server yourself before launching your first test.

### Launch the JUnit tests

The easiest way to run a single example JUnit test is to launch it from within your IDE.
For example you can run the test classes from within the Eclipse IDE, by right-clicking on
one of the test source class files and choosing **Run As -> JUnit Test** from the context
pop-up menu.

You can also launch the tests from a terminal command line prompt using the Maven `test` 
goal by executing the following command.

```bash
./gradlew clean test --tests <test_name>
```

## Example Test Source Files

### Android
* [CalculatorTest.java](src/test/java/android/CalculatorTest.java) - A basic Appium test, *without* Applitools Eyes
   ```bash
   ./gradlew clean test --tests CalculatorTest
   ```

* [CalculatorEyesTest.java](src/test/java/android/CalculatorEyesTest.java) - A basic Appium test, *with* Applitools Eyes
   ```bash
   ./gradlew clean test --tests CalculatorEyesTest
   ```
  
* [CalculatorEyesNMLTest.java](src/test/java/android/CalculatorEyesNMLTest.java) - A basic appium test *with* Applitools Eyes and uses the *Native Mobile Library and MultiViewPort capability* of Applitools for a native iOS app
   ```bash
   ./gradlew clean test --tests CalculatorEyesNMLTest
   ```

### iOS

* [WebiOSHelloWorldTest.java](src/test/java/ios/WebiOSHelloWorldTest.java) - A basic appium test *without* Applitools Eyes to test a web application (mobile-web) using a safari browser in a iOS device, 
   ```bash
   ./gradlew clean test --tests WebiOSHelloWorldTest
   ```

* [WebiOSHelloWorldEyesTest.java](src/test/java/ios/WebiOSHelloWorldEyesTest.java) - A basic appium test *with* Applitools Eyes to test a web application (mobile-web) using a safari browser in a iOS device,
   ```bash
   ./gradlew clean test --tests WebiOSHelloWorldEyesTest
   ```

* [AppiumNativeiOSHelloWorldTest.java](src/test/java/io/samples/appium/ios/AppiumNativeiOSHelloWorldTest.java) - A basic appium test *without* Applitools Eyes for a native iOS app
   ```bash
   ./gradlew clean test --tests AppiumNativeiOSHelloWorldTest
   ```

* [AppiumNativeiOSHelloWorldEyesTest.java](src/test/java/io/samples/appium/ios/AppiumNativeiOSHelloWorldEyesTest.java) - A basic appium test *with* Applitools Eyes for a native iOS app 
   ```bash
   ./gradlew clean test --tests AppiumNativeiOSHelloWorldEyesTest
   ```
  
  * [AppiumNativeiOSHelloWorldEyesNMLTest.java](src/test/java/io/samples/appium/ios/AppiumNativeiOSHelloWorldEyesNMLTest.java) - A basic appium test *with* Applitools Eyes and uses the *Native Mobile Library and MultiViewPort capability* of Applitools for a native iOS app
     ```bash
     ./gradlew clean test --tests AppiumNativeiOSHelloWorldEyesNMLTest
     ```
