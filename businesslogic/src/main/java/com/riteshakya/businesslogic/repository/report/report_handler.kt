package com.riteshakya.businesslogic.repository.report

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.riteshakya.core.exception.ErrorFetching
import io.reactivex.Single
import java.util.*

class ReportHandler {

//    var currentUser:String
//
//    init {
//
//
//    }


    fun createReport(data: HashMap<String, Any>) {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()
        db.collection("reports")
            .add(data)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.id)
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w("tag", "Error adding document", e)
            })
    }


    fun getMyReport(data: HashMap<String, String>) {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()
        db.collection("reports").whereEqualTo("user_id", currentUser).get()

    }


    fun getSchoolReport(data: HashMap<String, String>) {
        // Hit school col to get iD first
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid;
        var db = FirebaseFirestore.getInstance()
        db.collection("schools").whereEqualTo("user_id", currentUser).get()
            .addOnSuccessListener { documents ->

                db.collection("reports").whereEqualTo(
                    "schoold_id",
                    documents.documents[0].data?.get("school_id")
                ).get().addOnSuccessListener { documents ->
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
        return Single.create { emitter ->
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


    fun updateReport(documentId: String, status: String){


        FirebaseFirestore.getInstance()
            .collection("reports")
            .document(documentId).update("report_status", status)
            .addOnSuccessListener { println( "Report successfully updated!") }
            .addOnFailureListener { println( "Failed updating Report") }


    }


}