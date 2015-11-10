angular.module('pipelineModule').controller('Pipeline.DetailsController', [
  '$scope',
  '$routeParams',
  '$http',
  'FoundationApi',
  'Pipeline.Service',
  'PipelineStats.Class',

function (
    $scope,
    $routeParams,
    $http,
    foundationApi,
    pipelineService,
    PipelineStats
  ) {

  $scope.showScript = true;
  $scope.panelContexts = {
    status: '',
    title: 'N/A',
    list: []
  };
  $scope.showContexts = function(status) {
    if (status === $scope.panelContexts.status) {
      foundationApi.publish('contextsPanel', 'toggle');
      return;
    }
    $scope.panelContexts.status = status;
    $scope.panelContexts.list = (status === 'ALL') ? $scope.pipeline.getContexts() : $scope.pipeline.getContextsWithStatus(status);
    $scope.panelContexts.title = (status === 'ALL') ? 'All' : status;
    foundationApi.publish('contextsPanel', 'open');
  };

  $scope.submitScriptUpdate = function(formdata) {
    $http.patch('/pipelines/' + $routeParams.pipelineName + '/updateScript', formdata)
      .then(function(response) {
        foundationApi.publish('editModal', 'close');
      });
  };

  pipelineService.getPipeline($routeParams.pipelineName)
    .then((pipeline) => pipelineService.loadContexts(pipeline))
    .then((pipeline) => pipelineService.loadEvents(pipeline))
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
