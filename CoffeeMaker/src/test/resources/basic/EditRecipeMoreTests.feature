#Author: Neil Dey

Feature: Edit a recipe (new)
	As a user
	I want to be able to edit recipes 
	So that I can change my recipe to my liking
	
Scenario Outline: Invalid Edit
	Given the CoffeeMaker already has recipe with name: <recipeName>, price: <price> coffee: <amtCoffee>, milk: <amtMilk>, sugar: <amtSugar>, chocolate: <amtChocolate>
	When I edit that recipe to have price: <newPrice>, coffee: <newAmtCoffee>, milk: <newAmtMilk>, sugar: <newAmtSugar>, chocolate: <newAmtChocolate>
	Then the recipe retains its old values of price: <price> coffee: <amtCoffee>, milk: <amtMilk>, sugar: <amtSugar>, chocolate: <amtChocolate>
	
Examples:
	| recipeName | price | amtCoffee | amtMilk | amtSugar | amtChocolate | newPrice | newAmtCoffee | newAmtMilk | newAmtSugar | newAmtChocolate |
	| Coffee     | 50    | 3         | 1       | 1        | 0            | -5       | 2            | 0          | 0           | 1               |
	| Mocha      | 60    | 3         | 2       | 2        | 3            | 20       | -2           | 1          | 1           | 4               |
	| Latte      | 60    | 3         | 3       | 2        | 0            | 30       | 2            | -2         | 1           | 1               |
	| Cappuccino | 50    | 3         | 3       | 2        | 0            | 30       | 2            | 2          | -1          | 1               |
	| Cafe       | 50    | 3         | 3       | 2        | 0            | 30       | 2            | 2          | 1           | -1              |
	
	Scenario Outline: Valid Edit
	Given the CoffeeMaker already has recipe with name: <recipeName>, price: <price> coffee: <amtCoffee>, milk: <amtMilk>, sugar: <amtSugar>, chocolate: <amtChocolate>
	When I edit that recipe to have price: <newPrice>, coffee: <newAmtCoffee>, milk: <newAmtMilk>, sugar: <newAmtSugar>, chocolate: <newAmtChocolate>
	Then the recipe is edited with price: <newPrice>, coffee: <newAmtCoffee>, milk: <newAmtMilk>, sugar: <newAmtSugar>, chocolate: <newAmtChocolate>
	
Examples:
	| recipeName | price | amtCoffee | amtMilk | amtSugar | amtChocolate | newPrice | newAmtCoffee | newAmtMilk | newAmtSugar | newAmtChocolate |
	| Coffee     | 50    | 3         | 1       | 1        | 0            | 5        | 2            | 0          | 50          | 1               |
	| Mocha      | 60    | 3         | 2       | 2        | 3            | 20       | 4            | 1          | 2           | 4               |
	| Latte      | 60    | 3         | 3       | 2        | 0            | 50       | 5            | 2          | 4           | 0               |
	| Cappuccino | 50    | 3         | 3       | 2        | 0            | 650      | 7            | 3          | 17          | 8               |
	| Cafe       | 50    | 3         | 3       | 2        | 0            | 0        | 2            | 4          | 25          | 5              |