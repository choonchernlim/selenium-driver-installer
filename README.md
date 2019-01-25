# selenium-driver-installer [![Build Status](https://travis-ci.org/choonchernlim/selenium-driver-installer.svg?branch=master)](https://travis-ci.org/choonchernlim/selenium-driver-installer) [![codecov](https://codecov.io/gh/choonchernlim/selenium-driver-installer/branch/master/graph/badge.svg)](https://codecov.io/gh/choonchernlim/selenium-driver-installer)

Dynamically installs the latest Selenium browser driver and set its system property. Supported browser driver:-

* Chrome - `webdriver.chrome.driver` system property will be set to the installed driver path.

## Maven Dependency

```xml
<dependency>
    <groupId>com.github.choonchernlim</groupId>
    <artifactId>selenium-driver-installer</artifactId>
    <version>0.1.0</version>
</dependency>
```

## How to Use It

```groovy
class ServiceNowChangeRequestSeleniumClient {
    static void main(String[] args) {
        // Add this line... and that's it.
        SeleniumDriverInstaller.chrome()

        final WebDriver driver = new ChromeDriver()
        ...
    }
}
```