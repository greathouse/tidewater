package greenmoonsoftware.tidewater.plugins.aws.s3

class S3BucketDoesNotExistException extends RuntimeException {
    S3BucketDoesNotExistException() {
    }

    S3BucketDoesNotExistException(String var1) {
        super(var1)
    }

    S3BucketDoesNotExistException(String var1, Throwable var2) {
        super(var1, var2)
    }

    S3BucketDoesNotExistException(Throwable var1) {
        super(var1)
    }

    S3BucketDoesNotExistException(String var1, Throwable var2, boolean var3, boolean var4) {
        super(var1, var2, var3, var4)
    }
}
