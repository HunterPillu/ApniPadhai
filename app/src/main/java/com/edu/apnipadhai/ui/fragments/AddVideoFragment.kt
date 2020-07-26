package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.bumptech.glide.Glide
import com.covidbeads.app.assesment.util.shortToast
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Category
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.ui.adapter.SpinnerAdapter
import com.edu.apnipadhai.utils.Connectivity
import com.edu.apnipadhai.utils.CustomLog
import com.edu.apnipadhai.utils.Utils
import com.edu.apnipadhai.utils.YouTubeDataEndpoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_video.*
import kotlinx.android.synthetic.main.fragment_add_video.rlVideo
import kotlinx.android.synthetic.main.item_video.*

class AddVideoFragment : BaseFragment() {

    private var list0: ArrayList<Course> = ArrayList()
    private var list1: ArrayList<Course> = ArrayList()
    private var list2: ArrayList<Course> = ArrayList()
    private var selectItem2 = 0
    private var videoInfo: VideoModel? = null
    //var map: HashMap<Int, Course> = HashMap<Int, Course>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (null != layoutView) {
            return layoutView
        }

        layoutView = inflater.inflate(R.layout.fragment_add_video, container, false)
        //saveCategories()
        //fetchData(0)
        layoutView?.findViewById<View>(R.id.bNext)?.setOnClickListener { }

        getCategory(0, 0)

        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bPreview.setOnClickListener { v -> getVideoTitle(etVideoLink.text.toString()) }
        bSubmit.setOnClickListener { saveVideo() }
    }

    fun getCategory(type: Int, parentID: Int?) {
        if (parentID == -1) {
            return
        }
        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->


                val list = ArrayList<Course>()
                /*val header = Course().apply {
                    id = -1
                    name = "Select"
                    parentId = -1
                }*/
                list.add(Course().apply {
                    id = -1
                    name = "Select"
                    parentId = -1
                })
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                    list.add(course)
                }

                if (list.size == 1) {
                    Utils.showToast(context, getString(R.string.admin_no_subcategory))
                    hideSpinners(type)
                    return@addOnSuccessListener
                }

                if (type == 0) {
                    sp0.visibility = View.VISIBLE
                    sp1.visibility = View.GONE
                    sp2.visibility = View.GONE
                    //sp3.visibility = View.GONE
                    hideLowerContent()
                    list0 = list
                    setupZeroSpinner()
                } else if (type == 1) {
                    sp1.visibility = View.VISIBLE
                    sp2.visibility = View.GONE
                    //sp3.visibility = View.GONE
                    hideLowerContent()
                    list1 = list
                    setupFirstSpinner()

                } else if (type == 2) {
                    sp2.visibility = View.VISIBLE
                    //sp3.visibility = View.GONE
                    hideLowerContent()
                    list2 = list
                    setupSecondSpinner()

                } /*else if (type == 3) {
                    sp3.visibility = View.VISIBLE
                    list3 = list
                    setupThirdSpinner()
                }*/ else {
                    CustomLog.d(TAG, "returning")
                    return@addOnSuccessListener
                }

            }
            .addOnFailureListener { exception ->
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }

    private fun hideSpinners(type: Int) {
        if (type == 0) {
            sp0.visibility = View.GONE
        } else if (type == 1) {
            sp1.visibility = View.VISIBLE
        } /*else if (type == 2) {
            sp2.visibility = View.VISIBLE
            rlVideo.visibility = View.GONE
        }*/

        sp2.visibility = View.GONE
        hideLowerContent()
        selectItem2 = 0
        videoInfo = null
    }

    fun setupZeroSpinner() {
        val adapter = SpinnerAdapter(
            context!!,
            list0
        )
        sp0.adapter = adapter
        sp0.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                CustomLog.d(TAG, list0[position].name)
                getCategory(1, list0[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun setupFirstSpinner() {
        val adapter = SpinnerAdapter(
            context!!,
            list1
        )
        sp1.adapter = adapter
        sp1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                CustomLog.d(TAG, list1[position].name)
                getCategory(2, list1[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun setupSecondSpinner() {
        val adapter = SpinnerAdapter(
            context!!,
            list2
        )
        sp2.adapter = adapter
        sp2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                CustomLog.d(TAG, list2[position].name)
                if (list2[position].id == -1) {
                    return
                }
                selectItem2 = list2[position].id

                rlVideo.visibility = View.VISIBLE
                bPreview.visibility = View.VISIBLE

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_add_video))
    }

    fun saveVideo() {
        if (null == videoInfo) {
            CustomLog.d(TAG, "error : videoInfo is null")
            Utils.showToast(
                context!!,
                getString(R.string.invalid_video)
            )
            return
        }
        val mFirestore = FirebaseFirestore.getInstance();
        val videoList = mFirestore.collection("videos")
        videoList.add(videoInfo!!).addOnCompleteListener {
            videoInfo = null
            hideLowerContent()
            Utils.showToast(
                context!!,
                getString(R.string.video_uploaded)
            )
        }
    }

    fun saveCategories() {
        val mFirestore = FirebaseFirestore.getInstance();

        // Get a reference to the restaurants collection

        // Get a reference to the restaurants collection
        val catList = mFirestore.collection("category")

        // Add a new document to the restaurants collection
        catList.add(Category(1, "Grades", 0).toMap())
        catList.add(Category(2, "Exam Preparation", 0).toMap())
        catList.add(Category(3, "6th", 1).toMap())
        catList.add(Category(4, "7th", 1).toMap())
        catList.add(Category(5, "8th", 1).toMap())
        catList.add(Category(6, "9th", 1).toMap())
        catList.add(Category(7, "10th", 1).toMap())
        catList.add(Category(8, "11th Science", 1).toMap())
        catList.add(Category(9, "12th Science", 1).toMap())
        catList.add(Category(10, "11th Commerce", 1).toMap())
        catList.add(Category(11, "12th Commerce", 1).toMap())
        catList.add(Category(12, "UPSC", 2).toMap())
        catList.add(Category(13, "SSC", 2).toMap())
        catList.add(Category(14, "State Level", 2).toMap())
        catList.add(Category(15, "Physics", 8).toMap())
        catList.add(Category(16, "Chemistry", 8).toMap())
        catList.add(Category(17, "Biology", 8).toMap())
        catList.add(Category(18, "English", 8).toMap())
        catList.add(Category(19, "Maths", 8).toMap())
        catList.add(Category(20, "English", 8).toMap())

    }


    fun updateSelectedCourse(courseId: Int) {
        val map: MutableMap<String, Any> = HashMap()
        map["courseId"] = courseId
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid).set(map, SetOptions.merge())

    }

    private fun fetchData(parentID: Int?) {
        CustomLog.e(TAG, "fetchData")

        if (!Connectivity.isConnected(context)) {

            shortToast(getString(R.string.no_internet_connection))
            return
        }



        FirebaseFirestore.getInstance().collection("category")
            .whereEqualTo("parentId", parentID)
            .get()
            .addOnSuccessListener { documents ->

                val list: ArrayList<Course> = ArrayList()
                for (postSnapshot in documents) {
                    val course: Course = postSnapshot.toObject(Course::class.java)
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.w(TAG, "Error getting documents: ${exception.localizedMessage}")
            }
    }

    fun isValid(videoId: String): Boolean {
        return videoId.isEmpty()
    }

    private fun getVideoTitle(videoId: String) {

        if (isValid(videoId)) {
            CustomLog.e(TAG, "Empty video title")
            return
        }

        val observable: Single<VideoModel> =
            YouTubeDataEndpoint.getVideoInfoFromYouTubeDataAPIs(videoId);

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { video ->
                    CustomLog.d(TAG, "VIDEO_INFO " + Gson().toJson(video))
                    videoInfo = video
                    showVideoView()
                },
                { throwable ->
                    throwable.printStackTrace()
                    Utils.showToast(context!!, getString(R.string.invalid_video))
                    CustomLog.e(
                        TAG,
                        "Can't retrieve video title, are you connected to the internet?"
                    )
                })

    }

    fun showVideoView() {
        bPreview.visibility = View.GONE
        bSubmit.visibility = View.VISIBLE
        rlItemVideo.visibility = View.VISIBLE

        videoInfo?.categoryId = "$selectItem2"
        tvTitle.text = videoInfo?.name
        tvOther.text = videoInfo?.channel
        Glide.with(context!!).load(videoInfo?.thumbnailUrl).into(ivThumbnail)
    }

    fun hideLowerContent() {
        rlVideo.visibility = View.GONE
        bPreview.visibility = View.GONE
        bSubmit.visibility = View.GONE
        rlItemVideo.visibility = View.GONE
    }


    companion object {
        internal val TAG = AddVideoFragment::class.java.simpleName
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): AddVideoFragment {
            return AddVideoFragment()
        }
    }

}
