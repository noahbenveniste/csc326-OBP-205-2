package edu.ncsu.csc.cucumber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.selenium.SeleniumTest;

public class EditRecipeStepDefs extends SeleniumTest {
    /** The URL for CoffeeMaker - change as needed */
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    @Override
    protected void setUp () throws Exception {
        super.setUp();

        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
    }

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
    @Given ( "^the CoffeeMaker already has recipe with name: (.+), price: (\\d+) coffee: (\\d+), milk: (\\d+), sugar: (\\d+), chocolate: (\\d+)$" )
    public void createRecipe ( final String name, final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        try {
            this.setUp();
        }
        catch ( final Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
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

    @When ( "^When I edit that recipe to have price: price: (\\d+) coffee: (\\d+), milk: (\\d+), sugar: (\\d+), chocolate: (\\d+)$" )
    public void editRecipe ( final int price, final int coffee, final int milk, final int sugar, final int chocolate ) {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        fields.get( 0 ).sendKeys( price + "" );
        fields.get( 1 ).sendKeys( coffee + "" );
        fields.get( 2 ).sendKeys( milk + "" );
        fields.get( 3 ).sendKeys( sugar + "" );
        fields.get( 4 ).sendKeys( chocolate + "" );

        driver.findElement( By.className( "btn-primary" ) ).click();
    }

    @Then ( "^the recipe retains its old values of price: (\\d+) coffee: (\\d+), milk: (\\d+), sugar: (\\d+), chocolate: (\\d+)$" )
    public void uneditedRecipe ( final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        this.assertTextPresent( "Error while updating recipe", driver );
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        assertEquals( price + "", fields.get( 0 ).getText() );
        assertEquals( coffee + "", fields.get( 1 ).getText() );
        assertEquals( milk + "", fields.get( 2 ).getText() );
        assertEquals( sugar + "", fields.get( 3 ).getText() );
        assertEquals( chocolate + "", fields.get( 4 ).getText() );

        this.tearDown();
    }

    @Then ( "^Then the recipe is edited with  price: (\\d+) coffee: (\\d+), milk: (\\d+), sugar: (\\d+), chocolate: (\\d+)$" )
    public void editedRecipe ( final int price, final int coffee, final int milk, final int sugar,
            final int chocolate ) {
        this.assertTextNotPresent( "Error while updating recipe", driver );
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        waitForAngular();

        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        assertEquals( price + "", fields.get( 0 ).getText() );
        assertEquals( coffee + "", fields.get( 1 ).getText() );
        assertEquals( milk + "", fields.get( 2 ).getText() );
        assertEquals( sugar + "", fields.get( 3 ).getText() );
        assertEquals( chocolate + "", fields.get( 4 ).getText() );

        this.tearDown();
    }

    @Override
    public void tearDown () {
        final String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) ) {
            fail( verificationErrorString );
        }
        super.close();
    }
}