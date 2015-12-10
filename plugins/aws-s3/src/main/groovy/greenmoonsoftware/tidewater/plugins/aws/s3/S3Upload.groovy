package greenmoonsoftware.tidewater.plugins.aws.s3

import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.StepResult

class S3Upload extends AbstractStep {
    @Input String bucketName
    @Input String keyName
    @Input String upload

    @Override
    StepResult execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        File file = new File(context.attributes.workspace, upload)
        return StepResult.from(upload(file, log))
    }

    private boolean upload(File file, Closure<Void> log) {
        (file.isDirectory()) ? uploadDirectory(log, file) : uploadSingleFile(log, file)
    }

    private boolean uploadDirectory(Closure log, File directory) {
        log "Uploading directory ${directory.absolutePath}"
        new TidewaterS3TransferManager(log).transfer { directoryTransferUtility ->
            directoryTransferUtility.uploadDirectory(bucketName, keyName ?: '', directory, true)
        }
        return true
    }

    private boolean uploadSingleFile(Closure log, File file) {
        log "Uploading file: ${file.absolutePath}"
        new TidewaterS3TransferManager(log).transfer { directoryTransferUtility ->
            directoryTransferUtility.upload(bucketName, keyName ?: '', file)
        }
        return true
    }
}
