package com.riteshakya.ui.components

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.riteshakya.ui.R
import kotlinx.android.synthetic.main.custom_incident_details.view.*

class IncidentDetails @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var text: String = ""
        set(value) {
            field = value
            tv_incidentDetails.text = value
        }

    init {
        init()
        initTypedArray(attrs)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.custom_incident_details, this)
        tv_incidentDetails.setMovementMethod(ScrollingMovementMethod())
    }

    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs,R.styleable.IncidentDetails, 0, 0)
        text = ta.getString(R.styleable.IncidentDetails_android_text) ?: ""
        ta.recycle()
    }
}
