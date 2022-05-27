package pl.webtest.test.google;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import org.testng.annotations.Test;

import pl.webtest.test.SeleniumTestBase;

public class GoogleSearchSeleniumTest extends SeleniumTestBase {
	private static final Logger logger = LoggerFactory.getLogger(GoogleSearchSeleniumTest.class);

	
    @Test
    public void googleSuggestTest() {

    	getWebDriver().get("http://www.google.com/webhp?complete=1&hl=en");

        WebElement query = getWebDriver().findElement(By.name("q"));
        query.sendKeys("selenium");

        WebDriverWait wait = new WebDriverWait(getWebDriver(), 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sbsb_b")));

        List<WebElement> allSuggestions = getWebDriver().findElements(By.className("sbsb_b"));
        
        assertThat(allSuggestions.size(), greaterThan(0));
        Reporter.log("Google search successful");

        List<String> results = new ArrayList<String>();
        for (WebElement suggestion : allSuggestions) {
        	results.add(suggestion.getText());
        }
        Reporter.log("Results: " + StringUtils.join(results, ", "));
    }
}