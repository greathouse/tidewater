'use strict';

angular.module('application', [
  'ui.router',
  'ngAnimate',

  //foundation
  'foundation',
  'foundation.dynamicRouting',
  'foundation.dynamicRouting.animations',

  'pipelineModule',
  'notificationModule'
])
  .config(config)
  .run(run)
;

config.$inject = ['$urlRouterProvider', '$locationProvider'];

function config($urlProvider, $locationProvider) {
  $urlProvider.otherwise('/');

  $locationProvider.html5Mode({
    enabled:false,
    requireBase: false
  });

  $locationProvider.hashPrefix('!');
}

function run() {
  FastClick.attach(document.body);
  var channel = postal.channel('TidewaterEvents');

  var stompClient = null;
  var socket = new SockJS('/hello');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
      stompClient.subscribe('/topic/events', function(event){
          console.log(event);
          channel.publish( "event.received", JSON.parse(event.body) );
      });
  });
}
