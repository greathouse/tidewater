var contextModule = angular.module('contextModule', []);

contextModule.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/pipelines/:pipelineName/contexts', {
                templateUrl: 'templates/context/list.html',
                controller: 'Context.ListController'
            })
            ;
    }
]);