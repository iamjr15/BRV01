package com.riteshakya.student.feature.report.teacher_school


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.di.ManagementViewAdapter
import com.riteshakya.student.feature.report.di.ViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_unresolved_report.*


class UnsolvedReports : Fragment() {


    var report: ReportHandler = ReportHandler()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_unresolved_report, container, false)

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildResolvedReports(unsolved_recyclerview)
    }

    private fun buildResolvedReports(recyclerView:RecyclerView){

        report.getTeacherUnresolvedReport().observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.doOnSuccess {
                val adapter = ManagementViewAdapter(activity!!.baseContext, it,"teacher")
                unsolved_recyclerview.adapter = adapter
            }
            .subscribe()
    }

}