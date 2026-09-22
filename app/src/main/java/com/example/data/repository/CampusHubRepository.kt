package com.example.data.repository

import com.example.data.local.CampusHubDao
import com.example.data.local.CampusHubDatabase
import com.example.data.model.Achievement
import com.example.data.model.Opportunity
import com.example.data.model.ProjectRecommendation
import com.example.data.model.ResourceItem
import com.example.data.model.RoadmapPhase
import com.example.data.model.Skill
import com.example.data.model.StudentProfile
import com.example.data.model.StudySession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class CampusHubRepository(private val dao: CampusHubDao) {

    val profile: Flow<StudentProfile?> = dao.getStudentProfile()
    val skills: Flow<List<Skill>> = dao.getAllSkills()
    val achievements: Flow<List<Achievement>> = dao.getAllAchievements()
    val roadmapPhases: Flow<List<RoadmapPhase>> = dao.getAllRoadmapPhases()
    val projects: Flow<List<ProjectRecommendation>> = dao.getAllProjects()
    val opportunities: Flow<List<Opportunity>> = dao.getAllOpportunities()
    val resources: Flow<List<ResourceItem>> = dao.getAllResources()
    val studySessions: Flow<List<StudySession>> = dao.getAllStudySessions()

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        val currentProfile = dao.getStudentProfile().firstOrNull()
        if (currentProfile == null) {
            CampusHubDatabase.populateInitialData(dao)
        }
    }

    suspend fun updateProfile(profile: StudentProfile) = withContext(Dispatchers.IO) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun addSkill(skill: Skill) = withContext(Dispatchers.IO) {
        dao.insertSkill(skill)
    }

    suspend fun updateSkill(skill: Skill) = withContext(Dispatchers.IO) {
        dao.updateSkill(skill)
    }

    suspend fun deleteSkill(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteSkillById(id)
    }

    suspend fun addAchievement(achievement: Achievement) = withContext(Dispatchers.IO) {
        dao.insertAchievement(achievement)
    }

    suspend fun deleteAchievement(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteAchievementById(id)
    }

    suspend fun updatePhaseStatus(id: Long, status: String, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        dao.updatePhaseStatus(id, status, isCompleted)
    }

    suspend fun updateProject(project: ProjectRecommendation) = withContext(Dispatchers.IO) {
        dao.updateProject(project)
    }

    suspend fun toggleOpportunityBookmark(id: Long, isBookmarked: Boolean) = withContext(Dispatchers.IO) {
        dao.updateOpportunityBookmark(id, isBookmarked)
    }

    suspend fun toggleResourceBookmark(id: Long, isBookmarked: Boolean) = withContext(Dispatchers.IO) {
        dao.updateResourceBookmark(id, isBookmarked)
    }

    suspend fun logStudySession(session: StudySession) = withContext(Dispatchers.IO) {
        dao.insertStudySession(session)
    }
}
