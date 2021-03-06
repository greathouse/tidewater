angular.module('pipelineModule').factory('Pipeline.Service', [
    '$rootScope',
    '$q',
    '$http',
    'Pipeline.Class',
    'PipelineContext.Class',
    'FoundationApi',
    'Event.Service',

function($rootScope, $q, $http, Pipeline, PipelineContext, foundationApi, eventService) {
    eventService.register('Pipeline.Service', processEvent);

    var pipelines = {};
    var pipelineChangeListeners = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent': function(event) {
            var pipeline = new Pipeline(event.aggregateId, event.script);
            pipelines[event.aggregateId] = p;
            return pipeline;
        },
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent': function(event) {
            var pipeline = pipelines[event.aggregateId];
            pipeline.startContext(event.contextId.id, event.eventDateTime.epochSecond * 1000);
            return pipeline;
        },
        'greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent': function(event) {
            var pipeline = pipelines[event.pipelineName];
            pipeline.endContext(event.aggregateId, event.status, event.eventDateTime.epochSecond * 1000);
            return pipeline;
        },
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineScriptUpdatedEvent': function(event) {
            var pipeline = pipelines[event.aggregateId];
            pipeline.script = event.script;
            return pipeline;
        }
    };

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            var pipeline = eventHandlers[event.type](event);
            pipeline.addEvent(event);
            Object.keys(pipelineChangeListeners).forEach(function(key) {
                pipelineChangeListeners[key](pipelines);
            });
            $rootScope.$apply();
        }
    };

    function loadPipelines() {
        return $http.get('/pipelines')
            .then (
                function (response) {
                    angular.forEach(response.data, function(pipeline) {
                        pipelines[pipeline.name] = Pipeline.apiResponseTransformer(pipeline);
                    });
                },
                function(response) {
                    foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed to load pipelines' });
                }
            );
    };

    function getList() {
        if (Object.keys(pipelines).length === 0) {
            return loadPipelines()
            .then(() => { return pipelines });
        }
        return $q((resolve, reject) => { resolve(pipelines) });
    };

    function getPipeline(name) {
        return getList()
            .then(() => pipelines[name]);
    };

    function loadContexts(pipeline) {
        return $http.get('/pipelines/' + pipeline.name + '/contexts')
            .then((response) => {
                angular.forEach(response.data, context => pipeline.addContext(PipelineContext.apiResponseTransformer(context)));
                return pipeline;
            });
    }

    function loadEvents(pipeline) {
        return $http.get('/pipelines/' + pipeline.name + '/events')
            .then((response) => {
                pipeline.events = response.data;
                return pipeline;
            })
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
        getPipeline: getPipeline,
        loadContexts: loadContexts,
        loadEvents: loadEvents
    }
}

]);