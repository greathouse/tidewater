angular.module('contextModule').controller('Context.ListController', ['$scope', '$http', '$filter', '$routeParams', 'FoundationApi',

function ($scope, $http, $filter, $routeParams, foundationApi) {
  $scope.pipelineName = $routeParams.pipelineName;

  $http.get('/pipelines/' + $scope.pipelineName + '/contexts').
    then (function (response) {
      $scope.contexts = response.data;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    });
}
]);
