package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Tests the DomainObject class
 * 
 * @author Neil Dey (ndey3)
 *
 */
public class DomainObjectTest {

    /**
     * Tests that you cannot copy an inventory object to a recipe object.
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testCannotCopyFromRecipeToInventory () {
        final Recipe r = new Recipe();
        final Inventory i = new Inventory();
        r.copyFrom( i, false );
    }

    /**
     * Tests that recipes can be copied
     */
    @Test
    public void testCopyRecipe () {
        final Recipe r1 = new Recipe();
        final Recipe r2 = new Recipe();
        r2.setChocolate( 100 );
        r2.setCoffee( 27 );
        r2.setMilk( 34 );
        r2.setName( "New Name!" );
        r2.setPrice( 2344 );
        r2.setSugar( 1 );
        r1.copyFrom( r2, false );
        assertEquals( r1, r2 );
    }

    /**
     * Tests that getWhere() will return the correct objects.
     */
    @Test
    public void getWhereReturnsAppropriateObjects () {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setCoffee( 27 );
        r1.save();
        final Recipe r2 = new Recipe();
        r2.setCoffee( 27 );
        r2.save();
        final Recipe r3 = new Recipe();
        r3.save();
        List recipes = DomainObject.getWhere( Recipe.class, DomainObject.eqList( "coffee", 27 ) );
        assertEquals( 2, recipes.size() );
        recipes = DomainObject.getWhere( Recipe.class,
                DomainObject.createCriterionList( DomainObject.bt( "coffee", 0, 27 ) ) );
        assertEquals( 3, recipes.size() );
    }

    /**
     * Tests that getBy() will return the appropriate objects.
     */
    @Test
    public void testGetByName () {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "First name" );
        r1.save();
        final Recipe r2 = new Recipe();
        r2.setName( "Second name" );
        r2.save();
        final Recipe r = (Recipe) DomainObject.getBy( Recipe.class, "name", "First name" );
        assertEquals( r1, r );
    }

    /**
     * Tests that getById() returns the appropriate objects
     */
    @Test
    public void testGetByIdValid () {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "First" );
        r1.save();
        final Recipe r2 = new Recipe();
        r2.setName( "Second" );
        r2.save();
        final long id = r1.getId();
        assertEquals( r1, DomainObject.getById( Recipe.class, id ) );
    }

    /**
     * Tests that getById will appropriately throw exceptions.
     */
    @Test ( expected = org.hibernate.ObjectNotFoundException.class )
    public void testGetByIdInvalid () {
        DomainObject.deleteAll( Recipe.class );
        final Recipe r1 = new Recipe();
        r1.setName( "First" );
        r1.save();
        final long id = r1.getId();
        r1.delete();
        DomainObject.getById( Recipe.class, id );
    }
}
