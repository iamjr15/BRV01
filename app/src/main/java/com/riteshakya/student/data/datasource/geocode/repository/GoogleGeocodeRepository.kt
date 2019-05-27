package com.riteshakya.student.data.datasource.geocode.repository

import com.riteshakya.student.data.datasource.geocode.GeocodeService
import com.riteshakya.student.repository.geocode.GeocodeRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GoogleGeocodeRepository
@Inject constructor(
    private var geocodeService: GeocodeService
) : GeocodeRepository {

    override fun getCityName(postalCode: String): Single<String> {
        return geocodeService.getCityName(postalCode)
            .subscribeOn(Schedulers.io())
            .map {
                // it.formattedAddress
                it.results[0].formattedAddress
            }
    }
}