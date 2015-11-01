var notificationModule = angular.module('notificationModule', [])
.run(run);

run.$inject = ['FoundationApi', 'Event.Service'];

function run(foundationApi, eventService) {
    eventService.register('Notification.Module', processEvent);

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
