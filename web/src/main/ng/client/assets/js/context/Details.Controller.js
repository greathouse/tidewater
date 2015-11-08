angular.module('contextModule').controller('Context.DetailsController', [
  '$scope',
  '$routeParams',
  'Context.Service',

function ($scope, $routeParams, contextService) {
  $scope.showScript = true;
  $scope.pipelineName = $routeParams.pipelineName;

  $scope.isEmpty = function(object) {
    if (object === undefined) {
      return true;
    }
    var v = Object.keys(object).length;
    return v == 0;
  }

  contextService.getContext($routeParams.contextId).then(function(context) {
    $scope.context = context
  });

//  $scope.$watch(
//    function() { return contextService.getContext($routeParams.contextId); },
//    function(newVal) { $scope.context = newVal; },
//    true
//  );
}
]);
