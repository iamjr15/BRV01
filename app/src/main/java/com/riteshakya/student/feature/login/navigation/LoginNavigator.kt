package com.riteshakya.student.feature.login.navigation

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import com.riteshakya.core.navigation.NavigationController
import com.riteshakya.core.navigation.NavigationHelper
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.student.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginNavigator
@Inject constructor(
    private val navigationController: NavigationController
) {
    fun navigateToLogin(baseFragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            baseFragment, NavigationHelper(ActionOnlyNavDirections(R.id.loginFragment), args)
        )
    }

    fun navigateToRoleSelection(baseFragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            baseFragment,
            NavigationHelper(ActionOnlyNavDirections(R.id.roleSelectionFragment), args)
        )
    }

    fun navigateToStudentSignUp(fragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.studentSignUpFragment), args)
        )
    }

    fun navigateToTeacherSignUp(fragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.teacherSignUpFragment), args)
        )
    }

    fun navigateToSchoolSignUp(fragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.schoolSignUpFragment), args)
        )
    }

    fun navigateToPhone(fragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.phoneFragment))
        )
    }

    fun navigateToProfilePicture(fragment: BaseFragment, args: Bundle = Bundle()) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.profilePictureFragment))
        )
    }

    fun navigateToLogoUpload(fragment: BaseFragment) {
        navigationController.navigateTo(
            fragment, NavigationHelper(ActionOnlyNavDirections(R.id.logoUploadFragment))
        )
    }

}