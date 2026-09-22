package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.engine.ActionType
import com.example.ui.CampusHubViewModel
import com.example.ui.screens.AiAdvisorBottomSheet
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OpportunitiesScreen
import com.example.ui.screens.ResourcesAndHabitsScreen
import com.example.ui.screens.RoadmapScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.TimelineScreen
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.CampusHubTheme
import com.example.ui.theme.CampusIndigoPrimary
import com.example.ui.theme.GrowthEmerald
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: CampusHubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusHubTheme {
                CampusHubApp(viewModel = viewModel)
            }
        }
    }
}

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusHubApp(viewModel: CampusHubViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedScreenIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show notifications from the ViewModel
    LaunchedEffect(uiState.userNotification) {
        uiState.userNotification?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotification()
        }
    }

    val navItems = listOf(
        NavItem("Dashboard", Icons.Default.Home, "nav_dashboard"),
        NavItem("Roadmap", Icons.Default.Route, "nav_roadmap"),
        NavItem("Skills", Icons.Default.Code, "nav_skills"),
        NavItem("Timeline", Icons.Default.EmojiEvents, "nav_timeline"),
        NavItem("Explore", Icons.Default.Work, "nav_opportunities"),
        NavItem("Habits", Icons.Default.School, "nav_resources")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("campushub_main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CampusIndigoPrimary.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_campushub_logo),
                                    contentDescription = "CampusHub Logo",
                                    modifier = Modifier.size(24.dp).clip(CircleShape)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "CampusHub",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Digital Growth Companion",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.requestAiSkillGapAdvice() },
                        modifier = Modifier.testTag("top_bar_ai_button")
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AccentAmber.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI Advisor",
                                    tint = AccentAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedScreenIndex == index,
                        onClick = { selectedScreenIndex = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (selectedScreenIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CampusIndigoPrimary,
                            selectedTextColor = CampusIndigoPrimary,
                            indicatorColor = CampusIndigoPrimary.copy(alpha = 0.15f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedScreenIndex) {
                0 -> DashboardScreen(
                    uiState = uiState,
                    onTakeNextAction = {
                        val action = uiState.nextBestAction
                        if (action != null) {
                            when (action.actionType) {
                                ActionType.COMPLETE_ROADMAP_MILESTONE -> selectedScreenIndex = 1
                                ActionType.BUILD_PROJECT_STEP -> selectedScreenIndex = 4
                                ActionType.APPLY_OPPORTUNITY -> selectedScreenIndex = 4
                                ActionType.PRACTICE_SKILL -> selectedScreenIndex = 2
                                ActionType.STUDY_SESSION -> selectedScreenIndex = 5
                            }
                        }
                    },
                    onExplainSkillGap = { viewModel.requestAiSkillGapAdvice() },
                    onNavigateToRoadmap = { selectedScreenIndex = 1 },
                    onNavigateToTimeline = { selectedScreenIndex = 3 },
                    onNavigateToOpportunities = { selectedScreenIndex = 4 },
                    onToggleMilestone = { viewModel.togglePhaseCompletion(it) },
                    onLogQuickStudy = { selectedScreenIndex = 5 }
                )
                1 -> RoadmapScreen(
                    uiState = uiState,
                    onSelectCareer = { viewModel.selectCareer(it) },
                    onToggleMilestone = { viewModel.togglePhaseCompletion(it) },
                    onAskAiAdvice = { viewModel.requestAiSkillGapAdvice() }
                )
                2 -> SkillsScreen(
                    uiState = uiState,
                    onAddSkill = { name, cat, prof, c, p, g, cert, subs ->
                        viewModel.addSkill(name, cat, prof, c, p, g, cert, subs)
                    }
                )
                3 -> TimelineScreen(
                    uiState = uiState,
                    onAddAchievement = { title, cat, yr, date, desc, skills, ev, goal ->
                        viewModel.addAchievement(title, cat, yr, date, desc, skills, ev, goal)
                    }
                )
                4 -> OpportunitiesScreen(
                    uiState = uiState,
                    onToggleBookmark = { id, current ->
                        viewModel.toggleOpportunityBookmark(id, current)
                    },
                    onCompleteProject = { viewModel.markProjectCompleted(it) },
                    onAskAiProjectBlueprint = { name, skills ->
                        viewModel.requestAiProjectAdvice(name, skills)
                    }
                )
                5 -> ResourcesAndHabitsScreen(
                    uiState = uiState,
                    onToggleResourceBookmark = { id, current ->
                        viewModel.toggleResourceBookmark(id, current)
                    },
                    onLogStudySession = { sub, dur, notes ->
                        viewModel.logStudySession(sub, dur, notes)
                    },
                    onAskAiLinkedIn = { viewModel.requestAiLinkedInAdvice() },
                    onAskAiStudyPlan = { viewModel.requestAiStudyPlanAdvice(it) }
                )
            }

            // AI Advisor Sheet
            AiAdvisorBottomSheet(
                advice = uiState.activeAiAdvice,
                isLoading = uiState.isAiLoading,
                onDismiss = { viewModel.dismissAiAdvice() }
            )
        }
    }
}
