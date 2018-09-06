package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * Tests the methods of the APICoffeeController
 *
 * @author Neil Dey
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest ( properties = "logging.level.org.springframework.web=DEBUG" )
@AutoConfigureMockMvc
public class APICoffeeControllerTest {
    @Autowired
    private APICoffeeController controller;

    /**
     * Tests that a free beverage can be purchased
     */
    @Test
    public void testBuyFreeCoffee () {
        final Recipe free = new Recipe();
        free.setName( "Coffee" );

        assertEquals( HttpStatus.OK, controller.getCoffeeResponse( free, 0 ).getStatusCode() );
    }

    /**
     * Tests that a beverage can be purchased when paid with no change necessary
     */
    @Test
    public void testBuyCoffeeWithCorrectPrice () {
        final Recipe free = new Recipe();
        free.setName( "Coffee" );
        free.setPrice( 10 );

        assertEquals( HttpStatus.OK, controller.getCoffeeResponse( free, 10 ).getStatusCode() );
    }

    /**
     * Tests that a beverage can be purchased with change necessary
     */
    @Test
    public void testBuyCoffeeWithMoreThanEnoughMoney () {
        final Recipe free = new Recipe();
        free.setName( "Coffee" );
        free.setPrice( 10 );

        assertEquals( HttpStatus.OK, controller.getCoffeeResponse( free, 20 ).getStatusCode() );
    }

    /**
     * Tests that a beverage can be purchased with change necessary
     */
    @Test
    public void testBuyCoffeeWithoutEnoughMoney () {
        final Recipe free = new Recipe();
        free.setName( "Coffee" );
        free.setPrice( 10 );

        assertEquals( HttpStatus.CONFLICT, controller.getCoffeeResponse( free, 9 ).getStatusCode() );
    }

    /**
     * Tests that a beverage cannot be purchased with a noninteger payment
     */
    @Test
    public void testBuyCoffeeWithoutNonintegerMoney () {
        final Recipe free = new Recipe();
        free.setName( "Coffee" );
        free.setPrice( 10 );

        assertEquals( HttpStatus.CONFLICT, controller.getCoffeeResponse( free, 19.5 ).getStatusCode() );
    }
}
