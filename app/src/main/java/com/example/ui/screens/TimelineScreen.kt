package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Achievement
import com.example.ui.CampusHubUiState
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentRose
import com.example.ui.theme.CampusIndigoPrimary
import com.example.ui.theme.CampusTealSecondary
import com.example.ui.theme.GrowthEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimelineScreen(
    uiState: CampusHubUiState,
    onAddAchievement: (title: String, cat: String, year: Int, date: String, desc: String, skills: String, ev: String, goal: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }
    var showPortfolioDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Hackathon", "Project", "Certificate", "Competition", "Club/Leadership", "Internship")

    val filteredAchievements = if (selectedCategory == "All") {
        uiState.achievements
    } else {
        uiState.achievements.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    // Group by year
    val groupedByYear = filteredAchievements.groupBy { it.year }

    Box(modifier = modifier.fillMaxSize().testTag("timeline_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Growth Timeline",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Long-term chronological verified achievements record.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = { showPortfolioDialog = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Portfolio", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("timeline_category_row")
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CampusIndigoPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            groupedByYear.forEach { (year, achievementsInYear) ->
                item {
                    // Year Marker Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CampusIndigoPrimary,
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text(
                                text = "$year",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        )
                    }
                }

                items(achievementsInYear, key = { it.id }) { item ->
                    TimelineItemCard(achievement = item)
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Add Achievement FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = CampusIndigoPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_achievement_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Achievement")
        }
    }

    if (showAddDialog) {
        AddAchievementDialog(
            defaultGoal = uiState.selectedCareer,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, cat, yr, date, desc, skills, ev, goal ->
                onAddAchievement(title, cat, yr, date, desc, skills, ev, goal)
                showAddDialog = false
            }
        )
    }

    if (showPortfolioDialog) {
        PortfolioPreviewDialog(
            uiState = uiState,
            onDismiss = { showPortfolioDialog = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimelineItemCard(achievement: Achievement) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth().testTag("achievement_card_${achievement.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = getCategoryColor(achievement.category).copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = getCategoryIcon(achievement.category),
                                contentDescription = null,
                                tint = getCategoryColor(achievement.category),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = achievement.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = getCategoryColor(achievement.category)
                        )
                        Text(
                            text = achievement.dateString,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (achievement.associatedGoal.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "Goal: ${achievement.associatedGoal}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            )

            if (achievement.skillsInvolved.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    achievement.skillsInvolved.split(",").forEach { s ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CampusIndigoPrimary.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = s.trim(),
                                fontSize = 10.sp,
                                color = CampusIndigoPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            if (achievement.evidenceUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Evidence link",
                        tint = GrowthEmerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Verified Evidence Proof",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = GrowthEmerald
                    )
                }
            }
        }
    }
}

fun getCategoryIcon(cat: String): ImageVector {
    return when (cat.lowercase()) {
        "hackathon" -> Icons.Default.EmojiEvents
        "competition" -> Icons.Default.Star
        "project" -> Icons.Default.Lightbulb
        "certificate" -> Icons.Default.School
        "internship" -> Icons.Default.Work
        "club/leadership" -> Icons.Default.Group
        else -> Icons.Default.EmojiEvents
    }
}

fun getCategoryColor(cat: String): Color {
    return when (cat.lowercase()) {
        "hackathon" -> AccentAmber
        "competition" -> AccentRose
        "project" -> CampusTealSecondary
        "certificate" -> CampusIndigoPrimary
        "internship" -> GrowthEmerald
        "club/leadership" -> AccentCyan
        else -> CampusIndigoPrimary
    }
}

@Composable
fun AddAchievementDialog(
    defaultGoal: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, cat: String, year: Int, date: String, desc: String, skills: String, ev: String, goal: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Hackathon") }
    var yearStr by remember { mutableStateOf("2026") }
    var dateString by remember { mutableStateOf("April 2026") }
    var description by remember { mutableStateOf("") }
    var skillsInvolved by remember { mutableStateOf("") }
    var evidenceUrl by remember { mutableStateOf("") }
    var associatedGoal by remember { mutableStateOf(defaultGoal) }

    val categories = listOf("Hackathon", "Project", "Certificate", "Competition", "Club/Leadership", "Internship")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Verified Growth Milestone", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (e.g. Hackathon Finalist)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_achievement_title_input"),
                    singleLine = true
                )

                Text("Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = yearStr,
                        onValueChange = { yearStr = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = dateString,
                        onValueChange = { dateString = it },
                        label = { Text("Month / Date") },
                        modifier = Modifier.weight(1.5f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("What did you accomplish?") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = skillsInvolved,
                    onValueChange = { skillsInvolved = it },
                    label = { Text("Skills Involved (e.g. React, Node.js)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = evidenceUrl,
                    onValueChange = { evidenceUrl = it },
                    label = { Text("Evidence / GitHub / Cert Link") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val yr = yearStr.toIntOrNull() ?: 2026
                    onConfirm(title, category, yr, dateString, description, skillsInvolved, evidenceUrl, associatedGoal)
                },
                enabled = title.isNotBlank() && description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CampusIndigoPrimary),
                modifier = Modifier.testTag("submit_achievement_button")
            ) {
                Text("Log Milestone")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun PortfolioPreviewDialog(
    uiState: CampusHubUiState,
    onDismiss: () -> Unit
) {
    val profile = uiState.profile

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = CampusIndigoPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Digital Growth Portfolio", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    color = CampusIndigoPrimary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = profile?.name ?: "Pankush Kumar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${profile?.course} • ${profile?.college}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Target Career: ${uiState.selectedCareer}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CampusIndigoPrimary
                        )
                    }
                }

                Text(
                    text = "VERIFIED GROWTH SUMMARY:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "• ${uiState.achievements.size} Verified Milestones logged since 2024\n" +
                            "• ${uiState.skills.count { it.evidenceProjects }} Evidence-Backed Projects\n" +
                            "• ${uiState.roadmapPhases.count { it.isCompleted }} Roadmap Learning Phases Completed\n" +
                            "• ${uiState.habitMetrics?.totalMinutesLearned ?: 135} Minutes of Focused Problem Solving",
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Text(
                    text = "This verified growth record is ready to export to recruiter portfolios, GitHub READMEs, and LinkedIn posts.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CampusIndigoPrimary)
            ) {
                Text("Done")
            }
        }
    )
}
