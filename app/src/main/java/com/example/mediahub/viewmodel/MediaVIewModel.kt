package com.example.mediahub.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Message
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
    fun loadPublicMedia(){
        viewModelScope.launch {
            try{
// first we retrieve the firestore collection
// filter the data via the isPublic field = true
// order our data by the latest i.e. UploadedAt field
val snapshot = db.collection("media")
    .whereEqualTo("isPublic", true)
    .orderBy("uploadedAt",
        Query.Direction.DESCENDING).get().await()
//now populate the viewmodel reference for public media
// via capturing the snapshot and mapping each record in the
// collection to our MediaItem model
_publicMedia.value = snapshot.documents.map{ doc ->
    doc.toObject(MediaItem::class.java)!!.copy(
        id = doc.id
    )
}

            }catch(e: Exception){
 _mediaState.value = MediaState.Error(e.message ?:
 "Failed to load media items.")
            }
        }
    }
    // Load users private media items
    fun loadMyMedia(){
 val uid = auth.currentUser?.uid ?: return
 viewModelScope.launch {
     try{
         val snapshot=db.collection("media")
             .whereEqualTo("ownerId", uid)
             .orderBy("uploadedAt",
                 Query.Direction.DESCENDING).get()
             .await()
 _myMedia.value = snapshot.documents.map{doc ->
     doc.toObject(MediaItem::class.java)!!.copy(
         id = doc.id
     )
 }
     } catch (e: Exception){
         _mediaState.value = MediaState.Error(e.message ?:
         "Failed to load media items.")
     }

 }
    }
    // Load all media for teachers access /view
    fun loadAllMedia(){
        viewModelScope.launch {
            try{
                val snapshot=db.collection("media")
                    .orderBy("uploadedAt",
                        Query.Direction.DESCENDING).get()
                    .await()
                _allMedia.value = snapshot.documents.map{doc ->
                    doc.toObject(MediaItem::class.java)!!.copy(
                        id = doc.id
                    )
                }
            } catch (e: Exception){
                _mediaState.value = MediaState.Error(e.message ?:
                "Failed to load media items.")
            }
        }
    }
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
             // push our item to firebase 4 storage
             db.collection("media")
                 .add(mediaItem.toMap())
                 .await()
                //change the progress value
                _uploadProgress.value = 0f
                _mediaState.value = MediaState.Success
            }catch (e: Exception){
_mediaState.value= MediaState.Error(e.message ?: "Upload Fail")
            }
        }
    }
    // update existing media
    fun updateMedia(
        mediaId : String,
        title: String,
        description: String,
        isPublic: Boolean
    ){
        viewModelScope.launch{
            _mediaState.value = MediaState.Loading
            try{
db.collection("media")
    .document(mediaId)
    .update(mapOf(
        "title" to title,
        "description" to description,
        "isPublic" to isPublic
    )).await()
    _mediaState.value = MediaState.Success
            } catch (e: Exception){
_mediaState.value = MediaState.Error(e.message ?:
"Update Failed")
            }
        }
    }
    // delete existing media
    fun deleteMedia(item: MediaItem){
        viewModelScope.launch{
            try{
db.collection("media").document(
    item.id
).delete().await()
_mediaState.value  = MediaState.Success
            } catch (e: Exception){
_mediaState.value= MediaState.Error(e.message ?:
"Delete Failed!! ")
            }
        }
    }
    //clearstate
    fun clearState(){
        _mediaState.value = MediaState.Idle
    }
}






