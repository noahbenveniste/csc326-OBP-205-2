package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the Inventory class
 *
 * @author Neil Dey (ndey3)
 *
 */
public class InventoryTest {
    private Inventory inv;

    /**
     * Sets up the inventory object
     */
    @Before
    public void setUp () {
        inv = new Inventory( 100, 100, 100, 100 );
    }

    /**
     * Tests the checkChocolate() method throws an exception on a non-numeric
     * input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testCheckChocolateNonNumberInput () {
        inv.checkChocolate( "234NotANumber" );
    }

    /**
     * Tests the checkChocolate() method throws an exception on a negative input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testCheckChocolateNegativeNumberInput () {
        inv.checkChocolate( "-234" );
    }

    /**
     * Tests the checkChocolate() method throws an exception on noninteger input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testCheckChocolateDecimalInput () {
        inv.checkChocolate( "1.5" );
    }

    /**
     * Tests the checkChocolate() yields the correct outputs on an input
     */
    @Test
    public void testCheckChocolateValid () {
        assertEquals( 0, inv.checkChocolate( "0" ) );
        assertEquals( 25, inv.checkChocolate( "25" ) );
        assertEquals( 10000, inv.checkChocolate( "10000" ) );
    }

    /**
     * Tests the checkMilk() method throws an exception on a non-numeric input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckMilkNonNumberInput () {
        inv.checkMilk( "234NotANumber" );
    }

    /**
     * Tests the checkMilk() method throws an exception on a negative input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckMilkNegativeNumberInput () {
        inv.checkMilk( "-234" );
    }

    /**
     * Tests the checkMilk() method throws an exception on noninteger input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckMilkDecimalInput () {
        inv.checkMilk( "1.5" );
    }

    /**
     * Tests the checkMilk() yields the correct outputs on an input
     */
    @Test
    public void testcheckMilkValid () {
        assertEquals( 0, inv.checkMilk( "0" ) );
        assertEquals( 25, inv.checkMilk( "25" ) );
        assertEquals( 10000, inv.checkMilk( "10000" ) );
    }

    /**
     * Tests the checkSugar() method throws an exception on a non-numeric input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckSugarNonNumberInput () {
        inv.checkSugar( "234NotANumber" );
    }

    /**
     * Tests the checkSugar() method throws an exception on a negative input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckSugarNegativeNumberInput () {
        inv.checkSugar( "-234" );
    }

    /**
     * Tests the checkSugar() method throws an exception on noninteger input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckSugarDecimalInput () {
        inv.checkSugar( "1.5" );
    }

    /**
     * Tests the checkSugar() yields the correct outputs on an input
     */
    @Test
    public void testcheckSugarValid () {
        assertEquals( 0, inv.checkSugar( "0" ) );
        assertEquals( 25, inv.checkSugar( "25" ) );
        assertEquals( 10000, inv.checkSugar( "10000" ) );
    }

    /**
     * Tests the checkCoffee() method throws an exception on a non-numeric input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckCoffeeNonNumberInput () {
        inv.checkCoffee( "234NotANumber" );
    }

    /**
     * Tests the checkCoffee() method throws an exception on a negative input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckCoffeeNegativeNumberInput () {
        inv.checkCoffee( "-234" );
    }

    /**
     * Tests the checkCoffee() method throws an exception on noninteger input
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testcheckCoffeeDecimalInput () {
        inv.checkCoffee( "1.5" );
    }

    /**
     * Tests the checkCoffee() yields the correct outputs on an input
     */
    @Test
    public void testcheckCoffeeValid () {
        assertEquals( 0, inv.checkCoffee( "0" ) );
        assertEquals( 25, inv.checkCoffee( "25" ) );
        assertEquals( 10000, inv.checkCoffee( "10000" ) );
    }

    /**
     * Tests that enoughIngredients() will return whether or not there are
     * enough ingredients to make the recipe.
     */
    @Test
    public void testEnoughIngredients () {
        final Recipe r = new Recipe();
        r.setChocolate( 100 );
        r.setCoffee( 100 );
        r.setMilk( 100 );
        r.setSugar( 100 );

        assertTrue( inv.enoughIngredients( r ) );

        r.setChocolate( 101 );
        assertFalse( inv.enoughIngredients( r ) );
        r.setChocolate( 100 );

        r.setCoffee( 101 );
        assertFalse( inv.enoughIngredients( r ) );
        r.setCoffee( 100 );

        r.setMilk( 101 );
        assertFalse( inv.enoughIngredients( r ) );
        r.setMilk( 100 );

        r.setSugar( 101 );
        assertFalse( inv.enoughIngredients( r ) );
        r.setSugar( 100 );
    }

    /**
     * Tests the toString() for the inventory
     */
    @Test
    public void testToString () {
        assertEquals( "Coffee: 100\nMilk: 100\nSugar: 100\nChocolate: 100\n", inv.toString() );
    }
}
