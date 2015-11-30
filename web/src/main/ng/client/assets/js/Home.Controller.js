angular.module('application').controller('Application.HomeController', ['$scope',

function($scope) {
    $scope.samples = [
        { title: 'Docker', template: '/templates/samples/docker.html' }
    ];
}

]);