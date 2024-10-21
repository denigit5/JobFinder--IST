package com.example.job_finder_ist

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class ApplyJobActivity : AppCompatActivity() {

    private lateinit var jobId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_job)

        // Retrieve jobId from intent
        jobId = intent.getStringExtra("jobId").orEmpty()

        // Check if jobId is not empty
        if (jobId.isEmpty()) {
            Toast.makeText(this, "Job ID is missing", Toast.LENGTH_SHORT).show()
            finish() // Close activity if no job ID
            return
        }

        val nameEditText: EditText = findViewById(R.id.name_edit_text)
        val emailEditText: EditText = findViewById(R.id.email_edit_text)
        val messageEditText: EditText = findViewById(R.id.message_edit_text)
        val applyButton: Button = findViewById(R.id.apply_for_job_position)

        // Handle Apply button click
        applyButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val message = messageEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // Check network availability before applying
                if (isNetworkAvailable()) {
                    applyForJob(name, email, message)
                } else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyForJob(name: String, email: String, message: String) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val applicationData = hashMapOf(
                "jobId" to jobId,  // Job ID from the intent
                "userId" to userId,  // The alumnus ID
                "name" to name,
                "email" to email,
                "message" to message,
                "status" to "pending",  // Initial status is pending
                "appliedAt" to System.currentTimeMillis()
            )

            // Save the application in the centralized job_applications collection
            db.collection("job_applications")
                .add(applicationData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Application successful", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after successful application
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to apply", Toast.LENGTH_SHORT).show()
                }
        }
    }


    // Method to check network availability
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
