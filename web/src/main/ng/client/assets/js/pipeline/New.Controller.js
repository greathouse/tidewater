angular.module('pipelineModule').controller('Pipeline.NewController', ['$scope', '$http', 'FoundationApi',

function ($scope, $http, foundationApi) {
  $scope.postScript = function(formData) {
    $http.post('/pipeline', formData).
      then (function (response) {
        foundationApi.publish('main-notifications', { color: 'success', autoclose: 3000, content: 'Pipeline Created' });
      }, function(response) {
        foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
      });
  }
}

]);
