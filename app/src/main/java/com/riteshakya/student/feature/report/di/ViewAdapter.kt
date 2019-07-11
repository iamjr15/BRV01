package com.riteshakya.student.feature.report.di

import android.content.Context
import android.os.Build
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
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import com.riteshakya.student.R
import kotlinx.android.synthetic.main.report_card.view.*
import java.util.*
import kotlin.collections.HashMap

class ViewAdapter(val context: Context, val reportList: ArrayList<ReportModel>, val role:String) :
    RecyclerView.Adapter<ViewAdapter.ViewHolder>() {


    var report: ReportHandler = ReportHandler()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_card, parent, false)
        return ViewHolder(v)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(context, reportList[position])

        holder.itemView.resolve_type.setOnClickListener{
            if (reportList[position].report_status == "unresolved") {
                holder.itemView.resolve_type.text="resolved"
                holder.itemView.resolve_type.backgroundColor="#ad10e825"

                //Updating the report
                var updateData: HashMap<String, Any> = hashMapOf()
                updateData["report_status"] = "resolved"
                println(reportList[position])
                report.updateReport( reportList[position].id,updateData).doOnSubscribe {

                }.doOnSuccess {
                    println("Data updated")
                }.subscribe()
            }
        }

    }


    override fun getItemCount(): Int {
        return reportList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(context: Context, report: ReportModel) {

            val reportBtn = itemView.findViewById(R.id.resolve_type) as com.riteshakya.ui.components.RoundedButton
            val textViewName = itemView.findViewById(R.id.user_name) as TextView
            val textViewCategory = itemView.findViewById(R.id.report_category) as TextView
            val textViewDate = itemView.findViewById(R.id.report_date) as TextView
            val imageView = itemView.findViewById(R.id.user_image) as ImageView
            val classSection = itemView.findViewById(R.id.class_section) as TextView


            //Setting report status button
            reportBtn.text= if(report.report_status == "unresolved") "resolve this" else "resolved"
            reportBtn.backgroundColor= if(report.report_status == "unresolved") "#ff853dd7" else "#ad10e825"
            Glide.with(context).load( report.profile_picture ).into(imageView)
            textViewDate.text = dateFormat(report.date)
            classSection.text = report.class_name+" - "+report.section
            textViewName.text = report.name
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