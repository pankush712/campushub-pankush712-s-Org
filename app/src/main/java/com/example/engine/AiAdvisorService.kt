package com.example.engine

import com.example.data.model.Skill
import com.example.data.model.StudentProfile

object AiAdvisorService {

    data class AiAdvisorResponse(
        val category: String,
        val summary: String,
        val bulletPoints: List<String>,
        val actionableMilestone: String
    )

    fun generateSkillGapAdvice(
        profile: StudentProfile?,
        gapResult: SkillGapResult
    ): AiAdvisorResponse {
        val target = gapResult.targetCareer
        val missing = gapResult.missingSkills.joinToString(", ")
        val acquired = gapResult.masteredSkills.joinToString(", ")

        return AiAdvisorResponse(
            category = "Career Skill-Gap Remediation",
            summary = "To become an industry-ready $target, your current foundation in $acquired is solid (${gapResult.readinessPercentage}% ready). Your primary bottleneck is in backend & database integration ($missing).",
            bulletPoints = listOf(
                "Focus immediately on RESTful API conventions: master HTTP methods, status codes, and JSON error contracts before moving into full microservices.",
                "Build end-to-end evidence: Rather than isolated tutorials, pair Node.js with MongoDB in one concrete project (like the Student Lost & Found platform).",
                "Address evidence depth: ${gapResult.evidenceGaps.firstOrNull() ?: "Add GitHub repositories with documented READMEs for your current skills"}."
            ),
            actionableMilestone = "Commit 1 hour daily to building 3 CRUD endpoints with authentication middleware."
        )
    }

    fun generateProjectRoadmapAdvice(
        projectName: String,
        skillsDeveloped: String
    ): AiAdvisorResponse {
        return AiAdvisorResponse(
            category = "Project Implementation Blueprint",
            summary = "Blueprint for '$projectName' targeting verified skills: $skillsDeveloped.",
            bulletPoints = listOf(
                "Phase 1: Architecture & UI — Mock wireframes, establish responsive layout components, and define clean client state.",
                "Phase 2: Data Schema & Storage — Model entities with clear relationships and validation rules in MongoDB.",
                "Phase 3: Secure REST Endpoints — Implement password hashing, JWT authorization headers, and input sanitization.",
                "Phase 4: Cloud Integration & Evidence — Connect Cloudinary for asset uploads, deploy to production hosting, and write an engineer-grade README with architecture diagrams."
            ),
            actionableMilestone = "Initialize Git repository, configure environment variables, and commit initial schema design."
        )
    }

    fun generateLinkedInChecklist(): AiAdvisorResponse {
        return AiAdvisorResponse(
            category = "Professional Growth & LinkedIn",
            summary = "A recruiter-optimized LinkedIn profile turns academic achievements into tangible inbound opportunities.",
            bulletPoints = listOf(
                "Headline: Replace 'Student at XYZ' with 'Aspiring Full Stack Engineer | React, Node.js, MongoDB | Building CampusHub & Full-Stack Projects'.",
                "Featured Section: Pin your Top 2 projects with direct demo links and GitHub repo links.",
                "Achievement Storytelling: Post about hackathon participation focusing on challenges overcome and metrics achieved rather than just the certificate.",
                "Skills & Endorsements: Add top 5 core technical skills and request endorsements from project teammates and club leads."
            ),
            actionableMilestone = "Update your LinkedIn About section using our generated impact summary."
        )
    }

    fun generateStudyPlanAdvice(weakAreas: String): AiAdvisorResponse {
        return AiAdvisorResponse(
            category = "Adaptive Study Strategy",
            summary = "Optimized schedule focusing on current academic weak areas: $weakAreas.",
            bulletPoints = listOf(
                "Spaced Repetition: Dedicate Monday/Wednesday/Friday mornings (45 mins) to deep problem-solving in weak areas.",
                "Active Recall: After studying DBMS or OS concepts, write out the mechanism from memory before checking notes.",
                "Peer Teaching: Explain tricky concepts (like concurrency or BCNF normalization) to peers during GDGC study circles."
            ),
            actionableMilestone = "Complete today's 20-minute targeted practice session."
        )
    }
}
