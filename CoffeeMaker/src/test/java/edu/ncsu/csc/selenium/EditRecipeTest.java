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
 * @author Noah Benveniste (nnbenven@ncsu.edu)
 */

public class EditRecipeTest extends SeleniumTest {
    private final StringBuffer verificationErrors = new StringBuffer();

    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
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
        this.deleteAll();
        addRecipe( "Coffee", "1" );
        addRecipe( "Ripoff", "2" );

        navigateToEdit();
        assertEquals( 2, driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).size() );
    }

    /**
     * Tests that when two recipes are added, their values are prefilled in the
     * edit form.
     *
     * @throws Exception
     */
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

    /**
     * Tests that recipes actually update after submitting the form.
     *
     * @throws Exception
     */
    @Test
    public void testRecipeChangeOnSubmitButton () throws Exception {
        this.deleteAll();
        addRecipe( "Coffee", "0" );
        navigateToEdit();
        final List<WebElement> options = driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) );
        options.get( 0 ).click();

        final List<WebElement> fields = driver.findElements( By.className( "input-sm" ) );

        fields.get( 0 ).sendKeys( "1" );

        driver.findElement( By.className( "btn-primary" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "1", driver.findElements( By.className( "input-sm" ) ).get( 0 ).getAttribute( "value" ) );

    }

    /**
     * Tests that the form validates inputs properly
     */
    @Test
    public void testFormValidation () throws Exception {
        // Add a recipe
        this.deleteAll();
        addRecipe( "Coffee", "9001" );

        // Give a chance for backend to process.
        Thread.sleep( 1000 );

        // Go to the edit page
        navigateToEdit();
        final List<WebElement> options = driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) );
        options.get( 0 ).click();

        /* Scenario #1a: Negative price */

        // Get the input forms for each of the fields
        WebElement priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        WebElement coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        WebElement milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        WebElement sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        WebElement chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        // Input values
        priceInput.sendKeys( "-1" );
        coffeeInput.sendKeys( "0" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "0" );

        // Attempt to submit
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        // Refresh page
        navigateToEdit();

        // Re-select the recipe
        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        // Check that none of the values changed
        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #1b: Non-integer price */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "1.618" );
        coffeeInput.sendKeys( "0" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "0" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #2a: Negative coffee */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "-1" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "0" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #2b: Non-integer coffee */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "3.14" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "0" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #3a: Negative milk */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "3" );
        milkInput.sendKeys( "-6" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "1" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #3b: Non-integer milk */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "2" );
        coffeeInput.sendKeys( "0" );
        milkInput.sendKeys( "1.2345" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "1" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #4a: Negative sugar */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "1" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "-1" );
        chocolateInput.sendKeys( "4" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #4b: Non-integer sugar */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "1" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "2.5" );
        chocolateInput.sendKeys( "0" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #5a: Negative chocoloate */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "1" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "-1" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #5b: Non-integer chocolate */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "0" );
        coffeeInput.sendKeys( "0" );
        milkInput.sendKeys( "0" );
        sugarInput.sendKeys( "0" );
        chocolateInput.sendKeys( "1.00001" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #6: All fields negative */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "-1" );
        coffeeInput.sendKeys( "-2" );
        milkInput.sendKeys( "-3" );
        sugarInput.sendKeys( "-4" );
        chocolateInput.sendKeys( "-5" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* Scenario #7: All fields non-integer */

        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "2.5" );
        coffeeInput.sendKeys( "3.14" );
        milkInput.sendKeys( "1.6789" );
        sugarInput.sendKeys( "4.56" );
        chocolateInput.sendKeys( "0.000000009" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

        /* BONUS ROUND: mix */
        priceInput = driver.findElement( By.xpath( "//input[@name='price']" ) );
        coffeeInput = driver.findElement( By.xpath( "//input[@name='coffee']" ) );
        milkInput = driver.findElement( By.xpath( "//input[@name='milk']" ) );
        sugarInput = driver.findElement( By.xpath( "//input[@name='sugar']" ) );
        chocolateInput = driver.findElement( By.xpath( "//input[@name='chocolate']" ) );

        priceInput.sendKeys( "-2.5" );
        coffeeInput.sendKeys( "-3" );
        milkInput.sendKeys( "1.6789" );
        sugarInput.sendKeys( "4.5" );
        chocolateInput.sendKeys( "9" );

        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        navigateToEdit();

        driver.findElements( By.cssSelector( "input[type=\"radio\"]" ) ).get( 0 ).click();

        assertEquals( "9001", driver.findElement( By.name( "price" ) ).getAttribute( "value" ) );
        assertEquals( "1", driver.findElement( By.name( "coffee" ) ).getAttribute( "value" ) );
        assertEquals( "2", driver.findElement( By.name( "milk" ) ).getAttribute( "value" ) );
        assertEquals( "3", driver.findElement( By.name( "sugar" ) ).getAttribute( "value" ) );
        assertEquals( "4", driver.findElement( By.name( "chocolate" ) ).getAttribute( "value" ) );

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
