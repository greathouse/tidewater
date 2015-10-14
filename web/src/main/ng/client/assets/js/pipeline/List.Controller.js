angular.module('pipelineModule').controller('ListController', ['$scope', '$http', '$filter',

function ($scope, $http, $filter) {
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
      $scope.pipelines = [];
      angular.forEach(response.data, function(pipeline) {
        $scope.pipelines.push({name: pipeline.name, img: './assets/img/green-check.svg'})
      });
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
