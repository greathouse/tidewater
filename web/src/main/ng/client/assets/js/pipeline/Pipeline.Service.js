angular.module('pipelineModule').factory('Pipeline.Service', ['$rootScope', '$http', 'Event.Service',

function($rootScope, $http, eventService) {
    eventService.register('Context.Service', processEvent);

    var pipelines = {};
    var eventHandlers = {

    };

    function processEvent(event, index) {
        if (eventHandlers.hasOwnProperty(event.type)) {
            eventHandlers[event.type](event);
            $rootScope.$apply();
        }
    };
}

]);