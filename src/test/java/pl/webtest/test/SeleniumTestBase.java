package pl.webtest.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import pl.webtest.Config;
import pl.webtest.WebDriverFactory;
import pl.webtest.listener.ScreenshotListener;

/**
 * Base class for all selenium test classes
 */
@Listeners(ScreenshotListener.class)
public abstract class SeleniumTestBase {
	private static final Logger logger = LoggerFactory.getLogger(SeleniumTestBase.class);
    private static List<WebDriver> webDriverPool = Collections.synchronizedList(new ArrayList<WebDriver>());
    private static ThreadLocal<WebDriver> webDriver = null;
    
    /**
     * Webdriver setup for a test suite.
     * 
	 * @param browser (firefox by default) name of the browser
	 * @param gridHubUrl selenium grid hub URL. Leave it empty to create a local browser @code WebDriver
	 * @param browserPlatform desired browser platform
	 * @param browserVersion desired browser version
	 * @param firefoxProfileName firefox profile name
	 * @param firefoxProfilePath firefox profile path
	 * @param javascriptEnabled enable javascript on the browser (true by default)
     */
    @BeforeSuite(alwaysRun = true)
    @Parameters({"webtest.browser", "webtest.gridHubUrl", 
    	"webtest.browserPlatform", "webtest.browserVersion",
    	"webtest.firefox.profileName", "webtest.firefox.profilePath",
    	"webtest.javascriptEnabled"})
    public static void setupWebDriver(
    		@Optional("firefox") final String browser, 
    		@Optional final String gridHubUrl,
    		@Optional final String browserPlatform, 
    		@Optional final String browserVersion, 
    		@Optional final String firefoxProfileName,
    		@Optional final String firefoxProfilePath,
    		@Optional("true") final boolean javascriptEnabled) {

    	webDriver = new ThreadLocal<WebDriver>() {
            @Override
            protected WebDriver initialValue() {
            	DesiredCapabilities capabilities;
            	WebDriverFactory wdf;
            	
            	String message = String.format("Webdriver configuration ["
            			+ "browser: %s, "
            			+ "grid: %s, "
            			+ "platform: %s, "
            			+ "version: %s, "
            			+ "firefox profile name: %s, "
            			+ "firefox profile path: %s "
            			+ "javascript: %b]", 
            			browser, 
            			gridHubUrl,
            			browserPlatform,
            			browserVersion,
            			firefoxProfileName,
            			firefoxProfilePath,
            			javascriptEnabled);
            	Reporter.log(message);
            	logger.info(message);
            	
            	if(StringUtils.isEmpty(gridHubUrl)) {
            		capabilities = new DesiredCapabilities();
            		wdf = new WebDriverFactory(browser);
            	} else {
        			if (BrowserType.CHROME.equalsIgnoreCase(browser)) {
        				capabilities = DesiredCapabilities.chrome();
        			} else if (BrowserType.FIREFOX.equalsIgnoreCase(browser)) {
        				capabilities = DesiredCapabilities.firefox();
        			} else if (BrowserType.IE.equalsIgnoreCase(browser)) {
        				capabilities = DesiredCapabilities.internetExplorer();
        	        } else if (BrowserType.SAFARI.equalsIgnoreCase(browser)) {
        	        	capabilities = DesiredCapabilities.safari();
        	        } else if (BrowserType.HTMLUNIT.equalsIgnoreCase(browser)) {
        	        	capabilities = DesiredCapabilities.htmlUnit();
        			} else {
        				throw new RuntimeException("Unsupported browser: " + browser);
        			}

        			if (StringUtils.isNotEmpty(browserPlatform)) {
        				capabilities.setPlatform(Platform.valueOf(browserPlatform.toUpperCase()));
        			} else {
        				capabilities.setPlatform(Platform.ANY);
        			}

        			if (browserVersion != null) {
        				capabilities.setVersion(browserVersion);
        			}   
        			
        			wdf = new WebDriverFactory(gridHubUrl);
            	}            	

            	if (BrowserType.CHROME.equalsIgnoreCase(browser)) {
	                String[] switches={"--ignore-certificate-errors", "--disable-popup-blocking", "--disable-translate"};
	                capabilities.setCapability("chrome.switches",Arrays.asList(switches));
            	}

            	if (BrowserType.IE.equalsIgnoreCase(browser)) {
            		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            	}

            	if (BrowserType.FIREFOX.equalsIgnoreCase(browser)) {
            		FirefoxProfile profile = null;
            		if(StringUtils.isNotEmpty(firefoxProfileName)) {
                		ProfilesIni profilesIni = new ProfilesIni();
                		profile = profilesIni.getProfile(firefoxProfileName);
            		} else if (StringUtils.isNotEmpty(firefoxProfilePath)) {
            			profile = new FirefoxProfile(new File(firefoxProfilePath));
            		}
            		if (profile != null) {
                		
                		capabilities.setCapability(FirefoxDriver.PROFILE, profile);            			
            		}
            	}  

            	capabilities.setJavascriptEnabled(javascriptEnabled);

                WebDriver webDriver = wdf.createDriver(capabilities);

                webDriver.manage().timeouts().implicitlyWait(
                        Config.WEBDRIVER_TIMEOUTS_IMPLICITY_WAIT,
                        TimeUnit.SECONDS);
                webDriver.manage().window().maximize();

                webDriverPool.add(webDriver);
                
                return webDriver;
            }
        };    	
    }
    
    @AfterMethod
    public static void clearCookies() {
    	getWebDriver().manage().deleteAllCookies();
    }     

	@AfterSuite(alwaysRun = true)
	public static void quitWebDriver() {
		for (WebDriver webDriver : webDriverPool) {
			webDriver.quit();
		}
	}   
    
    public static WebDriver getWebDriver() {
        return webDriver.get();
    }
}
