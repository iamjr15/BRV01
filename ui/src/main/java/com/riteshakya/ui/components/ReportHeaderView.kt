package com.riteshakya.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_report_header_view.view.*


class ReportHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var text: String = ""
        set(value) {
            field = value
            textTxt.text = value
        }

    private var src: Int = 0
        set(value) {
            field = value
            profileImage.setImageResource(value)
        }

    val image: ImageView by lazy { profileImage }

    init {
        init()
        initTypedArray(attrs)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(com.riteshakya.ui.R.layout.custom_report_header_view, this)
    }


    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs,
            com.riteshakya.ui.R.styleable.ReportHeaderView, 0, 0)
        text = ta.getString(com.riteshakya.ui.R.styleable.ReportHeaderView_android_text) ?: ""
        src = ta.getResourceId(com.riteshakya.ui.R.styleable.ReportHeaderView_android_src, 0)
        ta.recycle()
    }
}
