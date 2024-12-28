package com.nevidimka655.tink_lab.files

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.crypto.tink.integration.android.AndroidKeystore
import com.nevidimka655.astracrypt.core.di.IoDispatcher
import com.nevidimka655.astracrypt.utils.Mapper
import com.nevidimka655.crypto.tink.core.encoders.Base64Util
import com.nevidimka655.tink_lab.work.TinkLabFilesWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STATE_DESTINATION_DIR_URI = "destination_uri"
private const val STATE_DESTINATION_DIR_NAME = "destination_name"
private const val STATE_SOURCE_URI = "uri_source"
private const val STATE_SOURCE_NAME = "source_name"
private const val STATE_ASSOCIATED_DATA = "ad"

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val state: SavedStateHandle,
    @IoDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val workManager: WorkManager,
    private val base64Util: Base64Util,
    private val uriToStringMapper: Mapper<Uri, String>
): ViewModel() {
    private val destinationUriString = state.getStateFlow<String?>(STATE_DESTINATION_DIR_URI, null)
    private val sourceUriString = state.getStateFlow<String?>(STATE_SOURCE_URI, null)
    val destinationDirName = state.getStateFlow(STATE_DESTINATION_DIR_NAME, "")
    val sourceDirName = state.getStateFlow(STATE_SOURCE_NAME, "")
    val associatedData = state.getStateFlow(STATE_ASSOCIATED_DATA, "")

    fun startFilesWorker(
        rawKeyset: String,
        mode: Boolean
    ) = viewModelScope.launch(defaultDispatcher) {
        AndroidKeystore.generateNewAes256GcmKey(TinkLabFilesWorker.ANDROID_KEYSET_ALIAS)
        val dataAead = AndroidKeystore.getAead(TinkLabFilesWorker.ANDROID_KEYSET_ALIAS)
        val workerAD = TinkLabFilesWorker.ASSOCIATED_DATA.toByteArray()
        val encryptedSourceArray = arrayOf(sourceUriString.value!!).map {
            base64Util.encode(dataAead.encrypt(it.toByteArray(), workerAD))
        }.toTypedArray()
        val encryptedTargetUri = base64Util.encode(
            dataAead.encrypt(destinationUriString.value!!.toByteArray(), workerAD)
        )
        val encryptedAssociatedData = base64Util.encode(
            dataAead.encrypt(associatedData.value.toByteArray(), workerAD)
        )
        val encryptedKeyset = base64Util.encode(
            dataAead.encrypt(rawKeyset.toByteArray(), workerAD)
        )
        val data = workDataOf(
            TinkLabFilesWorker.Args.SOURCE_URI_ARRAY to encryptedSourceArray,
            TinkLabFilesWorker.Args.TARGET_URI to encryptedTargetUri,
            TinkLabFilesWorker.Args.ENCRYPTED_AD to encryptedAssociatedData,
            TinkLabFilesWorker.Args.ENCRYPTED_KEYSET to encryptedKeyset,
            TinkLabFilesWorker.Args.MODE to mode
        )
        val workerRequest = OneTimeWorkRequestBuilder<TinkLabFilesWorker>().apply {
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            setInputData(data)
        }.build()
        workManager.enqueue(workerRequest)
    }

    fun setSource(context: Context, uri: Uri) = viewModelScope.launch(defaultDispatcher) {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        state[STATE_SOURCE_URI] = uriToStringMapper(uri)
        state[STATE_SOURCE_NAME] = documentFile?.name ?: ""
    }

    fun setDestinationDir(context: Context, uri: Uri) = viewModelScope.launch(defaultDispatcher) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        state[STATE_DESTINATION_DIR_URI] = uriToStringMapper(uri)
        state[STATE_DESTINATION_DIR_NAME] = documentFile?.name ?: ""
    }

    fun setAssociatedData(data: String) = state.set(STATE_ASSOCIATED_DATA, data)

}