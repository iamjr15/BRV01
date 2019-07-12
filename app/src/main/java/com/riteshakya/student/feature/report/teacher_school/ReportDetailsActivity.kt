package com.riteshakya.student.feature.report.teacher_school

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.student.R
import kotlinx.android.synthetic.main.activity_report_details.*
import java.io.File
import java.util.*


class ReportDetailsActivity : AppCompatActivity() {

    var report: ReportHandler = ReportHandler()
    lateinit var alertDialog: AlertDialog
    lateinit var doc: DocumentSnapshot

    var phoneNo: HashMap<String, String> = hashMapOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_details)


        alertDialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_view)
            .setTitle("Fetching Details...")
            .setCancelable(false)
            .create()

        alertDialog.show()

        val reportDetails = intent.extras!!.getString("report_details")
        val reportMedia = intent.extras!!.getString("report_media")
        val date = intent.extras!!.getString("date")
        val reportCategory = intent.extras!!.getString("report_category")
        val userId = intent.extras!!.getString("user_id")

        report.studentDetails(userId)
            .doOnSubscribe { }
            .doOnSuccess {
                doc = it
                phoneNo = it.data?.get("phone_no") as (HashMap<String, String>)

                btn_report_media.isVisible = reportMedia != ""
                //Setting up the values
                Glide.with(this).load(getData("profile_picture")).into(im_profileImage)
                tv_name.text = getData("first_name") + "\n" + getData("last_name")
                tv_date.text = date
                tv_class_name.text = getData("class_name")
                tv_section.text = getData("section")
                tv_phone_number.text = phoneNo["dial_code"] + " " + phoneNo["phone_no"]
                tv_report_category.text = reportCategory
                tv_report_details.text = reportDetails
                downloadMedia()
                contact()
                alertDialog.cancel()
            }
            .subscribe()
    }


    private fun downloadMedia() {
        btn_report_media.setOnClickListener {

        }
    }

    private fun contact() {
        contact.setOnClickListener {
            val u = Uri.parse("tel:" + phoneNo["dial_code"] + " " + phoneNo["phone_no"])
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(this, "Error while opening phone dialer", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getData(field: String): String {
        return doc.data?.get(field).toString()
    }


    private fun downloadAttachment(url: String) {

        var httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        var type: String = ""

        when {
            url.contains(".mp4") -> {
                type = "mp4"
                alertDialog = AlertDialog.Builder(this)
                    .setView(R.layout.progress_view)
                    .setTitle("Downloading Video...")
                    .setCancelable(false)
                    .create()
                alertDialog.show()
            }
            url.contains(".jpeg") -> {
                type = "jpeg"
                alertDialog = AlertDialog.Builder(this)
                    .setView(R.layout.progress_view)
                    .setTitle("Downloading Image...")
                    .setCancelable(false)
                    .create()
                alertDialog.show()
            }
            else -> {
                Snackbar.make(
                    root_layout,
                    "unsupported.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        val localFile =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .absolutePath

        val path = localFile + File.separator + "${Date().time}.$type"
        val file = File(path)
        httpsReference.getFile(file).addOnSuccessListener {
            alertDialog.cancel()
        }.addOnFailureListener {
            alertDialog.cancel()
            Snackbar.make(
                root_layout,
                "Failed to download.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}

