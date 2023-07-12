package com.fadlurahmanf.starterappmvvm.core.unknown.external.lib

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView


class ZoomableImageView(context: Context, attr: AttributeSet?) : androidx.appcompat.widget.AppCompatImageView(context, attr),
    View.OnTouchListener {

    var initialMatrix: Matrix = Matrix()
    var lastMatrix: Matrix = Matrix()
    var savedMatrix: Matrix = Matrix()

    init {
        setOnTouchListener(this)
    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val view: ImageView = v as ImageView
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                v.matrix?.let {
                    lastMatrix.set(it)
                }
                savedMatrix.set(lastMatrix)
            }
            MotionEvent.ACTION_POINTER_UP ->{
                println("masuk pointer up ${event.x} dan ${event.y}")

            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                println("masuk x ${event.x}")
                println("masuk y ${event.y}")
            }
        }
        view.imageMatrix = matrix
        return true
    }

}