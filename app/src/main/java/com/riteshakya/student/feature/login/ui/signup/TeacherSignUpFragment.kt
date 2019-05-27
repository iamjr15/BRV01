package com.riteshakya.student.feature.login.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.model.TEACHER
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.validation.types.NameValidation
import com.riteshakya.core.validation.types.NonEmptyValidation
import com.riteshakya.core.validation.types.PasswordValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
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
        teacherSwitch.setOnCheckedChangeListener {
            signUpViewModel.teacher.value = it
        }

        addHereTxt.setOnClickListener {
            navigateToSchoolSignUp()
        }

        nextBtn.setOnClickListener {
            navigateToPhone()
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