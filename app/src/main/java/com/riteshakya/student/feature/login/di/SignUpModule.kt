package com.riteshakya.student.feature.login.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.riteshakya.businesslogic.interactor.school.GetSchoolsInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpSchoolInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpStudentInteractor
import com.riteshakya.businesslogic.interactor.signup.SignUpTeacherInteractor
import com.riteshakya.businesslogic.interactor.user.DoesPhoneNumberExistInteractor
import com.riteshakya.businesslogic.repository.auth.PhoneRepository
import com.riteshakya.core.di.PerFragment
import com.riteshakya.core.di.ViewModelKey
import com.riteshakya.core.exception.FailureMessageMapper
import com.riteshakya.student.feature.login.ui.onboarding.OnBoardingFragment
import com.riteshakya.student.feature.login.ui.signup.RoleSelectionFragment
import com.riteshakya.student.feature.login.ui.signup.SchoolSignUpFragment
import com.riteshakya.student.feature.login.ui.signup.StudentSignUpFragment
import com.riteshakya.student.feature.login.ui.signup.TeacherSignUpFragment
import com.riteshakya.student.feature.login.ui.signup.logoupload.LogoUploadFragment
import com.riteshakya.student.feature.login.ui.signup.phone.PhoneFragment
import com.riteshakya.student.feature.login.ui.signup.profilepicture.ProfilePictureFragment
import com.riteshakya.student.feature.login.vm.OnBoardingViewModel
import com.riteshakya.student.feature.login.vm.PhoneVerificationViewModel
import com.riteshakya.student.feature.login.vm.SignUpViewModel
import com.riteshakya.student.interactor.geocode.GetCityNameInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [SignUpModule.ProvideViewModel::class])
abstract class SignUpModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [InjectOnBoardingViewModel::class])
    abstract fun providesOnBoardingFragment(): OnBoardingFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesRoleSelectionFragment(): RoleSelectionFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesStudentSignUpFragment(): StudentSignUpFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesSchoolSignUpFragment(): SchoolSignUpFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesTeacherSignUpFragment(): TeacherSignUpFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [InjectPhoneVerificationViewModel::class])
    abstract fun providesPhoneFragment(): PhoneFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesProfilePictureFragment(): ProfilePictureFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun provideLogoUploadFragment(): LogoUploadFragment

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(SignUpViewModel::class)
        fun provideSignUpViewModel(
            getSchoolsInteractor: GetSchoolsInteractor,
            getCityNameInteractor: GetCityNameInteractor,
            signUpStudentInteractor: SignUpStudentInteractor,
            signUpTeacherInteractor: SignUpTeacherInteractor,
            signUpSchoolInteractor: SignUpSchoolInteractor,
            failureMessageMapper: FailureMessageMapper
        ): ViewModel = SignUpViewModel(
            getSchoolsInteractor, getCityNameInteractor, signUpStudentInteractor,
            signUpTeacherInteractor, signUpSchoolInteractor, failureMessageMapper
        )

        @Provides
        @IntoMap
        @ViewModelKey(PhoneVerificationViewModel::class)
        fun providePhoneVerificationViewModel(
            failureMessageMapper: FailureMessageMapper,
            phoneRepository: PhoneRepository
        ): ViewModel = PhoneVerificationViewModel(phoneRepository, failureMessageMapper)

        @Provides
        @IntoMap
        @ViewModelKey(OnBoardingViewModel::class)
        fun provideOnBoardingViewModel(
            doesPhoneNumberExistInteractor: DoesPhoneNumberExistInteractor
        ): ViewModel = OnBoardingViewModel(doesPhoneNumberExistInteractor)
    }

    @Module
    class InjectPhoneVerificationViewModel {

        @Provides
        fun provideLoginViewModel(
            factory: ViewModelProvider.Factory,
            target: PhoneFragment
        ): PhoneVerificationViewModel =
            ViewModelProviders.of(target, factory).get(PhoneVerificationViewModel::class.java)
    }

    @Module
    class InjectOnBoardingViewModel {

        @Provides
        fun provideOnBoardingViewModel(
            factory: ViewModelProvider.Factory,
            target: OnBoardingFragment
        ): OnBoardingViewModel =
            ViewModelProviders.of(target, factory).get(OnBoardingViewModel::class.java)
    }
}