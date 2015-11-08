angular.module('pipelineModule').factory('Pipeline.Class', [

function() {
    function Pipeline(name, script) {
        this.name = name;
        this.script = script;
    };

    Pipeline.apiResponseTransformer = function(responseData) {
        return new Pipeline(responseData.name, responseData.script);
    };

    return Pipeline;
}

]);