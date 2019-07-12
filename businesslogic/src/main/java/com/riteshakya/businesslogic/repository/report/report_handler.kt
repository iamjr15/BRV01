package com.riteshakya.businesslogic.repository.report


import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riteshakya.businesslogic.repository.report.model.ManagementReportModel
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import io.reactivex.Single
import io.reactivex.Single.create
import java.util.*

@Suppress("DEPRECATION", "NAME_SHADOWING")
class ReportHandler {

    fun createReport(data: HashMap<String, Any>): Single<String> {
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        return create { emitter ->
            db.collection("users").document(currentUser).get().addOnSuccessListener { document ->
                if (document != null) {
                    data["user_id"] = currentUser
                    data["school_id"] = document.data?.get("school") as String
                    data["name"] =
                        document.data?.get("first_name").toString() + " " + document.data?.get("last_name").toString()
                    data["report_status"] = "unresolved"
                    data["class_name"] = document.data?.get("class_name") as String
                    data["section"] = document.data?.get("section") as String
                    data["profile_picture"] = document.data?.get("profile_picture") as String
                    data["date"] = Date()
                    db.collection("reports")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            emitter.onSuccess(documentReference.id)
                        }
                        .addOnFailureListener {
                            emitter.onError(it)
                        }
                } else {
                    println("Something went wrong")
                }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }


    fun getStudentReports(): Single<ArrayList<ReportModel>> {
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ReportModel> = ArrayList()

        return create { emitter ->
            db.collection("reports")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("user_id", currentUser)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        reports.add(document.toObject(ReportModel::class.java).also {
                            it.id = document.id
                        })
                    }
                    emitter.onSuccess(reports)
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    fun getManagementUnresolvedReport(
        startAfter: Timestamp,
        limit: Int
    ): Single<ArrayList<ManagementReportModel>> {

        var teacher: Query

        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagementReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        if (startAfter == Timestamp(Date(1, 1, 1))) {

                            teacher = db.collection("reports")
                                .orderBy("date", Query.Direction.DESCENDING)
                                .whereEqualTo("school_id", document.data?.get("school"))
                                .whereEqualTo("class_name", document.data?.get("class_name"))
                                .whereEqualTo("report_status", "unresolved")
                                .whereEqualTo("section", document.data?.get("section"))
                                .limit(limit.toLong())
                        } else {
                            teacher = db.collection("reports")
                                .orderBy("date")
                                .whereEqualTo("school_id", document.data?.get("school"))
                                .whereEqualTo("class_name", document.data?.get("class_name"))
                                .whereEqualTo("report_status", "unresolved")
                                .whereEqualTo("section", document.data?.get("section"))
                                .startAfter(startAfter)
                                .limit(limit.toLong())
                        }

                        var school = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("report_status", "unresolved")
                            .orderBy("date")

                        var query =
                            if (document.data?.get("role").toString() == "School") school else teacher

                        query.get()
                            .addOnSuccessListener { documents ->
                                for (doc in documents) {
                                    reports.add(doc.toObject(ManagementReportModel::class.java).also {
                                        it.id = doc.id
                                    })
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener { e ->
                                emitter.onError(e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    fun getManagementResolvedReport(
        startAfter: Timestamp,
        limit: Int
    ): Single<ArrayList<ManagementReportModel>> {
        var teacher: Query
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagementReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (startAfter == Timestamp(Date(1, 1, 1))) {
                            teacher = db.collection("reports")
                                .orderBy("date", Query.Direction.DESCENDING)
                                .whereEqualTo("school_id", document.data?.get("school"))
                                .whereEqualTo("class_name", document.data?.get("class_name"))
                                .whereEqualTo("report_status", "resolved")
                                .whereEqualTo("section", document.data?.get("section"))
                                .limit(limit.toLong())
                        } else {
                            teacher = db.collection("reports")
                                .orderBy("date")
                                .whereEqualTo("school_id", document.data?.get("school"))
                                .whereEqualTo("class_name", document.data?.get("class_name"))
                                .whereEqualTo("report_status", "resolved")
                                .whereEqualTo("section", document.data?.get("section"))
                                .startAfter(startAfter)
                                .limit(limit.toLong())
                        }
                        var school = db.collection("reports")
                            .whereEqualTo("school_id", document.data?.get("school"))
                            .whereEqualTo("report_status", "resolved")
                            .orderBy("date")
                        var query =
                            if (document.data?.get("role").toString().equals("School")) school else teacher
                        query.get()
                            .addOnSuccessListener { documents ->
                                for (doc in documents) {
                                    reports.add(doc.toObject(ManagementReportModel::class.java).also {
                                        it.id = doc.id
                                    })
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener { e ->
                                emitter.onError(e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }
    }

    fun updateReport(reportId: String, data: Map<String, Any>): Single<String> {
        val updateReportReference =
            FirebaseFirestore.getInstance().collection("reports").document(reportId)

        return create { emitter ->
            updateReportReference.update(data).addOnSuccessListener {
                emitter.onSuccess("updated")
            }.addOnFailureListener {
                emitter.onError(it)

            }
        }
    }


    fun studentDetails(userId: String): Single<DocumentSnapshot> {
        val updateReportReference =
            FirebaseFirestore.getInstance().collection("users").document(userId)

        return create { emitter ->
            updateReportReference.get().addOnSuccessListener {
                emitter.onSuccess(it)
            }.addOnFailureListener {
                emitter.onError(it)

            }
        }
    }

    fun reportLeft(max: Int): Single<Int> {
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val currentDate = Date(Date().year, Date().month, 1)
        val db = FirebaseFirestore.getInstance()
        val previousReportReference =
            FirebaseFirestore.getInstance().collection("reports")
                .whereGreaterThan("date", currentDate)

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        previousReportReference.get()
                            .addOnSuccessListener { documents ->
                                emitter.onSuccess(max - documents.size())
                            }
                            .addOnFailureListener { e ->
                                emitter.onError(e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }

    }

}