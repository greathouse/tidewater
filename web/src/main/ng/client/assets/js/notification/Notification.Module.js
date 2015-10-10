var notificationModule = angular.module('notificationModule', []);

notificationModule.run(function () {
    var stompClient = null;
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/greetings/ping', function(event){
            console.log(event);
        });
    });
});
