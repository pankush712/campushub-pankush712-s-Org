package com.example.engine

import com.example.data.model.CareerProfile
import com.example.data.model.Opportunity
import com.example.data.model.ProjectRecommendation
import com.example.data.model.RoadmapPhase
import com.example.data.model.Skill
import com.example.data.model.StudentProfile
import com.example.data.model.StudySession

data class SkillGapResult(
    val targetCareer: String,
    val totalRequired: Int,
    val acquiredCount: Int,
    val readinessPercentage: Int,
    val masteredSkills: List<String>,
    val inProgressSkills: List<String>,
    val missingSkills: List<String>,
    val evidenceGaps: List<String>
)

data class NextBestAction(
    val title: String,
    val category: String,
    val description: String,
    val whyItMatters: String,
    val estimatedTime: String,
    val actionType: ActionType,
    val targetEntityId: Long = 0L
)

enum class ActionType {
    COMPLETE_ROADMAP_MILESTONE,
    BUILD_PROJECT_STEP,
    APPLY_OPPORTUNITY,
    PRACTICE_SKILL,
    STUDY_SESSION
}

data class OpportunityMatchResult(
    val opportunity: Opportunity,
    val matchPercentage: Int,
    val matchedSkills: List<String>,
    val missingSkills: List<String>
)

data class HabitMetrics(
    val totalMinutesLearned: Int,
    val sessionsCount: Int,
    val currentStreakDays: Int,
    val adaptiveAlert: String?
)

object GrowthEngine {

    val CAREER_PROFILES: Map<String, CareerProfile> = mapOf(
        "Full Stack Developer" to CareerProfile(
            title = "Full Stack Developer",
            description = "Builds responsive end-to-end web applications, client UI architectures, scalable REST/GraphQL APIs, and database models.",
            requiredSkills = listOf(
                "HTML / CSS",
                "JavaScript",
                "React",
                "Git & GitHub",
                "REST APIs",
                "Node.js & Express",
                "MongoDB",
                "Data Structures & Algorithms"
            ),
            recommendedRoadmap = listOf(
                "Web Foundations (HTML/CSS)",
                "Modern JavaScript (ES6+)",
                "React State Architecture",
                "REST APIs & Node.js",
                "Databases (SQL & NoSQL)",
                "Full Stack Deployment"
            )
        ),
        "AI / ML Engineer" to CareerProfile(
            title = "AI / ML Engineer",
            description = "Develops machine learning models, neural networks, data preprocessing pipelines, and generative AI integrations.",
            requiredSkills = listOf(
                "Python",
                "Data Structures & Algorithms",
                "Linear Algebra & Stats",
                "Machine Learning",
                "Deep Learning (PyTorch/TF)",
                "Data Preprocessing (Pandas/NumPy)",
                "Git & GitHub",
                "Model Deployment (FastAPI/ONNX)"
            ),
            recommendedRoadmap = listOf(
                "Python & DSA Mastery",
                "Applied Mathematics & Stats",
                "Classical Machine Learning",
                "Deep Learning Architectures",
                "LLMs & GenAI Engineering",
                "MLOps & Model Serving"
            )
        ),
        "Mobile App Developer" to CareerProfile(
            title = "Mobile App Developer",
            description = "Architects native and cross-platform mobile apps with modern declarative UI (Jetpack Compose / Flutter), offline storage, and clean architecture.",
            requiredSkills = listOf(
                "Kotlin",
                "Jetpack Compose",
                "Android Architecture (MVVM)",
                "Room Database",
                "Coroutines & Flow",
                "Git & GitHub",
                "REST APIs & Retrofit",
                "Material Design 3"
            ),
            recommendedRoadmap = listOf(
                "Kotlin Fundamentals & OOP",
                "Jetpack Compose UI & M3",
                "State Management & Architecture",
                "Room DB & Offline Cache",
                "Networking & Retrofit",
                "Play Store Release Readiness"
            )
        ),
        "Cloud & DevOps Engineer" to CareerProfile(
            title = "Cloud & DevOps Engineer",
            description = "Automates infrastructure, continuous integration/delivery pipelines, containerization, and cloud resource provisioning.",
            requiredSkills = listOf(
                "Linux Fundamentals",
                "Git & GitHub",
                "Docker & Containers",
                "Kubernetes",
                "CI/CD Pipelines",
                "Cloud Providers (AWS/GCP)",
                "Infrastructure as Code (Terraform)",
                "Bash Scripting"
            ),
            recommendedRoadmap = listOf(
                "Linux Administration & Networking",
                "Scripting with Bash & Python",
                "Docker Containerization",
                "CI/CD with GitHub Actions",
                "Kubernetes Orchestration",
                "Cloud Architecture & Terraform"
            )
        )
    )

    fun calculateSkillGap(
        skills: List<Skill>,
        targetCareerTitle: String
    ): SkillGapResult {
        val career = CAREER_PROFILES[targetCareerTitle]
            ?: CAREER_PROFILES["Full Stack Developer"]!!

        val acquiredSkillNames = skills.map { it.name.trim().lowercase() }
        val mastered = mutableListOf<String>()
        val inProgress = mutableListOf<String>()
        val missing = mutableListOf<String>()
        val evidenceGaps = mutableListOf<String>()

        career.requiredSkills.forEach { req ->
            val matchingSkill = skills.find {
                it.name.trim().equals(req, ignoreCase = true) ||
                        it.name.contains(req, ignoreCase = true) ||
                        req.contains(it.name, ignoreCase = true)
            }

            if (matchingSkill != null) {
                if (matchingSkill.proficiency.equals("Advanced", ignoreCase = true)) {
                    mastered.add(matchingSkill.name)
                } else {
                    inProgress.add(matchingSkill.name)
                }
                // Check evidence depth
                if (!matchingSkill.evidenceProjects && !matchingSkill.evidenceGitHub) {
                    evidenceGaps.add("${matchingSkill.name} (needs project evidence)")
                }
            } else {
                missing.add(req)
            }
        }

        val total = career.requiredSkills.size
        val acquiredCount = mastered.size + inProgress.size
        val readinessPct = if (total > 0) ((acquiredCount.toFloat() / total) * 100).toInt() else 0

        return SkillGapResult(
            targetCareer = career.title,
            totalRequired = total,
            acquiredCount = acquiredCount,
            readinessPercentage = readinessPct,
            masteredSkills = mastered,
            inProgressSkills = inProgress,
            missingSkills = missing,
            evidenceGaps = evidenceGaps
        )
    }

