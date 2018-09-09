package edu.ncsu.csc.coffee_maker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.coffee_maker.models.persistent.Inventory;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * This is the single controller in the CoffeeMaker application that handles
 * REST API endpoints In a larger application, we would want multiple REST API
 * controllers, one per model.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICoffeeController extends APIController {

    /**
     * REST API method to make coffee by completing a POST request with the ID
     * of the recipe as the path variable and the amount that has been paid as
     * the body of the response
     *
     * @param name
     *            recipe name
     * @param payment
     *            amount paid
     * @return The change the customer is due if successful
     */
    @PostMapping ( BASE_PATH + "/makecoffee/{name}" )
    public ResponseEntity makeCoffee ( @PathVariable ( "name" ) final String name, @RequestBody final Number payment ) {
        final Recipe recipe = Recipe.getByName( name );
        return getCoffeeResponse( recipe, payment );
    }

    /**
     * Yields the appropriate response entity when making coffee.
     *
     * Extracted for unit testing purposes
     *
     * @param recipe
     *            Recipe Name
     * @param payment
     *            Amount Paid
     * @return The response entity with the change the customer is due, or the
     *         error message if unsuccessful
     */
    ResponseEntity getCoffeeResponse ( final Recipe recipe, final Number payment ) {
        if ( recipe == null ) {
            System.out.println( "No recipe selected" );
            return new ResponseEntity( errorResponse( "No recipe selected" ), HttpStatus.NOT_FOUND );
        }

        final int amtPaid = payment.intValue();
        if ( !payment.equals( amtPaid ) ) {
            return new ResponseEntity( errorResponse( "Noninteger amount of money" ), HttpStatus.CONFLICT );
        }

        System.out.println( "recipe: " + recipe.getName() + "    amt: " + amtPaid );
        final int change = makeCoffee( recipe, amtPaid );

        // error handling
        if ( change < 0 ) {
            if ( amtPaid < recipe.getPrice() ) {
                return new ResponseEntity( errorResponse( "Not enough money paid" ), HttpStatus.CONFLICT );
            }
            else {
                return new ResponseEntity( errorResponse( "Not enough inventory" ), HttpStatus.CONFLICT );
            }
        }

        System.out.println( "change: " + change );
        return new ResponseEntity<String>( successResponse( "change: " + change ), HttpStatus.OK );

    }

    /**
     * Helper method to make coffee
     *
     * @param toPurchase
     *            recipe that we want to make
     * @param amtPaid
     *            money that the user has given the machine
     * @return change if there was enough money to make the coffee, -1
     *         otherwise.
     *
     * @throws IllegalArgumentException
     *             if recipe is not found
     */
    public static int makeCoffee ( final Recipe toPurchase, final int amtPaid ) {
        int change = amtPaid;
        final Inventory inventory = Inventory.getInventory();

        if ( toPurchase == null ) {
            throw new IllegalArgumentException( "Recipe not found" );
        }
        else if ( toPurchase.getPrice() <= amtPaid ) {
            if ( inventory.useIngredients( toPurchase ) ) {
                inventory.save();
                change = amtPaid - toPurchase.getPrice();
                return change;
            }
            else {
                // not enough inventory
                return -1;
            }
        }
        // not enough money
        return -1;
    }
}
