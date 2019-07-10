package com.riteshakya.businesslogic.repository.report

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.riteshakya.businesslogic.repository.report.model.ManagmentReportModel
import com.riteshakya.businesslogic.repository.report.model.ReportModel
import com.riteshakya.core.exception.ErrorFetching
import io.reactivex.Single
import io.reactivex.Single.create
import java.util.*

class ReportHandler {

    fun createReport(data: HashMap<String, Any>):Single<String> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()

        return create { emitter ->
            db.collection("users").document(currentUser).get().addOnSuccessListener { document ->
                if (document != null) {
                    data.set("user_id",currentUser)
                    data.set("school_id", document.data?.get("school") as String)
                    data.set("name", document.data?.get("first_name").toString()+document.data?.get("last_name").toString())
                    data.set("report_status", "unresolved")
                    data.set("class_name", document.data?.get("class_name") as String )
                    data.set("section", document.data?.get("section") as String )
                    data.set("profile_picture", document.data?.get("profile_picture") as String )
                    data.set("date",  Date())
                    db.collection("reports")
                        .add(data)
                        .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                            emitter.onSuccess(documentReference.id)
                            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.id)
                        })
                        .addOnFailureListener(OnFailureListener { e ->
                            Log.w("tag", "Error adding document", e)
                        })
                } else {
                    println("Something went wrong")
                }
            }.addOnFailureListener { exception ->
                emitter.onError(ErrorFetching("Adding"))
            }
        }
    }


    fun getStudentReports():Single<ArrayList<ReportModel>> {

        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ReportModel> = ArrayList()
        return create { emitter ->
            db.collection("reports")
                .whereEqualTo("user_id",currentUser)
                .orderBy("date").get()
                .addOnSuccessListener{ documents->
                   for(document in documents){
                       reports.add(document.toObject(ReportModel::class.java))
                   }
                    emitter.onSuccess(reports)
                }
                .addOnFailureListener{ e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }


    fun getTeacherResolvedReport():Single<ArrayList<ManagmentReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagmentReportModel> = ArrayList()


        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener{document ->
                    if (document != null) {
                        db.collection("reports")
                            .whereEqualTo("school_id",document.data?.get("school"))
                            .whereEqualTo("class_name",document.data?.get("class_name"))
                            .whereEqualTo("report_status","resolved")
                            .whereEqualTo("section",document.data?.get("section"))
                            .orderBy("date")
                            .get()
                            .addOnSuccessListener{ documents->
                                for(document in documents){
                                    reports.add(document.toObject(ManagmentReportModel::class.java))
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener{ e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener{ e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }

    fun getTeacherUnresolvedReport():Single<ArrayList<ManagmentReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ManagmentReportModel> = ArrayList()


        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener{document ->
                    if (document != null) {
                        println("user data  ${document.data}")
                        db.collection("reports")
                            .whereEqualTo("school_id",document.data?.get("school"))
                            .whereEqualTo("class_name",document.data?.get("class_name"))
                            .whereEqualTo("report_status","unresolved")
                            .whereEqualTo("section",document.data?.get("section")).get()
                            .addOnSuccessListener{ documents->
                                for(doc in documents){
                                    println("document data ${doc.data}")
                                    reports.add(doc.toObject(ManagmentReportModel::class.java))
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener{ e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener{ e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }


    fun getSchoolReports():Single<ArrayList<ReportModel>> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        var db = FirebaseFirestore.getInstance()
        val reports: ArrayList<ReportModel> = ArrayList()

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener{document ->
                    if (document != null) {
                        db.collection("reports")
                            .whereEqualTo("school_id",document.data?.get("school"))
                            .orderBy("date").get()
                            .addOnSuccessListener{ documents->
                                for(document in documents){
                                    reports.add(document.toObject(ReportModel::class.java))
                                }
                                emitter.onSuccess(reports)
                            }
                            .addOnFailureListener{ e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener{ e ->
                    Log.w("tag", "Error adding document", e)
                }
        }
    }

    fun getTeacherReport(data: HashMap<String, String>) {
        // Hit user col to get iD first
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentUser).get().addOnSuccessListener { documents ->

            db.collection("reports").whereEqualTo("schoold_id", documents.data?.get("school"))
                .whereEqualTo("class", documents.data?.get("class_name"))
                .whereEqualTo("sec", documents.data?.get("section")).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("Reports", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Reports", "Error getting documents: ", exception)
                }
        }
            .addOnFailureListener { exception ->
                Log.w("Reports", "Error getting documents: ", exception)
            }


    }


    fun fetchReport(): Single<QuerySnapshot> {
        val currentDate = (Date().time) / 1000

        var db =
            FirebaseFirestore.getInstance().collection("reports")
                .whereGreaterThan("date", 1561978814)
        return create { emitter ->
            db.get().addOnSuccessListener {
                if (it.documents != null) {
                    emitter.onSuccess(
                        it
                    )

                    println("accessed")
                } else {
                    emitter.onError(ErrorFetching("Schools"))
                }
            }
        }

    }


    fun reportLeft(): Single<Int> {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        val currentDate = Date(Date().year,Date().month,1)
        var db =
            FirebaseFirestore.getInstance()
        var previourReportReference =
            FirebaseFirestore.getInstance().collection("reports")
                .whereGreaterThan("date",currentDate )

        return create { emitter ->
            db.collection("users")
                .document(currentUser).get()
                .addOnSuccessListener{document ->
                    if (document != null) {
                        previourReportReference.get()
                            .addOnSuccessListener{ documents->
                                emitter.onSuccess(3-documents.size())
                            }
                            .addOnFailureListener{ e ->
                                Log.w("tag", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener{ e ->
                    Log.w("tag", "Error adding document", e)
                }
        }

    }


}