package com.riteshakya.student.feature.report.teacher_school

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private val PERMISSION_REQUEST_CODE = 200
    private var reportMedia: String = ""

    var reportMediaPath: String = ""


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
        reportMedia = intent.extras.getString("report_media")
        val date = intent.extras!!.getString("date")
        val reportCategory = intent.extras!!.getString("report_category")
        val userId = intent.extras!!.getString("user_id")
        tv_report_category.text = reportCategory
        btn_report_media.isVisible = reportMedia != ""

        if (isFileExist()) {
            btn_report_media.text = "Open Media"
        }

        report.studentDetails(userId)
            .doOnSubscribe { }
            .doOnSuccess {
                doc = it
                val phoneNo: HashMap<String, String> =
                    it.data?.get("phone_no") as (HashMap<String, String>)

                //Setting up the values
                Glide.with(this).load(getData("profile_picture")).into(im_profileImage)
                tv_name.text = getData("first_name") + "\n" + getData("last_name")
                tv_date.text = date
                tv_class_name.text = getData("class_name")
                tv_section.text = getData("section")
                tv_phone_number.text = phoneNo["dial_code"] + " " + phoneNo["phone_no"]
                tv_report_category.text = reportCategory
                tv_report_details.text = reportDetails
                btn_report_media.setOnClickListener {
                    if (checkPermission()) {
                        println("Is file exist ------------------------------------------- ${isFileExist()}")
                        if (isFileExist()) {
                            openFile()
                        } else {
                            downloadMedia()
                        }
                    }
                }

                //Setting contact button to open dialer
                openDialer(phoneNo)

                alertDialog.cancel()
            }
            .subscribe()
    }


    private fun isFileExist(): Boolean {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .absolutePath
        val fileExtension = if (reportMedia.contains(".mp4")) ".mp4" else ".jpeg"
        val fileNameRegex = reportMedia.split("token=".toRegex(), 2).toTypedArray()
        val filename = fileNameRegex[1]
        reportMediaPath = directory + File.separator + filename + fileExtension
        return File(reportMediaPath).exists()
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_VIEW);
        val uri =
            Uri.parse(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
        intent.setDataAndType(uri, "*/*")
        startActivity(Intent.createChooser(intent, "Open"))
    }

    private fun openDialer(phoneNo: HashMap<String, String>) {
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

    private fun downloadMedia() {
        var httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(reportMedia)

        alertDialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_view)
            .setTitle("Downloading Image...")
            .setCancelable(false)
            .create()

        alertDialog.show()

        val file = File(reportMediaPath)

        httpsReference.getFile(file).addOnSuccessListener {

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(reportMediaPath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            this.sendBroadcast(mediaScanIntent)

            alertDialog.cancel()

            btn_report_media.text = "Open Media"

        }.addOnFailureListener {
            alertDialog.cancel()
            Snackbar.make(
                root_layout,
                "Failed to download.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }


    //------------------------------------------------------------
    //------------------PERMISSION -------------------------------
    //------------------------------------------------------------

    private fun checkPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (result1 != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeStorage) {
                    if (isFileExist()) openFile() else downloadMedia()
                } else {
                    Snackbar.make(
                        root_layout,
                        "Please grant permission to access file.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

