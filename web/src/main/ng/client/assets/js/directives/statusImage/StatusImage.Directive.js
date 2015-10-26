var m = angular.module('statusImage', []);

m.directive('twStatusImage', function() {
    var twStatusImage = {
        scope: {
            twStatusImage: '@'
        },
        link: function(scope, element, attrs) {
            scope.$watch('twStatusImage', function() {
                var status = scope.twStatusImage;
                 var image = './assets/img/blank.png';
                 var clazz = '';
                 if (status === 'IN_PROGRESS') {
                     image = './assets/img/gears.svg';
                     clazz = 'blink';
                 }
                 else if (status === 'COMPLETE' || status === 'SUCCESS') {
                    image = './assets/img/green-check.svg';
                 }
                 else if (status === 'ERROR') {
                    image = './assets/img/error.svg';
                 }
                 element.attr("src", image);
                 element.attr('class', clazz);
            });
        }
    }
    return twStatusImage;
});