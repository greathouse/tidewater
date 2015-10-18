'use strict';

var application = angular.module('application', [
  'ngAnimate',
  'ngRoute',

  //foundation
  'foundation',

  'statusImage',
  'contextModule',
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

application.filter('getBy', function() {
  return function(input, attribute, value) {
     var i=0, len=input.length;
     for (; i<len; i++) {
       if (input[i][attribute] === value) {
         return input[i];
       }
     }
     return null;
   }
});

config.$inject = ['$routeProvider', '$locationProvider'];

function config($routeProvider, $locationProvider) {
  $routeProvider
    .when('/', {
      templateUrl: 'templates/home.html'
    }).
    otherwise({
      redirectTo: '/'
    });
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
