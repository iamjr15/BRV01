package com.riteshakya.student.feature.report.student_parent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.di.ViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_report_complaint_home.*
import kotlinx.android.synthetic.main.report_card.view.*
import java.util.*

class ReportComplaintHomeActivity : AppCompatActivity() {

    var report: ReportHandler = ReportHandler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_complaint_home)



        remainingRequest.text = ""

        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setRemainingRequest()
        buildMyReports(recyclerView)
    }


    private fun setRemainingRequest(){
        report.reportLeft(3).observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.doOnSuccess {
                remainingRequest.text = it.toString()
                createNewReport(it != 0)
            }
            .subscribe()
    }


    private fun createNewReport(canCreate: Boolean){

        newReport.setOnClickListener{
            if(canCreate){
                val intent = Intent(this, Report::class.java)
                startActivity(intent)
            }
            else{
                println("You can reports exhausted")
            }
        }
    }

    private fun buildMyReports(recyclerView:RecyclerView){
        report.getStudentReports().observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.doOnSuccess {
                val adapter = ViewAdapter(this, it,"student")
                recyclerView.adapter = adapter
            }
            .subscribe()
    }
}



