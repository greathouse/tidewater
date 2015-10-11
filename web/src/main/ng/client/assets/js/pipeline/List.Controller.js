angular.module('pipelineModule').controller('ListController', ['$scope', '$http',

function ($scope, $http) {
  $http.get('/pipelines').
    then (function (response) {
      $scope.pipelines = response.data;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    })
  ;

  $scope.startPipeline = function(pipelineName) {
    $http.get('/pipelines/' + pipelineName + '/start').
        then (function (response) {
        }, function(response) {
        });
  }
}
]);
