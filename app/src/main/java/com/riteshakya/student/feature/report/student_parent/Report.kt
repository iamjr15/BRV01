package com.riteshakya.student.feature.report.student_parent

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import com.google.common.base.Strings.isNullOrEmpty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.businesslogic.utils.upload
import com.riteshakya.student.R
import com.vincent.videocompressor.Util
import com.vincent.videocompressor.VideoCompress
import id.zelory.compressor.Compressor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_report.*
import java.io.File
import java.util.*

@Suppress("INACCESSIBLE_TYPE")
class Report : AppCompatActivity() {

    var report: ReportHandler = ReportHandler()
    var map: HashMap<String, Any> = hashMapOf()
    lateinit var alertDiag: AlertDialog
    private val PERMISSION_REQUEST_CODE = 200
    var storageRef = FirebaseStorage.getInstance().getReference()
    var file: File = File("")
    var type: String = ""

    var currentUser: String = FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.riteshakya.student.R.layout.activity_report)


        positionSelect.onItemChanged {
            map.set("report_category", it)
        }

        attachment.setOnClickListener {

            requestPickMedia()
        }

        println(positionSelect)

        report_submit_btn.setOnClickListener {

            if (positionSelect.selectedItemPosition == 0) {


                Snackbar.make(
                    report_layout,
                    "Please select the report category.",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else if (isNullOrEmpty(report_text.text.toString())) {

                Snackbar.make(
                    report_layout,
                    "Please enter the details of the report.",
                    Snackbar.LENGTH_SHORT
                ).show()


            } else {
                map.set("report_details", report_text.text.toString())


                if (!file.path.equals("")) {

                    uploadMedia(type).doOnSuccess {
                        map.set("report_media", it)
                        createReport(map)
                    }.subscribe()
                } else {

                    map.set("report_media", " ")
                    createReport(map)

                }


            }
        }
    }


    fun createReport(data: HashMap<String, Any>) {


        alertDiag = AlertDialog.Builder(this)
            .setView(R.layout.progress_view)
            .setTitle("Filing Report...")
            .setCancelable(false)
            .create()

        alertDiag.show()
        report.createReport(data).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {

        }.doOnSuccess {


            alertDiag.cancel()


            Snackbar.make(report_layout, "Reports Uploaded", Snackbar.LENGTH_SHORT)
            var intent = Intent(this, ReportComplaintHomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }.subscribe()

    }


    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )

    }

    fun requestPickMedia() {

        if (checkPermission()) {

            val mediaChooser = Intent(Intent.ACTION_GET_CONTENT)
            mediaChooser.type = "*/*"
            startActivityForResult(mediaChooser, 3)


        } else {
            requestPermission()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        data ?: return
        if (resultCode == Activity.RESULT_OK) {
            val cR = this.getContentResolver()

            if (cR.getType(data.getData())!!.contains("image")) {

                file =
                    Compressor(this).compressToFile(File(Util.getFilePath(this, data.data)))

                type = ".jpeg"
                attachment.setText("Added Image")
                attachment.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_image,
                    0,
                    R.drawable.ic_error,
                    0
                )


            } else if (cR.getType(data.getData())!!.contains("video")) {
                alertDiag = AlertDialog.Builder(this)
                    .setView(R.layout.progress_view)
                    .setTitle("Compressing Video...")
                    .setCancelable(false)
                    .create()
                alertDiag.show()


                val outputDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath()

                VideoCompress.compressVideoLow(
                    Util.getFilePath(this, data.data),
                    outputDir + File.separator + "compressMedia.mp4",
                    object : VideoCompress.CompressListener {
                        override fun onStart() {
                        }

                        override fun onSuccess() {

                            attachment.setText("Added Video")
                            attachment.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_video,
                                0,
                                R.drawable.ic_error,
                                0
                            )

                            alertDiag.cancel()

                            file = File(outputDir + File.separator + "compressMedia.mp4")
                            type = ".mp4"

                        }

                        override fun onFail() {

                            Snackbar.make(
                                report_layout,
                                "compression failed for video",
                                Snackbar.LENGTH_LONG
                            ).show()
                            Crashlytics.log(Log.ERROR, "ERROR", "compression failed for video");
                        }

                        override fun onProgress(percent: Float) {
                        }
                    })

            } else {

                alertDiag.cancel()
                Snackbar.make(report_layout, "Unsupported Format", Snackbar.LENGTH_LONG).show()
                Crashlytics.log(Log.ERROR, "ERROR", "Unsupported Format");

            }
        }
    }


    fun uploadMedia(s: String): Single<String> {


        alertDiag = AlertDialog.Builder(this)
            .setView(R.layout.progress_view)
            .setTitle("Uploading Media...")
            .setCancelable(false)
            .create()

        alertDiag.show()

        val reportRef =
            storageRef.child("report_complain_media/${currentUser}").child("${Date().time}${s}")
        return Single.create { emitter ->
            reportRef.upload(file.path).doOnSuccess {
                alertDiag.cancel()
                emitter.onSuccess(
                    it
                )
            }.doOnError {

                Crashlytics.log("UPLOAD ERROR: ${it.message}")
                alertDiag.cancel()
            }

                .subscribe()


        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {

                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED


                if (writeStorage) {
                    Snackbar.make(
                        report_layout,
                        "Permission Granted.",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {

                    Snackbar.make(
                        report_layout,
                        "Permission Denied.",
                        Snackbar.LENGTH_LONG
                    ).show()


                }
            }
        }
    }
}