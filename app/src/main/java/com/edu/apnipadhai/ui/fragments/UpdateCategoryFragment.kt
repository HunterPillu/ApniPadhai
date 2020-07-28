package com.edu.apnipadhai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.Count
import com.edu.apnipadhai.model.Course
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.CustomLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_update_category.*


class UpdateCategoryFragment : BaseFragment() {

    private lateinit var item: Course
    private lateinit var count: Count

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != layoutView) {
            return layoutView
        }
        layoutView = inflater.inflate(R.layout.fragment_update_category, container, false)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bUpdate.setOnClickListener { updateItem() }

        if (item.id != -1) {
            etName.setText(item.name)
            scActive.isChecked = item.active
        } else {
            fetchAutoIncrementId()
        }
    }

    private fun fetchAutoIncrementId() {
        FirebaseFirestore.getInstance().collection(Const.TABLE_COUNT)
            .get()
            .addOnSuccessListener { documents ->

                for (postSnapshot in documents) {
                    count = postSnapshot.toObject(Count::class.java)
                    count.fKey = postSnapshot.id
                    break
                }
            }
            .addOnFailureListener { exception ->
                CustomLog.w(
                    EditCategoryFragment.TAG,
                    "Error getting documents: ${exception.localizedMessage}"
                )
            }
    }

    private fun updateItem() {
        item.name = etName.text.toString()
        item.active = scActive.isChecked

        if (null != item.fKey) {
            FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY)
                .document(item.fKey!!)
                .set(item.toMap(), SetOptions.merge()).addOnCompleteListener { onBackPressed() }
        } else {
            count.categoryId++
            item.id = count.categoryId
            FirebaseFirestore.getInstance().collection(Const.TABLE_COUNT)
                .document(count.fKey!!)
                .set(count)
            FirebaseFirestore.getInstance().collection(Const.TABLE_CATEGORY).add(item.toMap())
                .addOnCompleteListener { onBackPressed() }

        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbarTitle(getString(R.string.update_category))
    }

    companion object {
        internal val TAG = UpdateCategoryFragment::class.java.simpleName
        fun newInstance(item: Course): UpdateCategoryFragment {
            val fragment = UpdateCategoryFragment()
            fragment.item = item
            return fragment
        }

    }

}