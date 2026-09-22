package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CampusHubDatabase
import com.example.data.model.Achievement
import com.example.data.model.Opportunity
import com.example.data.model.ProjectRecommendation
import com.example.data.model.ResourceItem
import com.example.data.model.RoadmapPhase
import com.example.data.model.Skill
import com.example.data.model.StudentProfile
import com.example.data.model.StudySession
import com.example.data.repository.CampusHubRepository
import com.example.engine.AiAdvisorService
import com.example.engine.GrowthEngine
import com.example.engine.HabitMetrics
import com.example.engine.NextBestAction
import com.example.engine.OpportunityMatchResult
import com.example.engine.SkillGapResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CampusHubUiState(
    val profile: StudentProfile? = null,
    val skills: List<Skill> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val roadmapPhases: List<RoadmapPhase> = emptyList(),
    val projects: List<ProjectRecommendation> = emptyList(),
    val opportunities: List<Opportunity> = emptyList(),
    val resources: List<ResourceItem> = emptyList(),
    val studySessions: List<StudySession> = emptyList(),
    val skillGapResult: SkillGapResult? = null,
    val nextBestAction: NextBestAction? = null,
    val matchedOpportunities: List<OpportunityMatchResult> = emptyList(),
    val habitMetrics: HabitMetrics? = null,
    val selectedCareer: String = "Full Stack Developer",
    val activeAiAdvice: AiAdvisorService.AiAdvisorResponse? = null,
    val isAiLoading: Boolean = false,
    val userNotification: String? = null
)

class CampusHubViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CampusHubRepository
    private val _selectedCareer = MutableStateFlow("Full Stack Developer")
    private val _activeAiAdvice = MutableStateFlow<AiAdvisorService.AiAdvisorResponse?>(null)
    private val _isAiLoading = MutableStateFlow(false)
    private val _userNotification = MutableStateFlow<String?>(null)

    init {
        val database = CampusHubDatabase.getInstance(application)
        repository = CampusHubRepository(database.campusHubDao())

        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    val uiState: StateFlow<CampusHubUiState> = combine(
        repository.profile,
        repository.skills,
        repository.achievements,
        repository.roadmapPhases,
        repository.projects,
        repository.opportunities,
        repository.resources,
        repository.studySessions,
        _selectedCareer,
        _activeAiAdvice,
        _isAiLoading,
        _userNotification
    ) { args: Array<Any?> ->
        val profile = args[0] as? StudentProfile
        @Suppress("UNCHECKED_CAST") val skills = (args[1] as? List<Skill>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val achievements = (args[2] as? List<Achievement>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val roadmapPhases = (args[3] as? List<RoadmapPhase>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val projects = (args[4] as? List<ProjectRecommendation>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val opportunities = (args[5] as? List<Opportunity>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val resources = (args[6] as? List<ResourceItem>) ?: emptyList()
        @Suppress("UNCHECKED_CAST") val studySessions = (args[7] as? List<StudySession>) ?: emptyList()
        val career = (args[8] as? String) ?: profile?.targetCareer ?: "Full Stack Developer"
        val aiAdvice = args[9] as? AiAdvisorService.AiAdvisorResponse
        val isAiLoading = (args[10] as? Boolean) ?: false
        val notification = args[11] as? String

        val skillGap = GrowthEngine.calculateSkillGap(skills, career)
        val nextAction = GrowthEngine.computeNextBestAction(profile, roadmapPhases, skills, projects, opportunities)
        val matchedOpps = GrowthEngine.matchOpportunities(skills, opportunities)
        val habitMetrics = GrowthEngine.computeHabitMetrics(studySessions)

        CampusHubUiState(
            profile = profile,
            skills = skills,
            achievements = achievements,
            roadmapPhases = roadmapPhases,
            projects = projects,
            opportunities = opportunities,
            resources = resources,
            studySessions = studySessions,
            skillGapResult = skillGap,
            nextBestAction = nextAction,
            matchedOpportunities = matchedOpps,
            habitMetrics = habitMetrics,
            selectedCareer = career,
            activeAiAdvice = aiAdvice,
            isAiLoading = isAiLoading,
            userNotification = notification
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CampusHubUiState()
    )

    fun selectCareer(career: String) {
        _selectedCareer.value = career
        viewModelScope.launch {
            val current = uiState.value.profile
            if (current != null) {
                repository.updateProfile(current.copy(targetCareer = career))
            }
            _userNotification.value = "Target career updated to $career. Growth engine recalculated."
        }
    }

    fun addSkill(
        name: String,
        category: String,
        proficiency: String,
        hasCourses: Boolean,
        hasProjects: Boolean,
        hasGitHub: Boolean,
        hasCert: Boolean,
        subtopics: String
    ) {
        viewModelScope.launch {
            val skill = Skill(
                name = name.trim(),
                category = category,
                proficiency = proficiency,
                evidenceCourses = hasCourses,
                evidenceProjects = hasProjects,
                evidenceGitHub = hasGitHub,
                evidenceCert = hasCert,
                isTargetCareerSkill = true,
                subtopics = subtopics.trim()
            )
            repository.addSkill(skill)
            _userNotification.value = "Skill '$name' added with verified evidence tags."
        }
    }

    fun addAchievement(
        title: String,
        category: String,
        year: Int,
        dateString: String,
        description: String,
        skillsInvolved: String,
        evidenceUrl: String,
        associatedGoal: String
    ) {
        viewModelScope.launch {
            val achievement = Achievement(
                year = year,
                title = title.trim(),
                category = category,
                dateString = dateString.trim(),
                description = description.trim(),
                skillsInvolved = skillsInvolved.trim(),
                evidenceUrl = evidenceUrl.trim(),
                associatedGoal = associatedGoal.trim()
            )
            repository.addAchievement(achievement)
            _userNotification.value = "Achievement added to your Growth Timeline!"
        }
    }

    fun togglePhaseCompletion(phase: RoadmapPhase) {
        viewModelScope.launch {
            val newCompleted = !phase.isCompleted
            val newStatus = if (newCompleted) "COMPLETED" else "IN_PROGRESS"
            repository.updatePhaseStatus(phase.id, newStatus, newCompleted)
            _userNotification.value = if (newCompleted) {
                "Milestone completed: ${phase.title}! Growth roadmap updated."
            } else {
                "Milestone marked as in progress."
            }
        }
    }

    fun markProjectCompleted(project: ProjectRecommendation) {
        viewModelScope.launch {
            val updated = project.copy(isCompleted = true)
            repository.updateProject(updated)
            // Add automatic verified achievement to timeline!
            repository.addAchievement(
                Achievement(
                    year = 2026,
                    title = "Capstone Project Deployed: ${project.title}",
                    category = "Project",
                    dateString = "September 2026",
                    description = project.description,
                    skillsInvolved = project.skillsDeveloped,
                    evidenceUrl = "https://github.com/pankush-kumar/${project.title.lowercase().replace(" ", "-")}",
                    associatedGoal = uiState.value.selectedCareer
                )
            )
            _userNotification.value = "Project completed! Added to your Growth Timeline & Portfolio."
        }
    }

    fun toggleOpportunityBookmark(id: Long, currentBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleOpportunityBookmark(id, !currentBookmarked)
            _userNotification.value = if (!currentBookmarked) "Opportunity bookmarked!" else "Bookmark removed."
        }
    }

    fun toggleResourceBookmark(id: Long, currentBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleResourceBookmark(id, !currentBookmarked)
            _userNotification.value = if (!currentBookmarked) "Resource saved to library!" else "Resource removed from library."
        }
    }

    fun logStudySession(subject: String, minutes: Int, notes: String) {
        viewModelScope.launch {
            val session = StudySession(
                subject = subject.trim(),
                durationMinutes = minutes,
                timestamp = System.currentTimeMillis(),
                notes = notes.trim()
            )
            repository.logStudySession(session)
            _userNotification.value = "Logged $minutes mins for $subject! Keep the growth momentum."
        }
    }

    fun requestAiSkillGapAdvice() {
        viewModelScope.launch {
            _isAiLoading.value = true
            delay(500) // smooth async feedback
            val gap = uiState.value.skillGapResult
            val profile = uiState.value.profile
            if (gap != null) {
                _activeAiAdvice.value = AiAdvisorService.generateSkillGapAdvice(profile, gap)
            }
            _isAiLoading.value = false
        }
    }

    fun requestAiProjectAdvice(projectName: String, skills: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            delay(400)
            _activeAiAdvice.value = AiAdvisorService.generateProjectRoadmapAdvice(projectName, skills)
            _isAiLoading.value = false
        }
    }

    fun requestAiLinkedInAdvice() {
        viewModelScope.launch {
            _isAiLoading.value = true
            delay(400)
            _activeAiAdvice.value = AiAdvisorService.generateLinkedInChecklist()
            _isAiLoading.value = false
        }
    }

    fun requestAiStudyPlanAdvice(weakAreas: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            delay(400)
            _activeAiAdvice.value = AiAdvisorService.generateStudyPlanAdvice(weakAreas)
            _isAiLoading.value = false
        }
    }

    fun dismissAiAdvice() {
        _activeAiAdvice.value = null
    }

    fun clearNotification() {
        _userNotification.value = null
    }
}
