package com.meripadhai.callbacks

interface GameEventListener {
    fun onParticipantInvited()
    fun onParticipantJoined()
    fun onParticipantLeave()
    fun onParticipantRejected()
}