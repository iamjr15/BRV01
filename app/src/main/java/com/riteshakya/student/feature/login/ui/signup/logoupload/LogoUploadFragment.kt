package com.riteshakya.student.feature.login.ui.signup.logoupload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.riteshakya.core.image.PhotoFragment
import com.riteshakya.student.BuildConfig
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import com.riteshakya.student.navigation.Navigator
import com.riteshakya.ui.components.MenuBottomSheet
import com.riteshakya.ui.imageloaders.IImageLoader
import kotlinx.android.synthetic.main.fragment_logo_upload.*
import javax.inject.Inject

class LogoUploadFragment : PhotoFragment() {

    @Inject
    internal lateinit var signUpViewModel: SignUpViewModel

    @Inject
    lateinit var imageLoader: IImageLoader

    @Inject
    lateinit var navigator: LoginNavigator

    @Inject
    internal lateinit var mainNavigator: Navigator
    private lateinit var mediaPickerBottomSheet: MenuBottomSheet

    // Variable to restrict multiple instance of camera or gallery to be opened
    private var bottomSheetActionHandled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_logo_upload, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
        initializeModeChanges()
        initializeMediaPicker()
        imageLoader.loadImage(signUpViewModel.schoolLogo.value ?: "", imageView)
    }
    override fun onDestroy() {
        super.onDestroy()
        StudentApp.instance?.mustDie(this)
    }

    private fun initializeClickListeners() {
        finishBtn.setOnClickListener {
            signUpSchool()
        }
        addImgBtn.setOnClickListener {
            showPhotoOptions()
        }
    }

    private fun signUpSchool() {
        signUpViewModel.signUpSchool()
            .addLoading()
            .subscribe({
                mainNavigator.showMain(context!!)
                activity?.finishAffinity()
            }, {})
            .untilStop()
    }

    private fun initializeModeChanges() {
        signUpViewModel.schoolLogo.observe(this, Observer {
            if (it.isNullOrBlank()) {
                addImgBtn.visibility = VISIBLE
                imageView.visibility = GONE
                finishBtn.isEnabled = false
            } else {
                addImgBtn.visibility = GONE
                imageView.visibility = VISIBLE
                finishBtn.isEnabled = true
            }
        })
    }

    private fun initializeMediaPicker() {
        mediaPickerBottomSheet = MenuBottomSheet.Builder(context!!)
            .setOnMenuSelectedListener(object : MenuBottomSheet.MenuSelectedListener {
                override fun onDismiss() {
                }

                override fun onMenuSelected(id: Int) {
                    if(!bottomSheetActionHandled) {
                        when (id) {
                            R.id.chooseAction -> {
                                photoPickHelper.requestPickPhoto()
                                bottomSheetActionHandled = true
                            }
                            R.id.takeAction -> {
                                photoPickHelper.requestTakePhoto(BuildConfig.APPLICATION_ID)
                                bottomSheetActionHandled = true
                            }
                        }
                    }
                }
            })
            .setTitle("Select")
            .setMenuItems(R.menu.photo_action_menu)
            .create()
    }

    override fun setUpImage(currentPhotoPath: String) {
        signUpViewModel.schoolLogo.value = currentPhotoPath
        imageLoader.loadImage(currentPhotoPath, imageView)
    }

    private fun showPhotoOptions() {
        bottomSheetActionHandled = false
        mediaPickerBottomSheet.show(fragmentManager!!)
    }
}