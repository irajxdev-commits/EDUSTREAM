package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.InAppVideoPlayerDialog
import com.example.ui.screens.BatchesScreen
import com.example.ui.screens.BhaiyaBaatScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.StudyPlannerScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            MyApplicationTheme(darkTheme = isDarkMode) {
                MainAppContent(
                    viewModel = viewModel,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(
    viewModel: MainViewModel,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val downloadedFiles by viewModel.downloadedFiles.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val activePlayingLecture by viewModel.activePlayingLecture.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("👑", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PADHO BHAI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Raj Bhaiya ke saath",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Telegram community link
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+AyTW4CI3xZpjZWFl"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.testTag("telegram_top_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Telegram Community",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Theme toggle button
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier.testTag("theme_toggle_action")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                // 1. Batches / Courses
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BATCHES || currentScreen == AppScreen.SUBJECT_DETAIL,
                    onClick = { viewModel.navigateTo(AppScreen.BATCHES) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Batches") },
                    label = { Text("Batches", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_batches")
                )

                // 2. Study Planner
                NavigationBarItem(
                    selected = currentScreen == AppScreen.PLANNER,
                    onClick = { viewModel.navigateTo(AppScreen.PLANNER) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Planner") },
                    label = { Text("Planner", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_planner")
                )

                // 3. Offline Downloads / Folders
                NavigationBarItem(
                    selected = currentScreen == AppScreen.DOWNLOADS,
                    onClick = { viewModel.navigateTo(AppScreen.DOWNLOADS) },
                    icon = {
                        if (downloadedFiles.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Badge {
                                        Text("${downloadedFiles.size}")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Folder, contentDescription = "Downloads")
                            }
                        } else {
                            Icon(Icons.Default.Folder, contentDescription = "Downloads")
                        }
                    },
                    label = { Text("Downloads", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_downloads")
                )

                // 4. Bhaiya ki Baat / Mentorship
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BHAIYA_BAAT,
                    onClick = { viewModel.navigateTo(AppScreen.BHAIYA_BAAT) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Mentor") },
                    label = { Text("Bhaiya", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_bhaiya")
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    AppScreen.BATCHES -> BatchesScreen(viewModel = viewModel)
                    AppScreen.SUBJECT_DETAIL -> SubjectDetailScreen(viewModel = viewModel)
                    AppScreen.DOWNLOADS -> DownloadsScreen(viewModel = viewModel)
                    AppScreen.PLANNER -> StudyPlannerScreen(viewModel = viewModel)
                    AppScreen.BHAIYA_BAAT -> BhaiyaBaatScreen(viewModel = viewModel)
                }
            }

            // In-App Video Preview Dialog if user requested in-app preview
            activePlayingLecture?.let { lecture ->
                InAppVideoPlayerDialog(
                    lecture = lecture,
                    onDismiss = { viewModel.setInAppPreviewLecture(null) }
                )
            }
        }
    }
}
