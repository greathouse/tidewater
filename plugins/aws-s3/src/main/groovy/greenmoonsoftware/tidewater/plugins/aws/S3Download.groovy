package greenmoonsoftware.tidewater.plugins.aws

import com.amazonaws.services.s3.transfer.TransferManager
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class S3Download extends AbstractStep {
    @Input String bucketName
    @Input String keyName
    @Input String destination
    @Input boolean isDirectory = false

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        File localTarget = new File(context.attributes.workspace, destination)
        return isDirectory ? downloadDirectory(log, localTarget) : downloadFile(log, localTarget)
    }

    private boolean downloadDirectory(Closure<Void> log, File localTarget) {
        localTarget.mkdirs()
        new TidewaterS3TransferManager(log).transfer {TransferManager t ->
            t.downloadDirectory(bucketName, keyName, localTarget)
        }
        return true
    }

    private boolean downloadFile(Closure<Void> log, File localTarget) {
        new TidewaterS3TransferManager(log).transfer {TransferManager t ->
            t.download(bucketName, keyName, localTarget)
        }
        return true
    }
}
