angular.module('pipelineModule').controller('Pipeline.NewController', ['$scope', '$http', 'FoundationApi', '$location',

function ($scope, $http, foundationApi, $location) {
  $scope.postScript = function(formData) {
    $http.post('/pipeline', formData).
      then (function (response) {
        foundationApi.publish('main-notifications', { color: 'success', autoclose: 3000, content: 'Pipeline Created' });
        $location.path('/pipelines/' + formData.name);
      }, function(response) {
        foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
      });
  }
}

]);
