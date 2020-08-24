package com.edu.apnipadhai.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.edu.apnipadhai.R
import com.edu.apnipadhai.model.User
import com.edu.apnipadhai.ui.activity.CommonActivity
import com.edu.apnipadhai.utils.Const
import com.edu.apnipadhai.utils.GlideApp
import com.edu.apnipadhai.utils.Utils.hideKeyboard
import com.edu.apnipadhai.utils.Utils.showToast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UserFragment : BaseFragment() {
    private var isLoggedIn: Boolean = false
    private var isUpdating: Boolean = false
    private val RC_SIGN_IN = 123
    private var userPhoto: CircleImageView? = null
    private var userName: AppCompatEditText? = null
    private var tvDob: AppCompatTextView? = null

    private var mobile: AppCompatTextView? = null
    private var userModel: User? = null
    private var userPhotoUri: Uri? = null
    private var bSignIn: MaterialButton? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_user, container, false)
        userName = layoutView?.findViewById(R.id.user_name)
        tvDob = layoutView?.findViewById(R.id.dob)
        mobile = layoutView?.findViewById(R.id.mobile)
        userPhoto = layoutView?.findViewById(R.id.user_photo)
        userPhoto?.setOnClickListener(userPhotoIVClickListener)
        bSignIn = layoutView?.findViewById(R.id.saveBtn)
        bSignIn?.setOnClickListener { login() }
        tvDob?.setOnClickListener(dobClickListener)
        layoutView?.findViewById<View>(R.id.ivBack)?.setOnClickListener { onBackPressed() }
        userInfoFromServer
        if (isLoggedIn) {
            updateToolbarTitle(getString(R.string.profile))
            bSignIn?.setText(R.string.update)
        } else {
            updateToolbarTitle(getString(R.string.signin))
            bSignIn?.setText(R.string.signin)
        }


        return layoutView
    }

    val mobileClick = View.OnClickListener {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.logo)
                .setIsSmartLockEnabled(false /* credentials */, true /* hints */)
                .setAvailableProviders(
                    Arrays.asList(
                        AuthUI.IdpConfig.PhoneBuilder().build()
                        // ,AuthUI.IdpConfig.EmailBuilder().build()
                        //, AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                ).build(),
            RC_SIGN_IN
        )
    }


    val userInfoFromServer: Unit
        get() {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                //return if not logged in
                isUpdating = false
                updateToolbarTitle(getString(R.string.signin))
                mobile?.setOnClickListener(mobileClick)
                return
            }
            isUpdating = true
            updateToolbarTitle(getString(R.string.profile))
            val uid = user.uid
            val docRef =
                FirebaseFirestore.getInstance().collection(Const.TABLE_USERS).document(uid)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                userModel =
                    documentSnapshot.toObject(User::class.java)
                //userId?.setText(userModel?.uid)
                userName?.setText(userModel?.name)
                tvDob?.setText(userModel?.dob)
                mobile?.setText(userModel?.mobile)
                if (userModel!!.photoUrl != null && "" != userModel!!.photoUrl) {
                    GlideApp.with(context!!)
                        .load(
                            FirebaseStorage.getInstance()
                                .getReference("userPhoto/" + userModel!!.photoUrl)
                        )
                        .placeholder(R.drawable.ic_user_200)
                        .error(R.drawable.ic_user_200)
                        .into(userPhoto!!)
                }
            }
        }

    var userPhotoIVClickListener =
        View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, PICK_FROM_ALBUM)
        }

    val myCalendar = Calendar.getInstance()

    var dobClickListener =
        View.OnClickListener {
            hideKeyboard(activity!!)
            val picker = DatePickerDialog(
                requireContext(),
                onDatePicked,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            picker.datePicker.maxDate = myCalendar.timeInMillis
            picker.show()
        }

    var onDatePicked =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel()
        }

    private fun updateLabel() {
        val myFormat = "MM-dd-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvDob?.setText(sdf.format(myCalendar.time))
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            userPhoto!!.setImageURI(data!!.data)
            userPhotoUri = data.data
        }

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                val user = User()
                mobile?.setText(FirebaseAuth.getInstance().currentUser?.phoneNumber)

                login()
                /* user.mobile = FirebaseAuth.getInstance().currentUser?.phoneNumber!!
                 FirebaseData.updateUserData(user)

                 val intent = Intent(activity, MainActivity::class.java)
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                 startActivity(intent)*/

                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast(context, getString(R.string.sign_in_cancelled))
                    return
                }

                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showToast(context, getString(R.string.no_internet_connection))
                    return
                }

                if (response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showToast(context, getString(R.string.unknown_error))
                    return
                }
            }

            showToast(context, getString(R.string.unknown_sign_in_response))
        }
    }

    fun login() {
        if (!validateForm()) return
        if (null == userModel) userModel = User()
        userModel?.name = userName!!.text.toString()
        userModel?.dob = tvDob!!.text.toString()
        userModel?.mobile = mobile!!.text.toString()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        if (userPhotoUri != null) {
            userModel!!.photoUrl = uid
        }
        db.collection("users").document(uid)
            .set(userModel!!)
            .addOnSuccessListener {
                showToast(context, getString(R.string.user_created_updated))
                if (userPhotoUri == null) {
                    moveNext()
                } else {
                    // small image
                    GlideApp.with(context!!)
                        .asBitmap()
                        .load(userPhotoUri)
                        .apply(RequestOptions())//.override(150, 150))
                        .into(object : SimpleTarget<Bitmap?>() {
                            override fun onResourceReady(
                                bitmap: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val baos = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                                val data = baos.toByteArray()
                                FirebaseStorage.getInstance().reference
                                    .child("userPhoto/$uid").putBytes(data)
                                moveNext()
                            }
                        })
                }
            }
    }

    private fun moveNext() {

        if (isUpdating) {
            onBackPressed()
        } else {
            startActivity(Intent(activity, CommonActivity::class.java).apply {
                putExtra(Const.EXTRA_TYPE, Const.SCREEN_COURSE)
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }


    }

    private fun validateForm(): Boolean {
        var valid = true
        val nameStr = userName!!.text.toString()
        if (TextUtils.isEmpty(nameStr)) {
            userName!!.error = "Required."
            valid = false
        } else {
            userName!!.error = null
        }
        val msgStr = tvDob!!.text.toString()
        if (TextUtils.isEmpty(msgStr)) {
            tvDob!!.error = "Required."
            valid = false
        } else {
            tvDob!!.error = null
        }

        if (null != FirebaseAuth.getInstance().currentUser) {
            mobile!!.error = null
        } else {
            mobile!!.error = "Required."
            valid = false
        }
        hideKeyboard(activity!!)
        return valid
    }

    companion object {
        fun newInstance(isLoggedIn: Boolean): UserFragment {
            return UserFragment().apply {
                this.isLoggedIn = isLoggedIn
            }
        }

        private const val PICK_FROM_ALBUM = 1
    }
}