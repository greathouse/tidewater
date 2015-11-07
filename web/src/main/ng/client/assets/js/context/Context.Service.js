angular.module('contextModule').factory('Context.Service', ['$rootScope', '$http', 'Event.Service', 'Pipeline.Service',

function($rootScope, $http, eventService, pipelineService) {
    eventService.register('Context.Service', processEvent);

    var contexts = {};
    var eventHandlers = {
        'greenmoonsoftware.tidewater.step.events.StepDefinedEvent': function(event) {
            var context = contexts[event.contextId.id];
            context.stepIndex[event.name] = context.steps.length;
            context.steps.push({
                stepName: event.name,
                stepType: event.stepType.substring(event.stepType.lastIndexOf('\.') + 1),
                attempts: [],
                errorCount: 0,
                failedCount: 0
            });
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent': function(event) {
            var context = contexts[event.aggregateId];
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
            context.steps[context.stepIndex[event.aggregateId]].inputs = event.step.inputs;
        },
        'greenmoonsoftware.tidewater.step.events.StepStartedEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[context.stepIndex[event.aggregateId]];
            step.status = 'IN_PROGRESS';
            step.attempts.push({
                status: 'IN_PROGRESS',
                startTime: event.eventDateTime.epochSecond * 1000,
                endTime: 0,
                logs: []
            });
        },
        'greenmoonsoftware.tidewater.step.events.StepLogEvent': function(event) {
            var context = contexts[event.contextId.id];
            var attempt = context.steps[context.stepIndex[event.aggregateId]].attempts.slice(-1)[0];
            attempt.logs.push({
                time: event.eventDateTime.epochSecond * 1000,
                message: event.message
            })
        },
        'greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[context.stepIndex[event.aggregateId]];
            context.lastCompletedStep = step;
            step.outputs = event.step.outputs;
            step.status = 'SUCCESS';
            var attempt = step.attempts.slice(-1)[0];
            attempt.status = 'SUCCESS';
            attempt.endTime = event.endDate;
            attempt.duration = attempt.endTime - attempt.startTime;
        },
        'greenmoonsoftware.tidewater.step.events.StepErroredEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[context.stepIndex[event.aggregateId]];
            context.lastCompletedStep = step;
            step.outputs = event.step.outputs;
            step.status = 'ERROR';
            step.errorCount = step.errorCount + 1;
            var attempt = step.attempts.slice(-1)[0];
            attempt.status = 'ERROR';
            attempt.endTime = event.endDate;
            attempt.duration = attempt.endTime - attempt.startTime;
            attempt.logs.push({
                time: event.endDate,
                message: event.stackTrace
            });
        },
        'greenmoonsoftware.tidewater.step.events.StepFailedEvent': function(event) {
            var context = contexts[event.contextId.id];
            var step = context.steps[context.stepIndex[event.aggregateId]];
            context.lastCompletedStep = step;
            step.outputs = event.step.outputs;
            step.status = 'FAILURE';
            step.failedCount = step.failedCount + 1;
            var attempt = step.attempts.slice(-1)[0];
            attempt.status = 'FAILURE';
            attempt.endTime = event.endDate;
            attempt.duration = attempt.endTime = attempt.startTime;
        },
        'greenmoonsoftware.tidewater.context.events.ContextExecutionEndedEvent': function(event) {
            var context = contexts[event.aggregateId];
            context.endTime = event.eventDateTime.epochSecond * 1000;
            var lastAttempt = context.lastCompletedStep.attempts.slice(-1)[0];
            context.status = lastAttempt.status;
            context.duration = context.endTime - context.startTime;
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
        var context = createContext(contextId);
        $http.get('/contexts/' + contextId + '/events').then(processEventResponse);
        contexts[contextId] = context;
        return context;
    };

    function processEventResponse(response) {
        angular.forEach(response.data, processEvent);
    }

    function createContext(contextId) {
        return {
            id: contextId,
            steps: [],
            stepIndex: {}
        }
    };

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