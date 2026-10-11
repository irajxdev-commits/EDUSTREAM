package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Chapter
import com.example.data.model.Lecture
import com.example.ui.AppScreen
import com.example.ui.MainViewModel

@Composable
fun SubjectDetailScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val subject = viewModel.selectedSubject.collectAsState().value
    val expandedChapterId by viewModel.expandedChapterId.collectAsState()
    val searchQuery by viewModel.chapterSearchQuery.collectAsState()
    val completedLectures by viewModel.completedLectures.collectAsState()
    val downloadedFiles by viewModel.downloadedFiles.collectAsState()
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val batch = viewModel.batch

    BackHandler {
        viewModel.navigateTo(AppScreen.BATCHES)
    }

    if (subject == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No subject selected")
        }
        return
    }

    val filteredChapters = subject.chapters.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.lectures.any { l -> l.title.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("subject_detail_screen")
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.BATCHES) },
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Batches",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = subject.iconEmoji, fontSize = 20.sp, modifier = Modifier.padding(end = 6.dp))
                            Text(
                                text = subject.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${subject.chapters.size} Chapters · Faculty: ${subject.facultyName}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Horizontal Subject Switcher Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 4.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    batch.subjects.forEach { s ->
                        val isSelected = s.key == subject.key
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.clickable { viewModel.openSubject(s) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(s.iconEmoji, fontSize = 12.sp, modifier = Modifier.padding(end = 4.dp))
                                Text(
                                    text = s.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setChapterSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("chapter_search_input"),
                    placeholder = { Text("Search chapters or topics...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Chapters List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredChapters) { chapter ->
                val isExpanded = expandedChapterId == chapter.id
                ChapterCardItem(
                    chapter = chapter,
                    subjectTitle = subject.title,
                    isExpanded = isExpanded,
                    onToggleExpand = { viewModel.toggleChapterExpansion(chapter.id) },
                    completedLectures = completedLectures,
                    downloadedFiles = downloadedFiles.map { it.lectureId }.toSet(),
                    activeDownloads = activeDownloads,
                    onPlayInPhonePlayer = { lecture ->
                        viewModel.playInPhoneVideoPlayer(context, lecture)
                    },
                    onInAppPreview = { lecture ->
                        viewModel.setInAppPreviewLecture(lecture)
                    },
                    onDownload = { lecture ->
                        viewModel.startDownload(lecture, subject.title)
                    },
                    onToggleCompleted = { lectureId ->
                        viewModel.toggleLectureDone(lectureId)
                    },
                    onOpenDocument = { url, title ->
                        viewModel.openDocument(context, url, title)
                    }
                )
            }
        }
    }
}

@Composable
fun ChapterCardItem(
    chapter: Chapter,
    subjectTitle: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    completedLectures: Set<String>,
    downloadedFiles: Set<String>,
    activeDownloads: Map<String, Int>,
    onPlayInPhonePlayer: (Lecture) -> Unit,
    onInAppPreview: (Lecture) -> Unit,
    onDownload: (Lecture) -> Unit,
    onToggleCompleted: (String) -> Unit,
    onOpenDocument: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chapter_item_${chapter.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (chapter.isFeatured) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (chapter.isFeatured) 1.5.dp else 1.dp,
            if (chapter.isFeatured) Color(0xFFF59E0B) else MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 3.dp else 1.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (chapter.isFeatured) Color(0xFFF59E0B) else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = String.format("%02d", chapter.number),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (chapter.isFeatured) Color(0xFF451A03) else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chapter.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${chapter.lectures.size} Video Lectures",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "· Notes & DPPs",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (chapter.isFeatured) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF59E0B).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "10 VIDEOS",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFD97706),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expanded content: Lectures List & Notes
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (chapter.lectures.isEmpty()) {
                        Text(
                            text = "Lectures for this chapter are being synchronized.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        chapter.lectures.forEach { lecture ->
                            val isCompleted = completedLectures.contains(lecture.id)
                            val isDownloaded = downloadedFiles.contains(lecture.id)
                            val downloadProgress = activeDownloads[lecture.id]

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Checkbox(
                                            checked = isCompleted,
                                            onCheckedChange = { onToggleCompleted(lecture.id) },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier.size(24.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = lecture.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "${lecture.duration} · ${lecture.faculty}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                if (isDownloaded) {
                                                    Surface(
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = MaterialTheme.colorScheme.primaryContainer
                                                    ) {
                                                        Text(
                                                            text = "OFFLINE ✓",
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Download Progress Bar if in progress
                                    if (downloadProgress != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text("Downloading file...", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                                Text("$downloadProgress%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            LinearProgressIndicator(
                                                progress = { downloadProgress / 100f },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(2.dp)),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Action Buttons: Open in phone video player, preview, download
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Primary action: Open in phone's video player (VLC, MX Player, Photos, System)
                                        Button(
                                            onClick = { onPlayInPhonePlayer(lecture) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                            modifier = Modifier
                                                .weight(1.3f)
                                                .testTag("play_phone_player_${lecture.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.OpenInNew,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Play in Player", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        // In-app player preview
                                        OutlinedButton(
                                            onClick = { onInAppPreview(lecture) },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                            modifier = Modifier.weight(0.9f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Tv,
                                                contentDescription = null,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text("Preview", fontSize = 11.sp)
                                        }

                                        // Download button
                                        OutlinedButton(
                                            onClick = { onDownload(lecture) },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("download_btn_${lecture.id}")
                                        ) {
                                            if (isDownloaded) {
                                                Icon(
                                                    imageVector = Icons.Default.FileDownloadDone,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text("Saved", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.Download,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text("Download", fontSize = 10.sp)
                                            }
                                        }
                                    }

                                    // Material files (Notes & DPP)
                                    if (lecture.notesUrl != null || lecture.dppUrl != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            lecture.notesUrl?.let { notes ->
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                                    modifier = Modifier.clickable { onOpenDocument(notes, "${lecture.title} Notes") }
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.PictureAsPdf,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "Notes PDF",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                }
                                            }
                                            lecture.dppUrl?.let { dpp ->
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                                    modifier = Modifier.clickable { onOpenDocument(dpp, "${lecture.title} DPP") }
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = "📝 DPP Sheet",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = MaterialTheme.colorScheme.secondary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}
