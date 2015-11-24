package greenmoonsoftware.tidewater.plugins.aws.s3

import com.amazonaws.services.s3.transfer.Transfer
import com.amazonaws.services.s3.transfer.TransferManager
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

class S3Download extends AbstractStep {
    @Input String bucketName
    @Input String keyName
    @Input String destination
    @Input boolean isDirectory = false

    @Output Transfer.TransferState transferState

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        File localTarget = new File(context.attributes.workspace, destination)
        transferState = isDirectory ? downloadDirectory(log, localTarget) : downloadFile(log, localTarget)
        return transferState == Transfer.TransferState.Completed
    }

    private boolean downloadDirectory(Closure<Void> log, File localTarget) {
        localTarget.mkdirs()
        return new TidewaterS3TransferManager(log).transfer {TransferManager t ->
            t.downloadDirectory(bucketName, keyName, localTarget)
        }
    }

    private boolean downloadFile(Closure<Void> log, File localTarget) {
        return new TidewaterS3TransferManager(log).transfer {TransferManager t ->
            t.download(bucketName, keyName, localTarget)
        }
    }
}
