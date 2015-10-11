var notificationModule = angular.module('notificationModule', [])
.run(run);

run.$inject = ['FoundationApi'];

function run(foundationApi) {
    var channel = postal.channel('TidewaterEvents');
    var subscription = channel.subscribe( "event.received", function ( data ) {
        processEvent(data);
    } );

    function processEvent(event) {
        if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextStartedEvent') {
            notifySuccess(event.aggregateId, event.pipelineName + ' Started');
        }
        if (event.type === 'greenmoonsoftware.tidewater.web.context.events.PipelineContextEndedEvent') {
            notifySuccess(event.aggregateId, 'Ended');
        }
    };

    function notifySuccess(title, text) {
        foundationApi.publish('main-notifications', { color: 'success', autoclose: 3000, title: title, content: text });
    };
};
