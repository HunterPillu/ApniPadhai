package com.meripadhai.firebase

import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.database.*


enum class MessageType(val value: String) {
    SDPMessage("sdp"),
    ICEMessage("ice"),
    PeerLeft("peer-left");

    override fun toString() = value
}

open class ClientMessage(@get:Exclude val type: MessageType)

data class SDPMessage(var sdp: String = "") : ClientMessage(MessageType.SDPMessage)
data class ICEMessage(var label: Int = 0, var id: String = "", var candidate: String = "") : ClientMessage(MessageType.ICEMessage)
class PeerLeft : ClientMessage(MessageType.PeerLeft)

class FirebaseSignaler(val callerID: String) {

    private val TAG = "FirebaseSignaler"

    private val refToData = FirebaseData.getRoomDataReference(callerID)
    private val refToStatus = FirebaseData.getRoomStatusReference(callerID)
    private val refMyData = FirebaseData.getRoomDataReference(FirebaseData.myID)
    private val refMyStatus = FirebaseData.getRoomStatusReference(FirebaseData.myID)
    var messageHandler: ((ClientMessage) -> Unit)? = null

    private val dataListener: ChildEventListener = object : ChildEventListener {
        fun onMessage(dataSnapshot: DataSnapshot) {
            if (!dataSnapshot.exists()) return
            val type = dataSnapshot.key
            val clientMessage =
                    when (type) {
                        "sdp" ->
                            dataSnapshot.getValue(SDPMessage::class.java)
                        "ice" ->
                            dataSnapshot.getValue(ICEMessage::class.java)
                        else ->
                            null
                    }
            CustomLog.i(TAG, "FirebaseSignaler: Decoded message as ${clientMessage?.type}")
            if (clientMessage != null) messageHandler!!.invoke(clientMessage)
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            onMessage(dataSnapshot)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            onMessage(dataSnapshot)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        }

        override fun onCancelled(e: DatabaseError) {
            CustomLog.e(TAG, "databaseError:", e.toException())
        }
    }

    private val statusListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists() && Const.STATUS_IN_GAME != dataSnapshot.getValue(Int::class.java)!!)
                messageHandler?.invoke(PeerLeft())
        }

        override fun onCancelled(e: DatabaseError) {
            CustomLog.e(TAG, "databaseError:", e.toException())
        }
    }


    fun init() {
        listen()
    }

    private fun listen() {
        refMyData.onDisconnect().removeValue()
        refMyStatus.onDisconnect().setValue(Const.STATUS_IDLE)
        refMyStatus.setValue(Const.STATUS_IN_GAME)
        refToData.addChildEventListener(dataListener)
        refToStatus.addValueEventListener(statusListener)
    }

    private fun send(clientMessage: ClientMessage) {
        val reference = refMyData.child(clientMessage.type.value)
        reference.setValue(clientMessage).addOnCompleteListener {
            if (it.isSuccessful) {
                CustomLog.i(TAG, "FirebaseSignaler: sent succesfully ${clientMessage.type.value}")
//                reference.removeValue()
            } else
                CustomLog.w(TAG, "Message of type '${clientMessage.type.value}' can't be sent to the server")
        }
    }

    fun close() {
        CustomLog.w(TAG, "close")
        refToData.removeEventListener(dataListener)
        refToStatus.removeEventListener(statusListener)
        refMyData.removeValue()
        refMyStatus.setValue(Const.STATUS_IDLE)
        FirebaseData.clearRoomIdReference()
//        webSocket?.close(1000, null)
    }

    fun sendSDP(sdp: String) {
        CustomLog.w(TAG, "sendSDP : " + sdp)
        send(SDPMessage(sdp))
    }

    fun sendCandidate(label: Int, id: String, candidate: String) {
        CustomLog.w(TAG, "sendCandidate :  $label $id $candidate")
        send(ICEMessage(label, id, candidate))
    }
}