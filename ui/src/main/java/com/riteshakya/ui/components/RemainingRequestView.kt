package com.riteshakya.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.riteshakya.ui.R
import kotlinx.android.synthetic.main.custom_remaining_request_view.view.*

class RemainingRequestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var text: String = ""
        set(value) {
            field = value
            count.text = value
        }

    init {
        init()
        initTypedArray(attrs)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.custom_remaining_request_view, this)
        (roundedButton.getBackground() as GradientDrawable).setColor(Color.WHITE)
    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs,R.styleable.RemainingRequestView, 0, 0)
        text = ta.getString(R.styleable.RemainingRequestView_android_text) ?: ""
        ta.recycle()
    }
}
