package com.riteshakya.student.feature.report.student_parent

import android.content.Context
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
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.complaint_items.view.*
import java.util.*

class ReportComplaintHomeActivity : AppCompatActivity() {


    var report: ReportHandler = ReportHandler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_complaint_home)


        val myDataset: ArrayList<ReportModel> = ArrayList()


        //getting recyclerview from xml
        val recyclerView = findViewById(R.id.my_recycler_view) as RecyclerView


        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this)






        report.fetchReport().observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {

            }.doOnSuccess {

                for (documents in it) {


                    var reportModel: ReportModel =
                        ReportModel("", "", "", "", "", "", "", "", "", "")

                    reportModel.name = documents.data["name"].toString()
                    reportModel.report_category = documents.data["report_category"].toString()
                    reportModel.date = documents.data["date"].toString()
                    reportModel.report_status = documents.data["report_status"].toString()
                    reportModel.report_id = documents.id




                    myDataset.add(reportModel)

                }


                val adapter = CustomAdapter(this, myDataset)
                recyclerView.adapter = adapter

            }
            .subscribe()

    }


    class CustomAdapter(val context: Context, val reportList: ArrayList<ReportModel>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


        var report: ReportHandler = ReportHandler()


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.complaint_items, parent, false)
            return ViewHolder(v)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(context, reportList[position])

            holder.itemView.resolve_type.setOnClickListener(View.OnClickListener {

                //updating report status
                if (reportList[position].report_status.equals("unresolved")) {

                    holder.itemView.resolve_type.text="resolved"
                    report.updateReport(reportList[position].report_id, "Resolved")

                }


            })

        }


        override fun getItemCount(): Int {
            return reportList.size
        }

        //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            fun bindItems(context: Context, report: ReportModel) {

                val reportBtn = itemView.findViewById(R.id.resolve_type) as com.riteshakya.ui.components.RoundedButton
                val textViewName = itemView.findViewById(R.id.user_name) as TextView
                val textViewCategory = itemView.findViewById(R.id.report_category) as TextView
                val textViewDate = itemView.findViewById(R.id.report_date) as TextView
                val imageView = itemView.findViewById(R.id.user_image) as ImageView

                reportBtn.text=report.report_status

                Glide.with(context).load(
                    "data:image/jpeg;base64,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"
                ).into(imageView)
                textViewDate.text = report.date
                textViewName.text = report.name
                textViewCategory.text = report.report_category


            }


        }
    }


}



