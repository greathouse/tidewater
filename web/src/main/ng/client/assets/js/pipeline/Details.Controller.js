angular.module('pipelineModule').controller('Pipeline.DetailsController', [
  '$scope',
  '$routeParams',
  'Pipeline.Service',

function (
    $scope,
    $routeParams,
    pipelineService
  ) {

  pipelineService.getPipeline($routeParams.pipelineName)
    .then(function(pipeline) {
        $scope.pipeline = pipeline;
    }
  );
}
]);
