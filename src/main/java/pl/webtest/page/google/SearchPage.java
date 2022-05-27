package pl.webtest.page.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pl.webtest.page.Page;

/**
 * Google search page
 */
public class SearchPage extends Page {

	@FindBy(name = "q")
	private WebElement searchBox;
	
    public SearchPage(WebDriver webDriver) {
        super(webDriver);
    }

	public SearchResultsPage search(String text) {
		searchBox.sendKeys(text);
		searchBox.submit();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ires")));
		
		return getInstance(webDriver, SearchResultsPage.class);
	}

}