package com.meripadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.covidbeads.app.assesment.adapter.UserAdapter
import com.meripadhai.R
import com.meripadhai.callbacks.ListItemClickListener
import com.meripadhai.model.User
import com.meripadhai.utils.Connectivity
import com.meripadhai.utils.Const
import com.meripadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore


class UserListFragment : BaseFragment(), ListItemClickListener<Int, User> {
    private lateinit var adapter: UserAdapter
    //private var swipeRefresh: SwipeRefreshLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_invite_user, container, false)
        setRecyclerView()
        fetchData()
        return layoutView
    }


    private fun setRecyclerView() {
        val rvRecords: RecyclerView? = layoutView?.findViewById<RecyclerView>(R.id.rvList)
        adapter = UserAdapter(context!!, this)
        rvRecords?.adapter = adapter
        //swipeRefresh = layoutView?.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        //swipeRefresh?.setOnRefreshListener(this)
    }


    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_invite))
    }


    private fun fetchData() {
        CustomLog.d(TAG, "fetchData")

        if (!Connectivity.isConnected(context!!)) {
            return
        }

        FirebaseFirestore.getInstance().collection(Const.TABLE_USERS)//.document(FirebaseData.myID)
            .get()
            .addOnSuccessListener { documents ->

                val users: ArrayList<User> = ArrayList()
                for (postSnapshot in documents) {
                    val user: User = postSnapshot.toObject(User::class.java)
                    users.add(user)
                }

                adapter.updateList(users)
            }
            .addOnFailureListener { exception ->
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }

    companion object {
        internal val TAG = UserListFragment::class.java.simpleName
    }

    override fun onItemClick(type: Int, item: User) {
        super.sendGameInvite(item)
    }

}
