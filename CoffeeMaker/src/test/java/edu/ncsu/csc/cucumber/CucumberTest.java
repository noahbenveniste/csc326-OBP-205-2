package edu.ncsu.csc.cucumber;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Base Cucumber test. Contains helper methods for checking text.
 *
 * @author Kai Presler-Marshall
 * @author Elizabeth Gilbert
 */
abstract class CucumberTest {

    static {
        ChromeDriverManager.getInstance().setup();
    }

    final static private String   OS = System.getProperty( "os.name" );

    static protected ChromeDriver driver;

    static public void setup () {

        final ChromeOptions options = new ChromeOptions();
        options.addArguments( "headless" );
        options.addArguments( "window-size=1200x600" );
        options.addArguments( "blink-settings=imagesEnabled=false" );

        if ( Linux() ) {
            options.setBinary( "/usr/bin/google-chrome" );
        }
        else if ( Windows() ) {
            options.setBinary( "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" );
        }
        else if ( Mac() ) {
            options.setBinary( "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" );
        }

        driver = new ChromeDriver( options );

    }

    static private boolean Mac () {
        return OS.contains( "Mac OS X" );
    }

    static private boolean Linux () {
        return OS.contains( "Linux" );
    }

    static private boolean Windows () {
        return OS.contains( "Windows" );
    }

    static public void tearDown () {
        driver.close();
        driver.quit();

        if ( Windows() ) {
            windowsKill();
        }
        else if ( Linux() || Mac() ) {
            unixKill();
        }

    }

    protected void waitForAngular () {
        new NgWebDriver( driver ).waitForAngularRequestsToFinish();
    }

    static private void windowsKill () {
        try {
            Runtime.getRuntime().exec( "taskkill /f /im chrome.exe" );
            Runtime.getRuntime().exec( "taskkill /f /im chromedriver.exe" );
        }
        catch ( final Exception e ) {
        }
    }

    static private void unixKill () {
        try {
            Runtime.getRuntime().exec( "pkill -f chromium-browser" );
            Runtime.getRuntime().exec( "pkill -f chrome" );
            Runtime.getRuntime().exec( "pkill -f chromedriver" );
        }
        catch ( final Exception e ) {
        }

    }

    /**
     * Asserts that the text is on the page
     *
     * @param text
     *            text to check
     * @param driver
     *            web driver
     */
    public void assertTextPresent ( final String text, final WebDriver driver ) {
        final List<WebElement> list = driver.findElements( By.xpath( "//*[contains(text(),'" + text + "')]" ) );
        Assert.assertTrue( "Text not found!", list.size() > 0 );
    }

    /**
     * Asserts that the text is not on the page. Does not pause for text to
     * appear.
     *
     * @param text
     *            text to check
     * @param driver
     *            web driver
     */
    public void assertTextNotPresent ( final String text, final WebDriver driver ) {
        Assert.assertFalse( "Text should not be found!",
                driver.findElement( By.cssSelector( "BODY" ) ).getText().contains( text ) );
    }
}
