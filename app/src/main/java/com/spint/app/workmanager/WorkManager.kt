package com.spint.app.workmanager


//class ExpiredMessagesCleanupWorker(context: Context, workerParams: WorkerParameters) :
//    Worker(context, workerParams) {
//
//
//    override fun doWork(): Result {
//        try {
//
//            val expirationTimeMillis = 24 * 60 * 60 * 1000 // 24 hours
//            chatRepository.deleteExpiredMessages(expirationTimeMillis.toLong())
//            return Result.success()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error cleaning up expired messages", e)
//            return Result.failure()
//        }
//    }
//
//    companion object {
//        private const val TAG = "ExpiredMessagesCleanupWorker"
//    }
//}
//fun scheduleExpiredMessagesCleanup(context: Context) {
//    val constraints = Constraints.Builder()
//        .setRequiredNetworkType(NetworkType.UNMETERED)
//        .setRequiresBatteryNotLow(true)
//        .setRequiresCharging(true)
//        .build()
//
//    val cleanupRequest = PeriodicWorkRequestBuilder<ExpiredMessagesCleanupWorker>(
//        repeatInterval = 24, // repeat every 24 hours
//        repeatIntervalTimeUnit = TimeUnit.HOURS
//    )
//        .setConstraints(constraints)
//        .build()
//
//    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//        "expired_messages_cleanup",
//        ExistingPeriodicWorkPolicy.KEEP,
//        cleanupRequest
//    )
//}
