var m = angular.module('startPipelineButton', []);

m.directive('startPipelineButton', function() {
    var d = {
        scope: {
            pipelineName: '='
        },
        restrict: 'E',
        templateUrl: '/templates/directives/startPipelineButton/template.html',
        controller: ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
            $scope.idle = function() {
                return !$scope.loading && !$scope.success && !$scope.error;
            };
            $scope.start = function() {
                $scope.loading = true;
                $http.get('/pipelines/' + $scope.pipelineName + '/start')
                .then (
                    function (response) {
                        $scope.loading = false;
                        $scope.success = true;
                        $timeout(function() {$scope.success = false;}, 3000)
                    },
                    function(response) {
                        $scope.loading = false;
                        $scope.error = true;
                    }
                );
            }
        }]
    }
    return d;
});