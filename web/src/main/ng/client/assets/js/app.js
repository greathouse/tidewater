'use strict';

var application = angular.module('application', [
  'ui.router',
  'ngAnimate',

  //foundation
  'foundation',
  'foundation.dynamicRouting',
  'foundation.dynamicRouting.animations',

  'executorModule',
  'notificationModule',
  'pipelineModule'
])
  .config(config)
  .run(run)
;

application.filter('getByName', function() {
   return function(input, name) {
     var i=0, len=input.length;
     for (; i<len; i++) {
       if (input[i].name === name) {
         return input[i];
       }
     }
     return null;
   }
 });

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
