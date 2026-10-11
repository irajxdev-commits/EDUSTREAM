package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object VideoPlayerHelper {

    /**
     * Opens a local downloaded video file or streaming URL in any video player app
     * available on the phone (VLC, MX Player, Google Photos, default video player, etc.).
     */
    fun openInPhoneVideoPlayer(
        context: Context,
        localPathOrUrl: String,
        isLocalFile: Boolean,
        videoTitle: String
    ) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri = if (isLocalFile) {
                val file = File(localPathOrUrl)
                if (!file.exists()) {
                    Toast.makeText(context, "Video file not found: $videoTitle", Toast.LENGTH_SHORT).show()
                    return
                }
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.parse(localPathOrUrl)
            }

            intent.setDataAndType(uri, "video/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val chooser = Intent.createChooser(intent, "Open video with...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Unable to open video player: ${e.localizedMessage ?: "Unknown error"}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Opens PDF notes, study material, or DPP files in phone's PDF reader or web viewer.
     */
    fun openDocument(
        context: Context,
        localPathOrUrl: String,
        isLocalFile: Boolean,
        title: String
    ) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri = if (isLocalFile) {
                val file = File(localPathOrUrl)
                if (!file.exists()) {
                    Toast.makeText(context, "Document not found: $title", Toast.LENGTH_SHORT).show()
                    return
                }
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } else {
                Uri.parse(localPathOrUrl)
            }

            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val chooser = Intent.createChooser(intent, "Open document with...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(localPathOrUrl))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(browserIntent)
            } catch (_: Exception) {
                Toast.makeText(context, "No PDF reader found on phone", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
