angular.module('contextModule').controller('Context.DetailsController', [
  '$scope',
  '$routeParams',
  'Context.Service',

function ($scope, $routeParams, contextService) {
  $scope.showScript = true;
  $scope.pipelineName = $routeParams.pipelineName;

  $scope.isEmpty = function(object) {
    var v = Object.keys(object).length;
    return v == 0;
  }

  $scope.$watch(
    function() { return contextService.getContext($routeParams.contextId); },
    function(newVal) { $scope.context = newVal; },
    true
  );
}
]);
