package greenmoonsoftware.tidewater.plugins.aws

import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class S3Upload extends AbstractStep {
    @Input String bucketName
    @Input String keyName
    @Input String upload

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        File file = new File(context.workspace, upload)
        return (file.isDirectory()) ? uploadDirectory(log, file) : uploadSingleFile(log, file)
    }

    private boolean uploadDirectory(Closure log, File directory) {
        log "Uploading directory ${directory.absolutePath}"
        new TidewaterS3TransferManager(log).transfer { directoryTransferUtility ->
            directoryTransferUtility.uploadDirectory(bucketName, keyName ?: '', directory, true)
        }
    }

    private boolean uploadSingleFile(Closure log, File file) {
        log "Uploading file: ${file.absolutePath}"
        new TidewaterS3TransferManager(log).transfer { directoryTransferUtility ->
            directoryTransferUtility.upload(bucketName, keyName ?: '', file)
        }
    }
}
