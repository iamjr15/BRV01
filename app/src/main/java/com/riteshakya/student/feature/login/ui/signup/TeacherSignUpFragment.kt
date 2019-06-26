package com.riteshakya.student.feature.login.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.extension.hideKeyboard
import com.riteshakya.core.extension.showError
import com.riteshakya.core.model.TEACHER
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.validation.types.NameValidation
import com.riteshakya.core.validation.types.NonEmptyValidation
import com.riteshakya.core.validation.types.PasswordValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import com.riteshakya.ui.components.CustomSpinner
import com.riteshakya.ui.components.SpinnerAdapter
import kotlinx.android.synthetic.main.fragment_teacher_sign_up.*
import javax.inject.Inject

class TeacherSignUpFragment : BaseFragment() {

    @Inject
    internal lateinit var navigator: LoginNavigator

    @Inject
    internal lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_teacher_sign_up, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeValidators()
        initializeVm()
        initializeSchools()
        addListeners()
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
        addValidationList(subjectTxt.addValidity(NonEmptyValidation(), {
            signUpViewModel.subject.value = it
        }))
        addValidationList(passwordTxt.addValidity(PasswordValidation(), {
            signUpViewModel.password.value = it
        }))
    }

    private fun initializeVm() {
        firstNameTxt.setText(signUpViewModel.firstName.value)
        lastNameTxt.setText(signUpViewModel.lastName.value)
        schoolSelect.setSelection(signUpViewModel.school.value)
        subjectTxt.setText(signUpViewModel.subject.value)
        passwordTxt.setText(signUpViewModel.password.value)
        teacherSwitch.isChecked = signUpViewModel.teacher.value ?: true
    }

    private fun initializeSchools() {
        signUpViewModel.schools
            .addLoading()
            .subscribe {
                val schoolList =
                    it.map { school ->
                        SpinnerAdapter.SpinnerModel(
                            school.id, school.schoolName, school.schoolLogo
                        )
                    }

                // Add "Select School" with -1 id as first item
                val finalSchoolList = listOf(
                    SpinnerAdapter.SpinnerModel(
                        "-1",
                        getString(R.string.txt_select_school)
                    )
                ) + schoolList

                schoolSelect.items = finalSchoolList
            }
            .untilStop()
    }

    private fun addListeners() {
        teacherSwitch.setOnCheckedChangeListener {
            signUpViewModel.teacher.value = it
        }

        addHereTxt.setOnClickListener {
            navigateToSchoolSignUp()
        }

        nextBtn.setOnClickListener {
            when {
                schoolSelect.findViewById<CustomSpinner>(R.id.spinner).selectedItemPosition == 0 -> {
                    activity?.hideKeyboard()
                    (activity as BaseActivity).showError(
                        getString(com.riteshakya.ui.R.string.error_select_school)
                    )
                }
                else -> {
                    navigateToPhone()
                }
            }
        }
    }

    private fun navigateToSchoolSignUp() {
        navigator.navigateToSchoolSignUp(this)
    }

    private fun navigateToPhone() {
        signUpViewModel.userRole = TEACHER
        navigator.navigateToPhone(this)
    }

    override fun setValidity(result: Boolean) {
        nextBtn.isEnabled = result
    }
}