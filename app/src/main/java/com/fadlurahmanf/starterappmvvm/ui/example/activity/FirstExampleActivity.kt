package com.fadlurahmanf.starterappmvvm.ui.example.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getBroadcast
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.helper.RSAHelper
import com.fadlurahmanf.starterappmvvm.core.helper.TranslationHelper
import com.fadlurahmanf.starterappmvvm.data.storage.language.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.dto.model.core.ImageModel
import com.fadlurahmanf.starterappmvvm.dto.model.core.ImageOrigin
import com.fadlurahmanf.starterappmvvm.dto.model.core.PdfModel
import com.fadlurahmanf.starterappmvvm.dto.model.core.PdfOrigin
import com.fadlurahmanf.starterappmvvm.databinding.ActivityFirstExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.core.activity.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.ui.core.activity.PdfViewerActivity
import com.fadlurahmanf.starterappmvvm.utils.notification.CallNotificationCallHelper
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationHelper
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationPlayerService
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random


class FirstExampleActivity : BaseActivity<ActivityFirstExampleBinding>(ActivityFirstExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var languageSpStorage: LanguageSpStorage

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun initSetup() {
        binding.btnChangeLanguage.setOnClickListener {
            val local = TranslationHelper.getCurrentLocale(this)
            if(local.language == "en"){
                TranslationHelper.changeLanguage(this, "in")
                languageSpStorage.languageId = "in"
            }else{
                TranslationHelper.changeLanguage(this, "en")
                languageSpStorage.languageId = "in"
            }
            recreate()
        }


        binding.button2.setOnClickListener {
            val intent = Intent(this, SecondExampleActivity::class.java)
            startActivity(intent)
        }

        binding.btnGenerateKey.setOnClickListener {
            RSAHelper.generateKey(RSAHelper.METHOD.PKCS1PEM)
            val public = RSAHelper.encodedPublicKey(RSAHelper.publicKey)
            val private = RSAHelper.encodedPrivateKey(RSAHelper.privateKey)

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", "${public}{{BATAS}}${private}")
            cm.setPrimaryClip(clipData)
        }

        var encryptedString = ""
        binding.btnEncrypt.setOnClickListener{
            encryptedString = RSAHelper.encrypt("tes tes") ?: ""
            println("masuk $encryptedString")
        }

        binding.btnDecrypt.setOnClickListener{
            var result = RSAHelper.decrypt(encryptedString)
            println("masuk decrypt $result")
        }

        binding.buttonPickPdf.setOnClickListener {
            val intent = Intent()
                .setType("application/pdf")
                .setAction(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
            @Suppress("DEPRECATION")
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        binding.buttonPdf.setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            startActivity(intent)
        }

        /**
         * reference: https://developer.android.com/training/data-storage/shared/photopicker
         * */
        binding.buttonPickImageFromGallery.setOnClickListener {
            // alternative:
            // pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val intent = Intent()
                .setType("image/*")
                // .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) /** add this line if you want to pick multiple */
                .setAction(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
            @Suppress("DEPRECATION")
            startActivityForResult(Intent.createChooser(intent, "Select Multiple File"), 121)
        }

        binding.buttonImageViewer.setOnClickListener {
            val intent = Intent(this, ImageViewerActivity::class.java)
            startActivity(intent)
        }

        binding.btnShowNotif.setOnClickListener {
            val builder = notificationHelper.builder
            builder.setContentTitle("Example Title Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Example Body Notification")

            with(NotificationManagerCompat.from(this)){
                notify(Random.nextInt(999), builder.build())
            }
        }

        binding.btnShowNotif1Action.setOnClickListener {
            val intent = Intent(this, NotificationBroadcastReceiver::class.java).apply {
                action = "SNOOZE"
            }
            val snoozePendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)
            val builder = notificationHelper.builder
            builder.setContentTitle("Example Notification 1 action")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Example Body Notification 1 action")
                .addAction(R.drawable.ic_launcher_background, "SNOOZE", snoozePendingIntent)

            with(NotificationManagerCompat.from(this)){
                notify(Random.nextInt(999), builder.build())
            }
        }

        binding.btnIncomingCallNotification.setOnClickListener {
            val intent = Intent(this, NotificationBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationCallHelper.EXTRA_NOTIFICATION_ID, 1)
                putString(CallNotificationCallHelper.EXTRA_CALLER_NAME, "Bank MAS")
            }
            intent.apply {
                action = NotificationBroadcastReceiver.ACTION_CALL_INCOMING
                putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
            }
            sendBroadcast(intent)
        }

        binding.btnScheduleIncomingNotification.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance()
            val intent = Intent(this, NotificationBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationCallHelper.EXTRA_NOTIFICATION_ID, 1)
                putString(CallNotificationCallHelper.EXTRA_CALLER_NAME, "Bank MAS")
            }
            intent.apply {
                action = NotificationBroadcastReceiver.ACTION_CALL_INCOMING
                putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
            }
            calendar.add(Calendar.SECOND, 10)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                )
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 111){
            val uri = data!!.data

            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra(PdfViewerActivity.PDF, PdfModel(origin = PdfOrigin.FILE, path = uri.toString()))
            startActivity(intent)
        }else if (resultCode == RESULT_OK && requestCode == 121){
            data?.data?.let {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putExtra(ImageViewerActivity.IMAGE, ImageModel(origin = ImageOrigin.URI, path = it.toString()))
                startActivity(intent)
            }
        }
    }


    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}
