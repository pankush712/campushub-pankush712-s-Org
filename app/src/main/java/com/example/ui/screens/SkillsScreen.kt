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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Skill
import com.example.ui.CampusHubUiState
import com.example.ui.components.EvidenceBadgeRow
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentRose
import com.example.ui.theme.CampusIndigoPrimary
import com.example.ui.theme.GrowthEmerald

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsScreen(
    uiState: CampusHubUiState,
    onAddSkill: (name: String, category: String, proficiency: String, courses: Boolean, projects: Boolean, github: Boolean, cert: Boolean, subtopics: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Frontend", "Backend", "Database", "Core CS", "DevOps/Tools", "Soft Skills")

    val filteredSkills = if (selectedCategory == "All") {
        uiState.skills
    } else {
        uiState.skills.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(modifier = modifier.fillMaxSize().testTag("skills_screen")) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Evidence-Based Skills",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Skills verified through coursework, repositories, deployed projects, and assessments.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category filters
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("skill_category_row")
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

            // Skills cards
            items(filteredSkills, key = { it.id }) { skill ->
                SkillItemCard(skill = skill)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // padding for FAB
            }
        }

        // Add Skill FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = CampusIndigoPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_skill_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Skill")
        }
    }

    if (showAddDialog) {
        AddSkillDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, cat, prof, c, p, g, cert, subs ->
                onAddSkill(name, cat, prof, c, p, g, cert, subs)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillItemCard(skill: Skill) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth().testTag("skill_card_${skill.name}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = skill.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        if (skill.isTargetCareerSkill) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CampusIndigoPrimary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "TARGET REQUIREMENT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CampusIndigoPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = skill.category,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (skill.proficiency) {
                        "Advanced" -> GrowthEmerald.copy(alpha = 0.15f)
                        "Intermediate" -> AccentAmber.copy(alpha = 0.15f)
                        else -> AccentCyan.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = skill.proficiency,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (skill.proficiency) {
                            "Advanced" -> GrowthEmerald
                            "Intermediate" -> AccentAmber
                            else -> AccentCyan
                        }
                    )
                }
            }

            if (skill.subtopics.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    skill.subtopics.split(",").forEach { sub ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = sub.trim(),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Evidence Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Evidence Proof:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                EvidenceBadgeRow(
                    hasCourses = skill.evidenceCourses,
                    hasProjects = skill.evidenceProjects,
                    hasGitHub = skill.evidenceGitHub,
                    hasCert = skill.evidenceCert
                )
            }
        }
    }
}

@Composable
fun AddSkillDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, cat: String, prof: String, c: Boolean, p: Boolean, g: Boolean, cert: Boolean, sub: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Frontend") }
    var proficiency by remember { mutableStateOf("Intermediate") }
    var subtopics by remember { mutableStateOf("") }
    var courses by remember { mutableStateOf(true) }
    var projects by remember { mutableStateOf(false) }
    var github by remember { mutableStateOf(false) }
    var cert by remember { mutableStateOf(false) }

    val categories = listOf("Frontend", "Backend", "Database", "Core CS", "DevOps/Tools", "Soft Skills")
    val proficiencies = listOf("Beginner", "Intermediate", "Advanced")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Evidence-Based Skill", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Skill Name (e.g. Docker)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_skill_name_input"),
                    singleLine = true
                )

                Text("Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Text("Proficiency:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    proficiencies.forEach { p ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = proficiency == p,
                                onClick = { proficiency = p }
                            )
                            Text(p, fontSize = 11.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = subtopics,
                    onValueChange = { subtopics = it },
                    label = { Text("Subtopics (e.g. Images, Compose, Volumes)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Evidence Breakdown:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = courses, onCheckedChange = { courses = it })
                        Text("Course", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = projects, onCheckedChange = { projects = it })
                        Text("Project", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = github, onCheckedChange = { github = it })
                        Text("GitHub", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = cert, onCheckedChange = { cert = it })
                        Text("Cert", fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, category, proficiency, courses, projects, github, cert, subtopics)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CampusIndigoPrimary),
                modifier = Modifier.testTag("submit_add_skill_button")
            ) {
                Text("Add Skill")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
