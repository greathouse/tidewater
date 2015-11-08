angular.module('pipelineModule').factory('PipelineContext.Class', [

function() {
    this.id;
    this.status;
    this.startTime;
    this.endTime;

    function PipelineContext(contextId, startTime) {
        this.id = contextId;
        this.startTime = startTime;
        this.status = 'IN_PROGRESS';
    };

    PipelineContext.apiResponseTransformer = (responseData) => {
        var c = new PipelineContext(responseData.contextId, responseData.startTime);
        c.status = responseData.status;
        c.endTime = responseData.endTime;
        return c;
    };

    return PipelineContext;
}

]);