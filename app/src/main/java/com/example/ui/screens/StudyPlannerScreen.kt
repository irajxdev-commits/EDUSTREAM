package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyTask
import com.example.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun StudyPlannerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedPlannerDate.collectAsState()
    val dailyPlans by viewModel.dailyPlans.collectAsState()
    val currentPlan = dailyPlans[selectedDate]

    var newLectureInput by remember { mutableStateOf("") }
    var newChapterInput by remember { mutableStateOf("") }
    var showRules by remember { mutableStateOf(false) }

    val tasks = currentPlan?.tasks ?: emptyList()
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val questionTarget = currentPlan?.questionTarget ?: 10
    val questionsDone = currentPlan?.questionsCompleted ?: 0

    val overallTotal = totalTasks + questionTarget
    val overallDone = completedTasks + questionsDone
    val progressPercent = if (overallTotal > 0) ((overallDone * 100) / overallTotal).coerceIn(0, 100) else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_planner_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DAILY RHYTHM",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Daily Study Planner",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "$overallDone / $overallTotal Done",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // 7-Day Interactive Horizontal Strip
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Calendar Week",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val calendar = Calendar.getInstance()
                        val sdfDay = SimpleDateFormat("EEE", Locale.getDefault())
                        val sdfDate = SimpleDateFormat("dd", Locale.getDefault())
                        val sdfFull = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                        // Center around today
                        calendar.add(Calendar.DAY_OF_YEAR, -2)
                        for (i in 0..6) {
                            val curDate = calendar.time
                            val fullString = sdfFull.format(curDate)
                            val isSelected = fullString == selectedDate
                            val dayLabel = sdfDay.format(curDate)
                            val dateNum = sdfDate.format(curDate)

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .width(46.dp)
                                    .clickable { viewModel.setPlannerDate(fullString) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = dayLabel.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = dateNum,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            calendar.add(Calendar.DAY_OF_YEAR, 1)
                        }
                    }
                }
            }
        }

        // 5 Simple Rules of Focus Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showRules = !showRules },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Padhai Se Pehle — 5 Simple Rules",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (showRules) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    AnimatedVisibility(visible = showRules) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RuleStepRow("1", "Baitho", "Ek quiet jagah par comfortably baith jao.")
                            RuleStepRow("2", "Eyes Close", "Normal breath lo, 2 deep calm breaths repeat karo.")
                            RuleStepRow("3", "Focus Mode ON", "Multitasking band, sirf ek lecture par focus.")
                            RuleStepRow("4", "Strategic Breaks", "Break lo, par Shorts/Reels scroll mat karo!")
                            RuleStepRow("5", "Refresh & Return", "Paani piyo, stretch karo, phir target poora karo.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "✨ Pehle mind ko calm karo, phir mind ko kaam do. ❤️‍🔥",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Daily Target Progress Ring Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Today's Target Completion",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$overallDone of $overallTotal goals achieved",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$progressPercent%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Question Practice Target Stepper
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Question Practice Target",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$questionsDone of $questionTarget questions solved",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.decrementQuestionsCompleted() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = "$questionsDone",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.incrementQuestionsCompleted() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Lecture Checklist Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lecture Checklist",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val lectureDone = tasks.count { it.category == "LECTURE" && it.isCompleted }
                    val lectureTotal = tasks.count { it.category == "LECTURE" }
                    Text(
                        text = "$lectureDone of $lectureTotal complete",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newLectureInput,
                        onValueChange = { newLectureInput = it },
                        modifier = Modifier.weight(1f).testTag("new_lecture_input"),
                        placeholder = { Text("Add a lecture (e.g. Ray Optics Lec 04)", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newLectureInput.isNotBlank()) {
                                viewModel.addPlannerTask(newLectureInput.trim(), "LECTURE")
                                newLectureInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_lecture_btn")
                    ) {
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val lectureTasks = tasks.filter { it.category == "LECTURE" }
        if (lectureTasks.isEmpty()) {
            item {
                Text(
                    text = "No lectures added yet for this date.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(lectureTasks) { task ->
                TaskRowItem(
                    task = task,
                    onToggle = { viewModel.togglePlannerTask(task.id) },
                    onDelete = { viewModel.deletePlannerTask(task.id) }
                )
            }
        }

        // Chapter Goals Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chapter Goals",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val chapterDone = tasks.count { it.category == "CHAPTER" && it.isCompleted }
                    val chapterTotal = tasks.count { it.category == "CHAPTER" }
                    Text(
                        text = "$chapterDone of $chapterTotal complete",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newChapterInput,
                        onValueChange = { newChapterInput = it },
                        modifier = Modifier.weight(1f).testTag("new_chapter_input"),
                        placeholder = { Text("Add chapter revision target", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newChapterInput.isNotBlank()) {
                                viewModel.addPlannerTask(newChapterInput.trim(), "CHAPTER")
                                newChapterInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("add_chapter_btn")
                    ) {
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val chapterTasks = tasks.filter { it.category == "CHAPTER" }
        if (chapterTasks.isEmpty()) {
            item {
                Text(
                    text = "No chapter targets added yet.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(chapterTasks) { task ->
                TaskRowItem(
                    task = task,
                    onToggle = { viewModel.togglePlannerTask(task.id) },
                    onDelete = { viewModel.deletePlannerTask(task.id) }
                )
            }
        }
    }
}

@Composable
fun RuleStepRow(num: String, title: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(22.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = num,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = detail,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun TaskRowItem(
    task: StudyTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.detail.isNotEmpty()) {
                    Text(
                        text = task.detail,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
