package com.riteshakya.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.drawable.GradientDrawable
import com.riteshakya.ui.R
import kotlinx.android.synthetic.main.custom_rounded_button.view.*


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

    var src: Int = 0
        set(value) {
            field = value
            (roundedButton.getBackground() as GradientDrawable).setColor(src)
        }


    init {
        init()
        initTypedArray(attrs)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.custom_rounded_button, this)
    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs,R.styleable.RoundedButton, 0, 0)
        text = ta.getString(R.styleable.RoundedButton_android_text) ?: ""
        ta.recycle()
    }
}
