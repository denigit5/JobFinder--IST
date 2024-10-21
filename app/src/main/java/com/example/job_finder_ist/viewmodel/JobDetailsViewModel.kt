package com.example.job_finder_ist.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.job_finder_ist.Job
import com.google.firebase.firestore.FirebaseFirestore

class JobDetailsViewModel : ViewModel() {

    private val _job = MutableLiveData<Job>()
    val job: LiveData<Job> = _job

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    // Function to fetch job details based on jobId from Firestore
    @SuppressLint("NullSafeMutableLiveData")
    fun fetchJobDetails(jobId: String) {
        val db = FirebaseFirestore.getInstance()

        // Fetch the job data from Firestore using the jobId
        db.collection("jobs").document(jobId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Convert Firestore document to Job object
                    val job = documentSnapshot.toObject(Job::class.java)
                    _job.value = job
                } else {
                    _statusMessage.value = "Job not found"
                }
            }
            .addOnFailureListener { exception ->
                _statusMessage.value = "Error fetching job: ${exception.message}"
            }
    }
}
