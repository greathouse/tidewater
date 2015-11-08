angular.module('pipelineModule').factory('Pipeline.Class', ['PipelineContext.Class',

   function(PipelineContext) {
       function Pipeline(name, script) {
           this.name = name;
           this.script = script;
       };

       var contexts = {};

       Pipeline.prototype.startContext = (contextId, startTime) => {
            contexts[contextId] = new PipelineContext(contextId, startTime);
            console.log(contexts[contextId]);
       }

       Pipeline.prototype.endContext = (contextId, status, endTime) => {
            var c = contexts[contextId];
            c.status = status;
            c.endTime = endTime;
       }

       Pipeline.prototype.addContext = context => contexts[context.id] = context;

       Pipeline.prototype.getContexts = () => Object.keys(contexts).map(key => contexts[key]);

       Pipeline.apiResponseTransformer = responseData => new Pipeline(responseData.name, responseData.script);

       return Pipeline;
   }

   ]);