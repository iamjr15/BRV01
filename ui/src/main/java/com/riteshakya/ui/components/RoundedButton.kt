package com.riteshakya.ui.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.drawable.GradientDrawable
import kotlinx.android.synthetic.main.custom_rounded_button.view.*
import com.riteshakya.ui.R


class RoundedButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var text: String = ""
        set(value) {
            field = value
            textTxt.text = value
        }

    var backgroundColor: String = "#fff"
        set(value) {
            field = value
            (roundedButton.getBackground() as GradientDrawable).setColor(Color.parseColor(backgroundColor))
        }

    init {
        init()
        initTypedArray(attrs)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.custom_rounded_button, this)
        val observer = roundedButton.viewTreeObserver
        observer.addOnGlobalLayoutListener { (roundedButton.getBackground() as GradientDrawable).setCornerRadius((roundedButton.height/2).toFloat())}


    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs,R.styleable.RoundedButton, 0, 0)
        text = ta.getString(R.styleable.RoundedButton_android_text) ?: ""
        backgroundColor = ta.getString(R.styleable.RoundedButton_backgroundColor) ?: "#fff"
        ta.recycle()
    }
}
