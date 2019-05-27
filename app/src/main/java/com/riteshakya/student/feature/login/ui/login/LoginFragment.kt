package com.riteshakya.student.feature.login.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.contants.Args
import com.riteshakya.core.extension.showSnackbar
import com.riteshakya.core.model.PhoneModel
import com.riteshakya.core.model.STUDENT
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.validation.types.PasswordValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.LoginViewModel
import com.riteshakya.student.navigation.Navigator
import com.riteshakya.ui.components.SpinnerAdapter
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var loginViewModel: LoginViewModel
    @Inject
    lateinit var navigator: LoginNavigator
    @Inject
    internal lateinit var mainNavigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readArguments()
        initializeValidators()
        initializeInputsFromVM()
        loginBtn.setOnClickListener {
            loginUser()
        }
        forgotPasswordTxt.setOnClickListener {
            showMessage("Under Development")
        }
        initializeSchools()
    }
    override fun onDestroy() {
        super.onDestroy()
        StudentApp.instance?.mustDie(this)
    }
    private fun readArguments() {
        if (arguments != null) {
            if (arguments!!.containsKey(Args.KEY_DIAL_CODE) && arguments!!.containsKey(Args.KEY_PHONE_NO)) {
                loginViewModel.phoneNo = PhoneModel(
                    arguments!!.getString(Args.KEY_DIAL_CODE)!!,
                    arguments!!.getString(Args.KEY_PHONE_NO)!!
                )
            }

            if (arguments!!.containsKey(Args.KEY_MESSAGE))
                (activity as BaseActivity).showSnackbar(arguments!!.getString(Args.KEY_MESSAGE)!!)
        }
    }

    private fun loginUser() {
        loginViewModel.loginUser()
            .addLoading()
            .subscribe({
                getUserObject()
            }, {
                it.printStackTrace()
            })
            .untilStop()
    }

    private fun getUserObject() {
        loginViewModel.getCurrentUser()
            .addLoading()
            .subscribe({
                /*if (!arrayListOf(STUDENT).contains(it.role)) {
                    roleNotSupported()
                } else {
                    mainNavigator.showMain(context!!)
                    activity?.finishAffinity()
                }*/

                mainNavigator.showMain(context!!)
                activity?.finishAffinity()

            }, {})
            .untilStop()
    }

    private fun roleNotSupported() {
        showMessage("User role not supported by app. Please use the teacher app.")
        loginViewModel.logout()
            .addLoading()
            .subscribe {

            }.untilStop()
    }

    private fun initializeSchools() {
        loginViewModel.schools
            .addLoading()
            .subscribe({
                schoolSelect.items =
                    it.map { school ->
                        SpinnerAdapter.SpinnerModel(
                            school.id, school.schoolName, school.schoolLogo
                        )
                    }
            }, {})
            .untilStop()
    }

    private fun initializeInputsFromVM() {
        passwordTxt.setText(loginViewModel.password.value)
        schoolSelect.setSelection(loginViewModel.school.value)
    }

    private fun initializeValidators() {
        addValidationList(passwordTxt.addValidity(PasswordValidation(), {
            loginViewModel.password.value = it
        }))
        addValidationList(schoolSelect.addValidity {
            loginViewModel.school.value = it
        })
    }

    override fun setValidity(result: Boolean) {
        loginBtn.isEnabled = result
    }
}
