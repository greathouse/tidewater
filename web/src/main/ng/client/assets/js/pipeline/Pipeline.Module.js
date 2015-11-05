var pipelineModule = angular.module('pipelineModule', []);

pipelineModule.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/pipelines', {
                templateUrl: 'templates/pipeline/list.html',
                controller: 'Pipeline.ListController'
            })
            .when('/pipelines/new', {
                templateUrl: 'templates/pipeline/new.html',
                controller: 'Pipeline.NewController'
            })
            .when('/pipelines/:pipelineName', {
                templateUrl: 'templates/pipeline/details.html',
                controller: 'Pipeline.DetailsController'
            });
    }
]);