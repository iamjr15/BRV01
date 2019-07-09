package com.riteshakya.businesslogic.repository.report.model


class ReportModel (


     var user_id: String ,
     var school_id: String,
     var user_class: String ,
     var user_section: String ,
     var report_text: String ,
     var report_status: String ,
     var report_media: String ,
     var report_category: String ,
     var name: String ="",
     var profile_picture: String ="",
     var date: String ="",
     var report_id: String = ""
    )




fun ReportModel.transform() = ReportModel(
    user_id = user_id,
    report_category = report_category,
    report_media = report_media,
    report_status = report_status,
    report_text = report_text,
    user_class = user_class,
    user_section = user_section,
    school_id = school_id,
    name = name,
    profile_picture = profile_picture,
    date = date
)