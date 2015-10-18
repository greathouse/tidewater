var m = angular.module('statusImage', []);

m.directive('twStatusImage', function() {
    var twStatusImage = {
        link: function(scope, element, attrs) {
            var status = attrs.twStatusImage;
            var image = './assets/img/green-check.svg';
            var clazz = '';
            if (status === 'IN_PROGRESS') {
                image = './assets/img/gears.svg';
                clazz = 'blink';
            }
            element.attr("src", image);
            element.attr('class', clazz);
        }
    }
    return twStatusImage;
});