package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@Composable
fun BhaiyaBaatScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("bhaiya_baat_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mentor Greeting Hero
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF091F26),
                                    Color(0xFF133F39),
                                    Color(0xFF0C241E)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF59E0B),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👑", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "RAJ BHAIYA KI TARAF SE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFF59E0B),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Padho Bhai Mentorship Space",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Arre dost,\naaj phir se shuru karte hain.",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Main Raj Bhaiya. Pressure lene nahi, saath chalne aaya hoon. Aaj bas ek chhota target pakad, usse poora kar, phir khud ko shabashi de.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "⭐ Yaad rakh: Speed se zyada zaroori hai wapas aana.",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A)
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Bhaiya ki Baat
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "01 / BHAIYA KI BAAT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tujhe sab kuch aaj hi nahi karna.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kabhi chapter bada lagta hai, kabhi mann nahi karta. Dono normal hain, dost. Padhai ka matlab har din perfect hona nahi; thoda ruk kar, phir apni jagah se dobara shuru karna hai.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "🔥 Comparison band, progress chalu. Tera pace tera hai.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Section 2: Aaj ka Formula
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "02 / AAJ KA FORMULA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Bas itna kar, dost:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    RoutineStepItem("01", "Ek lecture dhyaan se dekh", "Notes mein sirf kaam ki baat likh. Har line copy karne ki zaroorat nahi.")
                    Spacer(modifier = Modifier.height(10.dp))
                    RoutineStepItem("02", "Kuch questions khud solve kar", "Answer dekhne se pehle ek honest try. Galat attempt bhi progress hai.")
                    Spacer(modifier = Modifier.height(10.dp))
                    RoutineStepItem("03", "Kal ka next step likh de", "Chapter, lecture ya revision—subah decide nahi karna padega pehle se ready.")
                }
            }
        }

        // Telegram Community Join Card (Global Grade Gradient)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0088CC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0088CC),
                                    Color(0xFF006699),
                                    Color(0xFF004C73)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Join Our Telegram Community!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Get real-time updates, lecture links & student discussions",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+AyTW4CI3xZpjZWFl"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("join_telegram_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = Color(0xFF0088CC),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Open Telegram Group (Join Now)",
                                color = Color(0xFF0088CC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Phone Video Players Info Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Phone Video Players & Downloads Guide",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Downloaded lectures are saved directly in your phone storage under standard MP4 format.\n2. Tap 'Play in Player' to launch VLC Player, MX Player, or your phone's default video player.\n3. Take advantage of custom speed playback (0.5x to 2x), background audio, and zero-buffering study sessions!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RoutineStepItem(num: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = num,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}
