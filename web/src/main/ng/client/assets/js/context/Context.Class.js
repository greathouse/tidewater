angular.module('contextModule').factory('Context.Class', [

function() {
    function Context(contextId) {
        this.id = contextId;
        this.status;
        this.script;
        this.workspace;
        this.metaDirectory;
        this.startTime;
        this.endTime;
        this.steps = [];
        this.stepsByName = {}
    };

    Context.prototype.addStep = function(step) {
        this.stepsByName[step.name] = step;
        this.steps.push(step);
    };

    Context.prototype.addStepInput = function(stepName, inputs) {
        this.stepsByName[stepName].inputs = inputs;
    }

    Context.prototype.startStep = function(stepName, startTime) {
        this.stepsByName[stepName].start(startTime);
    }

    Context.prototype.stepLog = function(stepName, logId, time, message) {
        this.stepsByName[stepName].log(logId, time, message);
    }

    Context.prototype.stepSuccess = function(stepName, endTime, outputs) {
        this.stepsByName[stepName].success(endTime, outputs);
    }

    Context.prototype.stepError = function(stepName, endTime, outputs) {
        this.stepsByName[stepName].error(endTime, outputs);
    }

    Context.prototype.stepFail = function(stepName, endTime, outputs) {
        this.stepsByName[stepName].fail(endTime, outputs);
    }

    Context.prototype.end = function(endTime) {
        this.endTime = endTime;
        this.status = this.steps.slice(-1)[0].status;
    };

    Context.prototype.duration = function() {
        return this.endTime - this.startTime;
    };

    Context.prototype.steps = function() {
        return this.steps;
    }

    return Context;
}

]);