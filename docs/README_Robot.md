Back to main [README](../README.md)

# Running tests with Robot framework

This is the example project for the [Robot Framework tutorial](https://applitools.com/tutorials/quickstart/web/robot-framework).
It shows how to start automating visual tests  with [Applitools Eyes](https://applitools.com/platform/eyes/) and [Robot Framework](https://robotframework.org/) for Native Android apps

It uses:

* [Robot Framework](https://robotframework.org/) as the core test framework
* [Python](https://www.python.org/) as the programming platform underneath Robot Framework
* [Appium](https://www.appium.io/) for native app automation underneath Robot Framework
* [pip](https://packaging.python.org/en/latest/tutorials/installing-packages/) for dependency management
* [Applitools Eyes](https://applitools.com/platform/eyes/) for visual testing

To run this example project, you'll need:

1. An [Applitools account](https://auth.applitools.com/users/register), which you can register for free
2. [Python 3](https://www.python.org/) version 3.6 or higher
3. A good editor with Robot Framework support like [Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=robocorp.robotframework-lsp)
4. An android device (emulator or real device)
5. Appium server should be started manually (`appium`)

# Install dependencies

```
python3 -m pip install -r src/test/robot/requirements.txt
```

The main test case spec is [`acme_bank.robot`](acme_bank.robot).
By default, the project will run tests with Ultrafast Grid.
You can control how Applitools runs by changing the `EyesLibrary` `runner` setting.

To execute tests, set the `APPLITOOLS_API_KEY` environment variable
to your [account's API key](https://applitools.com/tutorials/guides/getting-started/registering-an-account),
and then run:

## [Setup and start Appium server](README_MachineSetupInstructions.md)

## [Start the Emulator](README_MachineSetupInstructions.md)

## Run tests against the Emulator 
```
APPLITOOLS_LOG_DIR=reports/robot/applitools-logs robot --outputdir reports/robot src/test/robot/calculator_android.robot
```

**For full instructions on running this project, take our
[Robot Framework tutorial](https://applitools.com/tutorials/quickstart/web/robot-framework)!**

Back to main [README](../README.md)
