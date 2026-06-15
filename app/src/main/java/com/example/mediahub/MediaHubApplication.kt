package com.example.mediahub

// Application classes control access to external services
// by creating a central initialization point
import android.app.Application
import com.cloudinary.android.MediaManager

class MediaHubApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // config cloudinary by pointing to the cloudname
        val config = mapOf(
            "cloud_name" to "dqlqmfjkt" // get cloud name from cloudinary
        )
        MediaManager.init(this, config)
    }
}








