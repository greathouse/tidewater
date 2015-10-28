angular.module('contextModule').factory('Context.Service', ['$http',

function($http) {
    var contexts = {};

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
        $http.get('/contexts/' + contextId + '/events')
            .then(function(response) {
                angular.forEach(response.data, processEvent);
            });
    };

    function createContext(contextId) {
        return {
            id: contextId,
            attributes: {},
            steps: {}
        }
    };

    function processEvent(event, index) {
        if (event.type === 'greenmoonsoftware.tidewater.step.events.StepDefinedEvent') {
            var context = contexts[event.contextId.id];
            context.steps[event.name] = {
                stepName: event.name,
                stepType: event.stepType,
                attempts: []
            };
        }
        else if (event.type === 'greenmoonsoftware.tidewater.context.events.ContextExecutionStartedEvent') {
            var context = contexts[event.aggregateId];
            context.script = event.script;
            context.attributes.workspace = event.workspace;
            context.attributes.workspace = event.metaDirectory;
        }
        else if (event.type === 'greenmoonsoftware.tidewater.step.events.StepConfiguredEvent') {
            var context = contexts[event.contextId.id];
            context.steps[event.aggregateId].inputs = event.step.inputs;
        }
        else if (event.type === 'greenmoonsoftware.tidewater.step.events.StepStartedEvent') {
            var context = contexts[event.contextId.id];
            var step = context.steps[event.aggregateId];
            step.attempts.push({
                status: 'IN_PROGRESS',
                startTime: event.eventDateTime.epochSecond,
                endTime: 0,
                logs: []
            });
        }
        else if (event.type === 'greenmoonsoftware.tidewater.step.events.StepLogEvent') {
            var context = contexts[event.contextId.id];
            var attempt = context.steps[event.aggregateId].attempts.slice(-1)[0];
            attempt.logs.push({
                time: event.eventDateTime.epochSecond,
                message: event.message
            })
        }
        else if (event.type === 'greenmoonsoftware.tidewater.step.events.StepSuccessfullyCompletedEvent') {
            var context = contexts[event.contextId.id];
            var step = context.steps[event.aggregateId];
            step.outputs = event.step.outputs;
            var attempt = step.attempts.slice(-1)[0];
            attempt.status = 'SUCCESS';
            attempt.endTime = event.endDate;
            attempt.duration = attempt.endTime - attempt.startTime;
        }
    };

    return {
        getContext: getContext
    };
}

]);