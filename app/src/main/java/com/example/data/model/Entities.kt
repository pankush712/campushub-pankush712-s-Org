package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Pankush Kumar",
    val college: String = "Campus Institute of Technology",
    val course: String = "B.Tech Computer Science & Engineering",
    val currentYear: String = "3rd Year (Semester 5)",
    val location: String = "Chandigarh, India",
    val targetCareer: String = "Full Stack Developer",
    val cgpa: Double = 8.64,
    val academicStrengths: String = "Data Structures, Database Management, UI Architecture",
    val weakAreas: String = "System Design, Microservices, Async Queues",
    val linkedInUrl: String = "linkedin.com/in/pankush-kumar",
    val gitHubUrl: String = "github.com/pankush-kumar",
    val portfolioUrl: String = "pankush.dev",
    val leetCodeHandle: String = "pankush_codes"
)

@Entity(tableName = "skills")
data class Skill(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // "Frontend", "Backend", "Database", "DevOps/Tools", "Core CS", "Soft Skills"
    val proficiency: String, // "Beginner", "Intermediate", "Advanced"
    val evidenceCourses: Boolean = false,
    val evidenceProjects: Boolean = false,
    val evidenceGitHub: Boolean = false,
    val evidenceCert: Boolean = false,
    val isTargetCareerSkill: Boolean = false,
    val subtopics: String = "" // comma-separated e.g. "DOM, Async, ES6+"
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val year: Int,
    val title: String,
    val category: String, // "Hackathon", "Internship", "Competition", "Certificate", "Project", "Club/Leadership"
    val dateString: String,
    val description: String,
    val skillsInvolved: String, // comma-separated e.g. "React, Node.js, REST"
    val evidenceUrl: String = "",
    val associatedGoal: String = ""
)

@Entity(tableName = "roadmap_phases")
data class RoadmapPhase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phaseNumber: Int,
    val title: String,
    val description: String,
    val status: String, // "COMPLETED", "IN_PROGRESS", "UPCOMING"
    val conceptsList: String, // comma-separated
    val practicalTask: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "project_recommendations")
data class ProjectRecommendation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val skillsDeveloped: String,
    val roadmapSteps: String, // comma-separated steps
    val isCompleted: Boolean = false,
    val githubUrl: String = ""
)

@Entity(tableName = "opportunities")
data class Opportunity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val organization: String,
    val type: String, // "Hackathon", "Internship", "Scholarship", "Fellowship", "Workshop"
    val deadline: String,
    val requiredSkills: String, // comma-separated
    val description: String,
    val eligibility: String,
    val applyUrl: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "resources")
data class ResourceItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val type: String, // "Notes", "PYQs", "Video Guide", "Documentation", "Practice"
    val url: String,
    val description: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isCompleted: Boolean = true
)

data class CareerProfile(
    val title: String,
    val description: String,
    val requiredSkills: List<String>,
    val recommendedRoadmap: List<String>
)
