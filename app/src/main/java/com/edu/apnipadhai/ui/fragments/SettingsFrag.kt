package com.edu.apnipadhai.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edu.apnipadhai.R
import com.edu.apnipadhai.callbacks.ListItemClickListener
import com.edu.apnipadhai.model.Setting
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.ui.activity.LoginActivity
import com.edu.apnipadhai.ui.adapter.SettingsAdapter
import com.edu.apnipadhai.utils.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class SettingsFrag : BaseFragment(), ListItemClickListener<Setting> {

    private lateinit var adapter: SettingsAdapter
    private lateinit var rlUserProfile: RelativeLayout
    private lateinit var tvUserName: AppCompatTextView
    private lateinit var civProfile: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_settings, container, false)
        init()
        setRecyclerView()
        return layoutView
    }

    private fun init() {
        rlUserProfile = layoutView!!.findViewById(R.id.rlUserProfile)
        tvUserName = layoutView!!.findViewById(R.id.tvUserName)
        civProfile = layoutView!!.findViewById(R.id.civProfile)
        userInfoFromServer
        rlUserProfile.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            activity!!.startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        val list = ArrayList<Setting>()

        list.add(Setting(R.drawable.bookmark, getString(R.string.bookmrk), Const.COURSE_ITEM))
        list.add(
            Setting(
                R.drawable.notification,
                getString(R.string.notification),
                Const.COURSE_ITEM
            )
        )

        list.add(Setting(R.drawable.share, getString(R.string.shareapp), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.touch, getString(R.string.Subscribe), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.phone, getString(R.string.contact), Const.COURSE_ITEM))
        list.add(Setting(R.drawable.document, getString(R.string.TC), Const.COURSE_HEADER))
        list.add(Setting(R.drawable.exit, getString(R.string.signout), Const.COURSE_HEADER))
        val rvRecords = layoutView?.findViewById<RecyclerView>(R.id.rvSetting)
        adapter = SettingsAdapter(list, this)
        rvRecords?.adapter = adapter
    }

    override fun onItemClick(item: Setting?) {
        when (item?.name) {
            getString(R.string.signout) ->
                signoutDialog()

            getString(R.string.Subscribe) ->
                activity!!.startActivity(getYouTubeIntent())
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
                tvUserName.setText(userModel?.name)
                if (userModel!!.photoUrl != null && "" != userModel.photoUrl) {
                    Glide.with(context!!)
                        .load(
                            FirebaseStorage.getInstance()
                                .getReference("userPhoto/" + userModel.photoUrl)
                        )
                        .into(civProfile)
                }
            }
        }
}