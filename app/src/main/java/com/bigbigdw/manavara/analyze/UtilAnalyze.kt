package com.bigbigdw.manavara.analyze

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

fun getJsonFiles(
    platform : String,
    type : String,
    root : String,
    callbacks: (List<String>) -> Unit
) {

    val storage = Firebase.storage
    val storageRef = storage.reference
    val testRef = storageRef.child("${platform}/${type}/$root")

    testRef.listAll().addOnSuccessListener { result ->
        val fileList = result.items.map { it.name }
        callbacks.invoke(fileList)
    }.addOnFailureListener {
        // Handle the error
    }
}