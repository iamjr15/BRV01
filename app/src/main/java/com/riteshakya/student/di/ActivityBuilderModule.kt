package com.riteshakya.student.di

import com.riteshakya.student.feature.main.di.MainActivityModule
import com.riteshakya.student.navigation.RouteActivityModule
import dagger.Module

@Module(includes = [RouteActivityModule::class, MainActivityModule::class])
abstract class ActivityBuilderModule