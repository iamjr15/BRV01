package com.riteshakya.student.feature.report.teacher_school

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.di.ManagementViewAdapter
import com.riteshakya.student.feature.report.di.ViewAdapter
import com.riteshakya.student.feature.report.student_parent.ReportComplaintHomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.complaint_items.view.*
import kotlinx.android.synthetic.main.fragment_resolved_report.*
import java.util.ArrayList

class SolvedReports : Fragment() {

    var report: ReportHandler = ReportHandler()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_resolved_report, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildResolvedReports(solved_recyclerview)
    }


    private fun buildResolvedReports(recyclerView:RecyclerView){

        report.getTeacherResolvedReport().observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.doOnSuccess {
                val adapter = ManagementViewAdapter(activity!!.baseContext, it,"teacher")
                solved_recyclerview.adapter = adapter
            }
            .subscribe()
    }
}


