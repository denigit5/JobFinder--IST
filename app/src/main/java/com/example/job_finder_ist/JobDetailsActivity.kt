package com.example.job_finder_ist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.job_finder_ist.viewmodel.JobDetailsViewModel

class JobDetailsActivity : AppCompatActivity() {

    private val viewModel: JobDetailsViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        // Retrieve jobId from intent
        val jobId = intent.getStringExtra("jobId")
        if (jobId == null) {
            Toast.makeText(this, "Job ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Observe job details and status messages
        viewModel.job.observe(this) { job ->
            job?.let { displayJobDetails(it) }
        }

        viewModel.statusMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        // Fetch job details via ViewModel
        viewModel.fetchJobDetails(jobId)

        // Set up apply button action to navigate to ApplyJobActivity
        findViewById<Button>(R.id.apply_for_job_button)?.setOnClickListener {
            val intent = Intent(this, ApplyJobActivity::class.java)
            intent.putExtra("jobId", jobId) // Pass the jobId to ApplyJobActivity
            startActivity(intent)
        }
    }

    private fun displayJobDetails(job: Job) {
        findViewById<TextView>(R.id.job_title)?.text = job.title
        findViewById<TextView>(R.id.job_description)?.text = job.description
        findViewById<TextView>(R.id.job_location)?.text = job.location
    }
}
