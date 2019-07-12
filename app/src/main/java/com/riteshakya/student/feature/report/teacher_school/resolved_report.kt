package com.riteshakya.student.feature.report.teacher_school

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.google.firebase.Timestamp
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.repository.report.model.ManagementReportModel
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.di.ManagementViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_resolved_report.*
import java.util.*
import kotlin.collections.ArrayList

class SolvedReports : Fragment() {
    var report: ReportHandler = ReportHandler()
    val limit = 10
    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var startAfter = 0

    var data: ArrayList<ManagementReportModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_resolved_report, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mLayoutManager = LinearLayoutManager(context)
        solved_recyclerview.layoutManager = mLayoutManager
        buildResolvedReports(solved_recyclerview, Timestamp(Date(1, 1, 1)))

        solved_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            buildResolvedReports(solved_recyclerview, data[startAfter - 1].date)
                        }
                    }
                }
            }
        })
    }

    private fun buildResolvedReports(
        recyclerView: RecyclerView,
        id: Timestamp
    ) {
        report.getManagementResolvedReport(id, limit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.doOnSuccess {
                startAfter += it.size
                data.addAll(it)
                if (it.size < limit) {
                    loading = false
                }
                val adapter = ManagementViewAdapter(activity!!.baseContext, data, "teacher")
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
            .doOnError {
                Crashlytics.log("Unresolved Reports $it")
            }
            .subscribe()

    }
}

