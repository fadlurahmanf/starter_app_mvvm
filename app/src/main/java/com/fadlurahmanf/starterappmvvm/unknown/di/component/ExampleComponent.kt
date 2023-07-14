package com.fadlurahmanf.starterappmvvm.unknown.di.component

import com.fadlurahmanf.starterappmvvm.core.room.presentation.ExampleRoomActivity
import com.fadlurahmanf.starterappmvvm.feature.language.presentation.ExampleLanguageActivity
import com.fadlurahmanf.starterappmvvm.core.logger.presentation.LogHistoryActivity
import com.fadlurahmanf.starterappmvvm.feature.notification.presentation.ExampleNotificationActivity
import com.fadlurahmanf.starterappmvvm.core.sp.presentation.ExampleSPActivity
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleAfterLoginActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.ExampleActivity
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleLoginActivity
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleSwitchDomainActivity
import com.fadlurahmanf.starterappmvvm.feature.mlkit.presentation.ExampleFaceDetectorActivity
import com.fadlurahmanf.starterappmvvm.feature.mlkit.presentation.ExampleImageLabelingActivity
import com.fadlurahmanf.starterappmvvm.feature.qris.presentation.ExampleQrisActivity
import com.fadlurahmanf.starterappmvvm.feature.qris.presentation.QrisActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.SecondExampleActivity
import dagger.Subcomponent

@Subcomponent
interface ExampleComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ExampleComponent
    }

    fun inject(activity: ExampleActivity)
    fun inject(activity: ExampleNotificationActivity)
    fun inject(activity: ExampleLanguageActivity)
    fun inject(activity: ExampleSPActivity)
    fun inject(activity: ExampleRoomActivity)
    fun inject(activity: ExampleQrisActivity)
    fun inject(activity: QrisActivity)
    fun inject(activity: ExampleSwitchDomainActivity)
    fun inject(activity: ExampleImageLabelingActivity)
    fun inject(activity: ExampleFaceDetectorActivity)
    fun inject(activity: SecondExampleActivity)
    fun inject(activity: ExampleLoginActivity)
    fun inject(activity: LogHistoryActivity)
    fun inject(activity: ExampleAfterLoginActivity)
}