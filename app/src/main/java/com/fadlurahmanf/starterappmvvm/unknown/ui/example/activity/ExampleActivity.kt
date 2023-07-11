package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import com.chuckerteam.chucker.api.Chucker
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.AnalyticEvent
import com.fadlurahmanf.starterappmvvm.core.data.constant.BuildFlavorConstant
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.data.dto.event.RxEvent
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.presentation.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.core.presentation.PdfViewerActivity
import com.fadlurahmanf.starterappmvvm.core.presentation.VideoPlayerActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import com.fadlurahmanf.starterappmvvm.feature.download.domain.service.DownloadService
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.constant.ImageOrigin
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.ImageModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.domain.interactor.FetchGalleryInteractor
import com.fadlurahmanf.starterappmvvm.feature.gallery.presentation.ExampleGalleryActivity
import com.fadlurahmanf.starterappmvvm.feature.language.data.storage.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.feature.language.presentation.ExampleLanguageActivity
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.LogHistoryActivity
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationHelper
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.PdfModel
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.PdfOrigin
import com.fadlurahmanf.starterappmvvm.unknown.utils.analytic.AnalyticHelper
import com.fadlurahmanf.starterappmvvm.unknown.utils.call.CallBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.unknown.utils.media.MediaPlayerService
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import javax.inject.Inject

@ExperimentalGetImage
class ExampleActivity :
    BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    lateinit var component: ExampleComponent

    @Inject
    lateinit var fetchGalleryInteractor: FetchGalleryInteractor

    lateinit var notificationHelper: NotificationHelper
    lateinit var notificationImpl: NotificationImpl

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

    override fun onLanguageChange(event: RxEvent.ChangeLanguageEvent) {
        super.onLanguageChange(event)
        binding.tvChangeLanguage.text = getString(R.string.change_language)
    }

    override fun initSetup() {
        isVisibleBtnPdfFromFile = remoteConfig().getBoolean("btn_pdf_from_file")
        binding.buttonPickPdf.visibility =
            if (isVisibleBtnPdfFromFile) View.VISIBLE else View.INVISIBLE

        binding.btnShowLoading.setOnClickListener {
            showLoadingDialog()
        }

        binding.btnLogger.setOnClickListener {
            val intent = Intent(this, LogHistoryActivity::class.java)
            startActivity(intent)
        }

        notificationHelper = NotificationHelper(this)
        notificationImpl = NotificationImpl(this)
        binding.btnChangeLanguage.setOnClickListener {
            val intent = Intent(this, ExampleLanguageActivity::class.java)
            startActivity(intent)
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
//            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

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
            val intent = Intent(this, ExampleGalleryActivity::class.java)
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
