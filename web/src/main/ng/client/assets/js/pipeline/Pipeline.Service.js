angular.module('pipelineModule').factory('Pipeline.Service', [
    '$rootScope',
    '$q',
    '$http',
    'FoundationApi',
    'Event.Service',

function($rootScope, $q, $http, foundationApi, eventService) {
    eventService.register('Pipeline.Service', processEvent);

    var pipelines = {};
    var pipelineChangeListeners = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent': function(event) {
            pipelines[event.aggregateId] = {
                name: event.aggregateId,
                script: event.script
            };
        }
    };

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            eventHandlers[event.type](event);
            Object.keys(pipelineChangeListeners).forEach(function(key) {
                pipelineChangeListeners[key](pipelines);
            });
            $rootScope.$apply();
        }
    };

    function loadPipelines() {
        return $http.get('/pipelines').
        then (
            function (response) {
                angular.forEach(response.data, function(pipeline) {
                    pipelines[pipeline.name] = pipeline;
                });
                return pipelines;
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

    function getPipeline(name) {
        if (Object.keys(pipelines).length === 0) {
            return loadPipelines().then(function(p) {
                return p[name];
            });
        }
        return $q(function(resolve, reject) {
            resolve(pipelines[name]);
        });
    }

    function registerChangeListener(name, listener) {
        pipelineChangeListeners[name] = listener;
    };

    function unregisterChangeListener(name) {
        delete pipelineChangeListeners[name];
    };

    return {
        registerChangeListener: registerChangeListener,
        unregisterChangeListener: unregisterChangeListener,
        getList: getList,
        getPipeline: getPipeline
    }
}

]);