package com.riteshakya.student.feature.report.teacher_school

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.di.ManagementViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_resolved_report.*

class SolvedReports : Fragment() {

    var report: ReportHandler = ReportHandler()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_resolved_report, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        solved_recyclerview.layoutManager = LinearLayoutManager(context)
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


