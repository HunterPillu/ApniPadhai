package com.edu.apnipadhai.firebase

import android.os.SystemClock
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId


object FirebaseData {

    //opponent info saved as pair : <UID: USER>
    private lateinit var item:  User

    // my UID : current user id
    var myID: String = ""

    //Firebase room name for game
    const val ROOM = "GameRoom"
    const val USERS = "UserStatus"

    val database = FirebaseDatabase.getInstance()


    //fun getRoomDataPath(id: String) = "$ROOM/$id/data"
    //fun getRoomStatusPath(id: String) = "$ROOM/$id/status"

    fun getRoomDataReference(id: String) = database.getReference("$ROOM/$id/data")
    fun getRoomStatusReference(id: String) = database.getReference("$ROOM/$id/status")
    fun getRoomIdReference(id: String?) = database.getReference("$ROOM/$id/id")
    fun getUserReference(id: String?) = database.getReference("$USERS/$id")


    fun init() {
        if (myID.isNotEmpty()) {
            // Firebase data is already initailzed
            return
        }
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            myID = it.uid
            database.getReference("$USERS/$myID/online").onDisconnect().setValue(false)
            database.getReference("$USERS/$myID/online").setValue(true)

            //todo experimental value "onlineStatus" , it can be used to show last online time
            database.getReference("$USERS/$myID/onlineStatus").onDisconnect().setValue("${SystemClock.currentThreadTimeMillis()}")
            database.getReference("$USERS/$myID/onlineStatus").setValue("Online")
        }
    }

    fun clearRoomIdReference() {
        if (null != item) {
            getRoomIdReference(item?.uid).removeValue()
        }
    }

    fun updateUserData(user: User?) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val newToken: String = instanceIdResult.token
            user?.firebaseToken = newToken
            user?.uid = FirebaseAuth.getInstance().currentUser?.uid!!
            CustomLog.e("newToken", newToken)
            database.getReference("$USERS/${FirebaseAuth.getInstance().uid}").setValue(user)
        }
    }

    fun updateToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val newToken: String = instanceIdResult.token
            CustomLog.e("newToken", newToken)
            database.getReference("$USERS/${FirebaseAuth.getInstance().uid}/firebaseToken").setValue(newToken)
        }

    }

    fun setItem(item: User) {
        this.item=item
    }
}