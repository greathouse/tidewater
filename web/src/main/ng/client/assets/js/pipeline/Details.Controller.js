angular.module('pipelineModule').controller('Pipeline.DetailsController', [
  '$scope',
  '$routeParams',
  '$http',
  'FoundationApi',
  'Pipeline.Service',

function (
    $scope,
    $routeParams,
    $http,
    foundationApi,
    pipelineService
  ) {

  $scope.showScript = true;

  $scope.submitScriptUpdate = function(formdata) {
    $http.patch('/pipelines/' + $routeParams.pipelineName + '/updateScript', formdata)
      .then(function(response) {
        foundationApi.publish('editModal', 'close');
      });
  };

  pipelineService.getPipeline($routeParams.pipelineName)
    .then((pipeline) => pipelineService.loadContexts(pipeline))
    .then((pipeline) => {
      $scope.pipeline = pipeline;
      $scope.formdata = {script: pipeline.script};
    });

  pipelineService.registerChangeListener(
    'Pipeline.DetailsController',
    (newList) => {
      $scope.pipeline = newList[$routeParams.pipelineName]
    }
  );

  $scope.$on("$destroy", () => pipelineService.unregisterChangeListener('Pipeline.DetailsController'));

}
]);
