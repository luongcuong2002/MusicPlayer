package com.kma.musicplayer.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {

    private val songs = mutableListOf<Song>()

    fun getAllAudios(context: Context): MutableList<Song> {
        if (songs.isNotEmpty()) {
            return songs
        }

        val audios = mutableListOf<Song>()

        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media._ID,
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use { cursor ->
            val pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            while (cursor.moveToNext()) {
                val path = cursor.getString(pathColumnIndex)
                if (File(path).exists()) {
                    audios.add(
                        Song(
                            id = "local-${idColumnIndex}",
                            title = File(path).nameWithoutExtension,
                            artist = null,
                            duration = getMediaDurationInSeconds(context, path).toInt(),
                            path = path
                        )
                    )
                }
            }
        }
        songs.addAll(audios)
        return audios
    }

    private fun getMediaDurationInSeconds(context: Context, path: String): Long {
        try {
            val file = File(path)
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, Uri.fromFile(file))
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillisec = time?.toLong() ?: 0
            retriever.release()
            return timeInMillisec / 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun saveOnlineSongToCacheDir(
        context: Context,
        onlineSong: OnlineSong,
        onDownloadSuccess: (File) -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        // Save online song to cache dir
        val fileName = onlineSong.title + "-" + onlineSong.id + ".mp3"
        saveMp3ToCacheDir(
            context,
            onlineSong.path,
            fileName,
            onDownloadSuccess,
            onDownloadFailed
        )
    }

    fun saveMp3ToCacheDir(
        context: Context,
        url: String,
        fileName: String,
        onDownloadSuccess: (File) -> Unit,
        onDownloadFailed: () -> Unit
    ) {
        try {
            val file = File(context.cacheDir, fileName)
            if (file.exists()) {
                onDownloadSuccess.invoke(file)
                return
            }

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.d("FileUtils", "onFailure: ${e.message}")
                    CoroutineScope(Dispatchers.Main).launch {
                        onDownloadFailed.invoke()
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                    val responseBody = response.body
                    if (responseBody != null) {
                        val inputStream = responseBody.byteStream()
                        val outputStream = FileOutputStream(file)
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        outputStream.close()
                        inputStream.close()
                        CoroutineScope(Dispatchers.Main).launch {
                            onDownloadSuccess.invoke(file)
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onDownloadFailed.invoke()
        }
    }

    fun getUriFromFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }
}