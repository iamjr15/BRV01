package com.riteshakya.student.feature.report.student_parent

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.riteshakya.businesslogic.repository.report.ReportHandler
import com.riteshakya.student.R
import com.vincent.videocompressor.Util
import com.vincent.videocompressor.VideoCompress
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_report.*
import java.io.File
import java.util.*


class Report : AppCompatActivity() {

    var report: ReportHandler = ReportHandler()
    var map: HashMap<String, Any> = hashMapOf()
    lateinit var alertDiag: AlertDialog
    private val PERMISSION_REQUEST_CODE = 200
    var storageRef = FirebaseStorage.getInstance().getReference()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.riteshakya.student.R.layout.activity_report)


        positionSelect.onItemChanged {
            map.set("report_category", it)
        }

        attachment.setOnClickListener {
            //            requestPickMedia()
//            attachment.
            requestPickMedia()
        }

        println(positionSelect)

        report_submit_btn.setOnClickListener {

            if (positionSelect.selectedItemPosition == 0) {
                println("Please select the report category")
            } else if (isNullOrEmpty(report_text.text.toString())) {
                println("Please enter the details of the report.")
            } else {
                map.set("report_details", report_text.text.toString())
                map.set(
                    "report_media",
                    "https://cdn.pixabay.com/photo/2016/03/09/09/43/person-1245959_960_720.jpg"
                )
                report.createReport(map).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
                    println("Subscribed to the report")
                }.doOnSuccess {
                    println("   sUCCESS $it")
                }.subscribe()
            }
        }
    }

//    private fun downloadAttachment() {
//        var url =
//            "https://firebasestorage.googleapis.com/v0/b/brv-01.appspot.com/o/report_complain_media%2Fuser_id.jpeg?alt=media&token=16e36234-5290-43b6-900d-479cb506b378"
//
//        attachment.isClickable = false
//
//        if (url.contains(".mp4")) {
//            alertDiag = AlertDialog.Builder(this)
//                .setView(R.layout.progress_view)
//                .setTitle("Downloading Video...")
//                .setCancelable(false)
//                .create()
//
//
//            alertDiag.show()
//
//            var islandRef = storageRef.child("report_complain_media").child("user_id.mp4")
//
//            val localFile =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    .absolutePath
//
//
//            val path = localFile + File.separator + "ReportMedia.mp4"
//
//
//            val file = File(path)
//
//            islandRef.getFile(file).addOnSuccessListener {
//
//                alertDiag.cancel()
//
//
//                println("downloaded successfully..:)")
//            }.addOnFailureListener {
//                Snackbar.make(
//                    root_layout,
//                    "failed to download.",
//                    Snackbar.LENGTH_LONG
//                ).show()
//
//                alertDiag.cancel()
//                println("failed to download... :(")
//
//            }
//
//
//        } else if (url.contains(".jpeg")) {
//
//            alertDiag = AlertDialog.Builder(this)
//                .setView(R.layout.progress_view)
//                .setTitle("Downloading Image...")
//                .setCancelable(false)
//                .create()
//            alertDiag.show()
//
//            var islandRef = storageRef.child("report_complain_media").child("user_id.jpeg")
//
//            val localFile =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    .absolutePath
//
//
//            val path = localFile + File.separator + "ReportMedia.jpeg"
//
//
//            val file = File(path)
//
//            islandRef.getFile(file).addOnSuccessListener {
//
//                println("downloaded successfully..:)")
//                alertDiag.cancel()
//            }.addOnFailureListener {
//
//                Snackbar.make(
//                    root_layout,
//                    "failed to download...",
//                    Snackbar.LENGTH_LONG
//                ).show()
//                println("failed to download... :(")
//                alertDiag.cancel()
//
//            }
//        } else {
//
//            Snackbar.make(
//                root_layout,
//                "unsupported.",
//                Snackbar.LENGTH_LONG
//            ).show()
//
//
//            println("unsupported format")
//            alertDiag.cancel()
//        }
//
//
//    }


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

//            Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show()

        } else {
            requestPermission()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        data ?: return
        if (resultCode == Activity.RESULT_OK) {
            val cR = this.getContentResolver()

            if (cR.getType(data.getData())!!.contains("image")) {
                alertDiag = AlertDialog.Builder(this)
                    .setView(R.layout.progress_view)
                    .setTitle("Uploading Image...")
                    .setCancelable(false)
                    .create()
                attachment.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_error, 0, 0, 0);
                alertDiag.show()
                val compressedImageFile =
                    Compressor(this).compressToFile(File(Util.getFilePath(this, data.data)))

                uploadReportMedia(compressedImageFile, ".jpeg")

            } else if (cR.getType(data.getData())!!.contains("video")) {
                alertDiag = AlertDialog.Builder(this)
                    .setView(R.layout.progress_view)
                    .setTitle("Uploading Video...")
                    .setCancelable(false)
                    .create()
                alertDiag.show()
                attachment.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_error, 0, 0, 0);

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

                            // uploading
                            uploadReportMedia(
                                File(outputDir + File.separator + "compressMedia.mp4"),
                                ".mp4"
                            )
                        }

                        override fun onFail() {

                            Snackbar.make(
                                root_layout,
                                "compression failed for video",
                                Snackbar.LENGTH_LONG
                            )
                            Crashlytics.log(Log.ERROR, "ERROR", "compression failed for video");
                        }

                        override fun onProgress(percent: Float) {
                        }
                    })

            } else {

                alertDiag.cancel()
                Snackbar.make(root_layout, "Unsupported Format", Snackbar.LENGTH_LONG)
                Crashlytics.log(Log.ERROR, "ERROR", "Unsupported Format");

            }
        }
    }


    private fun uploadReportMedia(data: File, s: String) {


        val reportRef = storageRef.child("report_complain_media/user_id").child("user_id${s}")

        reportRef.putFile(Uri.fromFile(File(data.path))).addOnFailureListener {

            alertDiag.cancel()
            Snackbar.make(
                root_layout,
                "Failed in uploading report... ",
                Snackbar.LENGTH_LONG
            ).show()
            Crashlytics.log(
                Log.ERROR,
                "ERROR",
                "Failed in uploading report... :( \n ${it.message}"
            );
        }.addOnSuccessListener {


            val fdelete = File(data.path)
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    alertDiag.cancel()
                } else {
                    alertDiag.cancel()
                    Snackbar.make(
                        root_layout,
                        "temperory file not deleted.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    Crashlytics.log(Log.ERROR, "ERROR", "temperory file not deleted");
                }
            }
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
                        root_layout,
                        "Permission Granted.",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {

                    Snackbar.make(
                        root_layout,
                        "Permission Denied.",
                        Snackbar.LENGTH_LONG
                    ).show()


                }
            }
        }
    }
}
