package com.edu.apnipadhai.utils


import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlin.random.Random


object FirebaseData {

    private var item: Pair<String, User>? = null
    var myID: String = ""
    const val CALLS = "calls"
    const val CATEGORY = "categories"
    const val VIDEOS = "videos"

    val database = FirebaseDatabase.getInstance()

    fun getCallDataReference(id: String) = database.getReference("${CALLS}/$id/data")
    fun getCallStatusReference(id: String) = database.getReference("${CALLS}/$id/status")
    fun getCallIdReference(id: String?) = database.getReference("${CALLS}/$id/id")
    fun getUserReference(id: String?) = database.getReference("users/$myID")
    fun getCategoryRef() = database.getReference(CATEGORY)
    fun getVideosRef() = database.getReference(VIDEOS)
    fun getVideosRef(categoryId: String?) = database.getReference("users/$myID")

    /*ref.child("messages").on("value", function(snapshot) {
        console.log("There are "+snapshot.numChildren()+" messages");
    })*/

    fun init(): FirebaseData {
        if (myID.isNotEmpty()) {
            // Firebase data is already initailized
            return this
        }
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            myID = it.uid
            database.getReference("users/${myID}/online").onDisconnect().setValue(false)
            database.getReference("users/${myID}/online").setValue(true)
        }
        return this
    }


    fun clearCallIdReference() {
        if (null != item) {
            getCallIdReference(item?.first).removeValue()
        }
    }

    fun setItem(item: Pair<String, User>) {
        this.item = item
    }

    fun updateUserData(user: User?) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val newToken: String = instanceIdResult.getToken()
            user?.firebaseToken = newToken
            user?.online = true
            CustomLog.e("newToken", newToken)
            init().database.getReference("users/${myID}").setValue(user)
        }
    }

    fun updateToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val newToken: String = instanceIdResult.getToken()
            CustomLog.e("newToken", newToken)
            init().database.getReference("users/${myID}/firebaseToken")
                .setValue(newToken)
        }

    }

    /* fun saveCategory() {
         val ref = getCategoryRef()
         ref.push().setValue(Category(1, "All", 0, true))
         ref.push().setValue(Category(2, "Chemistry", 0, true))
         ref.push().setValue(Category(3, "Physics", 0, true))
         ref.push().setValue(Category(4, "Maths", 0, true))
         ref.push().setValue(Category(5, "Biology", 0, true))
         ref.push().setValue(Category(6, "Hindi", 0, true))
         ref.push().setValue(Category(7, "English", 0, true))
     }*/


    fun saveDummyVideos() {
        val s = arrayOf("2Ckzduvbapk", "a_U5aBcWNoQ", "RBojcRbUELw")
        val ref = getVideosRef()
        for (i in 1..50) {
            val video = Video()
            video.id = "$i"
            video.categoryId = "${Random.nextInt(1, 8)}"
            video.videoId = s[Random.nextInt(0, 3)]
            ref.push().setValue(video)
        }
    }

    fun getCategories(callback: ValueEventListener) {
        getCategoryRef().addListenerForSingleValueEvent(callback)
    }

    fun getVideos(categoryId: Int, callback: ValueEventListener) {
        if (categoryId == 1) {
            getVideosRef().addListenerForSingleValueEvent(callback)
        } else {
            getCategoryVideos(categoryId, callback)
        }
    }

    fun getCategoryVideos(categoryId: Int, callback: ValueEventListener) {
        //database.getReference().child(CALLS).orderByChild("").equalTo("$categoryId")
        getVideosRef().orderByChild("categoryId").equalTo("$categoryId")
            .addListenerForSingleValueEvent(callback)
    }
}