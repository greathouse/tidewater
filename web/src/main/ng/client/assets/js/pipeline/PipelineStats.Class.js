angular.module('pipelineModule').factory('PipelineStats.Class', [

function() {
    this.total = 0;
    this.errored = 0;
    this.failed = 0;
    this.inProgress = 0;
    this.complete = 0;

    function PipelineStats() {
    };

    PipelineStats.prototype.percent = function(numerator) {
        return Math.round(numerator / this.total * 1000) / 10;
    };

    return PipelineStats;
}

]);