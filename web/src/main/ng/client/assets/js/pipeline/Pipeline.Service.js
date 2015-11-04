angular.module('pipelineModule').factory('Pipeline.Service', [
    '$rootScope',
    '$http',
    'FoundationApi',
    'Event.Service',

function($rootScope, $http, foundationApi, eventService) {
    eventService.register('Context.Service', processEvent);

    var pipelines = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent': function(event) {
            pipelines[event.aggregateId] = {
                name: event.aggregateId,
                script: event.scrip
            };
        }
    };

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            eventHandlers[event.type](event);
            $rootScope.$apply();
        }
    };

    function loadPipelines() {
        $http.get('/pipelines').
        then (
            function (response) {
                angular.forEach(response.data, function(pipeline) {
                    pipelines[pipeline.name] = pipeline;
                });
            },
            function(response) {
                foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed to load pipelines' });
            }
        );
    };

    function getList() {
        if (Object.keys(pipelines).length === 0) {
            loadPipelines();
        }
        return pipelines;
    };

    return {
        getList: getList
    }
}

]);