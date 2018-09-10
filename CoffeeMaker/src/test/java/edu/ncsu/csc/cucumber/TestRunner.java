package edu.ncsu.csc.cucumber;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * TestRunner class for the Cucumber tests. Adjust the "features" parameter
 * above as necessary to run just a subset of the tests.
 *
 * @author Kai Presler-Marshall
 * @author Sarah Elder
 *
 */
@RunWith ( Cucumber.class )
@CucumberOptions ( features = "src/test/resources/basic/",
        plugin = { "pretty", "junit:\\CoffeeMaker_JUnit_Cucumber\\cucumberTest.xml" } )
public class TestRunner {
    @BeforeClass
    public static void setUp () {
        CucumberTest.setup();
    }

    @AfterClass
    public static void tearDown () {
        CucumberTest.tearDown();
    }
}
