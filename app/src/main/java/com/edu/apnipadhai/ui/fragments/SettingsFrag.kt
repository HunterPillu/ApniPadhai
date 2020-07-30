package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.ui.activity.CommonActivity
import com.edu.apnipadhai.ui.adapter.SettingsAdapter
import com.edu.apnipadhai.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SettingsFrag : BaseFragment(), ListItemClickListener<Int, Setting> {

    private lateinit var adapter: SettingsAdapter
    private var userName: String? = null
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_settings, container, false)
        setRecyclerView()
        return layoutView
    }

    private fun setRecyclerView() {
        userInfoFromServer
        val list = ArrayList<Setting>()

        list.add(Setting(0, userName.toString(), Const.SETTING_USER))
        list.add(Setting(R.drawable.bookmark, getString(R.string.bookmrk), Const.COURSE_ITEM))
        list.add(
            Setting(
                R.drawable.notification,
                getString(R.string.notification),
                Const.COURSE_ITEM
            )
        )

        list.add(Setting(R.drawable.ic_share_24, getString(R.string.shareapp), Const.COURSE_ITEM))
        list.add(
            Setting(
                R.drawable.ic_course_24,
                getString(R.string.title_select_course),
                Const.COURSE_ITEM
            )
        )
        list.add(Setting(R.drawable.ic_subscription_24, getString(R.string.Subscribe), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.ic_contacts_24, getString(R.string.contact), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.ic_file_24, getString(R.string.TC), Const.COURSE_HEADER))
        list.add(Setting(R.drawable.ic_exit_24, getString(R.string.signout), Const.COURSE_HEADER))
        val rvRecords = layoutView?.findViewById<RecyclerView>(R.id.rvSetting)
        adapter = SettingsAdapter(list, this)
        rvRecords?.adapter = adapter
    }

    override fun onItemClick(type: Int, item: Setting) {
        when (item.id) {
            R.drawable.ic_exit_24 ->
                signoutDialog()
            R.drawable.touch ->
                startActivity(getYouTubeIntent())
            R.drawable.ic_course_24 ->
                openCourse()
            0 ->
                openScreen(Const.SCREEN_USER)
        }
    }

    val userInfoFromServer: Unit
        get() {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                return
            }
            val uid = user.uid
            val docRef =
                FirebaseFirestore.getInstance().collection("users").document(uid)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val userModel =
                    documentSnapshot.toObject(User::class.java)
                userName = userModel?.name
                if (userModel!!.photoUrl != null && "" != userModel.photoUrl) {
                    url = userModel.photoUrl
                }
                adapter.setUser(userName.toString(), url)
            }
        }


    fun openScreen(type: Int) {
        startActivity(Intent(activity, CommonActivity::class.java).apply {
            putExtra(Const.EXTRA_TYPE, type)
        })
    }

    fun openCourse() {
        startActivity(Intent(activity, CommonActivity::class.java).apply {
            putExtra(Const.EXTRA_TYPE, Const.SCREEN_COURSE)
            putExtra("is_update", true)
        })
    }
}