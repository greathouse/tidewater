package greenmoonsoftware.tidewater.plugins.aws.s3
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.event.ProgressListener
import com.amazonaws.services.s3.transfer.Transfer
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferProgress
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output
import greenmoonsoftware.tidewater.step.StepResult

class S3CopyObject extends AbstractStep {
    @Input String sourceBucketName
    @Input String sourceKeyName
    @Input String destinationBucketName
    @Input String destinationKeyName

    @Output Transfer.TransferState transferState

    @Override
    StepResult execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        transferState = copy(log)

        return StepResult.from(transferState == Transfer.TransferState.Completed)
    }

    private Transfer.TransferState copy(Closure<Void> log) {
        def manager = new TidewaterS3TransferManager(log)

        return manager.transfer { TransferManager t ->
            t.amazonS3Client.doesBucketExist(destinationBucketName) ?
                t.copy(sourceBucketName, sourceKeyName, destinationBucketName, destinationKeyName)
                : new NoBucketTransfer()
        }
    }

    private class NoBucketTransfer implements Transfer {

        @Override
        boolean isDone() {
            true
        }

        @Override
        void waitForCompletion() throws AmazonClientException, AmazonServiceException, InterruptedException {
        }

        @Override
        AmazonClientException waitForException() throws InterruptedException {
            return null
        }

        @Override
        String getDescription() {
            "Destination bucket '${destinationBucketName}' does not exist."
        }

        @Override
        Transfer.TransferState getState() {
            Transfer.TransferState.Failed
        }

        @Override
        void addProgressListener(ProgressListener listener) {
            throw new UnsupportedOperationException('Not implemented')
        }

        @Override
        void removeProgressListener(ProgressListener listener) {
            throw new UnsupportedOperationException('Not implemented')
        }

        @Override
        TransferProgress getProgress() {
            throw new UnsupportedOperationException('Not implemented')
        }

        @Override
        void addProgressListener(com.amazonaws.services.s3.model.ProgressListener listener) {
            throw new UnsupportedOperationException('Not implemented')
        }

        @Override
        void removeProgressListener(com.amazonaws.services.s3.model.ProgressListener listener) {
            throw new UnsupportedOperationException('Not implemented')
        }
    }
}
