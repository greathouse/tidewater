package greenmoonsoftware.tidewater.web.pipeline.view

class PipelineNotFoundException extends RuntimeException{
    PipelineNotFoundException(String message) {
        super(message)
    }
}
