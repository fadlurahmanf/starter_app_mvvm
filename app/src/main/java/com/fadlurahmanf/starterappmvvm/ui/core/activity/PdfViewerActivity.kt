package com.fadlurahmanf.starterappmvvm.ui.core.activity

import android.content.Intent
import android.net.Uri
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.dto.model.PdfModel
import com.fadlurahmanf.starterappmvvm.dto.model.PdfOrigin
import com.fadlurahmanf.starterappmvvm.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener

class PdfViewerActivity : BaseActivity<ActivityPdfViewerBinding>(ActivityPdfViewerBinding::inflate),
    OnRenderListener, OnErrorListener {

    companion object{
        const val PDF = "PDF"
    }

    var pdf: PdfModel? = null

    override fun initSetup() {
        pdf = intent.getParcelableExtra(PDF)

        if (pdf == null){
            return
        }

        if(pdf!!.origin == PdfOrigin.NETWORK){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.soundczech.cz/temp/lorem-ipsum.pdf"))
            startActivity(intent)
        }else if(pdf!!.origin == PdfOrigin.FILE){
            binding.pdfView.fromUri(Uri.parse(pdf!!.path))
                .enableDoubletap(true)
                .onLoad {
                    println("masuk on load")
                }
                .onRender(this)
                .onError(this)
                .load()
        }
    }

    override fun inject() {

    }

    override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
        println("masuk on initial render")
    }

    override fun onError(t: Throwable?) {
        println("masuk error ${t.toString()}")
    }

}