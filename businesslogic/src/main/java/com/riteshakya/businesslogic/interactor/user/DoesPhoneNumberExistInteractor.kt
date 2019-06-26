package com.riteshakya.businesslogic.interactor.user

import com.riteshakya.businesslogic.repository.user.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class DoesPhoneNumberExistInteractor
@Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(dialCode: String, phoneNumber: String): Single<Boolean> {
        return repository.doesPhoneNumberExist(dialCode, phoneNumber)
    }
}