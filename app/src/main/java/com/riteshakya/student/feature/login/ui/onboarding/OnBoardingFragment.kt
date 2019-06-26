package com.riteshakya.student.feature.login.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.contants.Args
import com.riteshakya.core.extension.hideKeyboard
import com.riteshakya.core.extension.showError
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.core.validation.ValidationResult
import com.riteshakya.core.validation.types.PhoneValidation
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.OnBoardingViewModel
import com.riteshakya.ui.components.CountrySpinner
import com.riteshakya.ui.components.CustomFontTextInputEditText
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import javax.inject.Inject

class OnBoardingFragment : BaseFragment() {

    @Inject
    lateinit var onBoardingViewModel: OnBoardingViewModel
    @Inject
    internal lateinit var navigator: LoginNavigator

    lateinit var countrySpinner: CountrySpinner
    lateinit var inputTxt: CustomFontTextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_on_boarding, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countrySpinner = phoneSelector.findViewById(R.id.countrySpinner)
        inputTxt = phoneSelector.findViewById(R.id.inputTxt)

        nextView.setOnClickListener {
            val result: ValidationResult<Any> =
                PhoneValidation(
                    8,
                    getString(com.riteshakya.ui.R.string.error_phone_number_length)
                ).validate(inputTxt.text.toString())

            if (result.isValid) {
                doesPhoneNumberExist()

            } else {
                if (result.reason != null) {
                    activity?.hideKeyboard()
                    showError(result.reason!!)
                }
            }
        }
    }

    private fun doesPhoneNumberExist() {
        onBoardingViewModel.doesPhoneNumberExist(
            countrySpinner.dialCode,
            inputTxt.text.toString() /* Phone number */
        )
            .addLoading()
            .subscribe({
                when (it) {
                    true -> navigateToLogin()
                    false -> navigateToRoleSelection()
                }
            }, {
                it.printStackTrace()
            })
            .untilStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        StudentApp.instance?.mustDie(this)
    }

    private fun navigateToRoleSelection() {
        val args = Bundle()
        args.putString(Args.KEY_DIAL_CODE, countrySpinner.dialCode)
        args.putString(Args.KEY_PHONE_NO, inputTxt.text.toString())
        // args.putString(Args.KEY_MESSAGE, getString(R.string.txt_sign_up_message))
        navigator.navigateToRoleSelection(this, args)
    }

    private fun navigateToLogin() {
        val args = Bundle()
        args.putString(Args.KEY_DIAL_CODE, countrySpinner.dialCode)
        args.putString(Args.KEY_PHONE_NO, inputTxt.text.toString())
        // args.putString(Args.KEY_MESSAGE, getString(R.string.txt_login_message))
        navigator.navigateToLogin(this, args)
    }
}
