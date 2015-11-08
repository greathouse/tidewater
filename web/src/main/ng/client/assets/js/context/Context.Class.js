angular.module('contextModule').factory('Context.Class', [

function() {
    this.id;
    this.status;
    this.startTime;
    this.endTime;

    function Context(contextId) {
        this.id = contextId;
    }

    Context.apiResponseTransformer = (responseData) => {
        var c = new Context(responseData.contextId);
        c.status = responseData.status;
        c.startTime = responseData.startTime;
        c.endTime = responseData.endTime;
        return c;
    };

    return Context;
}

]);