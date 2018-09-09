package edu.ncsu.csc.cucumber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.selenium.BrowserHandler;

public class EditRecipeStepDefs {

    final static private String OS = System.getProperty( "os.name" );

    static protected WebDriver  driver;

    protected void setUp () throws Exception {
        driver = BrowserHandler.getInstance().getDriver();
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

    public void close () {
        driver.close();
        driver.quit();

        if ( Windows() ) {
            windowsKill();
        }
        else if ( Linux() || Mac() ) {
            unixKill();
        }

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

    /**
     * wait method that will let angular finish loading before continuing
     */
    protected void waitForAngular () {
        new NgWebDriver( (ChromeDriver) driver ).waitForAngularRequestsToFinish();
    }

    /** The URL for CoffeeMaker - change as needed */
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    /**
     * Deletes all recipes.
     *
     * Based off of delete() from DeleteRecipeTest.java
     *
     * @throws Exception
     */
    public void deleteAll () {
        waitForAngular();
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Delete Recipe" ) ).click();

        // Select the recipe to delete and delete it.
        driver.findElement( By.cssSelector( "input[type=\"checkbox\"]" ) ).click();
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Creates a recipe with the given parameters
     *
     * @param name
     *            The name of the recipe
     * @param price
     *            The price of the recipe
     * @param coffee
     *            The amount of coffee
     * @param milk
     *            The amount of milk
     * @param sugar
     *            The amount of sugar
     * @param chocolate
     *            The amount of chocolate
     */
    @Given ( "^the CoffeeMaker already has recipe with name: (.+), price: (-?\\d+) coffee: (-?\\d+), milk: (-?\\d+), sugar: (-?\\d+), chocolate: (-?\\d+)$" )
    public void createRecipe ( final String name, final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        try {
            this.setUp();
        }
        catch ( final Exception e ) {
            // TODO Auto-generated catch block
            Assert.fail();
        }

        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

        deleteAll();

        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( name );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( price + "" );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( coffee + "" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( milk + "" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( sugar + "" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( chocolate + "" );

        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    @When ( "^I edit that recipe to have price: (-?\\d+), coffee: (-?\\d+), milk: (-?\\d+), sugar: (-?\\d+), chocolate: (-?\\d+)$" )
    public void editRecipe ( final int price, final int coffee, final int milk, final int sugar, final int chocolate ) {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        fields.get( 0 ).clear();
        fields.get( 0 ).sendKeys( price + "" );
        fields.get( 1 ).clear();
        fields.get( 1 ).sendKeys( coffee + "" );
        fields.get( 2 ).clear();
        fields.get( 2 ).sendKeys( milk + "" );
        fields.get( 3 ).clear();
        fields.get( 3 ).sendKeys( sugar + "" );
        fields.get( 4 ).clear();
        fields.get( 4 ).sendKeys( chocolate + "" );

        driver.findElement( By.className( "btn-primary" ) ).click();
    }

    @Then ( "^the recipe retains its old values of price: (-?\\d+), coffee: (-?\\d+), milk: (-?\\d+), sugar: (-?\\d+), chocolate: (-?\\d+)$" )
    public void uneditedRecipe ( final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        this.assertTextPresent( "Error while updating recipe", driver );
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        Assert.assertEquals( price + "", fields.get( 0 ).getAttribute( "value" ) );
        Assert.assertEquals( coffee + "", fields.get( 1 ).getAttribute( "value" ) );
        Assert.assertEquals( milk + "", fields.get( 2 ).getAttribute( "value" ) );
        Assert.assertEquals( sugar + "", fields.get( 3 ).getAttribute( "value" ) );
        Assert.assertEquals( chocolate + "", fields.get( 4 ).getAttribute( "value" ) );

        // this.tearDown();
    }

    @Then ( "^the recipe is edited with price: (-?\\d+), coffee: (-?\\d+), milk: (-?\\d+), sugar: (-?\\d+), chocolate: (-?\\d+)$" )
    public void editedRecipe ( final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        this.assertTextNotPresent( "Error while updating recipe", driver );
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        Assert.assertEquals( price + "", fields.get( 0 ).getAttribute( "value" ) );
        Assert.assertEquals( coffee + "", fields.get( 1 ).getAttribute( "value" ) );
        Assert.assertEquals( milk + "", fields.get( 2 ).getAttribute( "value" ) );
        Assert.assertEquals( sugar + "", fields.get( 3 ).getAttribute( "value" ) );
        Assert.assertEquals( chocolate + "", fields.get( 4 ).getAttribute( "value" ) );
    }
}
