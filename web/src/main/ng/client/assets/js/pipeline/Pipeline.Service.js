angular.module('pipelineModule').factory('Pipeline.Service', [
    '$rootScope',
    '$q',
    '$http',
    'FoundationApi',
    'Event.Service',

function($rootScope, $q, $http, foundationApi, eventService) {
    eventService.register('Pipeline.Service', processEvent);

    var pipelines = {};
    var contextsToPipeline = {};
    var pipelineChangeListeners = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent': function(event) {
            pipelines[event.aggregateId] = {
                name: event.aggregateId,
                script: event.script
            };
        },
        'greenmoonsoftware.tidewater.web.pipeline.events.PipelineStartedEvent': function(event) {
            var pipeline = pipelines[event.aggregateId];
            pipeline.contexts[event.contextId.id] = {};
            contextsToPipeline[event.contextId.id] = pipeline.name;
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
        return $http.get('/pipelines')
            .then (
                function (response) {
                    angular.forEach(response.data, function(pipeline) {
                        var p = createPipeline()
                        p.name = pipeline.name;
                        p.script = pipeline.script;
                        pipelines[pipeline.name] = p;
                    });
                },
                function(response) {
                    foundationApi.publish('main-notifications', { color: 'alert', autoclose: 3000, content: 'Failed to load pipelines' });
                }
            );
    };

    function createPipeline() {
        return {
            contexts: {},
            get contextsAsList() { return Object.keys(this.contexts).map(key => this.contexts[key]); }
        };
    }

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
                pipeline.contexts = response.data.reduce((c, context) => {
                    c[context.contextId] = context;
                    return c;
                }, {});
                return pipeline;
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
        getPipeline: getPipeline,
        loadContexts: loadContexts
    }
}

]);