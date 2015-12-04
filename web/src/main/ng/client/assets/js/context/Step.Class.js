angular.module('contextModule').factory('Step.Class', ['StepAttempt.Class',

function(StepAttempt) {
    function Step(name, type) {
        this.name = name;
        this.type = type;
        this.status;
        this.attempts = [];
        this.inputs = {};
        this.outputs = {};

        this.errorCount = 0;
        this.failedCount = 0;

        this.currentAttempt = function() {
            return this.attempts.slice(-1)[0];
        }
    };

    Step.prototype.typeSimpleName = function() {
        return this.type.substring(this.type.lastIndexOf('\.') + 1);
    }

    Step.prototype.start = function(startTime) {
        this.status = 'IN_PROGRESS';
        this.attempts.push(new StepAttempt(startTime));
    }

    Step.prototype.log = function(id, time, message) {
        this.currentAttempt().log(id, time, message);
    }

    Step.prototype.success = function(endTime, outputs) {
        this.outputs = outputs;
        this.status = 'SUCCESS';
        this.currentAttempt().success(endTime);
    }

    Step.prototype.error = function(endTime, outputs) {
        this.outputs = outputs;
        this.status = 'ERROR';
        this.errorCount = this.errorCount + 1;
        this.currentAttempt().error(endTime);
    }

    Step.prototype.fail = function(endTime, outputs) {
        this.outputs = outputs;
        this.status = 'FAILURE';
        this.failedCount = this.failedCount + 1;
        this.currentAttempt().fail(endTime);
    }

    Step.prototype.disabled = function(time) {
        this.status = 'DISABLED'
    }

    return Step;
}

]);