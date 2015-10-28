angular.module('contextModule').controller('Context.DetailsController', ['$scope', '$http', '$sce', '$filter', '$routeParams', 'FoundationApi', 'Context.Service',

function ($scope, $http, $sce, $filter, $routeParams, foundationApi, contextService) {
  self = this;

  $scope.contextId = $routeParams.contextId;
  $scope.showScript = true;

  $http.get('/contexts/' + $scope.contextId).
    then (function (response) {
      data = {};
      data.pipelineName = response.data.pipelineName;
      data['scriptText'] = response.data.scriptText;
      data['workspace'] = response.data.workspace;
      data['metadataDirectory'] = response.data.metadataDirectory;
      data['startTime'] = response.data.startTime;
      data['endTime'] = response.data.endTime;
      data.duration = response.data.duration;
      data.status = response.data.status;
      data['steps'] = [];

      angular.forEach(response.data.steps, function(step, index) {
        if (step.attempts.length === 0) {
          data.steps.push({
            stepName: step.stepName,
            stepType: step.stepType,
            attributes: step.attributes,
            outcome: 'NA',
            duration: 0,
            logs: $sce.trustAsHtml('<h1 style="text-align: center; font-weight: bold">¯\\_(ツ)_/¯</h1>'),
            show: false
          })
        }
        else {
          angular.forEach(step.attempts, function(attempt, attemptIndex) {
            data.steps.push({
              stepName: step.stepName,
              stepType: step.stepType,
              attributes: step.attributes,
              outcome: attempt.outcome,
              duration: attempt.duration,
              logs: $sce.trustAsHtml(attempt.logs.reduce(function(previousValue, currentValue, index, array) {
                var previousDay = index > 0 ? $filter('date')(array[index - 1].dateTime, 'MM/dd/yyyy') : '';
                var currentDay = $filter('date')(currentValue.dateTime, 'MM/dd/yyyy');
                var printDay = '          ';
                if (previousDay != currentDay) { printDay = currentDay; }
                return previousValue + '<span style="font-weight: bold">' + printDay + ' ' + $filter('date')(currentValue.dateTime, 'hh:mm:ss') + '</span> ' + currentValue.message + '\n';
              }, '')),
              show: false
            });
          });
        }
      });

      $scope.context = data;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    });
}
]);
