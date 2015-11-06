angular.module('pipelineModule').controller('Pipeline.ListController', [
    '$scope',
    '$filter',
    'Pipeline.Service',

function ($scope, $filter, pipelineService) {
    pipelineService.getList().then((pipelines) => { $scope.pipelines = pipelines });

    pipelineService.registerChangeListener('Pipeline.ListController', function (newList) {
        $scope.pipelines = newList;
    });

    $scope.$on("$destroy", function() {
        pipelineService.unregisterChangeListener('Pipeline.ListController');
    });
}
]);
