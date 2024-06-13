package com.kma.musicplayer.ui.screen.core

import android.Manifest
import android.app.RecoverableSecurityException
import android.os.Build
import android.os.Bundle
import android.os.ConditionVariable
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.kma.musicplayer.utils.FileUtils
import com.kma.musicplayer.utils.PermissionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ActivityHavingDeleteMediaFeature<DB : ViewDataBinding> : BaseActivity<DB>() {

    private val condVarWaitState = ConditionVariable()

    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var requestExternalPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var onSuccessfulDelete: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intentSenderLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if(it.resultCode == RESULT_OK) {
                this.onSuccessfulDelete.invoke()
            }
        }

        requestExternalPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                condVarWaitState.open()
            }
        }
    }

    fun deleteAudioMedia(mediaPath: String, onSuccessfulDelete: () -> Unit) {

        val uri = FileUtils.getAudioMediaContentUri(this, mediaPath) ?: return

        this.onSuccessfulDelete = onSuccessfulDelete

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intentSender =
                MediaStore.createDeleteRequest(contentResolver, listOf(uri)).intentSender
            intentSender.let { sender ->
                intentSenderLauncher.launch(IntentSenderRequest.Builder(sender).build())
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            try {
                contentResolver.delete(uri, null, null)
                onSuccessfulDelete.invoke()
            } catch (e: SecurityException) {
                val recoverableSecurityException = e as? RecoverableSecurityException
                val intentSender =
                    recoverableSecurityException?.userAction?.actionIntent?.intentSender

                intentSender?.let { sender ->
                    intentSenderLauncher.launch(IntentSenderRequest.Builder(sender).build())
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                var hasWriteExternalPermission = true

                if (!PermissionUtils.isWriteExternalStoragePermissionGranted(this@ActivityHavingDeleteMediaFeature)) {
                    requestExternalPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )

                    condVarWaitState.close()
                    hasWriteExternalPermission = condVarWaitState.block(1000) // stop and wait until the permission is granted
                }

                if (hasWriteExternalPermission) {
                    contentResolver.delete(uri, null, null)
                    withContext(Dispatchers.Main) {
                        onSuccessfulDelete.invoke()
                    }
                }
            }
        }
    }
}