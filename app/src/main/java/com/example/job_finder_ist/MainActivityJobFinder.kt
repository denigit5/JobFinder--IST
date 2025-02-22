package com.example.job_finder_ist

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class MainActivityJobFinder : AppCompatActivity() {

    private val jobsList = mutableListOf<Job>() // Store job listings
    private lateinit var jobsAdapter: JobsAdapter // Adapter for RecyclerView
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    private val REQUEST_NOTIFICATION_PERMISSION = 1 // Constant for notification permission request

    // Activity Result API for image picking
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val profilePicture = findViewById<ImageView>(R.id.profile_picture)
            profilePicture.setImageURI(it)

            // Upload the image to Firebase Storage and save the URL to Firestore
            uploadImageToFirebase(it)
        }
    }

    // Function to upload the image to Firebase Storage
    private fun uploadImageToFirebase(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/${FirebaseAuth.getInstance().currentUser?.uid}")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Get the download URL
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Save the download URL to Firestore
                    saveProfilePictureUrl(downloadUri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivityJobFinder", "Image upload failed", exception)
            }
    }

    // Function to save the profile picture URL to Firestore
    private fun saveProfilePictureUrl(downloadUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocRef = firestore.collection("alumni").document(userId)

            // Update the profile picture URL field in the user's Firestore document
            val updates = hashMapOf<String, Any>(
                "profilePictureUrl" to downloadUrl
            )

            userDocRef.update(updates)
                .addOnSuccessListener {
                    Log.d("MainActivityJobFinder", "Profile picture URL saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivityJobFinder", "Error saving profile picture URL", e)
                }
        }
    }

    private fun loadProfilePicture() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocRef = firestore.collection("alumni").document(userId)

            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val profilePictureUrl = documentSnapshot.getString("profilePictureUrl")
                        profilePictureUrl?.let {
                            val profilePicture = findViewById<ImageView>(R.id.profile_picture)
                            Glide.with(this)
                                .load(it)
                                .placeholder(R.drawable.image) // default image
                                .into(profilePicture)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivityJobFinder", "Error loading profile picture", e)
                }
        }
    }

    // Function to handle UI setup and lifecycle events
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Fetch user profile picture URL from Firestore
        fetchProfilePictureUrl()

        // Notification dot visibility handling
        val notificationDot = findViewById<View>(R.id.notification_dot)
        notificationDot.visibility = if (isNewJobPosted()) View.VISIBLE else View.GONE

        // Set up click listener for the profile picture
        val profilePicture = findViewById<ImageView>(R.id.profile_picture)
        profilePicture.setOnClickListener {
            showProfileOptions()
        }

        // Load the profile picture when the activity is created
        loadProfilePicture()

        // Fetch job listings from Firestore
        fetchJobsFromFirestore()

        // Subscribe to Firebase Cloud Messaging (FCM) for job notifications
        subscribeToJobNotifications()

        // Check notification permissions for API 33+
        checkNotificationPermission()

        // Initialize RecyclerView with job listings
        jobsAdapter = JobsAdapter(jobsList) // Pass the apply job click listener
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = jobsAdapter // Attaching adapter to RecyclerView

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_item_spacing) // Define spacing in dimens
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))
    }

    // Request notification permission for Android 13 (API level 33) and above
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION)
            }
        }
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with notifications
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to fetch job data from Firestore and update the RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchJobsFromFirestore() {
        Log.d("MainActivityJobFinder", "Fetching jobs from Firestore...")
        db.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                jobsList.clear() // Clear the list before adding new jobs
                for (document in result) {
                    val job = document.toObject(Job::class.java) // Convert Firestore document to Job object
                    jobsList.add(job)  // Add each job to the list
                }
                Log.d("MainActivityJobFinder", "Jobs fetched successfully: $jobsList")

                jobsAdapter.notifyDataSetChanged() // Notify adapter to refresh the RecyclerView
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivityJobFinder", "Error fetching jobs: ", exception)
            }
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = space // Space below each item
        }
    }

    // Function to display profile options menu
    @SuppressLint("ResourceType")
    private fun showProfileOptions() {
        // Inflate the custom layout
        val popupView = layoutInflater.inflate(R.menu.profile_menu, null)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(popupView)
            .create()

        // Set click listeners for the buttons
        popupView.findViewById<Button>(R.id.change_picture).setOnClickListener {
            openImagePicker() // Open image picker
            dialog.dismiss()
        }
        popupView.findViewById<Button>(R.id.new_job_notifications).setOnClickListener {
            showJobNotifications() // Show job notifications
            dialog.dismiss()
        }
        popupView.findViewById<Button>(R.id.logout).setOnClickListener {
            logoutUser() // Logout user
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    // Function to display job notifications in a dialog
    private fun showJobNotifications() {
        AlertDialog.Builder(this)
            .setTitle("Job Notifications")
            .setMessage("You have new job postings available.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // Open the image picker to change profile picture
    private fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }

    // Function to fetch the profile picture URL from Firestore
    private fun fetchProfilePictureUrl() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val docRef = db.collection("alumni").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val profilePictureUrl = document.getString("profilePictureUrl")
                        val profilePicture = findViewById<ImageView>(R.id.profile_picture)

                        profilePictureUrl?.let {
                            // Load profile picture using Glide
                            Glide.with(this).load(it).into(profilePicture)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivityJobFinder", "Error fetching profile picture URL", e)
                }
        }
    }

    // Logout function to handle user sign out
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    // Function to determine if a new job has been posted
    private fun isNewJobPosted(): Boolean {
        // Check for new job postings logic
        return true // Placeholder for now, can be linked with actual job posting data
    }

    // Subscribe to Firebase Cloud Messaging (FCM) for job notifications
    private fun subscribeToJobNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("new_job_notifications")
            .addOnCompleteListener { task ->
                val msg = if (task.isSuccessful) "Subscribed to job notifications" else "Failed to subscribe to job notifications"
                Log.d("MainActivityJobFinder", msg)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }
}
