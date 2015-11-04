angular.module('pipelineModule').controller('Pipeline.ListController', [
    '$scope',
    '$filter',
    'Pipeline.Service',

function ($scope, $filter, pipelineService) {
    $scope.$watch(
        function() { return pipelineService.getList(); },
        function(newVal) {
            $scope.pipelines = Object.keys(newVal).map(key => newVal[key]);
        },
        true
    );
}
]);
