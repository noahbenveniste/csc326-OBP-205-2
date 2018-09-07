package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the recipe class
 * 
 * @author Neil Dey (ndey3)
 */
public class RecipeTest {

    /**
     * Tests that recipes are properly updated using updateRecipe()
     */
    @Test
    public void testUpdateRecipe () {
        final Recipe r = new Recipe();
        final Recipe set = new Recipe();
        set.setCoffee( 10 );
        set.setChocolate( 10 );
        set.setMilk( 10 );
        set.setPrice( 10 );
        set.setPrice( 10 );
        set.setSugar( 10 );
        r.updateRecipe( set );
        assertEquals( r, set );
    }
}
