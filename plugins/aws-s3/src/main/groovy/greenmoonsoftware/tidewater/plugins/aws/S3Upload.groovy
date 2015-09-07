package greenmoonsoftware.tidewater.plugins.aws
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.transfer.TransferManager
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

class S3Upload extends AbstractStep {
    @Input String bucketName
    @Input String keyName = ''
    @Input String upload
    @Input int sleepInSeconds = 5

    @Output String etag


    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider())
        File file = new File(upload)
        if (file.isDirectory()) {
            uploadDirectory(log, s3client, file)
        }
        else {
            uploadSingleFile(log, s3client, file)
        }
        return true
    }

    private void uploadDirectory(Closure log, AmazonS3 s3client, File directory) {
        log "Uploading directory ${directory.absolutePath}"
        def directoryTransferUtility = new TransferManager(s3client)
        def upload = directoryTransferUtility.uploadDirectory(bucketName, '', directory, true)
        while (!upload.done) {
            def progress = upload.progress
            log "${progress.percentTransferred}% (${progress.bytesTransferred} / ${progress.totalBytesToTransfer} bytes)"
            sleep sleepInSeconds * 1000
        }
        log 'Upload successful'
    }

    private void uploadSingleFile(Closure log, AmazonS3Client s3client, File file) {
        log "Uploading file: ${file.absolutePath}"
        def result = s3client.putObject(new PutObjectRequest(bucketName, keyName, file))
        etag = result.ETag
        log 'Upload successful'
    }

}
