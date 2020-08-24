package com.meripadhai.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.meripadhai.R
import com.meripadhai.callbacks.FragmentEventListener
import com.meripadhai.firebase.FirebaseData
import com.meripadhai.model.User
import com.meripadhai.ui.fragments.BaseFragment
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.meripadhai.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

open class BaseActivity : AppCompatActivity(), FragmentEventListener {
    private val TAG = "BaseActivity"
    private lateinit var callRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //automatic initialize Firebase database for gmae event listener : Game-Invitation
        firebaseInit()
    }

    override fun onBackPressed() {
        val c = supportFragmentManager.backStackEntryCount
        CustomLog.d(TAG, "total entry = $c")
        if (c > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun handleNavigation(ivBack: AppCompatImageView) {
        ivBack.setOnClickListener { onBackPressed() }
    }

    fun showSnackbar(stringRes: Int) {
        Snackbar.make(findViewById(R.id.root)!!, stringRes, Snackbar.LENGTH_LONG).show()
    }

    private fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    override fun openFragment(fragment: BaseFragment) {
        //this.fragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, Integer.toString(getFragmentCount()))
            .addToBackStack(null).commit()
    }

    private fun getFragmentAt(index: Int): BaseFragment? {
        return (if (getFragmentCount() > 0) supportFragmentManager.findFragmentByTag(
            Integer.toString(
                index
            )
        ) as BaseFragment? else null)
    }

    fun getCurrentFragment(): BaseFragment? {
        return getFragmentAt(getFragmentCount() - 1)
    }

    override fun updateToolbarTitle(title: String) {
        //handle this on child class
    }

    //Firebase game listeners

    override fun onInviteOpponent(opponent: User) {
        if (!Const.CAN_REQUEST_IF_OFFLINE && !opponent.online) {
            showSnackbar(R.string.user_offline)
            return
        }
        FirebaseData.setItem(opponent)
        // set my game status to "IN_GAME"
        FirebaseData.getRoomStatusReference(FirebaseData.myID).setValue(Const.STATUS_IN_GAME)

        //set opponent game status to "IDLE"
        FirebaseData.getRoomStatusReference(opponent.uid).setValue(Const.STATUS_IDLE)

        //onDisconnect will be called if activity got destroyed : in that case , remove all game status of opponent
        FirebaseData.getRoomIdReference(opponent.uid).onDisconnect().removeValue()

        //save current user id to opponent's room so that he knows who is inviting him
        FirebaseData.getRoomIdReference(opponent.uid).setValue(FirebaseData.myID)
        //VideoCallActivity.startCall(this, item.first)
    }

    fun receiveVideoCall(key: String) {
        //show invitation dialog
        Utils.showToast(this, "receiveVideoCall $key")
        //VideoCallActivity.receiveCall(this, key)
    }

    /*override fun onStartCallClicked(item: Pair<String, User?>) {
        startVideoCall(item)
    }*/

    override fun onResume() {
        super.onResume()
        callRef.addValueEventListener(callListener)
    }

    override fun onPause() {
        super.onPause()
        callRef.removeEventListener(callListener)

    }

    @SuppressLint("SetTextI18n")
    fun firebaseInit() {
        // initialize Firebase variables
        FirebaseData.init()
        //listen for game invitation
        callRef = FirebaseData.getRoomIdReference(FirebaseData.myID)
    }

    private val callListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                receiveVideoCall(dataSnapshot.getValue(String::class.java)!!)
                callRef.removeValue()
            }
        }

        override fun onCancelled(p0: DatabaseError) {
        }
    }
}