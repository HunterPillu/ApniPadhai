package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_admin.*


class AdminFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_admin, container, false)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bAddCategory.setOnClickListener { openFragment(EditCategoryFragment.newInstance()) }
        bAddVideos.setOnClickListener { openFragment(AddVideoFragment.newInstance()) }
        bSyncCount.setOnClickListener { updateVideoCount() /*updateUserUid()*/ }
        bAddAffairs.setOnClickListener { openFragment(AddAffairFragment.newInstance()) }
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_admin))
    }

    fun showLoader() {
        bSyncCount?.visibility = View.GONE
        pb?.visibility = View.VISIBLE
    }

    fun hideLoader() {
        pb?.visibility = View.GONE
        bSyncCount?.visibility = View.VISIBLE

    }


    private fun updateVideoCount() {
        //val parentID = PrefUtil.getCourseId(context!!)

        if (!Connectivity.isConnected(context!!)) {
            shortToast(getString(R.string.no_internet_connection))
            return
        }

        showLoader()

        // val itemId = "${item.id}"
        //CustomLog.d(VideoFragment.TAG, "crse : itemId = $itemId")
        FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
            // .whereEqualTo("categoryId", itemId)//"${item.id}")
            .get()
            .addOnSuccessListener { documents ->

                // val list = ArrayList<VideoModel>()
                val map = HashMap<Int, Int>()

                for (postSnapshot in documents) {

                    val model: VideoModel = postSnapshot.toObject(VideoModel::class.java)
                    //course.fKey = postSnapshot.id
                    val id = model.categoryId.toInt()
                    if (map.containsKey(id)) {
                        map[id] = map[id]!! + 1
                    } else {
                        map[id] = 1
                    }

                    if (null == model.fKey || model.courseId == 0) {
                        model.fKey = postSnapshot.id
                        updateVideoModel(model)
                    }
                    // list.add(model)
                }

                updateVideoCount(map)

            }
            .addOnFailureListener { exception ->
                hideLoader()
                CustomLog.e(
                    VideoFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }

    private fun updateVideoCount(map: HashMap<Int, Int>) {

        for (item in map) {
            updateCount(item.key, item.value)
        }
    }

    private fun updateCount(key: Int, value: Int) {
        // var parentID = 0
        /*if (!key.isEmpty()) {
            parentID = key
        }*/

        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("id", key)
            .get()
            .addOnSuccessListener { documents ->


                var course: Course
                for (postSnapshot in documents) {
                    course = postSnapshot.toObject(Course::class.java)
                    course.fKey = postSnapshot.id
                    course.videoCount = value
                    //list1.add(course)
                    FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
                        .document(postSnapshot.id).set(course)
                        .addOnSuccessListener { updateParentCourseCount(course.parentId) }

                }
                hideLoader()
            }
            .addOnFailureListener { exception ->
                hideLoader()
                CustomLog.w(
                    EditCategoryFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }

    private fun updateVideoModel(value: VideoModel) {

        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("id", value.categoryId.toInt())
            .get()
            .addOnSuccessListener { documents ->
                //var course: Course
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                    //course.videoCount = value
                    //list1.add(course)
                    value.courseId = course.parentId
                    FirebaseFirestore.getInstance().collection(Const.TABLE_VIDEOS)
                        .document(value.fKey!!).set(value)


                }
                hideLoader()
            }
            .addOnFailureListener { exception ->
                hideLoader()
                CustomLog.w(
                    EditCategoryFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }

    private fun updateUserUid() {

        FirebaseFirestore.getInstance().collection(Const.TABLE_USERS)
            // .whereEqualTo("id", value.categoryId.toInt())
            .get()
            .addOnSuccessListener { documents ->
                //var course: Course
                for (postSnapshot in documents) {
                    val user: User = postSnapshot.toObject(User::class.java)
                    //course.videoCount = value
                    //list1.add(course)
                    user.uid = postSnapshot.id
                    FirebaseFirestore.getInstance().collection(Const.TABLE_USERS)
                        .document(user.uid).set(user)


                }
                hideLoader()
            }
            .addOnFailureListener { exception ->
                hideLoader()
                CustomLog.w(
                    EditCategoryFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }


    private fun updateParentCourseCount(parentId: Int) {
        // var parentID = 0
        /*if (!key.isEmpty()) {
            parentID = key
        }*/

        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("parentId", parentId)
            .get()
            .addOnSuccessListener { documents ->


                val map = HashMap<Int, Int>()

                for (postSnapshot in documents) {

                    val model: Course = postSnapshot.toObject(Course::class.java)
                    //course.fKey = postSnapshot.id
                    if (map.containsKey(model.parentId)) {
                        map[model.parentId] = map[model.parentId]!! + model.videoCount
                    } else {
                        map[model.parentId] = model.videoCount
                    }
                    //list.add(model)
                }

                updateVideoCount(map)

            }
            .addOnFailureListener { exception ->
                hideLoader()
                CustomLog.w(
                    EditCategoryFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }


    companion object {
        internal val TAG = AdminFragment::class.java.simpleName

        fun newInstance(): AdminFragment {
            return AdminFragment()
        }
    }
}