package edu.ncsu.csc.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Tests Add Recipe functionality.
 *
 * @author Kai Presler-Marshall (kpresle@ncsu.edu)
 */

public class AddRecipeTest extends SeleniumTest {

    /** The URL for CoffeeMaker - change as needed */
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    @Override
    @Before
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
    public void deleteAll () throws Exception {
        waitForAngular();
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Delete Recipe" ) ).click();

        // Select the recipe to delete and delete it.
        driver.findElement( By.cssSelector( "input[type=\"checkbox\"]" ) ).click();
        final List<WebElement> submitButton = driver.findElements( By.cssSelector( "input[type=\"submit\"]" ) );
        if ( submitButton.size() != 0 ) {
            submitButton.get( 0 ).click();
        }
    }

    private void addRecipeHelper () {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( "Coffee" );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( "50" );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "3" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "1" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "1" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "1" );

        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Test for a adding a recipe. Expect to get an appropriate success message.
     *
     * @throws Exception
     */
    @Test
    public void testAddRecipe1 () throws Exception {

        deleteAll();

        addRecipeHelper();

        waitForAngular();
        assertEquals( "", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        // Make sure the proper message was displayed.
        assertTextPresent( "Recipe Created", driver );

        System.out.println( "Recipe created" );
    }

    /**
     * addRecipe2 Test for a adding a duplicate recipe. Expect to get an
     * appropriate error message.
     *
     * @throws Exception
     */
    @Test
    public void testAddRecipe2 () throws Exception {
        deleteAll();

        addRecipeHelper();
        addRecipeHelper();

        waitForAngular();
        assertEquals( "50", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertTextPresent( "Error while adding recipe", driver );
    }

    @Override
    @After
    public void tearDown () {
        final String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) ) {
            fail( verificationErrorString );
        }
    }

    @AfterClass
    @Override
    public void close () {
        super.close();
    }

}
