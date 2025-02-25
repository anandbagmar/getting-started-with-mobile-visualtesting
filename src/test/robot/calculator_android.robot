*** Settings ***
| Library | AppiumLibrary | run_on_failure=AppiumLibrary.CapturePageScreenshot
Library     Process
Library     Collections
Library     OperatingSystem
Library     EyesLibrary       runner=mobile_native    config=applitools.yaml
Resource    Resource/init.robot

# Declare setup and teardown routines.
Test Setup        Setup
Test Teardown     Teardown

*** Variables ***
${DIGIT_1_BTN} =    //android.widget.ImageButton[@content-desc="1"]
${DIGIT_2_BTN} =    //android.widget.ImageButton[@content-desc="2"]
${DIGIT_8_BTN} =    //android.widget.ImageButton[@content-desc="8"]
${DIGIT_4_BTN} =    //android.widget.ImageButton[@content-desc="4"]
${ADD_BTN} =        //android.widget.ImageButton[@content-desc="plus"]
${SUBTRACT_BTN} =   //android.widget.ImageButton[@content-desc="minus"]
${EQ_BTN} =         //android.widget.ImageButton[@content-desc="equals"]
${APPIUM_HOST}    127.0.0.1
${APPIUM_PORT}    4723

*** Keywords ***
# For setup, load the demo site's login page and open Eyes to start visual testing.
Setup
    Open Application  ${APPIUM_SERVER}
    ...    automationName=${AUTOMATION_NAME}
    ...    platformName=${PLATFORM_NAME}
    ...    deviceName=${DEVICE_NAME}
    ...    app=${APP}
    Eyes Open

# For teardown, close Eyes and the browser.
Teardown
    Eyes Close Async
    Close application

*** Test Cases ***
# This test performs a calculator operation in Android device
# The interactions use typical Selenium WebDriver calls,
# but the verifications use one-line snapshot calls with Applitools Eyes.
# If the page ever changes, then Applitools will detect the changes and highlight them in the Eyes Test Manager.
# Traditional assertions that scrape the page for text values are not needed here.

Add
    Log to console      Calculator launched
    Eyes Check Window    Calculator launched

    Log to console      "Click 1"
    Click Element       ${DIGIT_1_BTN}
    Eyes Check Window    "1"

    Log to console      "Click +"
    Click Element       ${ADD_BTN}
    Eyes Check Window    "ADD"

    Log to console      "Click 2"
    Click Element       ${DIGIT_2_BTN}
    Eyes Check Window    "2"

    Log to console      "Click ="
    Click Element       ${EQ_BTN}
    Eyes Check Window    "EQ"

Subtract
    Log to console      Calculator launched
    Eyes Check Window    Calculator launched

    Log to console      "Click 8"
    Click Element       ${DIGIT_8_BTN}
    Eyes Check Window    "1"

    Log to console      "Click -"
    Click Element       ${SUBTRACT_BTN}
    Eyes Check Window    "MINUS"

    Log to console      "Click 4"
    Click Element       ${DIGIT_4_BTN}
    Eyes Check Window    "2"

    Log to console      "Click ="
    Click Element       ${EQ_BTN}
    Eyes Check Window    "EQ"
