angular.module('application').controller('Application.HomeController', ['$scope',

function($scope) {
    $scope.samples = [
        { title: 'Docker', template: '/templates/samples/docker.html' },
        { title: 'Git / Gradle', template: '/templates/samples/git-gradle.html' },
        { title: 'Backup, Create, and Publish Static Site to AWS S3', template: '/templates/samples/s3-docker.html' }
    ];
}

]);