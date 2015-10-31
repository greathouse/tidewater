angular.module('contextModule').directive('stepAttemptDetails', [

function() {
    var d = {
        scope: {
            attempt: '='
        },
        restrict: 'E',
        templateUrl: '/templates/context/stepAttemptDetails.html'
    }
    return d;
}

]);