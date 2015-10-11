var notificationModule = angular.module('notificationModule', [])
.run(run)
;

run.$inject = ['FoundationApi'];

function run(foundationApi) {
    var stompClient = null;
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/events', function(event){
            console.log(event);
            processEvent(JSON.parse(event.body));
        });
    });

    function processEvent(event) {
        var response = document.getElementById('response');
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
