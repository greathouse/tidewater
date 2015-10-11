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
            foundationApi.publish('main-notifications', { color: 'success', autoclose: 3000, content: 'Some Event' });
        });
    });
};
