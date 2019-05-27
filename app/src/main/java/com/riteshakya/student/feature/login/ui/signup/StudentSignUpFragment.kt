package com.riteshakya.student.feature.login.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.businesslogic.repository.student.model.FEMALE
import com.riteshakya.businesslogic.repository.student.model.MALE
import com.riteshakya.businesslogic.repository.student.model.NOT_SELECTED
import com.riteshakya.businesslogic.repository.student.model.ParentModel
import com.riteshakya.core.extension.hideKeyboard
import com.riteshakya.core.extension.showError
import com.riteshakya.core.model.STUDENT
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.validation.types.NameValidation
import com.riteshakya.core.validation.types.PasswordValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import com.riteshakya.ui.components.SpinnerAdapter
import kotlinx.android.synthetic.main.fragment_student_sign_up.*
import javax.inject.Inject


class StudentSignUpFragment : BaseFragment() {
    @Inject
    internal lateinit var navigator: LoginNavigator

    @Inject
    internal lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_student_sign_up, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeValidators()
        initializeVm()
        initializeSchools()
        addListeners()

        // Hide extra tab
        (genderSwitch.getChildAt(0) as ViewGroup).getChildAt(0).visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        StudentApp.instance?.mustDie(this)
    }
    private fun initializeValidators() {
        addValidationList(firstNameTxt.addValidity(NameValidation(), {
            signUpViewModel.firstName.value = it
        }))
        addValidationList(lastNameTxt.addValidity(NameValidation(), {
            signUpViewModel.lastName.value = it
        }))
        addValidationList(schoolSelect.addValidity {
            signUpViewModel.school.value = it
        })
        addValidationList(parentFirstNameTxt.addValidity(NameValidation(), {
            if (signUpViewModel.parent.value == null)
                signUpViewModel.parent.value =
                    ParentModel()

            signUpViewModel.parent.value?.firstName = it
        }))
        addValidationList(parentLastNameTxt.addValidity(NameValidation(), {
            if (signUpViewModel.parent.value == null)
                signUpViewModel.parent.value =
                    ParentModel()

            signUpViewModel.parent.value?.lastName = it
        }))
        addValidationList(passwordTxt.addValidity(PasswordValidation(), {
            signUpViewModel.password.value = it
        }))
    }

    private fun initializeVm() {
        firstNameTxt.setText(signUpViewModel.firstName.value)
        lastNameTxt.setText(signUpViewModel.lastName.value)
        schoolSelect.setSelection(signUpViewModel.school.value)
        classSelect.setSelection(signUpViewModel.classValue.value)
        sectionSelect.setSelection(signUpViewModel.sectionValue.value)
        passwordTxt.setText(signUpViewModel.password.value)
    }

    private fun initializeSchools() {
        signUpViewModel.schools
            .addLoading()
            .subscribe {
                schoolSelect.items =
                    it.map { school ->
                        SpinnerAdapter.SpinnerModel(
                            school.id, school.schoolName, school.schoolLogo
                        )
                    }
            }
            .untilStop()
    }

    private fun addListeners() {
        genderSwitch.addOnTabSelectedListener {
            signUpViewModel.gender.value =
                when (it) {
                    0 -> NOT_SELECTED
                    1 -> MALE
                    else -> FEMALE
                }
        }
        classSelect.onItemChanged {
            signUpViewModel.classValue.value = it
        }
        sectionSelect.onItemChanged {
            signUpViewModel.sectionValue.value = it
        }

        nextBtn.setOnClickListener {
            when {
                genderSwitch.selectedTabPosition == 0 -> {
                    activity?.hideKeyboard()
                    (activity as BaseActivity).showError(
                        getString(com.riteshakya.ui.R.string.error_select_gender)
                    )
                }
                classSelect.selectedItemPosition == 0 -> {
                    activity?.hideKeyboard()
                    (activity as BaseActivity).showError(
                        getString(com.riteshakya.ui.R.string.error_select_class)
                    )
                }
                sectionSelect.selectedItemPosition == 0 -> {
                    activity?.hideKeyboard()
                    (activity as BaseActivity).showError(
                        getString(com.riteshakya.ui.R.string.error_select_section)
                    )
                }
                else -> {
                    navigateToPhone()
                }
            }
        }
    }

    private fun navigateToPhone() {
        signUpViewModel.userRole = STUDENT
        navigator.navigateToPhone(this)
    }

    override fun setValidity(result: Boolean) {
        nextBtn.isEnabled = result
    }
}