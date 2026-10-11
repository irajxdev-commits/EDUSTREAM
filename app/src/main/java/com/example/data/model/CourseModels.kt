package com.example.data.model

data class Lecture(
    val id: String,
    val number: Int,
    val title: String,
    val chapterName: String,
    val subjectKey: String,
    val faculty: String,
    val duration: String,
    val videoUrl: String,
    val notesUrl: String? = null,
    val dppUrl: String? = null,
    val description: String = ""
)

data class Chapter(
    val id: String,
    val number: Int,
    val name: String,
    val subjectKey: String,
    val lectures: List<Lecture> = emptyList(),
    val notesCount: Int = 1,
    val homeworkCount: Int = 1,
    val isFeatured: Boolean = false
)

data class Subject(
    val key: String,
    val title: String,
    val iconEmoji: String,
    val facultyName: String,
    val chapters: List<Chapter> = emptyList()
)

data class Batch(
    val id: String,
    val title: String,
    val tag: String,
    val subtitle: String,
    val faculty: String,
    val stream: String,
    val isFree: Boolean,
    val subjects: List<Subject> = emptyList()
)

data class DownloadedFile(
    val id: String,
    val lectureId: String,
    val title: String,
    val subjectTitle: String,
    val chapterName: String,
    val localFilePath: String,
    val fileSizeBytes: Long,
    val downloadedAt: Long = System.currentTimeMillis(),
    val isVideo: Boolean = true
)

data class StudyTask(
    val id: String,
    val title: String,
    val category: String, // "LECTURE", "QUESTION", "CHAPTER"
    val isCompleted: Boolean = false,
    val detail: String = ""
)

data class DailyStudyPlan(
    val dateString: String, // "YYYY-MM-DD"
    val tasks: List<StudyTask> = emptyList(),
    val questionTarget: Int = 0,
    val questionsCompleted: Int = 0
)
