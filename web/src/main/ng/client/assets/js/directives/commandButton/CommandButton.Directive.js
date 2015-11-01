var m = angular.module('commandButton', []);

m.directive('twCommand', function() {
    var d = {
        scope: {
            command: '@',
            text: '@'
        },
        restrict: 'E',
        templateUrl: '/templates/directives/commandButton/template.html',
        controller: ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {

            $scope.idle = function() {
                return !$scope.loading && !$scope.success && !$scope.error;
            };
            $scope.start = function() {
                $scope.loading = true;
                $http.get($scope.command)
                .then (
                    function (response) {
                        $scope.loading = false;
                        $scope.success = true;
                        $timeout(function() {$scope.success = false;}, 6000)
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