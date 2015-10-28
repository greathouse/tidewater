angular.module('contextModule').factory('Context.Service', ['$http',

function($http) {
    var contexts = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.step.events.StepDefinedEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.steps[event.name] = {
                stepName: event.name,
                stepType: event.stepType,
                attempts: []
            };
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent': function(event) {
            var context = contexts[event.aggregateId];
            context.script = event.script;
            context.startTime = event.eventDateTime.epochSecond;
            context.workspace = event.workspace;
            context.metaDirectory = event.metaDirectory;
            context.status = 'IN_PROGRESS';
        },
        'greenmoonsoftware.tidewater.step.events.StepConfiguredEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.steps[event.aggregateId].inputs = event.step.inputs;
        },
        'greenmoonsoftware.tidewater.step.events.StepStartedEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[event.aggregateId];
            step.attempts.push({
                status: 'IN_PROGRESS',
                startTime: event.eventDateTime.epochSecond,
                endTime: 0,
                logs: []
            });
        },
        'greenmoonsoftware.tidewater.step.events.StepLogEvent': function(event) {
            var context = contexts[event.contextId.id];
            var attempt = context.steps[event.aggregateId].attempts.slice(-1)[0];
            attempt.logs.push({
                time: event.eventDateTime.epochSecond,
                message: event.message
            })
        },
        'greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[event.aggregateId];
            context.lastCompletedStep = step;
            step.outputs = event.step.outputs;
            var attempt = step.attempts.slice(-1)[0];
            attempt.status = 'SUCCESS';
            attempt.endTime = event.endDate;
            attempt.duration = attempt.endTime - attempt.startTime;
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent': function(event) {
            var context = contexts[event.aggregateId];
            context.endTime = event.eventDateTime.epochSecond;
            var lastAttempt = context.lastCompletedStep.attempts.slice(-1)[0];
            context.status = lastAttempt.status;
        }
    }

    function getContext(contextId) {
        var context = contexts[contextId];
        if (context != undefined) {
            return context;
        }
        loadContext(contextId);
        return contexts[contextId];
    };

    function loadContext(contextId) {
        if (!contexts.hasOwnProperty(contextId)) {
            contexts[contextId] = createContext(contextId);
        }
        $http.get('/contexts/' + contextId + '/events').then(processEventResponse);
    };

    function processEventResponse(response) {
        angular.forEach(response.data, processEvent);
    }

    function createContext(contextId) {
        return {
            id: contextId,
            steps: {}
        }
    };

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            eventHandlers[event.type](event);
        }
    };

    return {
        getContext: getContext
    };
}

]);