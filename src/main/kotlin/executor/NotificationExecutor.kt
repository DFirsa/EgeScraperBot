package ege.bot.executor

import ege.bot.service.notification.NotificationService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NotificationExecutor(
    private val notificationService: NotificationService
) {

    @Scheduled(cron = "0 0/10 * * * *")
    fun doJob() {
        notificationService.notifyUsers()
    }
}