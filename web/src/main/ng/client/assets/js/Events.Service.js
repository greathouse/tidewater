var application = angular.module('application').factory('Event.Service', [

function() {
    var eventListeners = {};

    var channel = postal.channel('TidewaterEvents');
    var subscription = channel.subscribe( "event.received", function ( data ) {
        Object.keys(eventListeners).forEach(function(key) {
            eventListeners[key](data);
        });
    } );


    function register(name, listener) {
        eventListeners[name] = listener;
    }

    function unregister(name) {
        delete eventListeners[name];
    }

    return {
        register: register,
        unregister: unregister
    }
}

]);