package com.riteshakya.student.feature.report.teacher_school

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riteshakya.student.R
import kotlinx.android.synthetic.main.activity_complaint_report_home.*

class ComplaintReportHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_report_home)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter


        tabs_main.setupWithViewPager(viewpager_main)
    }
}
