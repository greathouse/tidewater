angular.module('contextModule').controller('Context.DetailsController', [
  '$scope',
  '$routeParams',
  'Context.Service',

function ($scope, $routeParams, $http, contextService) {
  $scope.showScript = true;
  $scope.pipelineName = $routeParams.pipelineName;

  $scope.$watch(
    function() { return contextService.getContext($routeParams.contextId); },
    function(newVal) { $scope.context = newVal; },
    true
  );
}
]);
