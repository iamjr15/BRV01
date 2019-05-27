package com.riteshakya.student.feature.login.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riteshakya.core.contants.Args
import com.riteshakya.core.extension.showSnackbar
import com.riteshakya.core.model.PhoneModel
import com.riteshakya.core.platform.BaseActivity
import com.riteshakya.core.platform.BaseFragment
import com.riteshakya.student.R
import com.riteshakya.student.StudentApp
import com.riteshakya.student.feature.login.navigation.LoginNavigator
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_role_selection.*
import javax.inject.Inject

class RoleSelectionFragment : BaseFragment() {
    @Inject
    internal lateinit var navigator: LoginNavigator

    @Inject
    internal lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_role_selection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readArguments()
        addListeners()
    }
    override fun onDestroy() {
        super.onDestroy()
        StudentApp.instance?.mustDie(this)
    }
    private fun readArguments() {
        if (arguments != null) {
            if (arguments!!.containsKey(Args.KEY_DIAL_CODE) && arguments!!.containsKey(Args.KEY_PHONE_NO)) {
                signUpViewModel.phoneNo.value = PhoneModel(
                    arguments!!.getString(Args.KEY_DIAL_CODE)!!,
                    arguments!!.getString(Args.KEY_PHONE_NO)!!
                )
            }

            if (arguments!!.containsKey(Args.KEY_MESSAGE))
                (activity as BaseActivity).showSnackbar(arguments!!.getString(Args.KEY_MESSAGE)!!)
        }
    }

    private fun addListeners(){
        studentParentBtn.setOnClickListener { navigateToStudentSignUp() }
        teacherBtn.setOnClickListener { navigateToTeacherSignUp() }
        schoolManagementBtn.setOnClickListener { navigateToSchoolSignUp() }
    }

    private fun navigateToStudentSignUp() {
        navigator.navigateToStudentSignUp(this)
    }

    private fun navigateToTeacherSignUp() {
        navigator.navigateToTeacherSignUp(this)
    }

    private fun navigateToSchoolSignUp() {
        navigator.navigateToSchoolSignUp(this)
    }
}