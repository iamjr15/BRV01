package com.riteshakya.student.feature.report.di

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.repository.report.model.ManagmentReportModel
import com.riteshakya.student.R
import com.riteshakya.student.feature.report.teacher_school.ReportDetailsActivity
import kotlinx.android.synthetic.main.report_card.view.*
import java.io.Serializable
import java.util.*


class ManagementViewAdapter(
    val context: Context,
    val reportList: ArrayList<ManagmentReportModel>,
    val role: String
) :
    RecyclerView.Adapter<ManagementViewAdapter.ViewHolder>() {


    var report: ReportHandler = ReportHandler()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_card, parent, false)
        return ViewHolder(v)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(context, reportList[position])

        holder.itemView.resolve_type.setOnClickListener {

            val intent = Intent(context, ReportDetailsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("report", reportList[position])
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return reportList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(context: Context, report: ManagmentReportModel) {

            val reportBtn =
                itemView.findViewById(R.id.resolve_type) as com.riteshakya.ui.components.RoundedButton
            val textViewName = itemView.findViewById(R.id.user_name) as TextView
            val textViewCategory = itemView.findViewById(R.id.report_category) as TextView
            val textViewDate = itemView.findViewById(R.id.report_date) as TextView
            val classSection = itemView.findViewById(R.id.class_section) as TextView
            val imageView =
                itemView.findViewById(R.id.user_image) as ImageView

            reportBtn.text = "details"

            Glide.with(context).load(report.profile_picture).into(imageView)
            textViewDate.text = dateFormat(report.date)
            textViewName.text = report.name
            reportBtn.backgroundColor = "#ff3787e2"
            classSection.text = report.class_name + " - " + report.section
            textViewCategory.text = report.report_category
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun dateFormat(date: Timestamp): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = date.seconds * 1000
            val date = DateFormat.format("d MMM. yyyy", cal).toString()
            return date
        }
    }
}