angular.module('contextModule').controller('Context.DetailsController', ['$scope', '$http', '$sce', '$filter', '$routeParams', 'FoundationApi',

function ($scope, $http, $sce, $filter, $routeParams, foundationApi) {
  self = this;

  $scope.contextId = $routeParams.contextId;
  $scope.showScript = false;

  $http.get('/contexts/' + $scope.contextId).
    then (function (response) {
      data = {};
      data['scriptText'] = response.data.scriptText;
      data['workspace'] = response.data.workspace;
      data['metadataDirectory'] = response.data.metadataDirectory;
      data['startTime'] = response.data.startTime;
      data['steps'] = [];

      angular.forEach(response.data.steps, function(step, index) {
        angular.forEach(step.attempts, function(attempt, attemptIndex) {
          data.steps.push({
            stepName: step.stepName,
            stepType: step.stepType,
            attributes: step.attributes,
            outcome: attempt.outcome,
            logs: $sce.trustAsHtml(attempt.logs.map(function(message) {
              return '<span style="font-weight: bold">' + $filter('date')(message.dateTime, 'MM/dd/yyyy hh:mm:ss') + '</span> ' + message.message;
            }).join('\n'))
          });
        });
      });

      $scope.context = data;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    });

    $scope.toggleScript = function() {
      $scope.showScript = !$scope.showScript;
    }
}
]);
