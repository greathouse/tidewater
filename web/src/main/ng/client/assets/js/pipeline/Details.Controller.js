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
    .then((pipeline) => pipelineService.loadContexts(pipeline))
    .then((pipeline) => $scope.pipeline = pipeline);

  pipelineService.registerChangeListener(
    'Pipeline.DetailsController',
    (newList) => $scope.pipeline = newList[$routeParams.pipelineName]
  );

  $scope.$on("$destroy", () => pipelineService.unregisterChangeListener('Pipeline.DetailsController'));

}
]);
