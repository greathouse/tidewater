angular.module('contextModule').controller('Context.DetailsV2Controller', ['$scope', '$sce', '$filter', '$routeParams', 'Context.Service',

function ($scope, $sce, $filter, $routeParams, contextService) {
  $scope.showScript = true;
  $scope.$watch(
    function() {
      return contextService.getContext($routeParams.contextId);
    },
    function(newVal) {
      $scope.context = newVal;
    },
    true);
}
]);
