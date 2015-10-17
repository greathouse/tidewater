angular.module('pipelineModule').controller('Pipeline.ListController', ['$scope', '$http', '$filter', 'FoundationApi',

function ($scope, $http, $filter, foundationApi) {
  var channel = postal.channel('TidewaterEvents');
  var subscription = channel.subscribe( "event.received", function ( data ) {
      processEvent(data);
  } );

  function processEvent(event) {
      if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent') {
          var pipeline = $filter('getByName')($scope.pipelines, event.pipelineName);
          pipeline.status = 'IN_PROGRESS';
          $scope.$apply();
      }
      if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent') {
        var pipeline = $filter('getByName')($scope.pipelines, event.pipelineName);
        pipeline.status = 'COMPLETED';
        $scope.$apply();
      }
  };


  $http.get('/pipelines').
    then (function (response) {
      $scope.pipelines = response.data;
    }, function(response) {
      foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed' });
    })
  ;

  $scope.startPipeline = function(pipelineName) {
    $http.get('/pipelines/' + pipelineName + '/start').
        then (
            function (response) {
            },
            function(response) {
            }
        );
  }

  $scope.statusImage = function(pipeline) {
    if (pipeline.status === 'IN_PROGRESS') {
        return './assets/img/gears.svg';
    }
    else {
        return './assets/img/green-check.svg';
    }
  }
}
]);
