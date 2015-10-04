angular.module('pipelineModule').controller('ListController', ['$scope', '$http', 'FoundationApi',

function ($scope, $http, foundationApi) {
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
            foundationApi.publish('main-notifications', { color: 'success', autoclose: 3000, content: 'Pipeline Started' });
        }, function(response) {
            foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
        });
  }
}
]);
