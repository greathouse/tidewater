var contextModule = angular.module('contextModule', []);

contextModule.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/pipelines/:pipelineName/contexts', {
                templateUrl: 'templates/context/list.html',
                controller: 'Context.ListController'
            })
            .when('/pipelines/:pipelineName/contexts/:contextId', {
                templateUrl: 'templates/context/details.html',
                controller: 'Context.DetailsController'
            })
        ;
    }
]);