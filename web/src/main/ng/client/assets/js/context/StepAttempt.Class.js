angular.module('contextModule').factory('StepAttempt.Class', [

function() {
    function StepAttempt(startTime) {
        this.startTime = startTime;
        this.status = 'IN_PROGRESS';
        this.endTime;
        this.logs = [];
    };

    StepAttempt.prototype.log = function(time, message) {
        this.logs.push({time: time, message: message});
    }

    StepAttempt.prototype.success = function(endTime) {
        this.endTime = endTime;
        this.status = 'SUCCESS';
    }

    StepAttempt.prototype.error = function(endTime) {
        this.endTime = endTime;
        this.status = 'ERROR';
    }

    StepAttempt.prototype.fail = function(endTime) {
        this.endTime = endTime;
        this.status = 'FAILURE';
    }

    StepAttempt.prototype.duration = function() {
        this.endTime - this.startTime;
    }

    return StepAttempt;
}

]);