package edu.ncsu.csc.coffee_maker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for editing a recipe. The controller returns edit.html in
 * the /src/main/resources/templates folder.
 *
 * @author Neil Dey
 */
@Controller
public class EditRecipeController {

    /**
     * On a GET request to /recipe, the EditRecipeController will return
     * /src/main/resources/templates/edit.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( "/edit" )
    public String recipeForm ( final Model model ) {
        return "edit";
    }
}
