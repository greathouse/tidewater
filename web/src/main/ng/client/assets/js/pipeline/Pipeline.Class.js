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

       Pipeline.prototype.getContextsWithStatus = function(status)
       {
            return this.getContexts().filter( context => context.status === status );
       }

       Pipeline.prototype.erroredContexts = function() {
            return this.getContextsWithStatus('ERROR' );
       };

       Pipeline.prototype.failedContexts = function() {
            return this.getContextsWithStatus('FAILURE' );
       };

       Pipeline.prototype.completeContexts = function() {
            return this.getContextsWithStatus('COMPLETE' );
       };

       Pipeline.prototype.inProgressContexts = function() {
            return this.getContextsWithStatus('IN_PROGRESS' );
       };


       Pipeline.apiResponseTransformer = responseData => new Pipeline(responseData.name, responseData.script);

       return Pipeline;
   }

   ]);