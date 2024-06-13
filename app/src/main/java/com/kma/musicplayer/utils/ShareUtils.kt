package com.kma.musicplayer.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.widget.Toast
import com.kma.musicplayer.R
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import java.io.File

object ShareUtils {
    fun shareSong(context: Context, song: Song) {
        // Share song
        if (song is OnlineSong) {
            shareOnlineSong(context, song)
        } else {
            shareLocalSong(context, song)
        }
    }

    private fun shareOnlineSong(context: Context, onlineSong: OnlineSong) {
        FileUtils.saveOnlineSongToCacheDir(
            context,
            onlineSong,
            onDownloadSuccess = { file ->
                val uri = FileUtils.getUriFromFile(context, file)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "audio/mp3"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                val chooser = Intent.createChooser(shareIntent, "Share song")

                // Grant permission to all apps that can handle the intent
                val resInfoList: List<ResolveInfo> = context.packageManager
                    .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

                if (shareIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(chooser)
                } else {
                    Toast.makeText(context, context.getString(R.string.no_app_found_to_handle_the_share_action), Toast.LENGTH_SHORT).show()
                }
            },
            onDownloadFailed = {
                Toast.makeText(context, context.getString(R.string.can_t_download_the_song_to_cache), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun shareLocalSong(context: Context, song: Song) {
        val uri = FileUtils.getUriFromFile(context, File(song.path))
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        val chooser = Intent.createChooser(shareIntent, "Share audio file")

        // Grant permission to all apps that can handle the intent
        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        if (shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        } else {
            Toast.makeText(context, context.getString(R.string.no_app_found_to_handle_the_share_action), Toast.LENGTH_SHORT).show()
        }
    }
}