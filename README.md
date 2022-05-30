This repo contains appium-java tests with Applitools integrated for Visual Testing.
See this repo for Selenium-java tests with Applitools - https://github.com/anandbagmar/getting-started-with-visualtesting

# getting-started-with-mobile-visualtesting

* Set APPLITOOLS_API_KEY as an environment variable, OR, replace the line:
> eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

with 
> eyes.setApiKey("<replace_me>");

* If running Appium tests, you need to connect your device / start emulator
* Start appium on the local machine

## Samples included:

> Appium_Native_Calc_Test -> basic appium test, *without* Applitools
 
> Appium_Native_Cals_EyesTest -> appium test, *with* Applitools integrated

## Running the tests

You can run the test directly from any IDE, OR, you can run the test from the command line using the command:

> mvn clean test -Dtest=<test_name>