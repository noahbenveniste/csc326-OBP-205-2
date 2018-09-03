package edu.ncsu.csc.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

/**
 * Tests the Edit Recipe functionality.
 *
 * @author Neil Dey (ndey3@ncsu.edu)
 */

public class EditRecipeTest extends SeleniumTest {

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

    private void navigateToEdit () {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
        new NgWebDriver( (ChromeDriver) driver ).waitForAngularRequestsToFinish();
    }

    private void addRecipe ( final String name, final String price ) {
        driver.get( baseUrl );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( name );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( price );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "1" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "2" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "3" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "4" );

        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Test for the "no recipes" message on entry when no recipes are present.
     */
    @Test
    public void testNoRecipesMessage () throws Exception {
        deleteAll();
        navigateToEdit();
        assertTextPresent( "There are no recipes in the Coffee Maker.", driver );
    }

    /**
     * Tests that input boxes are read only when no recipe has been selected.
     *
     * @throws Exception
     */
    @Test
    public void testInputBoxesReadOnly () throws Exception {
        navigateToEdit();
        assertTextPresent( "Please select a recipe to edit", driver );
        assertNotNull( driver.findElement( By.cssSelector( "input[readonly]" ) ) );
    }

    /**
     * After adding two recipes, tests that they display as options in the edit
     * menu
     *
     * @throws Exception
     */
    @Test
    public void testRecipesDisplayedInEditMenu () throws Exception {
        addRecipe( "Coffee", "1" );
        addRecipe( "Ripoff", "2" );

        navigateToEdit();
        assertTextPresent( "Coffee", driver );
        assertTextPresent( "Ripoff", driver );
    }

    @Test
    public void testRecipeValuesPrefilledInEditMenu () throws Exception {
        this.deleteAll();
        addRecipe( "Coffee", "0" );
        addRecipe( "Ripoff", "2" );
        navigateToEdit();
        final List<WebElement> options = driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) );
        options.get( 0 ).click();

        List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );
        for ( int i = 0; i < 5; i++ ) {
            assertEquals( Integer.toString( i ), fields.get( i ).getAttribute( "value" ) );
        }

        options.get( 1 ).click();
        fields = driver.findElements( By.className( "input-sm" ) );

        // The price for Ripoff is different from that of coffee
        assertEquals( "2", fields.get( 0 ).getAttribute( "value" ) );
        for ( int i = 1; i < 5; i++ ) {
            assertEquals( Integer.toString( i ), fields.get( i ).getAttribute( "value" ) );
        }
    }

    //
    // @Test
    // public void test02InputBoxesReadonly () throws Exception {
    // driver.get( baseUrl );
    // driver.findElement( By.linkText( "Edit a Recipe" ) ).click();
    // }

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
