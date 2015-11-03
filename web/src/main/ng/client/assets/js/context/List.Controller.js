angular.module('contextModule').controller('Context.ListController', [
  '$scope',
  '$http',
  '$filter',
  '$routeParams',
  'FoundationApi',
  'Event.Service',
  'Context.Service',

function ($scope, $http, $filter, $routeParams, foundationApi, eventService, contextService) {
  self = this;
  var contexts;

  eventService.register('Context.ListController', processEvent);

  function processEvent(event) {
      if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent') {
        $scope.contexts.unshift({
          contextId: event.aggregateId,
          pipelineName: $routeParams.pipelineName,
          status: 'IN_PROGRESS',
          startTime: event.eventDateTime.epochSecond * 1000,
        });
        $scope.$apply();
      }
      else if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent') {
        var c = $filter('getBy')(self.contexts, 'contextId', event.aggregateId);
        c.status = event.status;
        c.endTime = event.endTime.epochSecond * 1000;
        $scope.$apply();
      }
  };

  $scope.pipelineName = $routeParams.pipelineName;

  $http.get('/pipelines/' + $scope.pipelineName + '/contexts').
    then (function (response) {
      self.contexts = response.data.reverse();
      $scope.contexts = self.contexts;
      loadFirstContextWithStatus(self.contexts, 'COMPLETE');
      loadFirstContextWithStatus(self.contexts, 'ERROR');
      loadFirstContextWithStatus(self.contexts, 'FAILURE');
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    });

  function loadFirstContextWithStatus(contexts, status) {
    var context = contexts.find(function(c) {
      return c.status === status;
    });
    if (context !== undefined) {
      contextService.getContext(context.contextId);
    }
  }
}
]);
