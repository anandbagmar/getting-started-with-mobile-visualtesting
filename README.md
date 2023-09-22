This repo contains appium-java tests with Applitools integrated for Visual Testing.
See this repo for Selenium-java tests with Applitools - https://github.com/anandbagmar/getting-started-with-visualtesting

# getting-started-with-mobile-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
> eyes.setApiKey("<replace_me>");

* If running Appium tests, you need to connect your device / start emulator
* Install appium using the provided package.json
  * `npm install`
* The test will automatically start/stop the appium server

## Samples included:

> CalcTest -> basic appium test, *without* Applitools
 
> FirstEyesTest -> appium test, *with* Applitools integrated
> SecondEyesTest -> appium test, *with* Applitools integrated (same as FirstEyesTest, but helpful to demonstrate multiple tests running)

## Running the tests

You can run the test directly from any IDE, OR, you can run the test from the command line using the command:

> mvn clean test -Dtest=<test_name>