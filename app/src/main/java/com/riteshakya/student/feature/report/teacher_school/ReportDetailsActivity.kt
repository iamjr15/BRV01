package com.riteshakya.student.feature.report.teacher_school

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riteshakya.businesslogic.repository.report.model.ManagmentReportModel
import com.riteshakya.student.R

class ReportDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_details)

        val bundle: Bundle? = intent.extras

        bundle?.let {
            bundle.apply {
                //Serializable Data
                val report = getSerializable("report") as ManagmentReportModel?
                println("Report getting is $report")
                if (report != null) {
                }

            }
        }

    }
}
