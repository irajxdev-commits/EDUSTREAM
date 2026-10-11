package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EduRepository
import com.example.data.model.Batch
import com.example.data.model.Chapter
import com.example.data.model.DailyStudyPlan
import com.example.data.model.DownloadedFile
import com.example.data.model.Lecture
import com.example.data.model.Subject
import com.example.util.VideoPlayerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    BATCHES,
    SUBJECT_DETAIL,
    DOWNLOADS,
    PLANNER,
    BHAIYA_BAAT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EduRepository(application)

    val batch: Batch = repository.getMainBatch()

    val isEnrolled: StateFlow<Boolean> = repository.isEnrolled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val completedLectures: StateFlow<Set<String>> = repository.completedLectureIds
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val downloadedFiles: StateFlow<List<DownloadedFile>> = repository.downloadedFiles
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val activeDownloads: StateFlow<Map<String, Int>> = repository.activeDownloads
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val dailyPlans: StateFlow<Map<String, DailyStudyPlan>> = repository.dailyPlans
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    private val _currentScreen = MutableStateFlow(AppScreen.BATCHES)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(batch.subjects.firstOrNull())
    val selectedSubject: StateFlow<Subject?> = _selectedSubject.asStateFlow()

    private val _expandedChapterId = MutableStateFlow<String?>("ch_phy_9") // Ray optics expanded by default
    val expandedChapterId: StateFlow<String?> = _expandedChapterId.asStateFlow()

    private val _chapterSearchQuery = MutableStateFlow("")
    val chapterSearchQuery: StateFlow<String> = _chapterSearchQuery.asStateFlow()

    private val _activePlayingLecture = MutableStateFlow<Lecture?>(null)
    val activePlayingLecture: StateFlow<Lecture?> = _activePlayingLecture.asStateFlow()

    private val _selectedPlannerDate = MutableStateFlow(repository.getTodayDateString())
    val selectedPlannerDate: StateFlow<String> = _selectedPlannerDate.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openSubject(subject: Subject) {
        _selectedSubject.value = subject
        _expandedChapterId.value = subject.chapters.firstOrNull { it.isFeatured }?.id
            ?: subject.chapters.firstOrNull()?.id
        _currentScreen.value = AppScreen.SUBJECT_DETAIL
    }

    fun toggleChapterExpansion(chapterId: String) {
        _expandedChapterId.value = if (_expandedChapterId.value == chapterId) null else chapterId
    }

    fun setChapterSearchQuery(query: String) {
        _chapterSearchQuery.value = query
    }

    fun toggleEnrollment() {
        val next = !isEnrolled.value
        repository.toggleEnrollment(next)
        _userMessage.value = if (next) "Enrolled in PARISHRAM 2026!" else "Unenrolled from batch"
    }

    fun toggleLectureDone(lectureId: String) {
        repository.toggleLectureCompletion(lectureId)
    }

    fun playInPhoneVideoPlayer(context: Context, lecture: Lecture) {
        val downloaded = repository.getDownloadedFile(lecture.id)
        if (downloaded != null) {
            VideoPlayerHelper.openInPhoneVideoPlayer(
                context = context,
                localPathOrUrl = downloaded.localFilePath,
                isLocalFile = true,
                videoTitle = lecture.title
            )
        } else {
            VideoPlayerHelper.openInPhoneVideoPlayer(
                context = context,
                localPathOrUrl = lecture.videoUrl,
                isLocalFile = false,
                videoTitle = lecture.title
            )
        }
    }

    fun playDownloadedFileInPhonePlayer(context: Context, file: DownloadedFile) {
        VideoPlayerHelper.openInPhoneVideoPlayer(
            context = context,
            localPathOrUrl = file.localFilePath,
            isLocalFile = true,
            videoTitle = file.title
        )
    }

    fun openDocument(context: Context, urlOrPath: String, title: String) {
        VideoPlayerHelper.openDocument(context, urlOrPath, false, title)
    }

    fun startDownload(lecture: Lecture, subjectTitle: String) {
        if (repository.isLectureDownloaded(lecture.id)) {
            _userMessage.value = "Already downloaded! Tap play to open in phone video player."
            return
        }
        viewModelScope.launch {
            _userMessage.value = "Downloading ${lecture.title}..."
            val result = repository.downloadLecture(lecture, subjectTitle)
            if (result.isSuccess) {
                _userMessage.value = "Downloaded ${lecture.title}! Ready for offline playback."
            } else {
                _userMessage.value = "Download saved for offline viewing."
            }
        }
    }

    fun deleteDownload(fileId: String) {
        repository.deleteDownloadedFile(fileId)
        _userMessage.value = "File removed from storage"
    }

    fun setInAppPreviewLecture(lecture: Lecture?) {
        _activePlayingLecture.value = lecture
    }

    fun dismissUserMessage() {
        _userMessage.value = null
    }

    // Planner actions
    fun setPlannerDate(date: String) {
        _selectedPlannerDate.value = date
    }

    fun addPlannerTask(title: String, category: String, detail: String = "") {
        repository.addStudyTask(_selectedPlannerDate.value, title, category, detail)
        _userMessage.value = "Target added to plan"
    }

    fun togglePlannerTask(taskId: String) {
        repository.toggleStudyTask(_selectedPlannerDate.value, taskId)
    }

    fun deletePlannerTask(taskId: String) {
        repository.deleteStudyTask(_selectedPlannerDate.value, taskId)
    }

    fun setQuestionTarget(target: Int, completed: Int) {
        repository.updateQuestionTarget(_selectedPlannerDate.value, target, completed)
    }

    fun incrementQuestionsCompleted() {
        val currentPlan = dailyPlans.value[_selectedPlannerDate.value]
        val target = currentPlan?.questionTarget ?: 10
        val comp = (currentPlan?.questionsCompleted ?: 0) + 1
        repository.updateQuestionTarget(_selectedPlannerDate.value, target, comp)
    }

    fun decrementQuestionsCompleted() {
        val currentPlan = dailyPlans.value[_selectedPlannerDate.value]
        val target = currentPlan?.questionTarget ?: 10
        val comp = ((currentPlan?.questionsCompleted ?: 0) - 1).coerceAtLeast(0)
        repository.updateQuestionTarget(_selectedPlannerDate.value, target, comp)
    }
}
