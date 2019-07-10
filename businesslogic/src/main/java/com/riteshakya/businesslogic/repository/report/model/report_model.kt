package com.riteshakya.businesslogic.repository.report.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.time.LocalDateTime


data class ReportModel (
     var id: String = "",
     var user_id: String ="",
     var school_id: String="",
     var user_class: String ="",
     var section: String ="",
     var report_text: String ="",
     var report_status: String ="",
     var report_media: String ="",
     var report_category: String ="",
     var name: String ="",
     var profile_picture: String ="",
     var date: Timestamp = Timestamp.now()
)

data class ManagmentReportModel (
     var id: String = "",
     var user_id: String ="",
     var school_id: String="",
     var user_class: String ="",
     var section: String ="",
     var report_text: String ="",
     var report_status: String ="",
     var report_media: String ="",
     var report_category: String ="",
     var name: String ="",
     var profile_picture: String ="",
     var class_name: String ="",
     var report_details: String ="",
     var date: Timestamp = Timestamp.now()
)
