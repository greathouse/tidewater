package greenmoonsoftware.tidewater.plugins.aws.s3

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectListing
import greenmoonsoftware.tidewater.Context
import greenmoonsoftware.tidewater.step.AbstractStep
import greenmoonsoftware.tidewater.step.Input
import greenmoonsoftware.tidewater.step.Output

class S3CopyBucket extends AbstractStep {
    @Input String sourceBucketName
    @Input String destinationBucketName
    @Input String destinationKeyName

    @Output int numberOfObjects

    @Override
    boolean execute(Context context, File stepDirectory) {
        def log = context.&log.curry(this)
        def s3Client = new AmazonS3Client(new ProfileCredentialsProvider())

        ObjectListing objectListing = null
        while(objectListing == null || objectListing.isTruncated()) {
            objectListing = s3Client.listObjects(sourceBucketName)
            objectListing.objectSummaries.each {
                def toFullKey = "${destinationKeyName}/${it.key}"
                def msg = " Copying ${it.key} → ${toFullKey}"
                log toFullKey, ' ' + msg
                def result = s3Client.copyObject(it.bucketName, it.key, destinationBucketName, toFullKey)
                log toFullKey, "✔${msg} : ETag ${result.ETag}"
            }
        }
        return true
    }
}
