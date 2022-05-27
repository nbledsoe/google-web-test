package pl.webtest.test.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pl.webtest.test.SeleniumTestBase;

public class GoogleTranslateSeleniumTest extends SeleniumTestBase {
	
    @Test
    @Parameters({"fromLang", "toLang", "translateString", "expectedResult"})
    public void googleTranslateTest(String fromLang, String toLang, String translateString, String expectedResult) {

    	getWebDriver().get("https://translate.google.pl/#" + fromLang + "/" + toLang);

        WebElement source = getWebDriver().findElement(By.id("source"));
        source.sendKeys("selenium");
        
        getWebDriver().findElement(By.id("gt-submit")).click();

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 5);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("result_box"), expectedResult));

        Reporter.log("Google translation successful");
    }

}
