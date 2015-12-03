package greenmoonsoftware.tidewater.plugins.aws.s3
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.Transfer
import com.amazonaws.services.s3.transfer.TransferManager

import java.text.DecimalFormat

final class TidewaterS3TransferManager {
    private final Closure log
    private final Closure<Boolean> withS3ClientPreCheck
    private AmazonS3Client s3Client = new AmazonS3Client(new ProfileCredentialsProvider())

    TidewaterS3TransferManager(Closure log) {
        this(log, {return true})
    }

    TidewaterS3TransferManager(Closure log, Closure<Boolean> withS3ClientPreCheck) {
        this.log = log
        this.withS3ClientPreCheck = withS3ClientPreCheck
    }

    Transfer.TransferState transfer(Closure<Transfer> withTransferUtility) {
        withTransferManager { TransferManager transferManager ->
            def transfer = withTransferUtility(transferManager)
            monitor(transfer)
            transfer.waitForCompletion()
            return transfer.state
        }
    }

    private withTransferManager(Closure<Transfer.TransferState> c) {
        TransferManager transferManager = null
        try {
            transferManager = new TransferManager(s3Client)
            return c(transferManager)
        }
        finally {
            transferManager?.shutdownNow()
        }
    }

    private monitor(Transfer transfer) {
        log transfer.description
        new Thread({
            while (!transfer.done) {
                def progress = transfer.progress
                log 'transferProgress', buildProgressBar(progress.bytesTransferred, progress.totalBytesToTransfer)
                sleep 2000
            }
        }).start()
    }

    private String byteSizeToHuman(long size) {
        if(size <= 0) return '0'
        final String[] units = [ 'B', 'kB', 'MB', 'GB', 'TB' ] as String[]
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024))
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups]
    }

    private String buildProgressBar(long currentBytes, long totalBytes) {
        def percent = currentBytes / totalBytes * 100 as double
        def percentageScale = percent / 2
        def bar = ''.padRight(percentageScale, '█')
        return "${bar} ${percent.round(1)}%  ${byteSizeToHuman(currentBytes)} / ${byteSizeToHuman(totalBytes)}"
    }
}
