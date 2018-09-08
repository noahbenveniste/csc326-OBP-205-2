#Author: Daniel Grist

Feature: Edit Recipe Front End
	As a user
	I want to be able to edit recipes 
	So that I can change my recipe to my liking
	
	
	Scenario Outline: Invalid Edit
	Given the CoffeeMaker already has <existingRecipes> Recipes
	When I submit valid values for name: Coffee; cost: 50; and ingredients: 3 coffee, 1 milk, 1 sugar, 0 chocolate
	And I invalidly edit that recipe to have cost: <price>; and ingredients: <amtCoffee> coffee, <amtMilk> milk, <amtSugar> sugar, <amtChocolate> chocolate
	Then the error for an invalid amount of <ingredient> in a recipe occurs
	And the recipe is not edited
	
Examples:
	| existingRecipes | price | amtCoffee | amtMilk | amtSugar | amtChocolate | ingredient |
	| 0               | 50    | -5        | 2       | 2        | 4            | coffee     |
	| 0               | 50    | NA        | 2       | 2        | 4            | coffee     |
	| 0               | 50    | %         | 2       | 2        | 4            | coffee     |
	| 0               | 50    | 0.5       | 2       | 2        | 4            | coffee     |
	| 0               | 50    | null      | 2       | 2        | 4            | coffee     |
	| 0               | 10    | 3         | -1      | 1        | 0            | milk       |
	| 0               | 10    | 3         | NA      | 1        | 0            | milk       |
	| 0               | 10    | 3         | %       | 1        | 0            | milk       |
	| 0               | 10    | 3         | 0.25    | 1        | 0            | milk       |
	| 0               | 10    | 3         | null    | 1        | 0            | milk       |
	| 0               | 60    | 3         | 3       | -2       | 0            | sugar      |
	| 0               | 60    | 3         | 3       | a        | 0            | sugar      |
	| 0               | 60    | 3         | 3       | %        | 0            | sugar      |
	| 0               | 60    | 3         | 3       | 2.5      | 0            | sugar      |
	| 0               | 60    | 3         | 3       | null     | 0            | sugar      |
	| 0               | 10    | 3         | 1       | 1        | -1           | chocolate  |
	| 0               | 10    | 3         | 1       | 1        | Why?         | chocolate  |
	| 0               | 10    | 3         | 1       | 1        | %            | chocolate  |
	| 0               | 10    | 3         | 1       | 1        | 6.5          | chocolate  |
	| 0               | 10    | 3         | 1       | 1        | null         | chocolate  |
	
	
	
	Scenario Outline: Another Valid edit
	Given the CoffeeMaker already has <existingRecipes> Recipes
	When I submit valid values for name: <recipeName>; cost: <price>; and ingredients: <amtCoffee> coffee, <amtMilk> milk, <amtSugar> sugar, <amtChocolate> chocolate
	And I edit that recipe to have cost: <newPrice>; and ingredients: <newAmtCoffee> coffee, <newAmtMilk> milk, <newAmtSugar> sugar, <newAmtChocolate> chocolate
	Then the recipe is successfully edited
	
	Examples: 
		| existingRecipes | recipeName | price | amtCoffee | amtMilk | amtSugar | amtChocolate | newPrice | newAmtCoffee | newAmtMilk | newAmtSugar | newAmtChocolate |
    | 0								|			A			 |	 10  | 		 5  	 |		0		 |	 	 0 	 	|	 	 	 	0	 	 	 |		10		|				5			 |			1			|			1 			|				 1				|
    |	1								|			B			 |	 20  | 		 5  	 |		1		 |	 	 2	 	|	 	 	 	1	 	 	 |		15		|				4			 |			1			|			1				|				 1 				|
    |	2								|			C			 |	 10  | 		 5  	 |		2		 |	 	 1 	 	|	 	 	 	0	 	 	 |		15		|				6			 |			2			|			1				|				 2				|
    | 3								|			D			 |	 20  | 		 5  	 |		3		 |	 	 0 	 	|	 	 	 	1	 	 	 |		20		|				5			 |			3			|			2				|				 0				|
    | 3								|			E			 |	 10  | 		 5  	 |		0		 |	 	 3 	 	|	 	 	 	0	 	 	 |		20		|				6			 |			0			|			3				|				 2				|
    
	Scenario Outline: Invalid price edit
	Given the CoffeeMaker already has <existingRecipes> Recipes
  When I submit valid values for name: <recipeName>; cost: <price>; and ingredients: <amtCoffee> coffee, <amtMilk> milk, <amtSugar> sugar, <amtChocolate> chocolate
  And I attempt to edit the recipe price with <newPrice>
  Then the error for an invalid recipe price occurs 
  And the recipe is not edited 	  
  
  Examples:
  	| existingRecipes | recipeName | price | amtCoffee | amtMilk | amtSugar | amtChocolate | newPrice |
    | 0								|			A			 |	 10  | 		 5  	 |		0		 |	 	 0 	 	|	 	 	 	0	 	 	 |  -10     |
    |	1								|			B			 |	 20  | 		 5  	 |		1		 |	 	 2	 	|	 	 	 	1	 	 	 |	%    		|
    |	2								|			C			 |	 10  | 		 5  	 |		2		 |	 	 1 	 	|	 	 	 	0	 	 	 |	A   		|
    | 3								|			D			 |	 20  | 		 5  	 |		3		 |	 	 0 	 	|	 	 	 	1	 	 	 |	23.23		|
    | 3               |     E      |   10  |     5		 |    0		 |     3    |       0      |  null    |  
    
    Scenario Outline: Invalid ingredient edit
    Given the CoffeeMaker already has <existingRecipes> Recipes
 		When I submit valid values for name: <recipeName>; cost: <price>; and ingredients: 10 coffee, 5 milk, 3 sugar, 1 chocolate
 		And I attempt to edit that recipe to ingredients: <newAmtCoffee> coffee, <newAmtMilk> milk, <newAmtSugar> sugar, <newAmtChocolate> chocolate
  	Then the error for an invalid amount of <ingredient> in a recipe occurs
    And the recipe is not edited
		
		Examples:
  				| existingRecipes | recipeName | price |newAmtCoffee | newAmtMilk | newAmtSugar | newAmtChocolate | ingredient |
					| 0               |			A			 | 50    | 	 -10       | 	 	2       | 	 2        | 	 4            | coffee     |
					| 1               |     B      | 40    | 	 NA        | 		2       | 	 2        | 	 4            | coffee     |
					| 2               |			C			 | 30    | 	 %         | 		2       | 	 2        | 	 4            | coffee     |
					| 3               |			D			 | 20    | 	 0.534     | 		2       | 	 2        | 	 4	          | coffee     |
					| 0               |			E			 | 10    | 	 null      | 		2       | 	 2        | 	 4            | coffee     |
					| 0               |			F			 | 10    | 	 3         | 		-5      | 	 1        | 	 0            | milk       |		
					| 1               |		 	G			 | 20    | 	 3         | 		A       | 	 1        | 	 0            | milk       |
					| 2               |			H			 | 30    | 	 3         | 		/       | 	 1        | 	 0            | milk       |
					| 3               |			I		   | 40    | 	 3         | 		0.25    | 	 1        | 	 0            | milk       |
					| 0               |			J			 | 50    | 	 3         | 		null    | 	 1        | 	 0            | milk       |
					| 0               |			K			 | 50    |   3         | 		3       | 	 -2       | 	 0            | sugar      |
					| 1               |			L		 	 | 40    | 	 3         | 		3       | 	 a        | 	 0            | sugar      |
					| 2               |			M			 | 30    | 	 3         |	  3       | 	 %        | 	 0            | sugar      |
					| 3               |			N			 | 20    | 	 3         | 		3       | 	 2.5      | 	 0            | sugar      |
					| 0               |			O			 | 10    | 	 3         | 		3       |  	 null     | 	 0            | sugar      |	
					| 0               |			P			 | 10    | 	 3         | 		1       | 	 1        | 	 -1           | chocolate  |
					| 1               |			Q			 | 20    | 	 3         | 		1       | 	 1        | 	 LOL          | chocolate  |
					| 2               |			R		   | 30    | 	 3         | 		1       | 	 1        | 	 ?            | chocolate  |
					| 3               |			S			 | 40    | 	 3         | 		1       | 	 1        | 	 6.5          | chocolate  |
					| 0               |			T			 | 50    | 	 3         | 		1       | 	 1        | 	 null         | chocolate  |
	
	