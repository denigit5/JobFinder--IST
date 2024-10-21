package com.example.job_finder_ist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

//// Data class with a job ID field to uniquely identify each job
//data class Job(
//    val id: String = "", // Add an ID field for each job
//    val title: String = "",
//    val description: String = "",
//    val location: String = "Kenya", // Default location is Kenya
//    val company: String = "",
//    val expired: Boolean = false,
//    val jobType: String = "",
//    val postedTimestamp: Long = System.currentTimeMillis()
//)

class AddJobsActivity : AppCompatActivity() {

    // Initialize Firestore instance
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add 20 software jobs
        addSoftwareJobsToFirestore()
    }

    private fun addSoftwareJobsToFirestore() {
        val jobs = listOf(
            Job(
                id = "job1",
                title = "Android Developer",
                description = "As an Android Developer, you will be responsible for designing, developing, and maintaining high-performance Android applications. Your role involves working with cross-functional teams to define and implement new features, improve app performance, and fix bugs to ensure a smooth user experience.",
                company = "Tech Innovators",
                jobType = "Full-time"
            ),
            Job(
                id = "job2",
                title = "Full-Stack Developer",
                description = "We are looking for a Full-Stack Developer to create scalable software solutions. As part of a team, you will be developing both the front-end and back-end, ensuring efficient data flow and high performance across web and mobile applications. Experience with modern frameworks is required.",
                company = "Nairobi Tech",
                jobType = "Full-time"
            ),
            Job(
                id = "job3",
                title = "Backend Developer",
                description = "As a Backend Developer, your primary responsibility will be to build and maintain server-side logic, APIs, and databases. You will be working closely with front-end developers to integrate user-facing elements with server-side functionality, ensuring a seamless experience for users.",
                company = "Kenya Web Solutions",
                jobType = "Part-time"
            ),
            Job(
                id = "job4",
                title = "Frontend Developer",
                description = "The Frontend Developer will focus on designing and implementing user interfaces for web and mobile applications. You will work closely with UX/UI designers and backend developers to create intuitive, responsive, and visually appealing websites that provide a great user experience.",
                company = "Design Studios",
                jobType = "Contract"
            ),
            Job(
                id = "job5",
                title = "DevOps Engineer",
                description = "As a DevOps Engineer, you will manage our cloud infrastructure and handle continuous integration and deployment pipelines. Your role will be critical in automating tasks, maintaining system reliability, and collaborating with developers to optimize workflows and ensure smooth product releases.",
                company = "Cloud Masters",
                jobType = "Full-time"
            ),
            Job(
                id = "job6",
                title = "Software Engineer",
                description = "A Software Engineer at Global Software Inc. is responsible for building scalable enterprise applications. You will work across multiple projects, implementing software solutions that meet client requirements, while maintaining high standards of code quality and participating in code reviews.",
                company = "Global Software Inc.",
                jobType = "Full-time"
            ),
            Job(
                id = "job7",
                title = "Mobile App Developer",
                description = "We are seeking a Mobile App Developer skilled in iOS and Android development. You will be responsible for creating, testing, and maintaining mobile apps, ensuring they perform efficiently across different devices. Collaboration with UI/UX designers is crucial to deliver user-friendly applications.",
                company = "Mobile Corp",
                jobType = "Contract"
            ),
            Job(
                id = "job8",
                title = "AI Engineer",
                description = "As an AI Engineer, you will be working on cutting-edge artificial intelligence and machine learning models. Your tasks will include training data, developing algorithms, and deploying machine learning models to production environments. A strong background in data science is necessary.",
                company = "AI Solutions",
                jobType = "Full-time"
            ),
            Job(
                id = "job9",
                title = "Data Scientist",
                description = "We are looking for a Data Scientist to help us analyze large sets of structured and unstructured data. You will be responsible for extracting insights from data, building predictive models, and working with various teams to optimize business operations based on data-driven decisions.",
                company = "Data Analytics",
                jobType = "Part-time"
            ),
            Job(
                id = "job10",
                title = "QA Engineer",
                description = "As a QA Engineer, your role will be to ensure the quality and functionality of our software products. You will write and execute test cases, report bugs, and work closely with developers to improve product quality. Experience with both manual and automated testing is preferred.",
                company = "Testers Inc.",
                jobType = "Contract"
            ),
            Job(
                id = "job11",
                title = "Cloud Engineer",
                description = "The Cloud Engineer will be responsible for managing and optimizing our cloud infrastructure. You will work with cloud service providers to ensure the reliability, security, and performance of our systems, as well as automate processes to reduce manual intervention.",
                company = "Cloud Servers",
                jobType = "Full-time"
            ),
            Job(
                id = "job12",
                title = "Security Engineer",
                description = "As a Security Engineer, you will protect the integrity of IT systems by designing, implementing, and maintaining security measures. Your duties will include monitoring systems, responding to security incidents, and working with various teams to ensure the security of our infrastructure.",
                company = "SecureTech",
                jobType = "Full-time"
            ),
            Job(
                id = "job13",
                title = "Web Developer",
                description = "The Web Developer will be responsible for designing, coding, and modifying websites from layout to function according to client specifications. You will work with graphic designers and back-end developers to create visually appealing and user-friendly websites that are mobile-responsive.",
                company = "Nairobi Web Co.",
                jobType = "Full-time"
            ),
            Job(
                id = "job14",
                title = "Database Administrator",
                description = "As a Database Administrator, you will manage and maintain the company’s databases, ensuring data is secure, available, and well-organized. You will be responsible for database backups, performance optimization, and troubleshooting to ensure smooth database operations.",
                company = "Kenya Data",
                jobType = "Part-time"
            ),
            Job(
                id = "job15",
                title = "UX/UI Designer",
                description = "We are seeking a UX/UI Designer to create intuitive and user-friendly designs for mobile and web applications. You will conduct user research, create wireframes and prototypes, and collaborate with developers to ensure that the final products meet user needs and enhance the user experience.",
                company = "Creative Studios",
                jobType = "Contract"
            ),
            Job(
                id = "job16",
                title = "Blockchain Developer",
                description = "As a Blockchain Developer, you will develop secure and efficient blockchain-based applications. Your responsibilities include writing smart contracts, managing blockchain networks, and collaborating with cross-functional teams to integrate blockchain technology into various use cases.",
                company = "Crypto Kenya",
                jobType = "Full-time"
            ),
            Job(
                id = "job17",
                title = "Game Developer",
                description = "We are looking for a Game Developer with experience in mobile and web game development. You will design, develop, and test games, ensuring that they are both fun and functional. A strong knowledge of game mechanics, animation, and user engagement is highly desirable.",
                company = "GameTech",
                jobType = "Part-time"
            ),
            Job(
                id = "job18",
                title = "IT Support Specialist",
                description = "The IT Support Specialist will provide technical assistance and support to clients and employees. You will troubleshoot hardware and software issues, set up systems, and ensure that all IT-related problems are resolved quickly and efficiently to minimize downtime.",
                company = "TechCare",
                jobType = "Full-time"
            ),
            Job(
                id = "job19",
                title = "Systems Analyst",
                description = "As a Systems Analyst, you will evaluate IT systems and suggest improvements. Your role will involve gathering business requirements, performing system testing, and working with developers to implement system changes that enhance business operations and improve efficiency.",
                company = "Smart Systems",
                jobType = "Full-time"
            ),
            Job(
                id = "job20",
                title = "Network Engineer",
                description = "The Network Engineer will manage and maintain the company’s network infrastructure, ensuring network availability and performance. You will be responsible for troubleshooting network issues, implementing new technologies, and ensuring that security protocols are followed.",
                company = "Network Solutions",
                jobType = "Contract"
            )
        )

        for (job in jobs) {
            // Add each job to the "jobs" collection in Firestore with the assigned ID
            db.collection("jobs")
                .document(job.id) // Set the document ID to the job ID
                .set(job)
                .addOnSuccessListener {
                    Log.d("AddJobsActivity", "Job added with ID: ${job.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("AddJobsActivity", "Error adding job", e)
                }
        }
    }
}
