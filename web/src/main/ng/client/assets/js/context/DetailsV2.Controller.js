angular.module('contextModule').controller('Context.DetailsV2Controller', ['$scope', '$sce', '$filter', '$routeParams', 'Context.Service',

function ($scope, $sce, $filter, $routeParams, contextService) {
  $scope.showScript = true;
  $scope.context = contextService.getContext($routeParams.contextId);
}
]);
