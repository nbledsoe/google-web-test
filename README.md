# google-web-test

Selenium automation testing project (Maven based) written in Java with [Selenium](http://www.seleniumhq.org) Webdriver and [TestNG](http://testng.org).

## Installation and running

Requirements are Maven 3.x and JDK 1.8 or higher ready to use.

First [download](http://www.seleniumhq.org/download/) the browser drivers for required browser and 
configure it in `pom.xml`. Look for:

```xml
    <webdriver.gecko.driver>drivers/geckodriver.exe</webdriver.gecko.driver>
    <webdriver.chrome.driver>drivers/chromedriver.exe</webdriver.chrome.driver>
    <webdriver.ie.driver>drivers/IEDriverServer.exe</webdriver.ie.driver>
    <webdriver.edge.driver>drivers/MicrosoftWebDriver.exe</webdriver.edge.driver>
```

You can create `drivers` directory in the project location and put the driver executable files here.

Run all tests with the following command: 

```mvn test```

# Configuration

First there is a `pom.xml` file with a lot of configuration possibilities.

The most important setting is `webtest.testng`. By default it is set to `all` indicating the TestNG XML configuration file `all.testng.xml` in the `src\test\resources\` folder.