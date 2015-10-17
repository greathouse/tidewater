var pipelineModule = angular.module('pipelineModule', []);

pipelineModule.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/pipelines', {
                templateUrl: 'templates/pipeline/list.html',
                controller: 'ListController'
            }).
            when('/pipelines/new', {
                templateUrl: 'templates/pipeline/new.html',
                controller: 'NewController'
            });
    }
]);