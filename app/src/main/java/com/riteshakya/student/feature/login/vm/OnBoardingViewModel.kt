package com.riteshakya.student.feature.login.vm

import com.riteshakya.businesslogic.interactor.user.DoesPhoneNumberExistInteractor
import com.riteshakya.core.platform.BaseViewModel
import io.reactivex.Single
import javax.inject.Singleton

@Singleton
class OnBoardingViewModel(val doesPhoneNumberExistInteractor: DoesPhoneNumberExistInteractor) :
    BaseViewModel() {

    fun doesPhoneNumberExist(dialCode: String, phoneNumber: String): Single<Boolean> {
        return doesPhoneNumberExistInteractor(dialCode, phoneNumber)
    }
}