    fun computeNextBestAction(
        profile: StudentProfile?,
        phases: List<RoadmapPhase>,
        skills: List<Skill>,
        projects: List<ProjectRecommendation>,
        opportunities: List<Opportunity>
    ): NextBestAction {
        // Priority 1: In-progress roadmap phase practical task
        val currentPhase = phases.find { it.status == "IN_PROGRESS" }
        if (currentPhase != null) {
            return NextBestAction(
                title = "Complete ${currentPhase.title} Milestone",
                category = "Roadmap Progression",
                description = currentPhase.practicalTask,
                whyItMatters = "Directly satisfies requirements for ${profile?.targetCareer ?: "Target Goal"}. Current progress is awaiting this practical evidence.",
                estimatedTime = "3–4 Hours",
                actionType = ActionType.COMPLETE_ROADMAP_MILESTONE,
                targetEntityId = currentPhase.id
            )
        }

        // Priority 2: Missing high-impact skill in project
        val activeProject = projects.find { !it.isCompleted }
        if (activeProject != null) {
            return NextBestAction(
                title = "Build Feature for ${activeProject.title}",
                category = "Evidence-Based Project",
                description = "Implement next step: ${activeProject.roadmapSteps.split(",").firstOrNull() ?: activeProject.description}",
                whyItMatters = "Builds verified portfolio evidence for ${activeProject.skillsDeveloped.split(",").take(2).joinToString(", ")}.",
                estimatedTime = "2 Hours",
                actionType = ActionType.BUILD_PROJECT_STEP,
                targetEntityId = activeProject.id
            )
        }

        // Priority 3: Matched opportunity with approaching deadline
        val bestOpportunity = opportunities.firstOrNull()
        if (bestOpportunity != null) {
            return NextBestAction(
                title = "Prepare Application: ${bestOpportunity.title}",
                category = "Career Opportunity",
                description = "Review eligibility and prepare portfolio link for ${bestOpportunity.organization}.",
                whyItMatters = "High match rate for your profile. Closes on ${bestOpportunity.deadline}.",
                estimatedTime = "1 Hour",
                actionType = ActionType.APPLY_OPPORTUNITY,
                targetEntityId = bestOpportunity.id
            )
        }

        return NextBestAction(
            title = "Log Daily Skill Practice Session",
            category = "Habit & Consistency",
            description = "Spend 30 minutes practicing Data Structures & Algorithms or reviewing DBMS Normalization notes.",
            whyItMatters = "Consistent daily practice yields compounding growth over academic semesters.",
            estimatedTime = "30 Mins",
            actionType = ActionType.STUDY_SESSION
        )
    }

    fun matchOpportunities(
        skills: List<Skill>,
        opportunities: List<Opportunity>
    ): List<OpportunityMatchResult> {
        val acquiredSkillsLower = skills.map { it.name.trim().lowercase() }

        return opportunities.map { opp ->
            val required = opp.requiredSkills.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val matched = mutableListOf<String>()
            val missing = mutableListOf<String>()

            required.forEach { req ->
                val hasSkill = acquiredSkillsLower.any { acquired ->
                    acquired.contains(req.lowercase()) || req.lowercase().contains(acquired)
                }
                if (hasSkill) matched.add(req) else missing.add(req)
            }

            val score = if (required.isNotEmpty()) {
                ((matched.size.toFloat() / required.size) * 100).toInt()
            } else 80

            OpportunityMatchResult(
                opportunity = opp,
                matchPercentage = score.coerceIn(20, 100),
                matchedSkills = matched,
                missingSkills = missing
            )
        }.sortedByDescending { it.matchPercentage }
    }

    fun computeHabitMetrics(sessions: List<StudySession>): HabitMetrics {
        val totalMins = sessions.sumOf { it.durationMinutes }
        val count = sessions.size

        // Calculate simple streak (e.g. active in past 3 days)
        val now = System.currentTimeMillis()
        val oneDayMillis = 86400000L
        val recentSessions = sessions.filter { (now - it.timestamp) < (oneDayMillis * 7) }
        val streak = (recentSessions.size / 2).coerceAtLeast(1)

        val dsaSession = sessions.find { it.subject.contains("Data Structures", ignoreCase = true) || it.subject.contains("DSA", ignoreCase = true) }
        val dsaDaysAgo = if (dsaSession != null) ((now - dsaSession.timestamp) / oneDayMillis).toInt() else 5

        val adaptiveAlert = if (dsaDaysAgo >= 3) {
            "Notice: You haven't practiced DSA for $dsaDaysAgo days. Recommended: 20-minute session on Binary Trees today."
        } else null

        return HabitMetrics(
            totalMinutesLearned = totalMins,
            sessionsCount = count,
            currentStreakDays = streak,
            adaptiveAlert = adaptiveAlert
        )
    }
}
