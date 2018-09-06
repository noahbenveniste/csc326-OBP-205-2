'use strict';

angular.module('myApp').controller('AddRecipeController', ['$scope', 'AddRecipeService', function($scope, AddRecipeService) {
  var self = this;
  self.recipe={name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};

  self.submit = submit;
  self.reset = reset;

  async function addRecipe(recipe) {
    console.log("Trying to add recipe");
    $scope.success = false;
    $scope.failure = false;
    AddRecipeService.addRecipe(recipe)
    .then(
      function(successResponse) {
        $scope.success = true;
        reset();
      },
      function(errResponse) {
        $scope.failure = true;
      }
    );
  }

  function submit() {
    addRecipe(self.recipe);
  }


  function reset() {
    self.recipe={name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};
    $scope.addRecipeForm.$setPristine(); //reset Form
  }

}]);
