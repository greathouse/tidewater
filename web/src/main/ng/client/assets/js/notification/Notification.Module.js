var notificationModule = angular.module('notificationModule', []);

notificationModule.run(function () {
    var stompClient = null;
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/ping', function(event){
            console.log(event);
//            processEvent(JSON.parse(event.body));
        });
    });
});
