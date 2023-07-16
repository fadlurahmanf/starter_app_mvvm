package com.fadlurahmanf.starterappmvvm.feature.qris.external.customview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 * Reference: [
 * https://medium.com/@rey5137/let-s-drill-a-hole-in-your-view-e7f53fa23376
 * https://stackoverflow.com/questions/18387814/drawing-on-canvas-porterduff-mode-clear-draws-black-why,
 * https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.budiharso.info%2F2016%2F01%2F09%2FCreate-hole-in-android-view%2F&psig=AOvVaw1H6CAwEK6lTQtNTQQf4gRg&ust=1674365656463000&source=images&cd=vfe&ved=0CBEQjhxqFwoTCJjzlKP41_wCFQAAAAAdAAAAABAE
 * ]
 * */
class QrisOverlayView(context: Context, attrs: AttributeSet): View(context, attrs){

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val viewportMargin = 32
        val viewportCornerRadius = 8

        val eraser = Paint()
        eraser.isAntiAlias = true
        eraser.style = Paint.Style.FILL
        eraser.color = Color.WHITE
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        val width = width.toFloat()
        val height = (width * 0.7).toFloat()
        val frame = RectF((width * 0.1).toFloat(), 250f, (width * 0.9).toFloat(), height + 250)
        val path = Path()
        val stroke = Paint()
        stroke.isAntiAlias = true
        stroke.strokeWidth = 4f
        stroke.color = Color.WHITE
        stroke.style = Paint.Style.STROKE
        path.addRoundRect(
            frame,
            viewportCornerRadius.toFloat(),
            viewportCornerRadius.toFloat(),
            Path.Direction.CW
        )
        canvas?.drawPath(path, stroke)
        canvas?.drawPath(path, eraser)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}