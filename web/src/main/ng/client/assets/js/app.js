'use strict';

var application = angular.module('application', [
  'ngAnimate',
  'ngRoute',
  'ngSanitize',

  //foundation
  'foundation',

  'elastic',
  'statusImage',
  'commandButton',
  'contextModule',
  'notificationModule',
  'pipelineModule'
])
  .config(config)
  .run(run)
;

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

application.filter('millisecondsToTimeString', function() {
 return function(milliseconds) {
   var seconds = Math.floor(milliseconds / 1000);
   var days = Math.floor(seconds / 86400);
   var hours = Math.floor((seconds % 86400) / 3600);
   var minutes = Math.floor(((seconds % 86400) % 3600) / 60);
   var seconds = Math.floor((((seconds % 86400) % 3600) % 60));
   var timeString = '';
   if(days > 0) timeString += (days == 1) ? (days + " day ") : (days + " days ");
   if(hours > 0) timeString += (hours == 1) ? (hours + " hour ") : (hours + " hours ");
   if(minutes > 0) timeString += (minutes == 1) ? (minutes + " minute ") : (minutes + " minutes ");
   if(seconds >= 0) timeString += (seconds == 1) ? (seconds + " second ") : (seconds + " seconds ");
   return timeString;
}
});

config.$inject = ['$routeProvider', '$locationProvider'];

function config($routeProvider, $locationProvider) {
  $routeProvider
    .when('/', {
      templateUrl: 'templates/home.html',
      controller: 'Application.HomeController'
    })
    .otherwise({
      redirectTo: '/',
      controller: 'Application.HomeController'
    })
    ;
}

function run() {
  FastClick.attach(document.body);
  var channel = postal.channel('TidewaterEvents');

  var stompClient = null;
  var socket = new SockJS('/hello');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function(frame) {
      stompClient.subscribe('/topic/events', function(event){
          channel.publish( "event.received", JSON.parse(event.body) );
      });
  });
}
