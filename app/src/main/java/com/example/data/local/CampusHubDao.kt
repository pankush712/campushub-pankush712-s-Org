package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Achievement
import com.example.data.model.Opportunity
import com.example.data.model.ProjectRecommendation
import com.example.data.model.ResourceItem
import com.example.data.model.RoadmapPhase
import com.example.data.model.Skill
import com.example.data.model.StudentProfile
import com.example.data.model.StudySession
import kotlinx.coroutines.flow.Flow

@Dao
interface CampusHubDao {

    // Student Profile
    @Query("SELECT * FROM student_profile WHERE id = 1 LIMIT 1")
    fun getStudentProfile(): Flow<StudentProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StudentProfile)

    // Skills
    @Query("SELECT * FROM skills ORDER BY category ASC, name ASC")
    fun getAllSkills(): Flow<List<Skill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: Skill): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<Skill>)

    @Update
    suspend fun updateSkill(skill: Skill)

    @Query("DELETE FROM skills WHERE id = :id")
    suspend fun deleteSkillById(id: Long)

    // Achievements Timeline
    @Query("SELECT * FROM achievements ORDER BY year DESC, id DESC")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<Achievement>)

    @Query("DELETE FROM achievements WHERE id = :id")
    suspend fun deleteAchievementById(id: Long)

    // Roadmap Phases
    @Query("SELECT * FROM roadmap_phases ORDER BY phaseNumber ASC")
    fun getAllRoadmapPhases(): Flow<List<RoadmapPhase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoadmapPhases(phases: List<RoadmapPhase>)

    @Update
    suspend fun updateRoadmapPhase(phase: RoadmapPhase)

    @Query("UPDATE roadmap_phases SET status = :status, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updatePhaseStatus(id: Long, status: String, isCompleted: Boolean)

    // Project Recommendations
    @Query("SELECT * FROM project_recommendations ORDER BY id ASC")
    fun getAllProjects(): Flow<List<ProjectRecommendation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectRecommendation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectRecommendation): Long

    @Update
    suspend fun updateProject(project: ProjectRecommendation)

    // Opportunities
    @Query("SELECT * FROM opportunities ORDER BY id ASC")
    fun getAllOpportunities(): Flow<List<Opportunity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<Opportunity>)

    @Query("UPDATE opportunities SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun updateOpportunityBookmark(id: Long, bookmarked: Boolean)

    // Educational Resources
    @Query("SELECT * FROM resources ORDER BY subject ASC, title ASC")
    fun getAllResources(): Flow<List<ResourceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(resources: List<ResourceItem>)

    @Query("UPDATE resources SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun updateResourceBookmark(id: Long, bookmarked: Boolean)

    // Study Sessions & Habits
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllStudySessions(): Flow<List<StudySession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySession): Long
}
