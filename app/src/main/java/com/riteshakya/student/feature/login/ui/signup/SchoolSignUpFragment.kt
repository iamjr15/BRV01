package com.riteshakya.student.feature.login.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.extension.hideKeyboard
import com.riteshakya.core.extension.showError
import com.riteshakya.core.model.SCHOOL
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.platform.ResultState
import com.riteshakya.core.validation.types.EmailValidation
import com.riteshakya.core.validation.types.LengthValidation
import com.riteshakya.core.validation.types.NameValidation
import com.riteshakya.core.validation.types.PasswordValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_school_sign_up.*
import javax.inject.Inject

class SchoolSignUpFragment : BaseFragment() {

    @Inject
    internal lateinit var navigator: LoginNavigator

    @Inject
    internal lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_school_sign_up, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeValidators()
        initializeVm()
        initializeCityFromPostal()
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
        addValidationList(schoolNameTxt.addValidity(NameValidation(), {
            signUpViewModel.schoolName.value = it
        }))
        addValidationList(schoolEmailTxt.addValidity(EmailValidation(), {
            signUpViewModel.schoolEmail.value = it
        }))
        addValidationList(schoolAreaPinTxt.addValidity(LengthValidation(5), {
            signUpViewModel.schoolAreaCode.value = it
            signUpViewModel.getCityFromPostalCode(it)
        }))
        addValidationList(passwordTxt.addValidity(PasswordValidation(), {
            signUpViewModel.password.value = it
        }))
    }

    private fun initializeVm() {
        positionSelect.setSelection(signUpViewModel.authorityPosition.value)
        firstNameTxt.setText(signUpViewModel.firstName.value)
        lastNameTxt.setText(signUpViewModel.lastName.value)
        schoolNameTxt.setText(signUpViewModel.schoolName.value)
        schoolEmailTxt.setText(signUpViewModel.schoolEmail.value)
        schoolAreaPinTxt.setText(signUpViewModel.schoolAreaCode.value)
        passwordTxt.setText(signUpViewModel.password.value)
    }

    private fun initializeCityFromPostal() {
        with(signUpViewModel) {
            cityNameObserver
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotBlank()) {
                        // cityNameTxt.setText(it)
                        signUpViewModel.schoolCity.value = it
                    }
                }, {}).untilStop()
            cityNameState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is ResultState.Error -> {
                            // showMessage(it.failure)
                        }
                    }
                }.untilStop()
        }
    }

    private fun addListeners() {
        positionSelect.onItemChanged {
            signUpViewModel.authorityPosition.value = it
        }

        nextBtn.setOnClickListener {
            when {
                positionSelect.selectedItemPosition == 0 -> {
                    activity?.hideKeyboard()
                    (activity as BaseActivity).showError(
                        getString(com.riteshakya.ui.R.string.error_select_position)
                    )
                }
                else -> {
                    navigateToPhone()
                }
            }
        }
    }

    private fun navigateToPhone() {
        signUpViewModel.userRole = SCHOOL
        navigator.navigateToPhone(this)
    }

    override fun setValidity(result: Boolean) {
        nextBtn.isEnabled = result
    }
}