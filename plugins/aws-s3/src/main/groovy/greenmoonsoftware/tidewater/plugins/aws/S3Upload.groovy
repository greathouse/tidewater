package greenmoonsoftware.tidewater.plugins.aws
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.Upload
import greenmoonsoftware.tidewater.config.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input

import java.text.DecimalFormat

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
        return upload(log) { directoryTransferUtility ->
            directoryTransferUtility.uploadDirectory(bucketName, keyName ?: '', directory, true)
        }
    }

    private boolean uploadSingleFile(Closure log, File file) {
        log "Uploading file: ${file.absolutePath}"
        return upload(log) { directoryTransferUtility ->
            directoryTransferUtility.upload(bucketName, keyName ?: '', file)
        }
    }

    private boolean upload(Closure log, Closure<Upload> withTransferUtility) {
        def s3client = new AmazonS3Client(new ProfileCredentialsProvider())
        def directoryTransferUtility = new TransferManager(s3client)
        def upload = withTransferUtility(directoryTransferUtility)
        monitorUpload(upload, log)
        upload.waitForCompletion()
        directoryTransferUtility.shutdownNow()
        log 'Upload successful'
        return true
    }

    private monitorUpload(upload, Closure log) {
        new Thread({
            while (!upload.done) {
                def progress = upload.progress
                log "${progress.percentTransferred as int}% (${readableFileSize(progress.bytesTransferred)} / ${readableFileSize(progress.totalBytesToTransfer)} bytes)"
                sleep 2000
            }
        }).start()
    }

    private String readableFileSize(long size) {
        if(size <= 0) return '0'
        final String[] units = [ 'B', 'kB', 'MB', 'GB', 'TB' ] as String[]
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024))
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups]
    }
}
