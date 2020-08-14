package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.VideoModel
import com.edu.apnipadhai.utils.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_video.*
import kotlinx.android.synthetic.main.item_video.*

class AddAffairFragment : BaseFragment() {

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

        layoutView = inflater.inflate(R.layout.fragment_add_affairs, container, false)
        //saveCategories()
        //fetchData(0)
        //layoutView?.findViewById<View>(R.id.bNext)?.setOnClickListener { }


        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bPreview.setOnClickListener { v -> getVideoTitle(etVideoLink.text.toString()) }
        bSubmit.setOnClickListener { saveVideo() }
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.title_manage_current_affairs))
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
        videoInfo?.timestamp = FieldValue.serverTimestamp().toString()
        val mFirestore = FirebaseFirestore.getInstance();
        val videoList = mFirestore.collection(Const.TABLE_AFFAIRS)
        videoList.add(videoInfo!!).addOnCompleteListener {
            videoInfo = null
            hideLowerContent()
            Utils.showToast(
                context!!,
                getString(R.string.video_uploaded)
            )
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

        tvTitle.text = videoInfo?.name
        tvOther.text = videoInfo?.channel
        GlideApp.with(context!!).load(videoInfo?.thumbnailUrl).placeholder(R.drawable.placeholder)
            .into(ivThumbnail)
    }

    fun hideLowerContent() {
        etVideoLink.setText("")
        bPreview.visibility = View.VISIBLE
        bSubmit.visibility = View.GONE
        rlItemVideo.visibility = View.GONE
    }


    companion object {
        internal val TAG = AddAffairFragment::class.java.simpleName
        /* fun newInstance(item: Category): CategoryFragment {
             val fragment = CategoryFragment()
             fragment.item = item
             return fragment
         }*/

        fun newInstance(): AddAffairFragment {
            return AddAffairFragment()
        }
    }

}
