package com.bicifiapp.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.devhack.notifications.notification
import com.bicifiapp.R
import com.bicifiapp.extensions.userId
import com.bicifiapp.notificationssettings.notification.NotificationRepository
import com.bicifiapp.ui.activity.homescreen.HomeScreenActivity
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class RemainderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    companion object {
        const val SUNDAY_DAY = 6
        const val CONVERT_MY_DAYS = 2
    }

    override suspend fun doWork(): Result =
        coroutineScope {
            val notificationRepository: NotificationRepository by inject()

            notificationRepository.getByUser(userId())
                .either({
                    Result.failure()
                }, { notificationSetting ->
                    val days: List<Int> = notificationSetting.days
                    var dayOfWeekToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                    dayOfWeekToday =
                        if (dayOfWeekToday == 1) SUNDAY_DAY else dayOfWeekToday - CONVERT_MY_DAYS
                    if (containDay(days, dayOfWeekToday)) {
                        notification(applicationContext) {
                            title = applicationContext
                                .getString(R.string.lbl_title_remainder_evaluate_status)
                            description = applicationContext
                                .getString(R.string.lbl_title_remainder_evaluate_status)
                            bigTextStyle = true
                            smallIconResource = R.mipmap.ic_launcher
                            openActivity = HomeScreenActivity::class.java
                        }.show()
                    }
                    Result.success()
                })
            Result.success()
        }

    private fun containDay(days: List<Int>, dayToFind: Int): Boolean {
        for (day in days) {
            if (day == dayToFind) {
                return true
            }
        }
        return false
    }
}
