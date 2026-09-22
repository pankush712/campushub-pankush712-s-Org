package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Skill
import com.example.engine.GrowthEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CampusHub", appName)
  }

  @Test
  fun `verify growth engine skill gap calculation`() {
    val sampleSkills = listOf(
      Skill(name = "HTML / CSS", category = "Frontend", proficiency = "Advanced", evidenceProjects = true),
      Skill(name = "JavaScript", category = "Frontend", proficiency = "Intermediate", evidenceProjects = true),
      Skill(name = "React", category = "Frontend", proficiency = "Intermediate", evidenceProjects = true)
    )

    val gap = GrowthEngine.calculateSkillGap(sampleSkills, "Full Stack Developer")
    assertTrue(gap.acquiredCount >= 3)
    assertTrue(gap.missingSkills.contains("MongoDB") || gap.missingSkills.contains("REST APIs"))
    assertTrue(gap.readinessPercentage > 0)
  }
}
