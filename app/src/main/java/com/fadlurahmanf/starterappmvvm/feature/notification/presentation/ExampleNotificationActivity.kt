package com.fadlurahmanf.starterappmvvm.feature.notification.presentation

import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.AnalyticEvent
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleNotificationBinding
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant
import com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model.ContentNotificationModel
import com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model.NotificationActionModel
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.receiver.NotificationReceiver
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.utils.analytic.AnalyticHelper

class ExampleNotificationActivity :
    BaseActivity<ActivityExampleNotificationBinding>(ActivityExampleNotificationBinding::inflate) {
    private lateinit var notificationImpl: NotificationImpl

    override fun initSetup() {
        notificationImpl = NotificationImpl(this)
        binding.btnShowNotif.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_show_notif_click)
            notificationImpl.showNotification(title = "TES TITLE NOTIF", body = "TES BODY NOTIF")
        }

        binding.btnShowNotifPicture.setOnClickListener {
            notificationImpl.showImageNotification(
                title = "Example Image Notification",
                body = "Example Image Body Notification",
                imageUrl = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"
            )
        }

        binding.btnShowNotif1Action.setOnClickListener {
            val snoozeIntent = NotificationReceiver.getActionClickPendingIntent(
                this, NotificationConstant.DEFAULT_NOTIFICATION_ID, data = ContentNotificationModel(
                    type = "SNOOZE"
                )
            )
            notificationImpl.showActionNotification(
                title = "ACTION NOTIFICATION",
                body = "BODY NOTIFICATION",
                actions = listOf(
                    NotificationActionModel(
                        icon = R.drawable.ic_launcher_background, "SNOOZE", snoozeIntent
                    )
                )
            )
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}