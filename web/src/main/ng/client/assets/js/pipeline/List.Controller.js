angular.module('pipelineModule').controller('Pipeline.ListController', [
    '$scope',
    '$http',
    '$filter',
    'FoundationApi',
    'Event.Service',

function ($scope, $http, $filter, foundationApi, eventService) {
  eventService.register('Pipeline.ListController', processEvent);

  var self = this;
  var pipelines;


  function processEvent(event) {
      if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent') {
          var pipeline = $filter('getBy')(self.pipelines, 'name', event.pipelineName);
          pipeline.status = 'IN_PROGRESS';
          $scope.$apply();
      }
      if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent') {
        var pipeline = $filter('getBy')(self.pipelines, 'name', event.pipelineName);
        pipeline.status = 'COMPLETED';
        $scope.$apply();
      }
  };

  $http.get('/pipelines').
    then (function (response) {
      self.pipelines = response.data;
      $scope.pipelines = self.pipelines;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    });
}
]);
