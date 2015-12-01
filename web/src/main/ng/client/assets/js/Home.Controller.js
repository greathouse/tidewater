angular.module('application').controller('Application.HomeController', ['$scope',

function($scope) {
    $scope.samples = [
        { title: 'Docker', template: '/templates/samples/docker.html' },
        { title: 'Git / Gradle', template: '/templates/samples/git-gradle.html' }
    ];
}

]);