package com.riteshakya.student.interactor.geocode

import com.riteshakya.student.repository.geocode.GeocodeRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCityNameInteractor
@Inject constructor(
    private val repository: GeocodeRepository
) {
    operator fun invoke(
        postalCode: String
    ): Single<String> {
        return repository.getCityName(postalCode)
    }
}