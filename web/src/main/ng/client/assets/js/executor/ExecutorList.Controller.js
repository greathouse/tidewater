angular.module('executorModule').controller('ExecutorListController', ['$scope', '$http', 'FoundationApi',

function ($scope, $http, foundationApi) {
    $scope.pipelineContexts = [];

    var channel = postal.channel('TidewaterEvents');
    var subscription = channel.subscribe( "event.received", function ( event ) {
        if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent') {
            $scope.pipelineContexts.push({"pipelineContextName": event.aggregateId});
        }
    } );
}

]);
