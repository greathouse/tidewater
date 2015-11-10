angular.module('pipelineModule').factory('Pipeline.Class', [
    '$rootScope',
    'PipelineContext.Class',
    'PipelineStats.Class',

   function(
        $rootScope,
        PipelineContext,
        PipelineStats) {
       function Pipeline(name, script) {
           this.contexts = {};
           this.name = name;
           this.script = script;
           this.stats = new PipelineStats();
       };

       Pipeline.prototype.updateStats = function() {
            this.stats.total = this.getContexts().length;
            this.stats.errored = this.erroredContexts().length;
            this.stats.failed = this.failedContexts().length;
            this.stats.inProgress = this.inProgressContexts().length
            this.stats.complete = this.completeContexts().length;
            $rootScope.$apply();
       }

       Pipeline.prototype.startContext = function(contextId, startTime) {
            this.contexts[contextId] = new PipelineContext(contextId, startTime);
            this.updateStats();
       }

       Pipeline.prototype.endContext = function(contextId, status, endTime) {
            var c = this.contexts[contextId];
            c.status = status;
            c.endTime = endTime;
            this.updateStats();
       }

       Pipeline.prototype.addContext = function(context) {
            this.contexts[context.id] = context;
            this.updateStats();
       }

       Pipeline.prototype.getContexts = function() {
            return Object.keys(this.contexts).map(key => this.contexts[key]);
       }

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