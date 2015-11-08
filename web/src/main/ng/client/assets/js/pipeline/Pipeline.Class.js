angular.module('pipelineModule').factory('Pipeline.Class', ['Context.Class',

function(Context) {
    function Pipeline(name, script) {
        this.name = name;
        this.script = script;
    };

    var contexts = {};

    Pipeline.prototype.newContext = contextId => contexts[contextId] = new Context(contextId);

    Pipeline.prototype.addContext = context => contexts[context.id] = context;

    Pipeline.prototype.getContexts = () => Object.keys(contexts).map(key => contexts[key]);

    Pipeline.apiResponseTransformer = responseData => new Pipeline(responseData.name, responseData.script);

    return Pipeline;
}

]);