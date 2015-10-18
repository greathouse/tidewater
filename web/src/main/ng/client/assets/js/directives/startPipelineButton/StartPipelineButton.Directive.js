var m = angular.module('startPipelineButton', []);

m.directive('startPipelineButton', function() {
    var d = {
        scope: {
            pipelineName: '=',
        },
        restrict: 'E',
        templateUrl: '/templates/directives/startPipelineButton/template.html',
        controller: ['$scope', '$http', function($scope, $http) {
            $scope.start = function() {
                $http.get('/pipelines/' + $scope.pipelineName + '/start')
                .then (
                    function (response) {
                    },
                    function(response) {
                    }
                );
            }
        }]
    }
    return d;
});