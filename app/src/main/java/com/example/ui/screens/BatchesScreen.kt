package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel

@Composable
fun BatchesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isEnrolled by viewModel.isEnrolled.collectAsState()
    val downloadedFiles by viewModel.downloadedFiles.collectAsState()
    val completedLectures by viewModel.completedLectures.collectAsState()
    val batch = viewModel.batch

    val totalLectures = batch.subjects.sumOf { s -> s.chapters.sumOf { c -> c.lectures.size } }
    val progressPercent = if (totalLectures > 0) {
        ((completedLectures.size * 100) / totalLectures).coerceIn(0, 100)
    } else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("batches_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Stat & Quick-Pills Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak Pill
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFE8D6),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color(0xFFF97316),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("7 Days", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Active Streak", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Offline Downloads Pill
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(AppScreen.DOWNLOADS) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("${downloadedFiles.size} Offline", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Video Folders", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // 2. Global-Class Hero Batch Showcase
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_batch_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // Deep Ambient Gradient Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0A1E29),
                                        Color(0xFF0F363F),
                                        Color(0xFF144A47)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFFF59E0B),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = Color(0xFF451A03),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "100% FREE BATCH",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF451A03),
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }

                                Surface(
                                    color = Color.White.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "CLASS 12TH",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "PARISHRAM 2026",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Comprehensive Board & Competitive Preparation",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Faculty Chip
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF10E7A0),
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("RS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF03281E))
                                    }
                                }
                                Text(
                                    text = "Lead Faculty: Rakshak Sir & Senior Faculty",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(alpha = 0.95f)
                                )
                            }
                        }
                    }

                    // Progress & Primary Action Area
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Progress Information
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Course Progress",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${completedLectures.size} of $totalLectures lectures finished",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$progressPercent%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // CTA Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val physics = batch.subjects.firstOrNull { it.key == "physics" }
                                        ?: batch.subjects.first()
                                    viewModel.openSubject(physics)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("study_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEnrolled) "Continue Learning" else "Start Free Class",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            OutlinedButton(
                                onClick = { viewModel.toggleEnrollment() },
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("enroll_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isEnrolled) "Enrolled ✓" else "Enroll",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Featured Master Series Card (Ray Optics 10 Lectures)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val physics = batch.subjects.firstOrNull { it.key == "physics" }
                        if (physics != null) viewModel.openSubject(physics)
                    }
                    .testTag("ray_optics_shortcut"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF59E0B)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF451A03),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "FEATURED CHAPTER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFF59E0B),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Ray Optics & Optical Instruments",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "10 Full Lectures with Notes & DPP · Rakshak Sir",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 4. Section Title: Subjects & Video Folders
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curriculum Library",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Subjects & Lecture Folders",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "${batch.subjects.size} SUBJECTS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 5. Subject Cards (Sleek Modern Design)
        items(batch.subjects.size) { index ->
            val subject = batch.subjects[index]
            val totalLecs = subject.chapters.sumOf { it.lectures.size }
            val totalChapters = subject.chapters.size

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openSubject(subject) }
                    .testTag("subject_card_${subject.key}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Subject Emoji Avatar
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = subject.iconEmoji, fontSize = 26.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = subject.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$totalChapters Chapters · $totalLecs Video Lectures",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Faculty: ${subject.facultyName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { viewModel.openSubject(subject) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("open_subject_${subject.key}")
                    ) {
                        Text("View", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
