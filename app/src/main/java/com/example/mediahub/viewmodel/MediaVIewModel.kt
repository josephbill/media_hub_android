package com.example.mediahub.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Message
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.mediahub.model.MediaItem
import com.example.mediahub.utils.CloudinaryUploader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID // generate random ids
sealed class MediaState{
    object Idle : MediaState()
    object Loading : MediaState()
    object Success : MediaState()
    data class Error(val message: String) : MediaState()
}
class MediaVIewModel : ViewModel(){
    // reference variables for processes
    // we need to know our logged in user
    private val auth = FirebaseAuth.getInstance()
    // initialize firestore
    private val db = FirebaseFirestore.getInstance()
    // access to public media items in viewmodel
    private val _publicMedia=MutableStateFlow<List<MediaItem>>(
        emptyList()
    )
    // access to public media items in screens using this vm
    val publicMedia: StateFlow<List<MediaItem>> = _publicMedia

    // access to private media items in viewmodel
    private val _myMedia=MutableStateFlow<List<MediaItem>>(
        emptyList()
    )
    // access to private media items in screens using this vm
    val myMedia: StateFlow<List<MediaItem>> = _myMedia

    // access to all media items in viewmodel
    private val _allMedia=MutableStateFlow<List<MediaItem>>(
        emptyList()
    ) // teachers
    // access to all media items in screens using this vm
    val allMedia: StateFlow<List<MediaItem>> = _allMedia

    // status check
    private val _mediaState=MutableStateFlow<MediaState>(
        MediaState.Idle
    )
    val mediaState: StateFlow<MediaState> = _mediaState

    //progress indication
    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    // Load public media items
    fun loadPublicMedia(){}
    // Load users private media items
    fun loadMyMedia(){}
    // Load all media for teachers access /view
    fun loadAllMedia(){}
    // upload new Media
    fun uploadMedia(
        context : Context,
        title : String,
        description : String,
        category : String,
        isPublic : Boolean,
        mediaUri : Uri, // media item path from storage
        ownerName: String
    ){
        // we tag logged in user via their id
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch{
            _mediaState.value = MediaState.Loading
            try{
// 1. upload to cloudinary and get access url
                val mediaUrl = CloudinaryUploader.uploadImage(
                    context = context,
                    imageUri = mediaUri,
    onProgress = {progress -> _uploadProgress.value = progress}
                )
// 2. Save media asset to firestore with the correct url
val mediaItem = MediaItem(
    title = title,
    description = description,
    imageUrl = mediaUrl,
    ownerId = uid,
    ownerName = ownerName,
    category = category,
    isPublic = isPublic
)
            }catch (e: Exception){
_mediaState.value= MediaState.Error(e.message ?: "Upload Fail")
            }
        }
    }
    // update existing media
    fun updateMedia(){}
    // delete existing media
    fun deleteMedia(){}
    //clearstate
    fun clearState(){
        _mediaState.value = MediaState.Idle
    }
}






