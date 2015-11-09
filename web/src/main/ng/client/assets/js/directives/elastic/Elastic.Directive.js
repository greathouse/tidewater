angular.module('elastic', []).directive('elastic', [
    '$timeout',
    function($timeout) {
        return {
            restrict: 'A',
            link: function($scope, element) {
                $scope.initialHeight = $scope.initialHeight || element[0].style.height;
                var resize = function() {
                    element[0].style.height = $scope.initialHeight;
                    var height = window.innerHeight - 200 < element[0].scrollHeight ? window.innerHeight - 200 : element[0].scrollHeight;
                    element[0].style.height = "" + height + "px";
                };
                element.on("input change", resize);
                $timeout(resize, 0);
            }
        };
    }
]);