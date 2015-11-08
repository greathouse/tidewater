angular.module('contextModule').factory('Context.Service', [
    '$rootScope',
    '$http',
    'Context.Class',
    'Step.Class',
    'Event.Service',

function($rootScope, $http, Context, Step, eventService) {
    eventService.register('Context.Service', processEvent);

    var contexts = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.step.events.StepDefinedEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.addStep(new Step(event.aggregateId, event.stepType));
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent': function(event) {
            var context = new Context(event.aggregateId);
            contexts[event.aggregateId] = context;
            context.script = event.script;
            context.startTime = event.eventDateTime.epochSecond * 1000;
            context.workspace = event.workspace;
            context.metaDirectory = event.metaDirectory;
            context.status = 'IN_PROGRESS';
        },
        'greenmoonsoftware.tidewater.restart.events.ContextExecutionRestartedEvent': function(event) {
            var context = contexts[event.aggregateId];
            context.status = 'IN_PROGRESS';
        },
        'greenmoonsoftware.tidewater.step.events.StepConfiguredEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.addStepInput(event.aggregateId, event.step.inputs);
        },
        'greenmoonsoftware.tidewater.step.events.StepStartedEvent': function(event) {
            contexts[event.contextId.id].startStep(event.aggregateId, event.eventDateTime.epochSecond * 1000);
        },
        'greenmoonsoftware.tidewater.step.events.StepLogEvent': function(event) {
            contexts[event.contextId.id].stepLog(event.aggregateId, event.eventDateTime.epochSecond * 1000, event.message);
        },
        'greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent': function(event) {
            contexts[event.contextId.id].stepSuccess(event.aggregateId, event.endDate, event.step.outputs);
        },
        'greenmoonsoftware.tidewater.step.events.StepErroredEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.stepError(event.aggregateId, event.endDate, event.step.outputs);
            context.setLog(event.aggregateId, event.endDate, event.stackTrace);
        },
        'greenmoonsoftware.tidewater.step.events.StepFailedEvent': function(event) {
            contexts[event.contextId.id].fail(event.aggregateId, event.endDate, event.step.outputs);
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent': function(event) {
            contexts[event.aggregateId].end(event.eventDateTime.epochSecond * 1000);
        }
    }

    function getContext(contextId) {
        var context = contexts[contextId];
        if (context != undefined) {
            return context;
        }
        return loadContext(contextId);
    };

    function loadContext(contextId) {
        return $http.get('/contexts/' + contextId + '/events')
        .then(processEventResponse)
        .then(() => { return contexts[contextId] });
    };

    function processEventResponse(response) {
        angular.forEach(response.data, processEvent);
    }

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            eventHandlers[event.type](event);
            $rootScope.$apply();
        }
    };

    return {
        getContext: getContext
    };
}

]);