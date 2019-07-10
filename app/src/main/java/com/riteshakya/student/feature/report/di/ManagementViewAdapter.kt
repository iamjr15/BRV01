package com.riteshakya.student.feature.report.di

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.repository.report.model.ManagmentReportModel
import com.riteshakya.student.R
import kotlinx.android.synthetic.main.complaint_items.view.*
import java.util.ArrayList

class ManagementViewAdapter(val context: Context, val reportList: ArrayList<ManagmentReportModel>, val role:String) :
    RecyclerView.Adapter<ManagementViewAdapter.ViewHolder>() {


    var report: ReportHandler = ReportHandler()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.complaint_items, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(context, reportList[position])

        holder.itemView.resolve_type.setOnClickListener{
            if (reportList[position].report_status.equals("unresolved")) {
                holder.itemView.resolve_type.text="resolved"
            }
        }

    }


    override fun getItemCount(): Int {
        return reportList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(context: Context, report: ManagmentReportModel) {

            val reportBtn = itemView.findViewById(R.id.resolve_type) as com.riteshakya.ui.components.RoundedButton
            val textViewName = itemView.findViewById(R.id.user_name) as TextView
            val textViewCategory = itemView.findViewById(R.id.report_category) as TextView
            val textViewDate = itemView.findViewById(R.id.report_date) as TextView
            val imageView = itemView.findViewById(R.id.user_image) as ImageView

            reportBtn.text=report.report_status

            Glide.with(context).load( report.profile_picture ).into(imageView)
            textViewDate.text = report.date.toString()
            textViewName.text = report.name
            textViewCategory.text = report.report_category

        }
    }
}