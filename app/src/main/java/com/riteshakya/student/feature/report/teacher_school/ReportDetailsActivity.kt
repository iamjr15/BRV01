package com.riteshakya.student.feature.report.teacher_school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.student_parent.ReportComplaintHomeActivity
import kotlinx.android.synthetic.main.activity_report_details.*

class ReportDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_details)



        contact.setOnClickListener { View.OnClickListener {


            val intent = Intent(this, ReportComplaintHomeActivity::class.java)
            // start your next activity
            startActivity(intent)

        } }
    }
}
