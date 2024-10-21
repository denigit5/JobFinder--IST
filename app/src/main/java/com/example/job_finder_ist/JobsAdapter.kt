package com.example.job_finder_ist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Job(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = "Remote",
    val company: String = "",
    val expired: Boolean = false,
    val jobType: String = "",
    val postedTimestamp: Long = System.currentTimeMillis()
)

class JobsAdapter(var jobList: List<Job>) :
    RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_card, parent, false)
        return JobViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobList[position]

        // Set job details
        holder.jobTitle.text = job.title
        holder.companyName.text = job.company
        holder.jobLocation.text = job.location
        holder.jobType.text = job.jobType
        holder.jobPostedTime.text = "Posted: " + job.postedTimestamp.toString()

        // Disable apply button if the job is expired
        if (job.expired) {
            holder.applyButton.text = "Expired"
            holder.applyButton.isEnabled = false
        } else {
            holder.applyButton.text = "Apply"
            holder.applyButton.isEnabled = true

            // Handle navigation to JobDetailsActivity on button click
            holder.applyButton.setOnClickListener {
                val intent = Intent(it.context, JobDetailsActivity::class.java)
                intent.putExtra("jobId", job.id) // Pass job ID to JobDetailsActivity
                it.context.startActivity(intent)  // Navigate to JobDetailsActivity
            }
        }
    }

    override fun getItemCount(): Int = jobList.size

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var jobTitle: TextView = itemView.findViewById(R.id.job_title)
        var companyName: TextView = itemView.findViewById(R.id.company_name)
        var jobLocation: TextView = itemView.findViewById(R.id.job_location)
        var jobType: TextView = itemView.findViewById(R.id.job_type)
        var jobPostedTime: TextView = itemView.findViewById(R.id.job_posted_time)
        var applyButton: Button = itemView.findViewById(R.id.applyButton)
    }
}
