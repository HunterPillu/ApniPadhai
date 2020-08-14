package com.edu.apnipadhai.callbacks

interface GameEventListener {
    fun onParticipantInvited()
    fun onParticipantJoined()
    fun onParticipantLeave()
    fun onParticipantRejected()
}