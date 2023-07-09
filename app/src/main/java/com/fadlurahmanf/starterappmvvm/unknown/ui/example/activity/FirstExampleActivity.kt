package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chuckerteam.chucker.api.Chucker
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.data.constant.AnalyticEvent
import com.fadlurahmanf.starterappmvvm.core.data.constant.BuildFlavorConstant
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.unknown.utils.language.TranslationHelper
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.databinding.ActivityFirstExampleBinding
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.ImageModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.ImageOrigin
import com.fadlurahmanf.starterappmvvm.feature.gallery.domain.usecases.GalleryImpl
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.PdfModel
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.PdfOrigin
import com.fadlurahmanf.starterappmvvm.unknown.ui.core.activity.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.core.activity.PdfViewerActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.core.activity.VideoPlayerActivity
import com.fadlurahmanf.starterappmvvm.unknown.utils.analytic.AnalyticHelper
import com.fadlurahmanf.starterappmvvm.feature.download.domain.services.DownloadService
import com.fadlurahmanf.starterappmvvm.feature.download.domain.usecases.DownloadNotificationImpl
import com.fadlurahmanf.starterappmvvm.unknown.utils.media.MediaPlayerService
import com.fadlurahmanf.starterappmvvm.unknown.utils.call.CallBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.unknown.utils.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@ExperimentalGetImage
class FirstExampleActivity :
    BaseActivity<ActivityFirstExampleBinding>(ActivityFirstExampleBinding::inflate) {
    lateinit var component: ExampleComponent

    @Inject
    lateinit var languageSpStorage: LanguageSpStorage

    @Inject
    lateinit var galleryImpl: GalleryImpl

    lateinit var notificationHelper: NotificationHelper

    private var isVisibleBtnPdfFromFile: Boolean = true

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putExtra(
                    ImageViewerActivity.IMAGE,
                    ImageModel(origin = ImageOrigin.URI, path = it.toString())
                )
                startActivity(intent)
            }
        }

    private val pickMultipleImageResult =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            // todo get multiple
            uris?.forEach {
                println("URI: ${it.path}")
            }
        }

    override fun initSetup() {
        isVisibleBtnPdfFromFile = remoteConfig().getBoolean("btn_pdf_from_file")
        binding.buttonPickPdf.visibility =
            if (isVisibleBtnPdfFromFile) View.VISIBLE else View.INVISIBLE

        binding.btnShowLoading.setOnClickListener {
            showLoadingDialog()
        }

        binding.btnLogger.setOnClickListener {
            logConsole.i("TES TES", true)
        }

        notificationHelper = NotificationHelper(this)
        binding.btnChangeLanguage.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_change_language)
            val local = TranslationHelper.getCurrentLocale(this)
            if (local.language == "en") {
                TranslationHelper.changeLanguage(this, "in")
                languageSpStorage.languageId = "in"
            } else {
                TranslationHelper.changeLanguage(this, "en")
                languageSpStorage.languageId = "in"
            }
            recreate()
        }


        binding.button2.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_second_example_click)
            val intent = Intent(this, SecondExampleActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToLogin.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_login_click)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSeeLogChucker.setOnClickListener {
            if (BuildConfig.FLAVOR != BuildFlavorConstant.production) {
                val intent = Chucker.getLaunchIntent(this)
                startActivity(intent)
            } else {
                showSnackBar(binding.root, "only in dev or staging")
            }
        }

        binding.btnEncrypt.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_encrypt_click)
            val intent = Intent(this, ExampleEncryptDecryptActivity::class.java)
            startActivity(intent)
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
            /**
             * alternative
             * */
//             pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            /**
             * alternative
             * */
//            val intent = galleryImpl.getIntentNativePickImage()
//            startActivityForResult(intent, 121)

            pickMultipleImageResult.launch("image/*")
        }

        binding.buttonPickMultipleImageFromGallery.setOnClickListener {
            /**
             * alternative
             * */
//             pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            /**
             * alternative
             * */
//            val intent = galleryImpl.getIntentNativePickImage()
//            startActivityForResult(intent, 121)

            pickMultipleImageResult.launch("image/*")
        }

        binding.buttonImageViewer.setOnClickListener {
            val intent = Intent(this, ImageViewerActivity::class.java)
            startActivity(intent)
        }

        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        binding.btnShowNotif.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_show_notif_click)
            val intent = Intent(this, LoginActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingIntentFlag)
            val builder = notificationHelper.builder
            builder.setContentTitle("Example Title Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentText("Example Body Notification")
                .setContentIntent(pendingIntent)

            notificationHelper.notificationManager
                .notify(Random.nextInt(999), builder.build())
        }

        binding.btnShowNotifPicture.setOnClickListener {
            val imageUrl = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"

            val builder = notificationHelper.builder
            builder.setContentTitle("Example Image Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Example Image Notification")

            Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        builder.setLargeIcon(resource)
                        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationHelper.notificationManager
                            .notify(Random.nextInt(999), builder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                })
        }

        binding.btnShowNotif1Action.setOnClickListener {
            val intent = Intent(this, CallBroadcastReceiver::class.java).apply {
                action = "SNOOZE"
            }
            val snoozePendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)
            val builder = notificationHelper.builder
            builder.setContentTitle("Example Notification 1 action")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("Example Body Notification 1 action")
                .addAction(R.drawable.ic_launcher_background, "SNOOZE", snoozePendingIntent)

            with(NotificationManagerCompat.from(this)) {
//                notify(Random.nextInt(999), builder.build())
            }
        }

        binding.btnIncomingCallNotification.setOnClickListener {
            CallBroadcastReceiver.sendBroadcastIncomingCall(this)
        }

        binding.btnScheduleIncomingNotification.setOnClickListener {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance()
            val p0Intent = CallBroadcastReceiver.getIntentIncomingCall(this)
            calendar.add(Calendar.SECOND, 10)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    PendingIntent.getBroadcast(
                        this,
                        1,
                        p0Intent,
                        FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }
        }

        binding.btnDownload.setOnClickListener {
            DownloadService.startService(
                this,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                fileName = "BigBuckBunny.mp4"
            )
        }

        binding.btnPlayAudioForeground.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_play_audio_foreground)
            MediaPlayerService.playAudio(
                this,
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
            )
        }

        binding.btnQris.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_qris_click)
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

        binding.btnGetToken.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_get_fcm_token)
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                logConsole.d("token FCM: $it")
            }
        }

        binding.btnPlayRemoteVideo.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_play_remote_video)
            val intent = Intent(this, VideoPlayerActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToNsd.setOnClickListener {
            val intent = Intent(this, NetworkServiceDiscoveryActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToGallery.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_gallery_click)
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }

    private var cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback {
            val intent = Intent(this, QrisActivity::class.java)
            qrisActivityLauncher.launch(intent)
        }
    )

    private var qrisActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            if (it.resultCode == RESULT_OK) {
                showSnackBar(binding.root, it.data?.getStringExtra("RESULT") ?: "NULL")
            }
        }
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 111) {
            val uri = data!!.data
            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra(
                PdfViewerActivity.PDF,
                PdfModel(origin = PdfOrigin.FILE, path = uri.toString())
            )
            startActivity(intent)
        } else if (resultCode == RESULT_OK && requestCode == 121) {
            data?.data?.let {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putExtra(
                    ImageViewerActivity.IMAGE,
                    ImageModel(origin = ImageOrigin.URI, path = it.toString())
                )
                startActivity(intent)
            }
        }
    }


    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}
