var m = angular.module('statusImage', []);

m.directive('twStatusImage', function() {
    var imageMap = {
        'BLANK': './assets/img/blank.png',
        'IN_PROGRESS': './assets/img/gears.svg',
        'COMPLETE': './assets/img/green-check.svg',
        'SUCCESS': './assets/img/green-check.svg',
        'ERROR': './assets/img/error.svg',
        'FAILURE': './assets/img/failure.svg',
        'DISABLED': './assets/img/disabled.svg'
    }
    var twStatusImage = {
        scope: {
            twStatusImage: '@'
        },
        link: function(scope, element, attrs) {
            scope.$watch('twStatusImage', function() {
                var status = scope.twStatusImage;
                 var image = imageMap.hasOwnProperty(status) ? imageMap[status] : imageMap.BLANK;
                 element.attr("src", image);
            });
        }
    }
    return twStatusImage;
});