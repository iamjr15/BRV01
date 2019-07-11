package com.riteshakya.businesslogic.repository.report

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.riteshakya.businesslogic.repository.report.model.ManagmentReportModel
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import com.riteshakya.core.exception.ErrorFetching
import io.reactivex.Single
import io.reactivex.Single.create
import java.util.*

class ReportHandler {

    fun createReport(data: HashMap<String, Any>): Single<String> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()

        return create { emitter ->
            db.collection("users").document(currentUser).get().addOnSuccessListener { document ->
                if (document != null) {
                    data.set("user_id", currentUser)
                    data.set("school_id", document.data?.get("school") as String)
                    data.set(
                        "name",
                        document.data?.get("first_name").toString() + document.data?.get("last_name").toString()
                    )
                    data.set("report_status", "unresolved")
                    data.set("class_name", document.data?.get("class_name") as String)
                    data.set("section", document.data?.get("section") as String)
                    data.set("profile_picture", document.data?.get("profile_picture") as String)
                    data.set("date", Date())
                    db.collection("reports")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            emitter.onSuccess(documentReference.id)
                        }
                        .addOnFailureListener { e ->
                            Log.w("tag", "Error adding document", e)
                        }
                } else {
                    println("Something went wrong")
                }
            }.addOnFailureListener { exception ->
                emitter.onError(ErrorFetching("Adding"))
            }
        }
    }


    fun getStudentReports(): Single<ArrayList<ReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ReportModel> = ArrayList()

        return create { emitter ->
            db.collection("reports")
                .whereEqualTo("user_id", currentUser)
                .orderBy("date").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        reports.add(document.toObject(ReportModel::class.java).also {
                            it.id = document.id
                        })
                    }
                    emitter.onSuccess(reports)
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }


    fun getManagementResolvedReport(): Single<ArrayList<ManagmentReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagmentReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var teacher = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("class_name", document.data?.get("class_name"))
                            .whereEqualTo("report_status", "resolved")
                            .whereEqualTo("section", document.data?.get("section"))
                            .orderBy("date")

                        var school = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("report_status", "resolved")
                            .orderBy("date")

                        var query =
                            if (document.data?.get("role").toString().equals("School")) school else teacher
                        query.get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    reports.add(document.toObject(ManagmentReportModel::class.java).also {
                                        it.id = document.id
                                    })
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener { e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }

    fun getManagementUnresolvedReport(): Single<ArrayList<ManagmentReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagmentReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var teacher = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("class_name", document.data?.get("class_name"))
                            .whereEqualTo("report_status", "unresolved")
                            .whereEqualTo("section", document.data?.get("section"))
                            .orderBy("date")

                        var school = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("report_status", "unresolved")
                            .orderBy("date")

                        var query =
                            if (document.data?.get("role").toString().equals("School")) school else teacher

                        query.get()
                            .addOnSuccessListener { documents ->
                                for (doc in documents) {
                                    reports.add(doc.toObject(ManagmentReportModel::class.java).also {
                                        it.id = doc.id
                                    })
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener { e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }


    fun getSchoolReports(): Single<ArrayList<ReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .orderBy("date").get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    reports.add(document.toObject(ReportModel::class.java))
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener { e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }

    fun updateReport(reportId: String, data: Map<String, Any>): Single<String> {
        var updateReportReference =
            FirebaseFirestore.getInstance().collection("reports").document(reportId)

        return create { emitter ->
            updateReportReference.update(data).addOnSuccessListener {
                emitter.onSuccess("updated")
            }.addOnFailureListener {
                emitter.onError(it)
                println("Error while updating")
            }
        }

    }

    fun reportLeft(max: Int): Single<Int> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        val currentDate = Date(Date().year, Date().month, 1)
        var db =
            FirebaseFirestore.getInstance()
        var previourReportReference =
            FirebaseFirestore.getInstance().collection("reports")
                .whereGreaterThan("date", currentDate)

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        previourReportReference.get()
                            .addOnSuccessListener { documents ->
                                emitter.onSuccess(max - documents.size())
                            }
                            .addOnFailureListener { e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }

    }

}