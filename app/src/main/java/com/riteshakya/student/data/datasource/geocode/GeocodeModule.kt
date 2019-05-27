package com.riteshakya.student.data.datasource.geocode

import com.riteshakya.student.data.datasource.geocode.repository.GoogleGeocodeRepository
import com.riteshakya.student.repository.geocode.GeocodeRepository
import dagger.Module
import dagger.Provides

class GeocodeModule {
    @Module
    class Repositories {
        @Provides
        fun provideGeoCodeRepository(
            repository: GoogleGeocodeRepository
        ): GeocodeRepository = repository
    }
}