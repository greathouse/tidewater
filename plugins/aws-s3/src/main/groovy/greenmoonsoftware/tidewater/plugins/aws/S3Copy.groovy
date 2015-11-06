package greenmoonsoftware.tidewater.plugins.aws

import com.amazonaws.services.s3.transfer.TransferManager
import greenmoonsoftware.tidewater.context.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

class S3Copy extends AbstractStep {
    @Input String sourceBucketName
    @Input String sourceKeyName
    @Input String destinationBucketName
    @Input String destinationKeyName

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        new TidewaterS3TransferManager(log).transfer { TransferManager t ->
            t.copy(sourceBucketName, sourceKeyName, destinationBucketName, destinationKeyName)
        }
        return true
    }
}
