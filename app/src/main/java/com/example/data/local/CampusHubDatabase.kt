package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Achievement
import com.example.data.model.Opportunity
import com.example.data.model.ProjectRecommendation
import com.example.data.model.ResourceItem
import com.example.data.model.RoadmapPhase
import com.example.data.model.Skill
import com.example.data.model.StudentProfile
import com.example.data.model.StudySession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StudentProfile::class,
        Skill::class,
        Achievement::class,
        RoadmapPhase::class,
        ProjectRecommendation::class,
        Opportunity::class,
        ResourceItem::class,
        StudySession::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CampusHubDatabase : RoomDatabase() {

    abstract fun campusHubDao(): CampusHubDao

    companion object {
        @Volatile
        private var INSTANCE: CampusHubDatabase? = null

        fun getInstance(context: Context): CampusHubDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CampusHubDatabase::class.java,
                    "campushub_database.db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.campusHubDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: CampusHubDao) {
            // Profile
            dao.insertOrUpdateProfile(
                StudentProfile(
                    id = 1,
                    name = "Pankush Kumar",
                    college = "Campus Institute of Technology",
                    course = "B.Tech Computer Science & Engineering",
                    currentYear = "3rd Year (Semester 5)",
                    location = "Chandigarh, India",
                    targetCareer = "Full Stack Developer",
                    cgpa = 8.64,
                    academicStrengths = "Data Structures, Database Management, React & UI Architecture",
                    weakAreas = "Distributed Systems, Microservices, Async Message Queues",
                    linkedInUrl = "linkedin.com/in/pankush-kumar",
                    gitHubUrl = "github.com/pankush-kumar",
                    portfolioUrl = "pankush.dev",
                    leetCodeHandle = "pankush_codes"
                )
            )

            // Skills with Evidence
            dao.insertSkills(
                listOf(
                    Skill(
                        name = "HTML / CSS",
                        category = "Frontend",
                        proficiency = "Advanced",
                        evidenceCourses = true,
                        evidenceProjects = true,
                        evidenceGitHub = true,
                        evidenceCert = true,
                        isTargetCareerSkill = true,
                        subtopics = "Semantic HTML, Flexbox, Grid, Responsive Design, CSS Variables"
                    ),
                    Skill(
                        name = "JavaScript",
                        category = "Frontend",
                        proficiency = "Intermediate",
                        evidenceCourses = true,
                        evidenceProjects = true,
                        evidenceGitHub = true,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "ES6+, DOM Manipulation, Async/Await, Fetch API, Closures, Promises"
                    ),
                    Skill(
                        name = "React",
                        category = "Frontend",
                        proficiency = "Intermediate",
                        evidenceCourses = true,
                        evidenceProjects = true,
                        evidenceGitHub = true,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "Hooks, Component Lifecycle, State Management, Custom Hooks, Context"
                    ),
                    Skill(
                        name = "Git & GitHub",
                        category = "DevOps/Tools",
                        proficiency = "Intermediate",
                        evidenceCourses = true,
                        evidenceProjects = true,
                        evidenceGitHub = true,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "Branching, Pull Requests, Merge Conflict Resolution, GitHub Actions"
                    ),
                    Skill(
                        name = "REST APIs",
                        category = "Backend",
                        proficiency = "Beginner",
                        evidenceCourses = true,
                        evidenceProjects = false,
                        evidenceGitHub = false,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "HTTP Methods, Status Codes, JSON serialization, Express routing"
                    ),
                    Skill(
                        name = "Node.js & Express",
                        category = "Backend",
                        proficiency = "Beginner",
                        evidenceCourses = true,
                        evidenceProjects = false,
                        evidenceGitHub = false,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "Event Loop, Express middleware, routing, environment variables"
                    ),
                    Skill(
                        name = "MongoDB",
                        category = "Database",
                        proficiency = "Beginner",
                        evidenceCourses = true,
                        evidenceProjects = false,
                        evidenceGitHub = false,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "CRUD operations, Schema modeling, Indexes, Aggregation pipeline"
                    ),
                    Skill(
                        name = "Data Structures & Algorithms",
                        category = "Core CS",
                        proficiency = "Intermediate",
                        evidenceCourses = true,
                        evidenceProjects = false,
                        evidenceGitHub = true,
                        evidenceCert = false,
                        isTargetCareerSkill = true,
                        subtopics = "Arrays, Linked Lists, Trees, Graphs, Dynamic Programming, Two Pointers"
                    ),
                    Skill(
                        name = "Communication & Teamwork",
                        category = "Soft Skills",
                        proficiency = "Advanced",
                        evidenceCourses = false,
                        evidenceProjects = true,
                        evidenceGitHub = false,
                        evidenceCert = true,
                        isTargetCareerSkill = false,
                        subtopics = "Technical writing, Hackathon team leadership, Demo presentations"
                    )
                )
            )

            // Timeline Achievements
            dao.insertAchievements(
                listOf(
                    Achievement(
                        year = 2026,
                        title = "College Hackathon Finalist — Top 5",
                        category = "Hackathon",
                        dateString = "March 2026",
                        description = "Built an AI-assisted campus study group finder in 36 hours with a 4-person team. Handled frontend in React.",
                        skillsInvolved = "React, UI Design, Team Leadership, Git Collaboration",
                        evidenceUrl = "https://github.com/pankush-kumar/hack-campus",
                        associatedGoal = "Full Stack Developer"
                    ),
                    Achievement(
                        year = 2026,
                        title = "GDGC Web Development Core Member",
                        category = "Club/Leadership",
                        dateString = "January 2026",
                        description = "Selected to lead campus workshops on Modern Frontend and Git version control for 120+ first-year students.",
                        skillsInvolved = "Public Speaking, Mentorship, JavaScript, Web Standards",
                        evidenceUrl = "https://gdg.community.dev/campus-hub",
                        associatedGoal = "Leadership & Professional"
                    ),
                    Achievement(
                        year = 2025,
                        title = "First Full Web Application Launched",
                        category = "Project",
                        dateString = "October 2025",
                        description = "Engineered a responsive student note-sharing portal featuring markdown previews and local storage cache.",
                        skillsInvolved = "HTML5, CSS3, JavaScript, Responsive Design",
                        evidenceUrl = "https://notes-preview.pankush.dev",
                        associatedGoal = "Full Stack Developer"
                    ),
                    Achievement(
                        year = 2025,
                        title = "Complete JavaScript Certification",
                        category = "Certificate",
                        dateString = "July 2025",
                        description = "Successfully finished 60-hour intensive JavaScript course with 10 mini-projects on DOM and Async APIs.",
                        skillsInvolved = "JavaScript ES6+, Async/Await, DOM APIs",
                        evidenceUrl = "https://coursera.org/verify/JS-78921",
                        associatedGoal = "Frontend Mastery"
                    ),
                    Achievement(
                        year = 2024,
                        title = "State Science & Tech Innovation Fair",
                        category = "Competition",
                        dateString = "November 2024",
                        description = "Awarded 2nd Prize for Automated Greenhouse telemetry monitoring demonstration using microcontrollers.",
                        skillsInvolved = "Problem Solving, Hardware Integration, Technical Presentation",
                        evidenceUrl = "https://state-sciencefair.org/award/2024",
                        associatedGoal = "Foundational STEM"
                    )
                )
            )

            // Personalized Phased Roadmap
            dao.insertRoadmapPhases(
                listOf(
                    RoadmapPhase(
                        phaseNumber = 1,
                        title = "Phase 1: Web Fundamentals",
                        description = "Semantic HTML5, CSS Flexbox & Grid, responsive mobile-first layouts, accessibility standards.",
                        status = "COMPLETED",
                        conceptsList = "Semantic Tags, Box Model, Flexbox, Grid, Media Queries, Viewports",
                        practicalTask = "Build a responsive developer portfolio with dark mode toggle.",
                        isCompleted = true
                    ),
                    RoadmapPhase(
                        phaseNumber = 2,
                        title = "Phase 2: Modern JavaScript",
                        description = "Deep dive into ES6+, asynchronous programming, Fetch API, DOM manipulation, closures, and modular architecture.",
                        status = "COMPLETED",
                        conceptsList = "ES6 Modules, Promises, Async/Await, Event Bubbling, Error Handling",
                        practicalTask = "Create an interactive Weather Dashboard consuming OpenWeather API.",
                        isCompleted = true
                    ),
                    RoadmapPhase(
                        phaseNumber = 3,
                        title = "Phase 3: React & Modern State",
                        description = "Component architecture, Hooks (useState, useEffect, useMemo), React Query, and component library styling.",
                        status = "IN_PROGRESS",
                        conceptsList = "Virtual DOM, JSX, Hooks Lifecycle, State Lifting, Custom Hooks, Tailwind CSS",
                        practicalTask = "Develop Campus Event Hub with filtering, search, and dynamic state.",
                        isCompleted = false
                    ),
                    RoadmapPhase(
                        phaseNumber = 4,
                        title = "Phase 4: REST APIs & Backend Services",
                        description = "Build secure backend services with Node.js & Express, RESTful architecture, middleware, and JWT authentication.",
                        status = "UPCOMING",
                        conceptsList = "HTTP Verbs, Express Middleware, JWT Tokens, Auth Guards, CORS, Input Validation",
                        practicalTask = "Implement authentication API with password hashing and session tokens.",
                        isCompleted = false
                    ),
                    RoadmapPhase(
                        phaseNumber = 5,
                        title = "Phase 5: Databases & Data Modeling",
                        description = "Relational and Document databases: schema design, indexing, MongoDB aggregation, transactions, and ORMs/ODMs.",
                        status = "UPCOMING",
                        conceptsList = "Schema Normalization, Indexes, MongoDB CRUD, Mongoose Models, ACID vs BASE",
                        practicalTask = "Design database schema for Student Lost & Found with status tracking.",
                        isCompleted = false
                    ),
                    RoadmapPhase(
                        phaseNumber = 6,
                        title = "Phase 6: Full Stack Capstone Projects",
                        description = "Connect frontend to backend database, implement file uploads, webhooks, error telemetry, and end-to-end integration.",
                        status = "UPCOMING",
                        conceptsList = "Full Stack Integration, Cloudinary Uploads, State Sync, Error Boundaries",
                        practicalTask = "Complete and deploy the Student Lost & Found full stack platform.",
                        isCompleted = false
                    ),
                    RoadmapPhase(
                        phaseNumber = 7,
                        title = "Phase 7: Cloud & DevOps Foundations",
                        description = "Containerization with Docker, CI/CD automated test pipelines with GitHub Actions, and production cloud hosting.",
                        status = "UPCOMING",
                        conceptsList = "Dockerfiles, GitHub Actions, Environment Variables, Cloud Run / Vercel Deploy",
                        practicalTask = "Set up CI/CD pipeline running lint and test suites on git push.",
                        isCompleted = false
                    ),
                    RoadmapPhase(
                        phaseNumber = 8,
                        title = "Phase 8: Interview Prep & System Design",
                        description = "Scalable web architectures, caching with Redis, load balancing concepts, and behavioral interview mastery.",
                        status = "UPCOMING",
                        conceptsList = "Caching, CDN, Horizontal Scaling, Database Sharding, Mock Interviews",
                        practicalTask = "Solve 50 LeetCode Mediums and perform 2 peer system design mock interviews.",
                        isCompleted = false
                    )
                )
            )

            // Recommended Projects (Directly connected to skills)
            dao.insertProjects(
                listOf(
                    ProjectRecommendation(
                        title = "Student Lost & Found Portal",
                        description = "Campus portal where students report lost or found items with photo uploads, category filtering, and claim verification.",
                        difficulty = "Intermediate",
                        skillsDeveloped = "React, Node.js, Express, MongoDB, Cloudinary, JWT Auth",
                        roadmapSteps = "UI Mockup in React, REST API Design, MongoDB Schema Modeling, Auth Integration, Production Deployment",
                        isCompleted = false,
                        githubUrl = ""
                    ),
                    ProjectRecommendation(
                        title = "Campus Peer Tutoring Marketplace",
                        description = "Platform connecting senior mentors with junior students for subject tutoring sessions, schedule booking, and peer reviews.",
                        difficulty = "Intermediate",
                        skillsDeveloped = "React, Tailwind, Express, MongoDB, Calendar Scheduling",
                        roadmapSteps = "User Profiles (Tutor/Student), Booking Calendar Logic, Session Review System, Email Notifications",
                        isCompleted = false,
                        githubUrl = ""
                    ),
                    ProjectRecommendation(
                        title = "Interactive DSA Algorithm Visualizer",
                        description = "Educational web tool visualizing Sorting (Merge, Quick, Radix), Graph traversals (BFS, DFS, Dijkstra), and DP grids.",
                        difficulty = "Advanced",
                        skillsDeveloped = "React, Canvas API, Algorithmic Time Complexity, Async Animation",
                        roadmapSteps = "Canvas State Engine, Step-by-Step Generator, Speed & Array Size Controls, Complexity Explanation Panels",
                        isCompleted = false,
                        githubUrl = ""
                    )
                )
            )

            // Opportunities
            dao.insertOpportunities(
                listOf(
                    Opportunity(
                        title = "Smart India Hackathon 2026",
                        organization = "Ministry of Education & AICTE",
                        type = "Hackathon",
                        deadline = "April 15, 2026",
                        requiredSkills = "React, REST APIs, Git & GitHub, Problem Solving",
                        description = "Nationwide 36-hour hackathon tackling real-world problem statements proposed by central ministries and industry partners.",
                        eligibility = "Undergraduate & Graduate students in teams of 6 (minimum 1 female member).",
                        applyUrl = "https://sih.gov.in"
                    ),
                    Opportunity(
                        title = "Software Engineer Intern (Frontend)",
                        organization = "Razorpay Tech",
                        type = "Internship",
                        deadline = "May 10, 2026",
                        requiredSkills = "React, JavaScript, HTML / CSS, Git & GitHub",
                        description = "Join the developer experience team building next-generation merchant checkout SDKs and dashboard components.",
                        eligibility = "3rd / 4th year Engineering students graduating in 2026 or 2027 with strong CS fundamentals.",
                        applyUrl = "https://razorpay.com/jobs"
                    ),
                    Opportunity(
                        title = "Google Summer of Code (GSoC)",
                        organization = "Google Open Source",
                        type = "Fellowship",
                        deadline = "March 30, 2026",
                        requiredSkills = "Git & GitHub, JavaScript, REST APIs, Open Source",
                        description = "Global stipend program bringing student developers into open source software development organizations.",
                        eligibility = "Students and open source beginners 18+ years old.",
                        applyUrl = "https://summerofcode.withgoogle.com"
                    ),
                    Opportunity(
                        title = "AWS Cloud Student Developer Fellowship",
                        organization = "Amazon Web Services",
                        type = "Scholarship",
                        deadline = "June 1, 2026",
                        requiredSkills = "Cloud Basics, REST APIs, Linux Fundamentals",
                        description = "Provides $1,500 AWS cloud credits, 1-on-1 mentorship with senior solution architects, and free certification vouchers.",
                        eligibility = "Enrolled undergraduate students in STEM fields.",
                        applyUrl = "https://aws.amazon.com/students"
                    )
                )
            )

            // Resources
            dao.insertResources(
                listOf(
                    ResourceItem(
                        title = "DBMS Normalization from 1NF to BCNF",
                        subject = "Database Management",
                        type = "Notes",
                        url = "https://campushub.resources/dbms-normalization.pdf",
                        description = "Complete breakdown with functional dependency graphs, decomposition algorithms, and lossless join proofs.",
                        isBookmarked = true
                    ),
                    ResourceItem(
                        title = "Operating Systems: Concurrency & Semaphores PYQs",
                        subject = "Operating Systems",
                        type = "PYQs",
                        url = "https://campushub.resources/os-semaphores-pyq.pdf",
                        description = "Solved university exam questions covering Readers-Writers problem, Dining Philosophers, and deadlock prevention.",
                        isBookmarked = false
                    ),
                    ResourceItem(
                        title = "React 19 State Architecture & Server Components",
                        subject = "Web Development",
                        type = "Video Guide",
                        url = "https://youtube.com/watch?v=react19-guide",
                        description = "Comprehensive 3-hour architectural masterclass on hooks, transitions, and optimizing re-renders.",
                        isBookmarked = true
                    ),
                    ResourceItem(
                        title = "Blind 75 LeetCode Topic-Wise Roadmap & Patterns",
                        subject = "Data Structures",
                        type = "Practice",
                        url = "https://leetcode.com/discuss/general-discussion/460599/blind-75-leetcode-questions",
                        description = "Essential coding patterns: Sliding Window, Fast/Slow Pointers, Top-K Elements, Dynamic Programming intervals.",
                        isBookmarked = false
                    ),
                    ResourceItem(
                        title = "System Design Primer: Microservices vs Monolith",
                        subject = "System Design",
                        type = "Documentation",
                        url = "https://github.com/donnemartin/system-design-primer",
                        description = "Covers CAP theorem, load balancing, reverse proxies, database replication, and asynchronous queues.",
                        isBookmarked = false
                    )
                )
            )

            // Study Sessions
            dao.insertStudySession(
                StudySession(
                    subject = "Data Structures & Algorithms",
                    durationMinutes = 45,
                    timestamp = System.currentTimeMillis() - 86400000L * 2,
                    notes = "Solved 2 Tree traversal problems (Lowest Common Ancestor and Diameter of Binary Tree)."
                )
            )
            dao.insertStudySession(
                StudySession(
                    subject = "React Component Architecture",
                    durationMinutes = 60,
                    timestamp = System.currentTimeMillis() - 86400000L,
                    notes = "Implemented custom hook for debounced search and infinite scrolling."
                )
            )
            dao.insertStudySession(
                StudySession(
                    subject = "Database Normalization Practice",
                    durationMinutes = 30,
                    timestamp = System.currentTimeMillis() - 3600000L * 4,
                    notes = "Worked through 3NF and BCNF decomposition examples from textbook."
                )
            )
        }
    }
}
