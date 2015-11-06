package greenmoonsoftware.tidewater.plugins.aws
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.Transfer
import com.amazonaws.services.s3.transfer.TransferManager

import java.text.DecimalFormat

final class TidewaterS3TransferManager {
    private final Closure log

    TidewaterS3TransferManager(Closure log) {
        this.log = log
    }

    void transfer(Closure<Transfer> withTransferUtility) {
        withTransferManager { TransferManager transferManager ->
            def transfer = withTransferUtility(transferManager)
            monitor(transfer)
            transfer.waitForCompletion()
            log 'Transfer successful'
        }
    }

    private withTransferManager(Closure c) {
        TransferManager transferManager = null
        try {
            transferManager = new TransferManager(new AmazonS3Client(new ProfileCredentialsProvider()))
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
                log "${progress.percentTransferred as int}% (${byteSizeToHuman(progress.bytesTransferred)} / ${byteSizeToHuman(progress.totalBytesToTransfer)} bytes)"
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
}
